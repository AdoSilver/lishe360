package com.cardeanutrition.lishe360app.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cardeanutrition.lishe360app.R;
import com.cardeanutrition.lishe360app.dialogs.EditCommentDialog;
import com.cardeanutrition.lishe360app.services.ResizableCustomView;

public class FeedBackActivity extends BaseActivity implements EditCommentDialog.CommentDialogCallback  {
    private EditText feedbackEditText;
    private TextView tv;
    private static final int MAX_LINES =4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        final Button sendButton = (Button) findViewById(R.id.sendFeedback);
        feedbackEditText = (EditText) findViewById(R.id.feedbackEditText);
        tv = findViewById(R.id.tv);
        tv.setText(R.string.feednews);


        ResizableCustomView.doResizeTextView(tv, MAX_LINES, "View More", true);
        feedbackEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sendButton.setEnabled(charSequence.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasInternetConnection()) {

                    showSnackBar(R.string.feedresponse);
                    feedbackEditText.setText(null);
                } else {
                    showSnackBar(R.string.internet_connection_failed);
                }
            }
        });
    }


    @Override
    public void onCommentChanged(String newText, String commentId) {

    }


}
