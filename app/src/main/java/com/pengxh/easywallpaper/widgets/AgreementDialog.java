package com.pengxh.easywallpaper.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.pengxh.easywallpaper.R;
import com.pengxh.easywallpaper.ui.ExonerationActivity;
import com.pengxh.easywallpaper.ui.PrivacyActivity;

import org.jetbrains.annotations.NotNull;

/**
 * @author: Pengxh
 * @email: 290677893@qq.com
 * @description: TODO 自定义权限申请对话框
 * @date: 2020/2/18 14:55
 */
public class AgreementDialog extends AlertDialog implements View.OnClickListener {

    private static final String TAG = "AgreementDialog";
    private Context context;
    private String title;
    private String message;
    private OnDialogClickListener dialogListener;

    private AgreementDialog(Builder builder) {
        super(builder.mContext, R.style.AgreementDialog);
        this.context = builder.mContext;
        this.title = builder.title;
        this.message = builder.message;
        this.dialogListener = builder.listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_agreement);
        initView();
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    private void initView() {
        TextView dialogTitle = findViewById(R.id.dialogTitle);
        if (dialogTitle != null) {
            dialogTitle.setText(title);
        }

        TextView dialogMessage = findViewById(R.id.dialogMessage);
        if (dialogMessage != null) {
            dialogMessage.setText(message);
        }

        TextView subMessage = findViewById(R.id.subMessage);
        //TODO 下划线点击效果
        if (subMessage != null) {
            SpannableString spanText = new SpannableString(context.getString(R.string.agreement));
            //免责声明
            spanText.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    context.startActivity(new Intent(context, ExonerationActivity.class));
                }

                @Override
                public void updateDrawState(@NotNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.RED);       //设置文件颜色
                    ds.setUnderlineText(true);      //设置下划线
                }
            }, 17, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            subMessage.setMovementMethod(LinkMovementMethod.getInstance());
            subMessage.setText(spanText);

            //隐私政策
            spanText.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    context.startActivity(new Intent(context, PrivacyActivity.class));
                }

                @Override
                public void updateDrawState(@NotNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.RED);       //设置文件颜色
                    ds.setUnderlineText(true);      //设置下划线
                }
            }, spanText.length() - 7, spanText.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            subMessage.setMovementMethod(LinkMovementMethod.getInstance());
            subMessage.setText(spanText);
        }

        Button confirmButton = findViewById(R.id.confirmButton);
        if (confirmButton != null) {
            confirmButton.setOnClickListener(this);
        }
        TextView cancelView = findViewById(R.id.cancelView);
        if (cancelView != null) {
            cancelView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.confirmButton) {
            if (dialogListener != null) {
                dialogListener.onConfirmClick();
            }
        } else if (i == R.id.cancelView) {
            if (dialogListener != null) {
                dialogListener.onCancelClick();
            }
        }
        dismiss();
    }

    public static class Builder {
        private Context mContext;
        private String title;
        private String message;
        private OnDialogClickListener listener;

        public Builder setContext(Context context) {
            this.mContext = context;
            return this;
        }

        public Builder setDialogTitle(String s) {
            this.title = s;
            return this;
        }

        public Builder setDialogMessage(String s) {
            this.message = s;
            return this;
        }

        public Builder setOnDialogClickListener(OnDialogClickListener listener) {
            this.listener = listener;
            return this;
        }

        public AgreementDialog build() {
            return new AgreementDialog(this);
        }
    }

    public interface OnDialogClickListener {
        void onConfirmClick();

        void onCancelClick();
    }
}
