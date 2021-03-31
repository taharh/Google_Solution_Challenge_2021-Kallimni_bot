package com.example.myapplication.ui.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.google.android.material.appbar.AppBarLayout;

public class ChatToolBar extends RelativeLayout{

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private ImageView backButton;
    private ToolBarListener toolBarListener;

    public ChatToolBar(Context context) {
        super(context);
    }

    public ChatToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ChatToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.toolbar_view, this, true);

        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appBar);
        backButton = toolbar.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            if (toolBarListener != null)
                toolBarListener.onBackPressed();
        });
    }

    public AppBarLayout getAppBarLayout() {
        return appBarLayout;
    }

    public Toolbar getSupportToolbar() {
        return toolbar;
    }

    public void setToolBarListener(ToolBarListener toolBarListener) {
        this.toolBarListener = toolBarListener;
    }

    public interface ToolBarListener {
        void onBackPressed();
    }

}

