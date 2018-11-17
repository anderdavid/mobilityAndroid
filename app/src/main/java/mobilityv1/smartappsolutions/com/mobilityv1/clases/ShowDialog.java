package mobilityv1.smartappsolutions.com.mobilityv1.clases;

/**
 * Created by user on 07/12/2017.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class ShowDialog {

    private static final String TAG = "ShowDialog";
    private static final int TYPE_INFO =1;
    private static final int TYPE_CONFIRMATION =2;

    String message;



    String title;
    int type =TYPE_INFO;
    Activity activity;
    Context context;

    AlertDialog.Builder builder;
    AlertDialog dialog;

    ShowDialogListener showDialogListener;

    public ShowDialog() {
    }

    public ShowDialog(String title,String message, Activity activity, Context context) {
        Log.d(TAG,"ShowDialog()");

        this.message = message;
        this.title = title;
        this.activity = activity;
        this.context = context;
    }

    public ShowDialog( String title,String message, int type, Activity activity, Context context) {
        this.message = message;
        this.title = title;
        this.type = type;
        this.activity = activity;
        this.context = context;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void showDialog(){

        builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);


        if(type==TYPE_CONFIRMATION){

            builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dimissDialog();
                }
            });

        }

        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                showDialogListener.onSetPositiveButton();

            }
        });

        dialog =builder.create();
        dialog.show();
    }

    public void dimissDialog(){

        dialog.cancel();

    }

    public interface ShowDialogListener{

        public void onSetPositiveButton();
    }

    public void setmShowDialogListener(ShowDialogListener mShowDialogListener) {

        this.showDialogListener = mShowDialogListener;
    }
}