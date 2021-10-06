package com.perfect.easyshopplus.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.perfect.easyshopplus.Activity.HomeActivity;
import com.perfect.easyshopplus.R;

public class Helper {

    public static void quitapp(HomeActivity activity) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.quit_app_popup, null);
        dialogBuilder.setView(dialogView);

        LinearLayout quit_app_no = (LinearLayout) dialogView.findViewById(R.id.quit_app_no);
        LinearLayout quit_app_yes = (LinearLayout) dialogView.findViewById(R.id.quit_app_yes);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        quit_app_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        quit_app_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finishAffinity();
            }
        });

    }

    public static void setErrorAlert(Activity activity, String strMsg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.error_alert_popup, null);
        dialogBuilder.setView(dialogView);

        TextView tv_dismiss = (TextView) dialogView.findViewById(R.id.tv_dismiss);
        TextView tv_error_msg = (TextView) dialogView.findViewById(R.id.tv_error_msg);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        tv_error_msg.setText(""+strMsg);
        tv_dismiss.setText("Dismiss");

        tv_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}
