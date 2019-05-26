package com.bob.bobguestapp.activities.qrscanner.qrscannercodedialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.checkin.fragments.code.CheckInCodeFragment;
import com.bob.bobguestapp.activities.qrscanner.QRScannerActivity;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.uimodule.image.ImageUtilsManager;
import com.bob.uimodule.views.loadingcontainer.ManagementLayout;
import com.bumptech.glide.Glide;

import top.defaults.drawabletoolbox.DrawableBuilder;

public class QRScannerCodeDialogView extends ManagementLayout {

    //main view screen states
    public static final int QR_SCANNER_CODE = 10;

    //code layout
    private RelativeLayout qrScannerCodeLayout;
    private RelativeLayout qrScannerCodeButtonsLayout;
    private ImageView qrScannerCodePreviewImageView;
    private TextView qrScannerCodeTitleTextView;
    private TextView qrScannerCodeCodeTextView;
    private ImageView qrScannerCodeCancelButton;
    private ImageView qrScannerCodeConfirmButton;

    //listener
    private QRScannerDialogListener qrScannerDialogListener;

    //code
    private String code;

    //constructors
    public QRScannerCodeDialogView(Context context) {
        this(context, null);
    }

    public QRScannerCodeDialogView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QRScannerCodeDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);



    }


    @Override
    protected View onCreateMainView() {

        return this.initQRScannerCodeDialogView();

    }

    @Override
    protected void setMainViewScreenState(int screenState) {

        this.qrScannerCodeLayout.setVisibility(INVISIBLE);

        switch (screenState) {

            case QR_SCANNER_CODE:
                this.qrScannerCodeLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }


    }

    //main view
    private View initQRScannerCodeDialogView() {

        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View view = inflater.inflate(R.layout.dialog_qr_scanner_code, new RelativeLayout(this.getContext()));

        this.initQRScannerCodeLayout();

        return view;

    }

    private void initQRScannerCodeLayout () {

        this.qrScannerCodeLayout = (RelativeLayout) findViewById(R.id.qr_scanner_code_dialog_code_layout);

        //qr scanner image view
        this.initQRScannerCodeImageView();

        //qr scanner code text layout
        this.initQRScannerCodeTextLayout();


        //qr scanner buttons
        this.initQRScannerCodeButtonsLayout();

    }

    private void initQRScannerCodeImageView() {

        this.qrScannerCodePreviewImageView = (ImageView) findViewById(R.id.qr_scanner_code_dialog_code_image_view);

    }

    private void initQRScannerCodeTextLayout() {

        this.qrScannerCodeButtonsLayout = (RelativeLayout) findViewById(R.id.qr_scanner_code_dialog_code_text_layout);

        //qr scanner code text title
        this.initQRScannerCodeTitleTextView();

        //qr scanner code text
        this.initQRScannerCodeCodeTextView();

    }

    private void initQRScannerCodeTitleTextView() {

        this.qrScannerCodeTitleTextView = (TextView) findViewById(R.id.qr_scanner_code_dialog_code_title_text_view);

    }

    private void initQRScannerCodeCodeTextView() {

        this.qrScannerCodeCodeTextView = (TextView) findViewById(R.id.qr_scanner_code_dialog_code_code_text_view);

    }

    private void initQRScannerCodeButtonsLayout() {

        this.qrScannerCodeButtonsLayout = (RelativeLayout) findViewById(R.id.qr_scanner_code_dialog_code_buttons_layout);

        //qr scanner image view
        this.initQRScannerCodeConfirmButtons();

        //qr scanner image view
        this.initQRScannerCodeCancelButtons();

    }

    private void initQRScannerCodeConfirmButtons() {

        this.qrScannerCodeConfirmButton = (ImageView) findViewById(R.id.qr_scanner_code_dialog_code_confirm_button);

        Glide.with(this)
                .asDrawable()
                .load(R.drawable.ic_check_circle_green_24dp)
                .into(this.qrScannerCodeConfirmButton);

        this.qrScannerCodeConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (QRScannerCodeDialogView.this.qrScannerDialogListener != null
                    && QRScannerCodeDialogView.this.code != null
                    && !QRScannerCodeDialogView.this.code.equals("")) {

                    QRScannerCodeDialogView.this.qrScannerDialogListener.onSuccessClick(QRScannerCodeDialogView.this.code);

                }

            }
        });

    }

    private void initQRScannerCodeCancelButtons() {

        this.qrScannerCodeCancelButton = (ImageView) findViewById(R.id.qr_scanner_code_dialog_code_cancel_button);

        Glide.with(this)
                .asDrawable()
                .load(R.drawable.ic_cancel_red_24dp)
                .into(this.qrScannerCodeCancelButton);

        this.qrScannerCodeCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (QRScannerCodeDialogView.this.qrScannerDialogListener != null) {

                    QRScannerCodeDialogView.this.qrScannerDialogListener.onCancelClick();

                }

//                Intent intent = new Intent();
//                QRScannerActivity.this.setResult(RESULT_CANCELED, intent);
//                QRScannerActivity.this.finish();
            }
        });

    }

    //code
    public void setCode(String code) {

        //set code
        this.code = code;

        //set code text and image
        this.setCodeFromString(this.code);

    }

    private void setCodeFromString(String code) {

        if (code != null && !code.equals("")) {

            //set code text
            this.qrScannerCodeCodeTextView.setText(code);

            //get image from string
            Bitmap generatedBitmap = ImageUtilsManager.get().generateQRBitmap(
                    code,
                    (int) QRScannerCodeDialogView.this.getContext().getResources().getDimension(R.dimen.check_in_activity_qr_code_image_width),
                    (int) QRScannerCodeDialogView.this.getContext().getResources().getDimension(R.dimen.check_in_activity_qr_code_image_height),
                    MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_QR_BITMAP_PRIMARY_COLOR, this.screenSkin),
                    MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_QR_BITMAP_SECONDARY_COLOR, this.screenSkin)
            );

            //round corners
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), generatedBitmap);
            final float roundPx = (float) generatedBitmap.getWidth() * 0.06f;
            roundedBitmapDrawable.setCornerRadius(roundPx);

            //set code image
            this.qrScannerCodePreviewImageView.setImageDrawable(roundedBitmapDrawable);

        } else {

            //set code text
            this.qrScannerCodeCodeTextView.setText("");

            //set code image
            Glide.with(this)
                    .asDrawable()
                    .load(R.drawable.ic_qr_press_white)
                    .into(this.qrScannerCodePreviewImageView);

        }


    }

    //listener
    public void setQRScannerDialogListener(QRScannerDialogListener qrScannerDialogListener) {

        this.qrScannerDialogListener = qrScannerDialogListener;

    }

}
