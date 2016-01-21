package com.jjoseba.pecsmobile.ui.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.ui.EditTextBackEvent;

public class HiddenInputDialog extends DialogFragment{

    public interface InputListener {
        void onText(String text);
    }

    private EditTextBackEvent input;
    private InputListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        Dialog d = super.onCreateDialog(savedInstance);

        final InputMethodManager lManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                input.requestFocusFromTouch();
                lManager.showSoftInput(input, 0);
            }
        });
        return d;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Dialog dialog = getDialog();
        dialog.setCancelable(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCanceledOnTouchOutside(true);

        input = new EditTextBackEvent(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        input.setFocusableInTouchMode(true);
        input.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        input.setPadding(15, 0, 0, 0);
        input.setBackgroundResource(R.color.transparent);

        return input;
    }

    public void onResume()
    {
        Window window = getDialog().getWindow();
        final WindowManager.LayoutParams attrs = window.getAttributes();

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);

        attrs.gravity = Gravity.TOP | Gravity.LEFT;
        attrs.horizontalMargin = 0;
        attrs.verticalMargin = 0;
        attrs.height = (int) (metrics.density * 75);
        attrs.width = metrics.widthPixels;

        window.setAttributes(attrs);

        input.setOnEditTextImeBackListener(new EditTextBackEvent.EditTextImeBackListener() {
            @Override
            public void onImeBack(EditTextBackEvent ctrl, String text) {
                getDialog().dismiss();
            }
        });

        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String tempCardLabel = input.getText().toString();
                if (!tempCardLabel.equals("")) {
                    if (listener != null) listener.onText(tempCardLabel);
                }
                getDialog().dismiss();
                return true;
            }
        });

        super.onResume();
    }


    public void setOnTextListener(InputListener listener) {
        this.listener = listener;
    }
}
