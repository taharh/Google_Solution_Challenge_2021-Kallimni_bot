package com.example.myapplication.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.asynctaskcoffee.audiorecorder.uikit.IconsObj;
import com.asynctaskcoffee.audiorecorder.uikit.LangObj;
import com.asynctaskcoffee.audiorecorder.worker.AudioRecordListener;
import com.asynctaskcoffee.audiorecorder.worker.MediaPlayListener;
import com.asynctaskcoffee.audiorecorder.worker.Player;
import com.asynctaskcoffee.audiorecorder.worker.Recorder;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.callbacks.BotCallback;
import com.example.myapplication.ui.adapters.ViewPagerAdapter;
import com.example.myapplication.ui.customviews.ChatToolBar;
import com.example.myapplication.ui.customviews.CurvedBottomNavigationView;
import com.example.myapplication.ui.adapters.holders.VoiceMessageViewHolder;
import com.example.myapplication.ui.adapters.holders.WelcomeMessageViewHolder;
import com.example.myapplication.data.model.Message;
import com.example.myapplication.data.pubsub.GoogleCloudPubSubDataSource;
import com.example.myapplication.ui.viewmodels.MainViewModel;
import com.example.myapplication.ui.viewmodels.ViewModelFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Objects;


import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.myapplication.utils.utils.CONTENT_TYPE_VOICE;
import static com.example.myapplication.utils.utils.CONTENT_TYPE_WELCOME;
import static com.example.myapplication.utils.utils.CURRENT_USER;
import static com.example.myapplication.utils.utils.getBotAuthor;
import static com.example.myapplication.utils.utils.getUserAuthor;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        View.OnTouchListener,
        AudioRecordListener,
        MediaPlayListener,
        MessageHolders.ContentChecker<Message> {

    private static final String TAG = MainActivity.class.getName();

    protected ChatToolBar chatToolBar;


    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,};
    private static final int REQUEST_PERMISSIONS = 200;

    private Recorder recorder;
    private Player player;

    private String fileName = null;

    private boolean beepEnabled = true;
    private boolean permissionToRecordAccepted = false;
    private boolean mStartRecording = true;
    private boolean mStartPlaying = true;
    private LangObj langObj = new LangObj();
    private IconsObj iconsObj = new IconsObj();

    @BindView(R.id.btn_record)
    ImageView recordButton;
    @BindView(R.id.chr_record_duration)
    Chronometer recordDuration;
    @BindView(R.id.audio_delete)
    ImageView audioDelete;
    @BindView(R.id.audio_send)
    ImageView audioSend;
    @BindView(R.id.customBottomBar)
    CurvedBottomNavigationView curvedBottomNavigationView;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    ChatBotFragment chatBotFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        chatToolBar = findViewById(R.id.chatToolbar);
        setSupportActionBar(chatToolBar.getSupportToolbar());

        chatToolBar.setToolBarListener(() -> onBackPressed());

        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
        setViews();
        if (letsCheckPermissions())
            setListeners();

        chatBotFragment = ChatBotFragment.newInstance();
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(CommunityFragment.newInstance());
        viewPagerAdapter.addFragment(chatBotFragment);
        viewPagerAdapter.addFragment(SettingFragment.newInstance("par1", "par2"));
        viewPager.setOffscreenPageLimit(viewPagerAdapter.getCount());
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curvedBottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        curvedBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.community_menu:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.setting_menu:
                    viewPager.setCurrentItem(2);
                    break;
            }
            return true;
        });

    }

    void setViews() {
        recordButton.setImageDrawable(getDrawable(iconsObj.ic_start_record));
        audioDelete.setImageDrawable(getDrawable(iconsObj.ic_audio_delete));
        audioSend.setImageDrawable(getDrawable(iconsObj.ic_send_circle));
        curvedBottomNavigationView.inflateMenu(R.menu.bottom_menu);


//        enableBottomBar(false);
    }


    private void enableBottomBar(boolean enable) {
        for (int i = 0; i < curvedBottomNavigationView.getMenu().size(); i++) {
            curvedBottomNavigationView.getMenu().getItem(i).setEnabled(enable);
        }
    }

    private boolean letsCheckPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void resetWorkers() {
        if (recorder != null) {
            recorder.reset();
            recorder = null;
            recorder = new Recorder(this);
        }

        if (player != null) {
            player.reset();
            player = null;
            player = new Player(this);
        }
    }

    private void resetAudioView() {
        mStartRecording = true;
        mStartPlaying = true;
        resetWorkers();
        recordButton.setOnClickListener(null);
        recordButton.setOnTouchListener(this);
        audioDelete.setVisibility(View.INVISIBLE);
        audioSend.setVisibility(View.INVISIBLE);
        recordButton.setImageDrawable(getDrawable(iconsObj.ic_start_record));
        recordDuration.setText("00:00");
        recordDuration.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }

    public void dismiss() {

        resetAudioView();
        recordDuration.setBase(SystemClock.elapsedRealtime());
        recordDuration.stop();
        curvedBottomNavigationView.getMenu().getItem(1).setChecked(true);
        enableBottomBar(true);
    }


    void reflectRecord(String uri) {
        onAudioReady(uri);
        //BotCallback botCallback = chatBotFragment;// TODO: 3/30/2021
        chatBotFragment.sendAudio(uri);
    }

    private void startPlaying() {
        player.startPlaying();
    }

    private void stopPlaying() {
        player.stopPlaying();
    }

    public void deleteCurrentFile() {
        try {
            File file = new File(fileName);
            file.delete();
            if (file.exists()) {
                file.getCanonicalFile().delete();
                if (file.exists()) {
                    Objects.requireNonNull(deleteFile(file.getName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void beep() {
        if (beepEnabled)
            new ToneGenerator(AudioManager.STREAM_MUSIC, 70).startTone(ToneGenerator.TONE_CDMA_PIP, 150);
    }

    private void startRecording() {
        recordButton.setImageDrawable(getDrawable(iconsObj.ic_stop_record));
        beep();
        new Handler().postDelayed(() -> recorder.startRecord(MainActivity.this), 50);
    }

    private void onRecord(boolean start) {
        curvedBottomNavigationView.getMenu().getItem(1).setChecked(true);
        enableBottomBar(false);
        viewPager.setCurrentItem(1);
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void stopRecording() {
        audioDelete.setVisibility(View.VISIBLE);
        audioSend.setVisibility(View.VISIBLE);
        recordDuration.stop();
        recordButton.setOnTouchListener(null);
        recordButton.setOnClickListener(this);
        recordButton.setImageDrawable(getDrawable(iconsObj.ic_play_record));
        recorder.stopRecording();
    }

    @SuppressLint("ClickableViewAccessibility")
    void setListeners() {
        recorder = new Recorder(this);
        player = new Player(this);
        audioDelete.setOnClickListener(this);
        audioSend.setOnClickListener(this);
        recordButton.setOnTouchListener(this);
    }


    @Override
    public void onClick(View v) {
        if (recordButton.getId() == (v.getId())) {
            onPlay(mStartPlaying);
        } else if (audioDelete.getId() == (v.getId())) {
            if (audioDelete.getVisibility() == View.VISIBLE) {
                deleteCurrentFile();
                resetAudioView();
                recordDuration.setBase(SystemClock.elapsedRealtime());
                recordDuration.stop();
            }
            curvedBottomNavigationView.getMenu().getItem(1).setChecked(true);
            enableBottomBar(true);
        } else if (audioSend.getId() == (v.getId())) {
            if (audioSend.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(fileName)) {
                reflectRecord(fileName);
                dismiss();
            }
            curvedBottomNavigationView.getMenu().getItem(1).setChecked(true);
            enableBottomBar(true);
        }

    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onRecord(true);
                recordDuration.setBase(SystemClock.elapsedRealtime());
                recordDuration.stop();
                recordDuration.start();
                recordDuration.setVisibility(View.VISIBLE);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_BUTTON_RELEASE:
                onRecord(false);
                recordDuration.stop();
                return true;
        }
        return false;
    }

    @Override
    public void onAudioReady(@Nullable String audioUri) {
        fileName = audioUri;
        player.injectMedia(fileName);
    }

    @Override
    public void onRecordFailed(@Nullable String errorMessage) {
        fileName = null;
        dismiss();
        Toast.makeText(this, "Hold for record", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            permissionToRecordAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED) && ((grantResults[1] == PackageManager.PERMISSION_GRANTED));
            setListeners();
        }
        if (!permissionToRecordAccepted) dismiss();
    }

    @Override
    public void onStartMedia() {
        recordButton.setImageDrawable(getDrawable(iconsObj.ic_stop_play));
        mStartPlaying = !mStartPlaying;
    }

    @Override
    public void onStopMedia() {
        recordButton.setImageDrawable(getDrawable(iconsObj.ic_play_record));
        mStartPlaying = !mStartPlaying;
    }

    @Override
    public boolean hasContentFor(Message message, byte type) {
        if (type == CONTENT_TYPE_VOICE) {
            return message.getVoicePath() != null && !message.getVoicePath().isEmpty();
        } else if (type == CONTENT_TYPE_WELCOME)
            return message.isWelcomeMessage();
        return false;
    }
}