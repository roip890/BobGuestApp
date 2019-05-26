package com.bob.bobguestapp.activities.main.fragments.requests.requestslist.requestdetails;

import android.content.Context;
import android.graphics.Color;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;

import com.bob.uimodule.R;
import com.bob.uimodule.finals;
import com.bob.uimodule.menu.viewsinitializers.base.FormViewsInitializer;
import com.bob.uimodule.theme.ThemeUtilsManager;
import com.bob.uimodule.views.MyBaseView;
import com.bob.uimodule.views.MyView;
import com.bob.uimodule.views.choicegroups.MyCheckBoxGroup;
import com.bob.uimodule.views.choicegroups.MyRadioGroup;
import com.bob.uimodule.views.mediaviews.MyImageView;
import com.bob.uimodule.views.mediaviews.MyVideoView;
import com.bob.uimodule.views.textviews.MyAutoCompleteEditText;
import com.bob.uimodule.views.textviews.MyButton;
import com.bob.uimodule.views.textviews.MyEditText;
import com.bob.uimodule.views.textviews.MyLocationInputView;
import com.bob.uimodule.views.textviews.MyLocationOutputView;
import com.bob.uimodule.views.textviews.MyPhoneEditText;
import com.bob.uimodule.views.textviews.MyTextView;
import com.bob.uimodule.views.textviews.dialogviews.MyCheckBoxGroupDialog;
import com.bob.uimodule.views.textviews.dialogviews.MyDateTextView;
import com.bob.uimodule.views.textviews.dialogviews.MyNumberTextView;
import com.bob.uimodule.views.textviews.dialogviews.MyRadioGroupDialog;
import com.bob.uimodule.views.textviews.dialogviews.MyTextViewListDialog;
import com.bob.uimodule.views.textviews.dialogviews.MyTextViewMultiChoiceDialog;
import com.bob.uimodule.views.textviews.dialogviews.MyTextViewSingleChoiceDialog;
import com.bob.uimodule.views.textviews.dialogviews.MyTimeTextView;

import java.util.ArrayList;
import java.util.Calendar;

public class RequestDetailsFormInitializer extends FormViewsInitializer {

    public RequestDetailsFormInitializer(Context context, int appTheme, int skin, int dialogSkin) {
        super(context, appTheme, skin, dialogSkin);
    }

    @Override
    protected void customMyView(MyView myView, int skin, int dialogSkin) {

        myView.setSkin(skin);
        myView.setDialogSkin(dialogSkin);
        myView.setBackgroundColor(ThemeUtilsManager.get(this.context).getColor(ThemeUtilsManager.DEFAULT_MY_VIEW_BACKGROUND_COLOR_PRIMARY, skin));
        myView.setGravity(finals.gravity.get("start"));
//        myView.setTitleText("Default title");

        myView.setErrorTextColor(ThemeUtilsManager.get(this.context).getColor(ThemeUtilsManager.DEFAULT_MY_VIEW_TEXT_COLOR, skin));
        myView.setStartDrawableColor(ThemeUtilsManager.get(this.context).getColor(ThemeUtilsManager.DEFAULT_MY_VIEW_START_DRAWABLE_COLOR, skin));
        myView.setEndDrawableColor(ThemeUtilsManager.get(this.context).getColor(ThemeUtilsManager.DEFAULT_MY_VIEW_END_DRAWABLE_COLOR, skin));

    }

    @Override
    protected void customMyBaseView(MyBaseView myBaseView, int skin, int dialogSkin) {
        myBaseView.setTitleTextSize((int) this.context.getResources().getDimension(R.dimen.default_my_view_title_text_size));
        myBaseView.setTitleTextColor(ThemeUtilsManager.get(this.context).getColor(ThemeUtilsManager.DEFAULT_MY_VIEW_TITLE_COLOR, skin));
    }

    @Override
    protected void customMyImageView(MyImageView myImageView, int skin, int dialogSkin) {

        myImageView.setImageUri(null);

    }

    @Override
    protected void customMyVideoView(MyVideoView myVideoView, int skin, int dialogSkin) {

        myVideoView.setVideoUri(null);

    }

    @Override
    protected void customMyTextView(MyTextView myTextView, int skin, int dialogSkin) {

        myTextView.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);

        myTextView.setText("");

        myTextView.setTextViewTextColor(ThemeUtilsManager.get(this.context).getColor(ThemeUtilsManager.DEFAULT_MY_VIEW_TEXT_COLOR, skin));
        myTextView.setTitleTextColor(ThemeUtilsManager.get(this.context).getColor(ThemeUtilsManager.DEFAULT_MY_VIEW_TITLE_COLOR, skin));

        myTextView.setTextViewTextSize((int) this.context.getResources().getDimension(R.dimen.default_my_view_text_size));

    }

    @Override
    protected void customMyEditText(MyEditText myEditText, int skin, int dialogSkin) {

        myEditText.setEndDrawableOnFocusOnly(true);
        myEditText.setTextInputType(finals.inputTypes.get("text"));
        myEditText.setCursorColor(ThemeUtilsManager.get(this.context).getColor(ThemeUtilsManager.DEFAULT_MY_VIEW_CURSOR_COLOR, skin));
        myEditText.setTitleText("Please enter your text:");

    }

    @Override
    protected void customMyAutocompleteEditText(MyAutoCompleteEditText myAutoCompleteEditText, int skin, int dialogSkin) {

    }

    @Override
    protected void customMyPhoneTextView(MyPhoneEditText myPhoneEditText, int skin, int dialogSkin) {

        myPhoneEditText.setTitleText("Please enter phone number:");

    }

    @Override
    protected void customMyLocationOutputView(MyLocationOutputView myLocationOutputView, int skin, int dialogSkin) {

        myLocationOutputView.setTitleText("Please show map:");
        myLocationOutputView.setDialogTitleText("Please show map:");

    }

    @Override
    protected void customMyLocationInputView(MyLocationInputView myLocationInputView, int skin, int dialogSkin) {

        myLocationInputView.setTitleText("Please select place:");
        myLocationInputView.setDialogTitleText("Please select place:");

    }

    @Override
    protected void customMyDateTextView(MyDateTextView myDateTextView, int skin, int dialogSkin) {

        Calendar calendar = Calendar.getInstance();
        myDateTextView.setYears(calendar.get(Calendar.YEAR));
        myDateTextView.setMonths(calendar.get(Calendar.MONTH));
        myDateTextView.setDays(calendar.get(Calendar.DAY_OF_MONTH));
        myDateTextView.setTitleText("Please select date:");
        myDateTextView.setDialogTitleText("Please select date:");

    }

    @Override
    protected void customMyTimeTextView(MyTimeTextView myTimeViewView, int skin, int dialogSkin) {

        myTimeViewView.setHours(0);
        myTimeViewView.setMinutes(0);
        myTimeViewView.setTitleText("Please select time:");
        myTimeViewView.setDialogTitleText("Please select time:");

    }

    @Override
    protected void customMyNumberTextView(MyNumberTextView myNumberTextView, int skin, int dialogSkin) {

    }

    @Override
    protected void customMyCheckBoxGroupDialog(MyCheckBoxGroupDialog myCheckBoxGroupDialog, int skin, int dialogSkin) {

    }

    @Override
    protected void customMyRadioGroupDialog(MyRadioGroupDialog myRadioGroupDialog, int skin, int dialogSkin) {

    }

    @Override
    protected void customMyTextViewListDialog(MyTextViewListDialog myTextViewListDialog, int skin, int dialogSkin) {

        myTextViewListDialog.setItems(new ArrayList<String>());
        myTextViewListDialog.setSelectedItems(new ArrayList<Integer>());
        myTextViewListDialog.setItemsColor(ThemeUtilsManager.get(this.context).getColor(ThemeUtilsManager.DEFAULT_MY_VIEW_DIALOG_ITEMS_COLOR, dialogSkin));
        myTextViewListDialog.setItemsWidgetColor(ThemeUtilsManager.get(this.context).getColor(ThemeUtilsManager.DEFAULT_MY_VIEW_DIALOG_ITEMS_COLOR, dialogSkin));
        myTextViewListDialog.setItemsGravity(finals.dialogGravity.get("start"));
        myTextViewListDialog.setDisabledItems(new ArrayList<Integer>());

    }

    @Override
    protected void customMyTextViewSingleChoiceDialog(MyTextViewSingleChoiceDialog myTextViewSingleChoiceDialog, int skin, int dialogSkin) {

    }

    @Override
    protected void customMyTextViewMultiChoiceDialog(MyTextViewMultiChoiceDialog myTextViewMultiChoiceDialog, int skin, int dialogSkin) {

    }

    @Override
    protected void customMyRadioGroup(MyRadioGroup myRadioGroup, int skin, int dialogSkin) {

    }

    @Override
    protected void customMyCheckBoxGroup(MyCheckBoxGroup myCheckBoxGroup, int skin, int dialogSkin) {

    }

    @Override
    protected void customMyButton(MyButton myButton, int skin, int dialogSkin) {

        myButton.setButtonColor(ContextCompat.getColor(context, R.color.colorPrimary));
        myButton.setTitleText("Push");

    }
}
