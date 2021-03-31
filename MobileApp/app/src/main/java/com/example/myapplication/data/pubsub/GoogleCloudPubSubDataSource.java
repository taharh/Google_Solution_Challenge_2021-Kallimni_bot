package com.example.myapplication.data.pubsub;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.callbacks.PubSub;
import com.example.myapplication.data.model.Message;
import com.example.myapplication.data.model.SentimentDict;
import com.example.myapplication.data.repository.MessageRepository;
import com.example.myapplication.ui.viewmodels.MainViewModel;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.myapplication.utils.utils.getBotAuthor;

public class GoogleCloudPubSubDataSource implements MessageRepository, PubSub, MessageReceiver {
    public static final String TAG = GoogleCloudPubSubDataSource.class.getName();

    private Subscriber subscriber;
    MainViewModel viewModel;

    Gson gson;
    private GoogleCredentials credentials;
    private String projectName;
    private String topic;
    private List<SentimentDict> sentimentDictList;
    private MutableLiveData<Message> messageMutableLiveData = new MutableLiveData<>();

    public GoogleCloudPubSubDataSource(MainViewModel mainViewModel, InputStream emotionsDictStream, InputStream jsonCredentials, String projectName, String topic) {
        this.viewModel = mainViewModel;
        this.projectName = projectName;
        this.topic = topic;
        gson = new Gson();

        try {
            credentials = GoogleCredentials.fromStream(jsonCredentials);
            sentimentDictList = loadSentimentDict(emotionsDictStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public LiveData<Message> getMessage() {
        return messageMutableLiveData;
    }

    @Override
    public void sendMessage(String audio) {
        publish(topic, audio);
    }

    @Override
    public void subscribe(String subscription) {
        new Thread(() -> {
            try {
                ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectName, subscription);
                subscriber = Subscriber
                        .newBuilder(subscriptionName, GoogleCloudPubSubDataSource.this)
                        .setCredentialsProvider(() -> credentials)
                        .build();
                subscriber.startAsync().awaitRunning();
                subscriber.awaitTerminated();
            } catch (Throwable t) {
                Log.i("PUBSUB", t.getMessage() + "Something went wrong");
            }
        }).start();
    }

    @Override
    public void publish(String topicName, String message) {
        Publisher publisher = null;
        try {
            ProjectTopicName projectTopicName = ProjectTopicName.of(projectName, topicName);
            publisher = Publisher.newBuilder(projectTopicName)
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                    .build();
            ByteString data = ByteString.copyFromUtf8(message);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                    .setData(data)
                    .putAttributes("P-MOB", "MOB-9e31cf6d-1c80-44a0-b3f3-c0c5c02c23ad")
                    .build();
            ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);

            ApiFutures.addCallback(messageIdFuture, new ApiFutureCallback<String>() {
                        @Override
                        public void onFailure(Throwable t) {
                            Log.e(TAG, "failed to publish: +" + t);
                        }

                        @Override
                        public void onSuccess(String messageId) {
                            Log.d(TAG, "published with message id: " + messageId);
                        }
                    },
                    MoreExecutors.directExecutor());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (publisher != null) {
                publisher.shutdown();
                try {
                    publisher.awaitTermination(1, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
        consumer.ack();
        String attributesOrDefault = message.getAttributesOrDefault("P_BOT", null);
        if (attributesOrDefault.equals("BOT-fecc8d1e-a315-4c04-a4bc-11ba58d84769")) {
            Map<String, String> attributesMap = message.getAttributesMap();
            for (Map.Entry<String, String> entry : attributesMap.entrySet()) {
                if (!entry.getKey().equals("P_BOT")) {
                    SentimentDict sentimentDict = getSentimentDict(entry.getKey(), Integer.parseInt(entry.getValue()));
                    if (sentimentDict != null) {
                        Log.d(TAG, "receiveMessage: " + sentimentDict.toString());
                        if (sentimentDict.getImgUrl() != null)
                            messageMutableLiveData.postValue(new Message(getBotAuthor(), new Message.Image(sentimentDict.getImgUrl())));
                        messageMutableLiveData.postValue(new Message(getBotAuthor(), sentimentDict.getMessage()));
                        try {
                            Thread.sleep(900);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    private SentimentDict getSentimentDict(String key, int value) {
        for (SentimentDict sentimentDict : sentimentDictList) {
            if (sentimentDict.getSentiment().equals(key)
                    && sentimentDict.getMin() < value
                    && sentimentDict.getMax() >= value) {
                return sentimentDict;
            }
        }
        return null;
    }

    private List<SentimentDict> loadSentimentDict(InputStream inputStream) throws UnsupportedEncodingException {
        Reader reader = new InputStreamReader(inputStream, "UTF-8");
        return gson.fromJson(reader, new TypeToken<List<SentimentDict>>() {
        }.getType());
    }




}


