package com.app.court.helpers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import com.app.court.R;
import com.app.court.entities.MediaEntity;
import com.app.court.ui.views.AnyEditTextView;
import com.app.court.ui.views.AnyTextView;
import com.app.court.ui.views.CustomRatingBar;
import com.app.court.ui.views.TouchImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DialogHelper {
    private Dialog dialog;
    private Context context;
    private ImageLoader imageLoader;
    private RadioGroup rg;

    public DialogHelper(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);
        imageLoader = ImageLoader.getInstance();
    }

    public Dialog initlogout(int layoutID, View.OnClickListener onokclicklistener, View.OnClickListener oncancelclicklistener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button okbutton = (Button) dialog.findViewById(R.id.btn_yes);
        okbutton.setOnClickListener(onokclicklistener);
        Button cancelbutton = (Button) dialog.findViewById(R.id.btn_No);
        cancelbutton.setOnClickListener(oncancelclicklistener);
        return this.dialog;
    }

    public Dialog caseAcknowledge(int layoutID, View.OnClickListener yes, View.OnClickListener no) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button yesBtn = (Button) dialog.findViewById(R.id.btn_yes);
        yesBtn.setOnClickListener(yes);
        Button noBtn = (Button) dialog.findViewById(R.id.btn_No);
        noBtn.setOnClickListener(no);
        return this.dialog;
    }


    public Dialog addComments(int layoutID, View.OnClickListener onCommentClickListener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        AnyEditTextView edtWriteHere = (AnyEditTextView) dialog.findViewById(R.id.edt_write_heres);
        Button submit = (Button) dialog.findViewById(R.id.btn_submit_comment);
        submit.setOnClickListener(onCommentClickListener);
        return this.dialog;
    }

    public Dialog imageDisplayDialog(int layoutID, View.OnClickListener onokclicklistener, MediaEntity entity) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button okbutton = (Button) dialog.findViewById(R.id.btn_yes);
        okbutton.setOnClickListener(onokclicklistener);
        TouchImageView iv_image = (TouchImageView) dialog.findViewById(R.id.iv_image);
        AnyTextView tv_photo_name = (AnyTextView) dialog.findViewById(R.id.tv_photo_name);
        tv_photo_name.setText(entity.getName() + "");
        AnyTextView tv_photo_date = (AnyTextView) dialog.findViewById(R.id.tv_photo_date);
        tv_photo_date.setText(DateHelper.getLocalDate(entity.getDate()) + " | " + DateHelper.getLocalTime(entity.getDate()));
        imageLoader.displayImage(entity.getPhoto(), iv_image);
        return this.dialog;
    }

    public void showDialog() {
        dialog.show();
    }

    public AnyEditTextView edtWriteHere(){
        AnyEditTextView edtWriteHere = (AnyEditTextView) dialog.findViewById(R.id.edt_write_heres);
        return  edtWriteHere;
    }

    public void setCancelable(boolean isCancelable) {
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelable);
    }

    public void hideDialog() {
        dialog.dismiss();
    }
}
