package com.walng.dhagz.paypalcalc;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * @author Dhagz
 * @since 2016-03-27
 */
public class ChangeDialog {

    public interface OnOkClickListener {
        void onClick(DialogInterface dialog, String userInput);
    }

    public static void show(Context context, @StringRes int hintLabel, String defaultText, final OnOkClickListener onOkClickListener) {
        // get dialog_change.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_change, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set dialog_change.xml to alert dialog builder
        alertDialogBuilder.setView(promptsView);

        final TextInputLayout layout = (TextInputLayout) promptsView.findViewById(R.id.value_container);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.value);
        layout.setHint(context.getString(hintLabel));
        userInput.setHint(hintLabel);
        userInput.setText(defaultText);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (onOkClickListener != null) {
                            onOkClickListener.onClick(dialogInterface, userInput.getText().toString());
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}
