package com.quitter.quitter.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quitter.quitter.R;


/**
 * <p>
 * 自定义对话框
 * </p>
 *
 * @author Jack 2013年7月11日11:12:33
 */
public class CustomedDialog extends Dialog {

    public CustomedDialog(Context context, int theme) {
        super(context, theme);
    }

    public CustomedDialog(Context context) {
        this(context, 0);
    }

    /**
     * 创建只有一个确定按钮的提示对话框
     */
    public static CustomedDialog getInstance(final Context context,
                                             String title, String msg, String positiveString,
                                             OnClickListener onPositiveClickListener) {
        CustomedDialog.Builder customBuilder = new CustomedDialog.Builder(
                context);
        customBuilder.setTitle(title).setMessage(msg)
                .setPositiveButton(positiveString, onPositiveClickListener);
        CustomedDialog dialog = customBuilder.create();
        dialog.show();
        return dialog;
    }

    /**
     * 创建对话框实例，并且显示
     */
    public static CustomedDialog getInstance(final Context context, String msg) {
        CustomedDialog.Builder customBuilder = new CustomedDialog.Builder(
                context);
        customBuilder.setMessage(msg);
        CustomedDialog dialog = customBuilder.create();
        dialog.show();
        return dialog;
    }

    public static CustomedDialog getInstance(final Context context, String msg,
                                             View icon) {
        CustomedDialog.Builder customBuilder = new CustomedDialog.Builder(
                context);
        customBuilder.setMessage(msg).setContentView(icon);
        CustomedDialog dialog = customBuilder.create();
        return dialog;
    }

    /**
     * 根据MODE来判断得到的对话框的样式
     *
     * @param context
     * @param msg
     * @param icon
     * @param mode
     * @return
     */
    public static CustomedDialog getInstance(final Context context, String msg,
                                             View icon, int mode, String positiveString,
                                             OnClickListener onPositiveClickListener, String negativeButtonText,
                                             OnClickListener onNegativeButtonListener) {
        CustomedDialog.Builder customBuilder = new CustomedDialog.Builder(
                context);
        customBuilder
                .setMessage(msg)
                .setContentView(icon)
                .setPositiveButton(positiveString, onPositiveClickListener)
                .setNegativeButton(negativeButtonText, onNegativeButtonListener);
        CustomedDialog dialog = customBuilder.create();
        return dialog;
    }

    // 自定义dialog
    public static CustomedDialog getInstance(final Context context, String msg,
                                             String title, View contentView, String positiveString,
                                             OnClickListener onPositiveClickListener) {
        CustomedDialog.Builder customBuilder = new CustomedDialog.Builder(
                context);
        customBuilder.setMessage(msg).setContentView(contentView)
                .setTitle(title)
                .setPositiveButton(positiveString, onPositiveClickListener);
        CustomedDialog dialog = customBuilder.create();
        return dialog;
    }

    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {

        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;

        private OnClickListener positiveButtonClickListener,
                negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * Create the custom dialog
         */
        public CustomedDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomedDialog dialog = new CustomedDialog(context,
                    R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_show_message_layout,
                    null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            TextView tvTitle = layout.findViewById(R.id.tv_title);
            if (title != null) {
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(title);
            } else {
                tvTitle.setVisibility(View.GONE);
            }
            // set the confirm button
            boolean isHidePositive = false;
            boolean isHideNegative = false;
            TextView posiButton = layout.findViewById(R.id.tv_ok);
            TextView negaButton = layout.findViewById(R.id.tv_cancel);
            if (positiveButtonText != null) {
                negaButton.setTextColor(Color.parseColor("#ffae02"));
                posiButton.setText(positiveButtonText);
                posiButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (positiveButtonClickListener != null) {
                            positiveButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                    }
                });
            } else {
                isHidePositive = true;
                posiButton.setVisibility(View.GONE);
                negaButton.setTextColor(Color.parseColor("#ffae02"));
            }
            if (negativeButtonText != null) {
                negaButton.setText(negativeButtonText);
                negaButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (negativeButtonClickListener != null) {
                            negativeButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                    }
                });
            } else {
                isHideNegative = true;
                negaButton.setVisibility(View.GONE);
            }

            if (isHideNegative && isHidePositive) {
                layout.findViewById(R.id.btn_layout).setVisibility(View.GONE);
                if (layout.findViewById(R.id.line) != null) {
                    layout.findViewById(R.id.line).setVisibility(View.GONE);
                }
            } else {
                layout.findViewById(R.id.btn_layout)
                        .setVisibility(View.VISIBLE);
                layout.findViewById(R.id.line).setVisibility(View.VISIBLE);
            }

            if (message != null) {
                TextView msgView = layout
                        .findViewById(R.id.tv_message);
                msgView.setVisibility(View.VISIBLE);
                msgView.setText(message);
            }
            if (contentView != null) {
                LinearLayout linearLayout = layout
                        .findViewById(R.id.content);
                linearLayout.setVisibility(View.VISIBLE);
                linearLayout.addView(contentView);
            }
            return dialog;
        }
    }

}
