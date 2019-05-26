package com.bob.bobguestapp.activities.qrscanner;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.checkin.fragments.code.CheckInCodeFragment;
import com.bob.bobguestapp.activities.qrscanner.qrscannercodedialog.QRScannerCodeDialogView;
import com.bob.bobguestapp.activities.qrscanner.qrscannercodedialog.QRScannerDialogListener;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.finals;
import com.bob.uimodule.image.ImageUtilsManager;
import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

/**
 * Custom Scannner Activity extending from Activity to display a custom layout form scanner view.
 */
public class QRScannerActivity extends Activity {

    //app theme
    int appTheme = MyAppThemeUtilsManager.DEFAULT_THEME;
    int screenSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;

    //background
    private ConstraintLayout backgroundLayout;

    //scanner
    private DecoratedBarcodeView qrScannerBarcodeScannerView;

    //footer layout
    private RelativeLayout qrScannerFooterButtonsLayout;
    private ImageView qrScannerFooterPreviewImageView;
    private ImageView qrScannerFooterCameraFacingButton;
    private ImageView qrScannerFooterFlashlightButton;
    private ImageView qrScannerFooterPauseButton;
    private ImageView qrScannerFooterResumeButton;

    //barcode
    private BeepManager qrScannerBeepManager;
    private BarcodeCallback qrScannerBarcodeScannerCallback;
    private boolean isFlashlightOn;
    private String code;

    //code dialog
    private MaterialDialog qrScannerCodeMaterialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this).getSkin(appTheme, MyAppThemeUtilsManager.MAIN_ACTIVITY_SKIN);

        //init code
        this.initCode();

        //view
        this.setContentView(R.layout.activity_qr_scanner);

        //background
        this.initBackground();

        //scanner
        this.initBarcodeView();

        //beep manager
        this.initBeepManager();

        //footer
        this.initQRScannerFooterLayout();

    }

    private void initCode() {

        this.code = null;

    }

    private void initBeepManager() {

        this.qrScannerBeepManager = new BeepManager(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.qrScannerBarcodeScannerView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.qrScannerBarcodeScannerView.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return this.qrScannerBarcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);

    }

    //background
    private void initBackground() {

        this.backgroundLayout = (ConstraintLayout) findViewById(R.id.qr_scanner_activity_background);

        this.backgroundLayout.setBackgroundColor(MyAppThemeUtilsManager.get(this).getColor(MyAppThemeUtilsManager.DEFAULT_ACTIVITY_BACKGROUND_COLOR_PRIMARY, this.screenSkin));

    }

    //scanner
    protected void initBarcodeView() {

        this.qrScannerBarcodeScannerView = (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);

        this.qrScannerBarcodeScannerView.setStatusText("");
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        this.qrScannerBarcodeScannerView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));

        //flashlight state
        this.initFlashlightState();

        //flashlight listener
        this.initFlashlightListener();

        //barcode callback
        this.initBarcodeCallback();


    }

    private void initFlashlightState() {

        this.isFlashlightOn = false;

    }

    private void initFlashlightListener() {

        this.qrScannerBarcodeScannerView.setTorchListener(new DecoratedBarcodeView.TorchListener() {
            @Override
            public void onTorchOn() {
//                QRScannerActivity.this.qrScannerFooterFlashlightButton
            }

            @Override
            public void onTorchOff() {
//                QRScannerActivity.this.qrScannerFooterFlashlightButton
            }
        });

    }

    private void initBarcodeCallback() {

        this.qrScannerBarcodeScannerCallback = new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if(result.getText() == null
                        || result.getText().equals(QRScannerActivity.this.code)
                        || QRScannerActivity.this.qrScannerCodeMaterialDialog.isShowing()) {
                    // Prevent duplicate scans
                    return;
                }

                QRScannerActivity.this.setCode(result.getText());

                QRScannerActivity.this.initQRScannerCodeMaterialDialog();

                QRScannerActivity.this.qrScannerBeepManager.playBeepSoundAndVibrate();

            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
            }
        };

        this.qrScannerBarcodeScannerView.decodeContinuous(this.qrScannerBarcodeScannerCallback);

    }

    //footer view
    private void initQRScannerFooterLayout() {

        this.qrScannerFooterButtonsLayout = (RelativeLayout) findViewById(R.id.qr_scanner_activity_footer_buttons_layout);

        //qr scanner footer background
        this.initQRScannerFooterBackground();

        //qr scanner footer preview image view
        this.initQRScannerFooterPreviewImageView();

        //qr scanner footer camera facing button
        this.initQRScannerFooterCameraFacingButton();

        //qr scanner footer flashlight button
        this.initQRScannerFooterFlashlightButton();

        //qr scanner footer pause button
        this.initQRScannerFooterPauseButton();

        //qr scanner footer resume button
        this.initQRScannerFooterResumeButton();

    }

    private void initQRScannerFooterBackground() {

        GradientDrawable shapeDrawable = new GradientDrawable();
        shapeDrawable.setColor(MyAppThemeUtilsManager.get(this).getColor(MyAppThemeUtilsManager.DEFAULT_ACTIVITY_BACKGROUND_COLOR_PRIMARY, this.screenSkin));
        shapeDrawable.setCornerRadius(UIUtilsManager.get().convertDpToPixels(this, 10));
        this.qrScannerFooterButtonsLayout.setBackground(shapeDrawable);

    }

    private void initQRScannerFooterPreviewImageView() {

        this.qrScannerFooterPreviewImageView = (ImageView) findViewById(R.id.qr_scanner_activity_footer_preview_image_view);

        Glide.with(this)
                .asDrawable()
                .load(R.drawable.ic_qr_press_white)
                .into(this.qrScannerFooterPreviewImageView);

    }

    private void initQRScannerFooterCameraFacingButton() {

        this.qrScannerFooterCameraFacingButton = (ImageView) findViewById(R.id.qr_scanner_activity_footer_camera_facing_button);

        Glide.with(this)
                .asDrawable()
                .load(R.drawable.ic_switch_camera_white_24dp)
                .into(this.qrScannerFooterCameraFacingButton);
        this.qrScannerFooterCameraFacingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QRScannerActivity.this.onQRScannerFooterCameraFacingButtonClick();

            }
        });

    }

    private void onQRScannerFooterCameraFacingButtonClick() {

    }

    private void initQRScannerFooterFlashlightButton() {

        this.qrScannerFooterFlashlightButton = (ImageView) findViewById(R.id.qr_scanner_activity_footer_flashlight_button);

        Glide.with(this)
                .asDrawable()
                .load(R.drawable.ic_flash_on_white_24dp)
                .into(this.qrScannerFooterFlashlightButton);

        this.qrScannerFooterFlashlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QRScannerActivity.this.onQRScannerFooterFlashlightButtonClick();

            }
        });

        if (!this.hasFlash()) {
            this.qrScannerFooterFlashlightButton.setEnabled(false);
        }

    }

    private void onQRScannerFooterFlashlightButtonClick() {

        if (this.isFlashlightOn) {
            this.qrScannerBarcodeScannerView.setTorchOff();
            this.isFlashlightOn = false;
        } else {
            this.qrScannerBarcodeScannerView.setTorchOn();
            this.isFlashlightOn = true;
        }

    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    private void initQRScannerFooterPauseButton() {

        this.qrScannerFooterPauseButton = (ImageView) findViewById(R.id.qr_scanner_activity_footer_pause_button);

        Glide.with(this)
                .asDrawable()
                .load(R.drawable.ic_pause_white_24dp)
                .into(this.qrScannerFooterPauseButton);

        this.qrScannerFooterPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QRScannerActivity.this.onQRScannerFooterPauseButtonClick();

            }
        });

    }

    private void onQRScannerFooterPauseButtonClick() {

        this.qrScannerBarcodeScannerView.pause();

    }

    private void initQRScannerFooterResumeButton() {

        this.qrScannerFooterResumeButton = (ImageView) findViewById(R.id.qr_scanner_activity_footer_resume_button);

        Glide.with(this)
                .asDrawable()
                .load(R.drawable.ic_play_arrow_white_24dp)
                .into(this.qrScannerFooterResumeButton);

        this.qrScannerFooterResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QRScannerActivity.this.onQRScannerFooterResumeButtonClick();

            }
        });

    }

    private void onQRScannerFooterResumeButtonClick() {

        this.qrScannerBarcodeScannerView.resume();
    }

    //code dialog
    private void initQRScannerCodeMaterialDialog(){

        this.qrScannerCodeMaterialDialog = new MaterialDialog.Builder(this)
                .title("Confirm Code")
                .titleGravity(finals.dialogGravity.get("center"))
                .customView(this.initQRScannerCodeDialogView(), true)
                .backgroundColor(
                        MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_DIALOG_BACKGROUND_COLOR_PRIMARY, this.screenSkin)
                )
                .show();

        View dialogView = this.qrScannerCodeMaterialDialog.getWindow().getDecorView();
        GradientDrawable shapeDrawable = new GradientDrawable();
        shapeDrawable.setColor(MyAppThemeUtilsManager.get(this).getColor(MyAppThemeUtilsManager.DEFAULT_DIALOG_BACKGROUND_COLOR_PRIMARY, this.screenSkin));
        shapeDrawable.setCornerRadius(UIUtilsManager.get().convertDpToPixels(this, 10));
        dialogView.setBackground(shapeDrawable);

        ViewGroup.LayoutParams params = this.qrScannerCodeMaterialDialog.getWindow().getAttributes();
        params.width = (int) this.getResources().getDimension(R.dimen.requests_list_fragment_filter_requests_dialog_width);
        this.qrScannerCodeMaterialDialog.getWindow().setAttributes((WindowManager.LayoutParams) params);

    }

    private QRScannerCodeDialogView initQRScannerCodeDialogView() {

        QRScannerCodeDialogView qrScannerCodeDialogView = new QRScannerCodeDialogView(this);

        qrScannerCodeDialogView.setCode(this.code);

        qrScannerCodeDialogView.setQRScannerDialogListener(new QRScannerDialogListener() {
            @Override
            public void onSuccessClick(String code) {

                if (QRScannerActivity.this.code != null && !QRScannerActivity.this.code.equals("")) {
                    Intent intent = new Intent();
                    intent.putExtra("QR_CODE", QRScannerActivity.this.code);

//                    try {
//                        //Write file
//                        String filename = "bitmap.png";
//                        FileOutputStream stream = QRScannerActivity.this.openFileOutput(filename, Context.MODE_PRIVATE);
//                        QRScannerActivity.this.codeBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//
//                        //Cleanup
//                        stream.close();
//                        QRScannerActivity.this.codeBitmap.recycle();
//
//                        //Pop intent
//                        intent.putExtra("QR_BITMAP_FILENAME", filename);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }


//                    intent.putExtra("QR_BITMAP", QRScannerActivity.this.codeBitmap);

                    QRScannerActivity.this.setResult(RESULT_OK, intent);
                    QRScannerActivity.this.finish();
                } else {
                    Intent intent = new Intent();
                    QRScannerActivity.this.setResult(RESULT_CANCELED, intent);
                    QRScannerActivity.this.finish();
                }

            }

            @Override
            public void onCancelClick() {

                if (QRScannerActivity.this.qrScannerCodeMaterialDialog != null) {

                    QRScannerActivity.this.qrScannerCodeMaterialDialog.dismiss();

                }

            }
        });

        return qrScannerCodeDialogView;
    }

    protected void setCode(String code) {

        if (code != null && !code.equals("")) {

            QRScannerActivity.this.code = code;

        } else {

            QRScannerActivity.this.code = null;

        }

        this.setCodeFromString(this.code);

    }

    private void setCodeFromString(String code) {

        if (code != null && !code.equals("")) {

            //get image from string
            Bitmap generatedBitmap = ImageUtilsManager.get().generateQRBitmap(
                    code,
                    (int) QRScannerActivity.this.getResources().getDimension(R.dimen.check_in_activity_qr_code_image_width),
                    (int) QRScannerActivity.this.getResources().getDimension(R.dimen.check_in_activity_qr_code_image_height),
                    MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_QR_BITMAP_PRIMARY_COLOR, this.screenSkin),
                    MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_QR_BITMAP_SECONDARY_COLOR, this.screenSkin)
            );

            //round corners
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), generatedBitmap);
            final float roundPx = (float) generatedBitmap.getWidth() * 0.06f;
            roundedBitmapDrawable.setCornerRadius(roundPx);

            //set code image
            this.qrScannerFooterPreviewImageView.setImageDrawable(roundedBitmapDrawable);

        } else {

            //set code image
            Glide.with(this)
                    .asDrawable()
                    .load(R.drawable.ic_qr_press_white)
                    .into(this.qrScannerFooterPreviewImageView);

        }


    }

}
