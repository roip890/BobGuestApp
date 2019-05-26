package com.bob.bobguestapp.activities.checkin.fragments.code.codetypedialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.bob.bobguestapp.R;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.validators.Validator;
import com.bob.uimodule.icons.Icons;
import com.bob.uimodule.views.loadingcontainer.ManagementLayout;
import com.bob.uimodule.views.textviews.MyButton;
import com.bob.uimodule.views.textviews.MyEditText;
import com.mikepenz.iconics.IconicsDrawable;

public class CheckInCodeTypeDialogView extends ManagementLayout {

    //main view screen states
    public static final int TYPE_CODE = 10;

    //views
    private RelativeLayout checkInCodeTypeDialogLayout;
    private MyEditText qrCheckInCodeDialogEditText;
    private MyButton qrCheckInCodeDialogPositiveButton;
    private MyButton qrCheckInCodeDialogNegativeButton;

    //listener
    private CheckInTypeDialogListener checkInTypeDialogListener;

    //constructors
    public CheckInCodeTypeDialogView(Context context) {
        this(context, null);
    }

    public CheckInCodeTypeDialogView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckInCodeTypeDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @Override
    protected View onCreateMainView() {

        return this.initCheckInCodeDialogView();

    }

    @Override
    protected void setMainViewScreenState(int screenState) {

        this.checkInCodeTypeDialogLayout.setVisibility(INVISIBLE);

        switch (screenState) {

            case TYPE_CODE:
                this.checkInCodeTypeDialogLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }


    }

    //type code layout
    private View initCheckInCodeDialogView() {
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View view = inflater.inflate(R.layout.dialog_check_in_code_text_type, new RelativeLayout(this.getContext()));
        this.checkInCodeTypeDialogLayout = (RelativeLayout) view.findViewById(R.id.dialog_check_in_code_text_type_layout);

        //positive button
        this.initCheckInTypePositiveButton(view);

        //negative button
        this.initCheckInTypeNegativeButton(view);

        //edit text
        this.initCheckInTypeCodeEditText(view);

        return view;
    }

    private void initCheckInTypePositiveButton(View view) {

        this.qrCheckInCodeDialogPositiveButton = (MyButton) view.findViewById(R.id.dialog_check_in_code_text_type_positive_button);

        MyAppThemeUtilsManager.get().initMyButton(this.qrCheckInCodeDialogPositiveButton, "Ok", this.getContext(), this.screenSkin);

        this.qrCheckInCodeDialogPositiveButton.setWidth(
                (int) this.getResources().getDimension(R.dimen.check_in_activity_type_guest_code_dialog_positive_button_width)
        );

        this.qrCheckInCodeDialogPositiveButton.setTextViewTextSize(
                (int) this.getResources().getDimension(R.dimen.check_in_activity_type_guest_code_dialog_positive_button_text_size)
        );

        this.qrCheckInCodeDialogPositiveButton.setEnabled(false);

        this.qrCheckInCodeDialogPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckInCodeTypeDialogView.this.checkInTypeDialogListener != null) {

                    CheckInCodeTypeDialogView.this.checkInTypeDialogListener.onSuccessClick(
                            CheckInCodeTypeDialogView.this.qrCheckInCodeDialogEditText.getText().toString());

                }

            }
        });

    }

    private void initCheckInTypeNegativeButton(View view) {

        this.qrCheckInCodeDialogNegativeButton = (MyButton) view.findViewById(R.id.dialog_check_in_code_text_type_negative_button);

        MyAppThemeUtilsManager.get().initMyButton(this.qrCheckInCodeDialogNegativeButton, "Cancel", this.getContext(), this.screenSkin);

        this.qrCheckInCodeDialogNegativeButton.setWidth(
                (int) this.getResources().getDimension(R.dimen.check_in_activity_type_guest_code_dialog_negative_button_width)
        );

        this.qrCheckInCodeDialogNegativeButton.setTextViewTextSize(
                (int) this.getResources().getDimension(R.dimen.check_in_activity_type_guest_code_dialog_negative_button_text_size)
        );

        this.qrCheckInCodeDialogNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckInCodeTypeDialogView.this.checkInTypeDialogListener != null) {

                    CheckInCodeTypeDialogView.this.checkInTypeDialogListener.onCancelClick();

                }

            }
        });

    }

    private void initCheckInTypeCodeEditText(View view) {

        CheckInCodeTypeDialogView.this.qrCheckInCodeDialogEditText = (MyEditText) view.findViewById(R.id.dialog_check_in_code_text_type_code_edit_text);

        MyAppThemeUtilsManager.get().initMyEditText(CheckInCodeTypeDialogView.this.qrCheckInCodeDialogEditText, "Code",
                (IconicsDrawable) Icons.get().findDrawable(this.getContext(),"faw_key"), this.getContext(), this.screenSkin);

        CheckInCodeTypeDialogView.this.qrCheckInCodeDialogEditText.setValidator(new Validator<String>() {
            @Override
            public String validate(String text) {
                if (CheckInCodeTypeDialogView.this.isValidCode(text)) {
                    return null;
                } else {
                    return "Please enter valid code";
                }
            }
        });

        CheckInCodeTypeDialogView.this.qrCheckInCodeDialogEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CheckInCodeTypeDialogView.this.checkCheckInCodeTypeDialogFields();
            }
        });

        CheckInCodeTypeDialogView.this.qrCheckInCodeDialogEditText.setError(null);

    }

    //type code listener
    public void setCheckInTypeDialogListener(CheckInTypeDialogListener checkInTypeDialogListener) {

        this.checkInTypeDialogListener = checkInTypeDialogListener;

    }

    //code validation
    private boolean isValidCode(String code) {

        return code != null && !code.equals("");

    }

    private void checkCheckInCodeTypeDialogFields() {

        if (((this.qrCheckInCodeDialogEditText.getErrorText() != null) && (!this.qrCheckInCodeDialogEditText.getErrorText().toString().equals("")))
                || this.qrCheckInCodeDialogEditText.getText().toString().equals("")) {
            this.qrCheckInCodeDialogPositiveButton.setEnabled(false);
        } else {
            this.qrCheckInCodeDialogPositiveButton.setEnabled(true);
        }

    }

}
