package com.example.myapplication.ui.adapters.holders;

import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.data.model.Message;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.utils.DateFormatter;

import me.jagar.chatvoiceplayerlibrary.VoicePlayerView;

public class WelcomeMessageViewHolder
        extends MessageHolders.BaseMessageViewHolder<Message> {
    private TextView tvTime,tvMessage;

    public WelcomeMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
        tvTime = itemView.findViewById(R.id.messageTime);
        tvMessage = itemView.findViewById(R.id.messageText);
    }

    @Override
    public void onBind(Message message) {
        tvTime.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
        tvMessage.setText(message.getText());
    }
}
