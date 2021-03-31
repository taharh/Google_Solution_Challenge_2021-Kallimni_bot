package com.example.myapplication.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.callbacks.BotCallback;
import com.example.myapplication.data.model.Message;
import com.example.myapplication.data.pubsub.GoogleCloudPubSubDataSource;
import com.example.myapplication.ui.adapters.holders.VoiceMessageViewHolder;
import com.example.myapplication.ui.adapters.holders.WelcomeMessageViewHolder;
import com.example.myapplication.ui.viewmodels.MainViewModel;
import com.example.myapplication.ui.viewmodels.ViewModelFactory;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.myapplication.utils.utils.CONTENT_TYPE_VOICE;
import static com.example.myapplication.utils.utils.CONTENT_TYPE_WELCOME;
import static com.example.myapplication.utils.utils.CURRENT_USER;
import static com.example.myapplication.utils.utils.getBotAuthor;
import static com.example.myapplication.utils.utils.getUserAuthor;

public class ChatBotFragment extends Fragment implements BotCallback{

    public static final String TAG = "ChatBotFragment";

    @BindView(R.id.messagesList)
    MessagesList messagesList;


    private MainActivity instanceActivity;
    private MainViewModel mainViewModel;
    private GoogleCloudPubSubDataSource dataSource;
    private MessagesListAdapter<Message> messageMessagesListAdapter;

    private ImageLoader imageLoader = (imageView, url, payload) -> {
        Glide.with(this)
                .load(url.contains("http") ? url : R.drawable.logo)
                .into(imageView);
    };

    public static ChatBotFragment newInstance() {
        return new ChatBotFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_bot_fragment, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        setupMessageList();
        updateUI();
    }

    private void init() {
        instanceActivity = (MainActivity) getActivity();
        mainViewModel = new ViewModelProvider(this, new ViewModelFactory(dataSource)).get(MainViewModel.class);

        dataSource = new GoogleCloudPubSubDataSource(
                mainViewModel,
                getResources().openRawResource(R.raw.emotions_dict),
                getResources().openRawResource(R.raw.credentials),
                "solution-challenge-308122",
                "hello_topic"
        );

        dataSource.subscribe("sub_two");

        mainViewModel.setDataSource(dataSource);

    }

    private void updateUI() {
        Message welcome = new Message(getBotAuthor(), getString(R.string.welcome_chat), true);
        messageMessagesListAdapter.addToStart(welcome, true);

        mainViewModel.getMessageLive().observe(getActivity(), message ->
                messageMessagesListAdapter.addToStart(message, true));

        mainViewModel.getAudioUri().observe(getActivity(), uri -> {

        });
    }


    private void setupMessageList() {
        MessageHolders holders = new MessageHolders()
                .registerContentType(
                        CONTENT_TYPE_VOICE,
                        VoiceMessageViewHolder.class,
                        R.layout.item_voice_message,
                        VoiceMessageViewHolder.class,
                        R.layout.item_voice_message,
                        instanceActivity)
                .registerContentType(
                        CONTENT_TYPE_WELCOME,
                        WelcomeMessageViewHolder.class,
                        R.layout.item_welcome_message,
                        WelcomeMessageViewHolder.class,
                        R.layout.item_welcome_message,
                        instanceActivity);
        messageMessagesListAdapter = new MessagesListAdapter<>(CURRENT_USER, holders, imageLoader);
        messagesList.setAdapter(messageMessagesListAdapter);
    }

    @Override
    public void sendAudio(String uri) {
        Message voiceMessage = new Message(uri, getUserAuthor());
        messageMessagesListAdapter.addToStart(voiceMessage, true);
        mainViewModel.sendMessage("TODO !!! Must received Base64 of the audio file - Uri from the Local : " + uri);
    }
}


