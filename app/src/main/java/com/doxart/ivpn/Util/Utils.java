package com.doxart.ivpn.Util;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

import com.doxart.ivpn.DB.UsageDB;
import com.doxart.ivpn.Interfaces.OnAnswerListener;
import com.doxart.ivpn.R;
import com.doxart.ivpn.databinding.AskViewBinding;

import java.util.Date;

public class Utils {

    public static Dialog setProgress(Context context){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.progress_view);
        if (dialog.getWindow() != null) dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
        }

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_background);

        return dialog;
    }

    public static String getImgURL(int resourceId) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resourceId).toString();
    }

    public static void setSignalView(Context context, CardView s1, CardView s2, CardView s3, int latency) {
        int resGreen = ContextCompat.getColor(context, R.color.green);
        int resDark = ContextCompat.getColor(context, R.color.blat1);
        int resOrange = ContextCompat.getColor(context, R.color.orange);
        int resRed = ContextCompat.getColor(context, R.color.red);

        if (latency < 100) {
            s1.setCardBackgroundColor(resGreen);
            s2.setCardBackgroundColor(resGreen);
            s3.setCardBackgroundColor(resGreen);
        } else if (latency > 100 & latency < 300) {
            s1.setCardBackgroundColor(resOrange);
            s2.setCardBackgroundColor(resOrange);
            s3.setCardBackgroundColor(resDark);
        } else if (latency > 300) {
            s1.setCardBackgroundColor(resRed);
            s2.setCardBackgroundColor(resDark);
            s3.setCardBackgroundColor(resDark);
        } else {
            s1.setCardBackgroundColor(resDark);
            s2.setCardBackgroundColor(resDark);
            s3.setCardBackgroundColor(resDark);
        }
    }

    public static String getToday() {
        Date today = new Date();
        today.setTime(today.getTime()+100000);

        return UsageDB.dateFormat.format(today);
    }

    public static Dialog askQuestion(Context context, String title, String contain,
                                     String positive, String negative, String other,
                                     SpannableStringBuilder isSpanned, boolean showCheckImg,
                                     OnAnswerListener answerListener){
        View view = LayoutInflater.from(context).inflate(R.layout.ask_view, null);
        AskViewBinding b = AskViewBinding.bind(view);

        Dialog ask = new Dialog(context);
        ask.setContentView(b.getRoot());
        if (ask.getWindow() != null) ask.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ask.setCancelable(false);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (ask.getWindow() != null) {
            layoutParams.copyFrom(ask.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            ask.getWindow().setAttributes(layoutParams);
        }

        ask.getWindow().setBackgroundDrawableResource(R.drawable.alert_dialog_background);

        b.askTitle.setText(HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_COMPACT));

        if (showCheckImg) {
            b.checkImg.setVisibility(View.VISIBLE);
            b.askNegative.setVisibility(View.GONE);
        }

        if(!contain.isEmpty()) b.askContain.setText(Html.fromHtml(contain));
        else b.askContain.setText(isSpanned);

        if (!positive.isEmpty()) b.askPositive.setText(positive);
        if (!negative.isEmpty()) b.askNegative.setText(negative);
        if (!other.isEmpty()) {
            b.askOther.setVisibility(View.VISIBLE);
            b.askOther.setText(other);
        }
        else b.askOther.setVisibility(View.GONE);

        b.askPositive.setOnClickListener(v -> {
            ask.dismiss();
            if (answerListener != null) {
                answerListener.onPositive();
            }
        });

        b.askNegative.setOnClickListener(v -> {
            ask.dismiss();
            if (answerListener != null) {
                answerListener.onNegative();
            }
        });

        b.askOther.setOnClickListener(v -> {
            ask.dismiss();
            if (answerListener != null) {
                answerListener.onOther();
            }
        });
        return ask;
    }

    public static boolean checkConnection(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();

        return nInfo != null && nInfo.isConnectedOrConnecting();
    }
}
