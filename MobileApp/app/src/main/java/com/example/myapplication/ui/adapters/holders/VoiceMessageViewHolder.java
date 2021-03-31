package com.example.myapplication.ui.adapters.holders;

import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.data.model.Message;
import com.stfalcon.chatkit.messages.MessageHolders;

import com.stfalcon.chatkit.utils.DateFormatter;

import me.jagar.chatvoiceplayerlibrary.VoicePlayerView;

public class VoiceMessageViewHolder
        extends MessageHolders.BaseMessageViewHolder<Message> {

    private TextView tvTime;
    private VoicePlayerView voicePlayerView;

    public VoiceMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
        tvTime = itemView.findViewById(R.id.time);
        voicePlayerView = itemView.findViewById(R.id.voicePlayerView);
    }

    @Override
    public void onBind(Message message) {
        tvTime.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
        voicePlayerView.setAudio(message.getVoicePath());
    }
}
