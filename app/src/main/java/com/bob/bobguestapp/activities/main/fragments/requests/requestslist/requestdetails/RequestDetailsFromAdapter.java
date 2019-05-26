package com.bob.bobguestapp.activities.main.fragments.requests.requestslist.requestdetails;

import android.content.Context;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.database.objects.RequestItem;
import com.bob.uimodule.menu.adapters.base.FormAdapter;
import com.bob.uimodule.menu.viewsinitializers.MenuFormViewsInitializer;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestDetailsFromAdapter extends FormAdapter<RequestItem> {

    public static final int READ_ONLY_MODE = 0;
    public static final int READ_WRITE_MODE = 1;

    private int mode;

    public RequestDetailsFromAdapter(Context context, int appTheme, int skin, int dialogSkin) {

        super(context, appTheme, skin, dialogSkin);

    }

    public RequestDetailsFromAdapter(Context context) {

        super(context);

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.formSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.REQUEST_LIST_ITEM_REQUEST_DETAILS_DIALOG_SKIN);
        this.formDialogSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.REQUEST_LIST_ITEM_REQUEST_DETAILS_DIALOG_SKIN);

        //update form initializer
        this.initFormViewsInitializer();

        //init form mode
        this.initMode();

    }

    //form mode
    private void initMode() {

        this.mode = READ_ONLY_MODE;

    }

    public int getMode() {

        return this.mode;

    }

    public void setMode(int mode) {

        this.mode = mode;

    }

    @Override
    protected void initFormViewsInitializer() {

        this.formViewsInitializer = new RequestDetailsFormInitializer(this.context, this.appTheme, this.formSkin, this.formDialogSkin);

    }


    @Override
    protected void initFormItemsList() {
        this.formItems = new ArrayList<RequestItem>();
    }

    @Override
    protected HashMap<String, String> getFormItemProperties(int position) {

        HashMap<String, String> formItemProperties = new HashMap<String, String>();

        if (this.formItems != null && position < this.formItems.size() && this.formItems.get(position) != null) {

            RequestItem requestItem = this.formItems.get(position);

            formItemProperties.put("key", requestItem.getKey());
            formItemProperties.put("value", requestItem.getValue());

        }

        return formItemProperties;

    }

    @Override
    public int getItemViewType(int position) {

        if (formItems.get(position).getType().equals("text_view")) {
            return this.mode == READ_WRITE_MODE ? FORM_ITEM_TEXT_VIEW : FORM_ITEM_TEXT_VIEW;
        } else if (formItems.get(position).getType().equals("edit_text")) {
            return this.mode == READ_WRITE_MODE ? FORM_ITEM_EDIT_TEXT : FORM_ITEM_TEXT_VIEW;
        } else if (formItems.get(position).getType().equals("button")) {
            return this.mode == READ_WRITE_MODE ? FORM_ITEM_BUTTON : FORM_ITEM_BUTTON;
        } else if (formItems.get(position).getType().equals("date_view")) {
            return this.mode == READ_WRITE_MODE ? FORM_ITEM_DATE_VIEW : FORM_ITEM_TEXT_VIEW;
        } else if (formItems.get(position).getType().equals("time_view")) {
            return this.mode == READ_WRITE_MODE ? FORM_ITEM_TIME_VIEW : FORM_ITEM_TEXT_VIEW;
        } else if (formItems.get(position).getType().equals("phone_view")) {
            return this.mode == READ_WRITE_MODE ? FORM_ITEM_PHONE_VIEW : FORM_ITEM_TEXT_VIEW;
        } else if (formItems.get(position).getType().equals("location_output_view")) {
            return this.mode == READ_WRITE_MODE ? FORM_ITEM_LOCATION_OUTPUT : FORM_ITEM_LOCATION_OUTPUT;
        } else if (formItems.get(position).getType().equals("location_input_view")) {
            return this.mode == READ_WRITE_MODE ? FORM_ITEM_LOCATION_INPUT : FORM_ITEM_LOCATION_OUTPUT;
        } else if (formItems.get(position).getType().equals("image_view")) {
            return this.mode == READ_WRITE_MODE ? FORM_ITEM_IMAGE_VIEW : FORM_ITEM_IMAGE_VIEW;
        } else if (formItems.get(position).getType().equals("video_view")) {
            return this.mode == READ_WRITE_MODE ? FORM_ITEM_VIDEO_VIEW : FORM_ITEM_VIDEO_VIEW;
        } else if (formItems.get(position).getType().equals("text_list_view")) {
            return this.mode == READ_WRITE_MODE ? FORM_ITEM_TEXT_LIST_VIEW : FORM_ITEM_TEXT_LIST_VIEW;
        } else if (formItems.get(position).getType().equals("single_choice_view")) {
            return this.mode == READ_WRITE_MODE ? FORM_ITEM_SINGLE_CHOICE : FORM_ITEM_SINGLE_CHOICE;
        } else if (formItems.get(position).getType().equals("multi_choice_view")) {
            return this.mode == READ_WRITE_MODE ? FORM_ITEM_MULTI_CHOICE : FORM_ITEM_MULTI_CHOICE;
        } else {
            return this.mode == READ_WRITE_MODE ? FORM_ITEM_DEFAULT : FORM_ITEM_DEFAULT;
        }
    }


}
