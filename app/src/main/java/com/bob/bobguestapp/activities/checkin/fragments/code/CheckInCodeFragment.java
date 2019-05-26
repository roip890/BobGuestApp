package com.bob.bobguestapp.activities.checkin.fragments.code;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.checkin.connector.CheckInConnector;
import com.bob.bobguestapp.activities.checkin.fragments.code.codetypedialog.CheckInCodeTypeDialogView;
import com.bob.bobguestapp.activities.checkin.fragments.code.codetypedialog.CheckInTypeDialogListener;
import com.bob.bobguestapp.activities.intro.fragments.authentication.AuthenticationFragment;
import com.bob.bobguestapp.activities.qrscanner.QRScannerActivity;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.uimodule.finals;
import com.bob.uimodule.image.ImageUtilsManager;
import com.bob.uimodule.views.loadingcontainer.ManagementFragment;
import com.bob.uimodule.views.textviews.MyButton;
import com.bob.uimodule.views.textviews.MyEditText;
import com.bumptech.glide.Glide;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class CheckInCodeFragment extends ManagementFragment {

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";

    //check in url
    private static final String QR_CHECK_IN_URL = BOB_SERVER_MOBILE_SERVICES_URL + "qr_check_in_suffix";

    //activity result codes
    private final int QR_CODE = 0x00000fff;
    private final int SELECT_PICTURE = 0x000000ff;

    //screen states
    public static final int CHECK_IN_CODE = 10;

    //check in connector
    private CheckInConnector checkInConnector;

    //QR check in
    private LinearLayout checkInCodeLayout;
    private TextView checkInCodeTitleTextView, checkInCodeInfoTextView,
            checkInCodeErrorTextView, checkInCodeMessageTextView, checkInCodeLinkTextView,
            checkInCodeCodeTitleTextView, checkInCodeCodeTextTextView;
    private MyButton checkInCodeCodeButton, checkInCodeCodeScanButton,
            checkInCodeCodeTextButton, checkInCodeCodeUploadButton;
    private ImageView checkInCodeCodeImageView;
    private MaterialDialog checkInCodeTypeDialog;
    private MyEditText checkInCodeCodeDialogEditText;
    private MyButton checkInCodeCodeDialogPositiveButton, checkInCodeCodeDialogNegativeButton;

    public CheckInCodeFragment() {

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.AUTHENTICATION_FRAGMENT_SKIN);

    }

    public static AuthenticationFragment newInstance() {
        AuthenticationFragment fragment = new AuthenticationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //get arguments
//            mParam1 = getArguments().getString(ARG_PARAM1);
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        this.mListener = null
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateMainView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

        //init management skin
        this.managementViewContainer.setScreenSkin(this.screenSkin);

        //view
        View view = inflater.inflate(R.layout.fragment_authentication, container, false);

        //init check in code layouts
        this.initCheckInCodeLayout(view);

        this.managementViewContainer.setScreenState(CHECK_IN_CODE);

        return view;

    }

    //main view screen state
    @Override
    protected void setMainViewScreenState(int screenState) {

        this.checkInCodeLayout.setVisibility(INVISIBLE);

        switch (screenState) {

            case CHECK_IN_CODE:
                this.checkInCodeLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //check in connector
    public void setCheckInConnector(CheckInConnector checkInConnector) {
        this.checkInConnector = checkInConnector;
    }

    //check in code
    private void initCheckInCodeLayout(View view) {

        //check in code scan button
        this.initCheckInCodeScanButton(view);

        //check in code text button
        this.initCheckInCodeTypeButton(view);

        //check in code upload button
        this.initCheckInCodeUploadButton(view);

        //check in code code image view
        this.initCheckInCodeImageView(view);

        //check in code code title
        this.initCheckInCodeTitle(view);

        //check in code code text
        this.initCheckInCodeText(view);

        //check in code error
        this.initCheckInCodeError(view);

        //check in code button
        this.initCheckInCodeButton(view);

    }

    private void initCheckInCodeScanButton(View view) {

        this.checkInCodeCodeScanButton = (MyButton) view.findViewById(R.id.qr_check_in_code_scan_button);

        MyAppThemeUtilsManager.get().initRoundIconButton(this.checkInCodeCodeScanButton, "Scan",
                ContextCompat.getDrawable(this.getContext(), R.drawable.ic_qr_code_white), this.getContext(), this.screenSkin);

        this.checkInCodeCodeScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckInCodeFragment.this.onCheckInCodeScanButtonClick();

                //                new IntentIntegrator(CheckInActivity.this)
                //                        .setOrientationLocked(false)
                //                        .setCaptureActivity(QRScannerActivity.class)
                //                        .initiateScan();
                ////
                ////
                ////
                ////                new IntentIntegrator(CheckInActivity.this)
                ////                        .setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
                ////                        .setPrompt("Scan a barcode")
                ////                        .setCameraId(0)
                ////                        .setBeepEnabled(false)
                ////                        .setBarcodeImageEnabled(true)
                ////                        .setOrientationLocked(false)
                ////                        .initiateScan();
            }
        });

    }

    private void onCheckInCodeScanButtonClick() {

        Intent intent = new Intent(CheckInCodeFragment.this.getContext(), QRScannerActivity.class);
        CheckInCodeFragment.this.startActivityForResult(intent, QR_CODE);

    }

    private void initCheckInCodeTypeButton(View view) {

        //qr check in scan button
        this.checkInCodeCodeTextButton = (MyButton) view.findViewById(R.id.qr_check_in_code_text_button);
        MyAppThemeUtilsManager.get().initRoundIconButton(this.checkInCodeCodeTextButton, "Type",
                ContextCompat.getDrawable(this.getContext(), R.drawable.ic_title_white_24dp), this.getContext(), this.screenSkin);
        this.checkInCodeCodeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckInCodeFragment.this.onCheckInCodeTypeButton();

            }
        });

    }

    private void onCheckInCodeTypeButton() {

        CheckInCodeTypeDialogView checkInCodeTypeDialogView = new CheckInCodeTypeDialogView(CheckInCodeFragment.this.getContext());
        checkInCodeTypeDialogView.setCheckInTypeDialogListener(new CheckInTypeDialogListener() {
            @Override
            public void onSuccessClick(String code) {

                CheckInCodeFragment.this.setCodeFromString(code);

                if (CheckInCodeFragment.this.checkInCodeTypeDialog != null) {
                    CheckInCodeFragment.this.checkInCodeTypeDialog.dismiss();
                }

            }

            @Override
            public void onCancelClick() {

                if (CheckInCodeFragment.this.checkInCodeTypeDialog != null) {
                    CheckInCodeFragment.this.checkInCodeTypeDialog.dismiss();
                }

            }
        });

        CheckInCodeFragment.this.checkInCodeTypeDialog = new MaterialDialog.Builder(CheckInCodeFragment.this.getContext())
                .title("Type Your Code")
                .titleGravity(finals.dialogGravity.get("center"))
                .customView(checkInCodeTypeDialogView, true)
                .backgroundColor(ContextCompat.getColor(CheckInCodeFragment.this.getContext(), R.color.colorPrimary))
                .show();

    }

    private void initCheckInCodeUploadButton(View view) {

        this.checkInCodeCodeUploadButton = (MyButton) view.findViewById(R.id.qr_check_in_code_upload_button);

        MyAppThemeUtilsManager.get().initRoundIconButton(this.checkInCodeCodeUploadButton, "Upload",
                ContextCompat.getDrawable(this.getContext(), R.drawable.ic_file_upload_white_24dp), this.getContext(), this.screenSkin);

        this.checkInCodeCodeUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckInCodeFragment.this.onCheckInCodeUploadButton();

            }
        });

    }

    private void onCheckInCodeUploadButton() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

    }

    private void initCheckInCodeImageView(View view) {

        this.checkInCodeCodeImageView = (ImageView) view.findViewById(R.id.qr_check_in_code_image_view);
        Glide.with(this)
                .asDrawable()
                .load(R.drawable.ic_qr_press_white)
                .into(this.checkInCodeCodeImageView);

    }

    private void initCheckInCodeTitle(View view) {

        this.checkInCodeCodeTitleTextView = (TextView) view.findViewById(R.id.qr_check_in_code_title);

        MyAppThemeUtilsManager.get().initTextView(this.checkInCodeCodeTitleTextView, "Code:", this.getContext(), this.screenSkin);

        this.checkInCodeCodeTitleTextView.setTextSize(this.getResources().getDimension(R.dimen.default_field_info_text_size));

        this.checkInCodeCodeTitleTextView.setPaintFlags(this.checkInCodeCodeTitleTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

    }

    private void initCheckInCodeText(View view) {

        this.checkInCodeCodeTextTextView = (TextView) view.findViewById(R.id.qr_check_in_code_text);
        MyAppThemeUtilsManager.get().initTextView(this.checkInCodeCodeTextTextView, "", this.getContext(), this.screenSkin);
        this.checkInCodeCodeTextTextView.setTextSize(this.getResources().getDimension(R.dimen.default_field_info_text_size));
        this.checkInCodeCodeTextTextView.setMaxLines(5);
        this.checkInCodeCodeTextTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.checkInCodeCodeTextTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CheckInCodeFragment.this.checkCheckInCodeFields();
            }
        });

    }

    private void initCheckInCodeError(View view) {

        this.checkInCodeErrorTextView = (TextView) view.findViewById(R.id.qr_check_in_error_text_view);
        MyAppThemeUtilsManager.get().initTextView(this.checkInCodeErrorTextView, "", this.getContext(), this.screenSkin);
        this.checkInCodeErrorTextView.setTextSize(this.getResources().getDimension(R.dimen.default_field_info_text_size));
        this.checkInCodeErrorTextView.setTextColor(MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_FIELD_ERROR_COLOR, this.screenSkin));
        this.checkInCodeErrorTextView.setMaxLines(3);

    }

    private void initCheckInCodeButton(View view) {

        this.checkInCodeCodeButton = (MyButton) view.findViewById(R.id.qr_check_in_check_in_button);
        MyAppThemeUtilsManager.get().initMyButton(this.checkInCodeCodeButton, "Check In", this.getContext(), this.screenSkin);
        this.checkInCodeCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInCodeFragment.this.onCheckInCodeCodeButtonClick(null, null, null);
            }
        });
        this.checkInCodeCodeButton.setEnabled(false);

    }

    private void onCheckInCodeCodeButtonClick(String hotel, String guestEmail, String qr){

//        this.toast = new Toast(this);
//        this.toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
//        this.toast.setDuration(Toast.LENGTH_LONG);
//        this.toast.setView(this.initToastSuccessView());
//        this.toast.show();


    }

    private void checkCheckInCodeFields() {

        if ((this.checkInCodeCodeTextTextView!= null)
                && (this.checkInCodeCodeTextTextView.getText() != null)
                && (!this.checkInCodeCodeTextTextView.getText().toString().equals(""))){
            this.checkInCodeCodeButton.setEnabled(true);
        } else {
            this.checkInCodeCodeButton.setEnabled(false);
        }

    }

    private void setCheckInCodeError(String error) {

        //go to main view
        this.managementViewContainer.setScreenSkin(CHECK_IN_CODE);

        //set the error message
        this.checkInCodeErrorTextView.setText(error);

    }


    // Get the results from activities:
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case SELECT_PICTURE: {
                //get bitmap from gallery
                Uri imageUri = data.getData();
                try {

                    Bitmap generatedQRCode = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), imageUri);

                    //set code from image
                    this.setCodeFromQRCodeImage(generatedQRCode);

                } catch (IOException e) {

                    //set default empty code
                    this.setCodeFromString("");

                }
                break;
            }
            case QR_CODE: {

                if(resultCode == RESULT_OK) {

                    String code = (String) data.getExtras().get("QR_CODE");

                    //set code from string
                    this.setCodeFromString(code);

                } else {

                }
                break;
            }
            default:
                break;
        }

    }

    private void setCodeFromString(String code) {

        if (code != null && !code.equals("")) {

            //set code text
            this.checkInCodeCodeTextTextView.setText(code);

            //get image from string
            Bitmap generatedBitmap = ImageUtilsManager.get().generateQRBitmap(
                    code,
                    (int) CheckInCodeFragment.this.getContext().getResources().getDimension(R.dimen.check_in_activity_qr_code_image_width),
                    (int) CheckInCodeFragment.this.getContext().getResources().getDimension(R.dimen.check_in_activity_qr_code_image_height),
                    MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_QR_BITMAP_PRIMARY_COLOR, this.screenSkin),
                    MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_QR_BITMAP_SECONDARY_COLOR, this.screenSkin)
            );



            //round corners
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), generatedBitmap);
            final float roundPx = (float) generatedBitmap.getWidth() * 0.06f;
            roundedBitmapDrawable.setCornerRadius(roundPx);

            //set code image
            this.checkInCodeCodeImageView.setImageDrawable(roundedBitmapDrawable);

        } else {

            //set code text
            this.checkInCodeCodeTextTextView.setText("");

            //set code image
            Glide.with(this)
                    .asDrawable()
                    .load(R.drawable.ic_qr_press_white)
                    .into(this.checkInCodeCodeImageView);

        }


    }

    private void setCodeFromQRCodeImage(Bitmap imageCode) {

        try {

            //decode bitmap qr image
            int width = imageCode.getWidth();
            int height = imageCode.getHeight();
            int[] pixels = new int[width * height];
            imageCode.getPixels(pixels, 0, width, 0, 0, width, height);
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
            Reader reader = new MultiFormatReader();
            Result result = reader.decode(binaryBitmap);

            //get string code from bitmap
            String code = result.getText();

            //set code from string
            this.setCodeFromString(code);

        } catch (Exception e) {

            //set default empty code
            this.setCodeFromString("");

        }

    }

    //validations
    private boolean isValidHotelName(String hotelName) {

        return hotelName != null && !hotelName.equals("");

    }

    private boolean isValidQRCode(String code) {
        return true;
    }

    //back pressed handler
    public void onBackPressed() {

    }

}
