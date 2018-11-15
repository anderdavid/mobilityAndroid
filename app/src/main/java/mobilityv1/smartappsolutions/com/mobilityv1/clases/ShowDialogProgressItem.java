package mobilityv1.smartappsolutions.com.mobilityv1.clases;

/**
 * Created by user on 07/12/2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import mobilityv1.smartappsolutions.com.mobilityv1.R;

public class ShowDialogProgressItem {

    private static final String TAG = "ShowDialogProgressItem";

    String msg;
    Activity activity;
    Context context;
    AlertDialog.Builder builder;
    AlertDialog dialog;

    Timer timer;

    ShowDialogProgressItemListener mShowDialogProgressItemListener;

    public ShowDialogProgressItem(String msg, Activity activity, Context context) {

        Log.d(TAG,"ShowDialogProgressItem()");
        this.msg = msg;
        this.activity = activity;
        this.context = context;
    }

    public void showDialog(){

        Log.d(TAG,"showDialog()");

        builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View vi = inflater.inflate(R.layout.dialog_progress_bar, null);
        builder.setView(vi);
        builder.setTitle(msg);
        builder.setIcon(R.drawable.ic_hourglass_empty_black_24dp);
        builder.setCancelable(false);
        dialog =builder.create();
        dialog.show();

        timer =new Timer(10000, 1000);
        timer.start();

    }

    public void dimissDialog(){

        dialog.cancel();
        timer.cancel();

    }

    public class Timer extends CountDownTimer {

        Timer(long millisInFuture, long countDownInterval){
            super(millisInFuture,countDownInterval);
        }

        @Override
        public void onTick(long l) {

            Log.d(TAG,"onTick()");
        }

        @Override
        public void onFinish() {

            Log.d(TAG,"onFinish()");
            dimissDialog();
            mShowDialogProgressItemListener.onTimeOut();
        }
    }

    public interface ShowDialogProgressItemListener{

        public void onTimeOut();

    }

    public void setmShowDialogProgressItemListener(ShowDialogProgressItemListener mShowDialogProgressItemListener) {

        this.mShowDialogProgressItemListener = mShowDialogProgressItemListener;

    }
}