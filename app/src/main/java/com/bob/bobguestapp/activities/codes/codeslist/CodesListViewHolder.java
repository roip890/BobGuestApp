package com.bob.bobguestapp.activities.codes.codeslist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.codes.codeslist.codedetails.CodeDetailsDialogView;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.database.objects.GuestCode;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.finals;
import com.bob.uimodule.image.ImageUtilsManager;
import com.bumptech.glide.Glide;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by User on 07/09/2018.
 */

public class CodesListViewHolder extends RecyclerView.ViewHolder{

    private static final int CODE_ITEM_DROPDOWN_MENU_SHOW_ID = 0;
    private static final int CODE_ITEM_DROPDOWN_MENU_DISCONNECT_ID = 1;

    //app theme
    private int appTheme = MyAppThemeUtilsManager.DEFAULT_THEME;
    private int screenSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;

    //context
    protected Context context;

    //views
    protected View view;
    private RelativeLayout codeLayout;
    private ImageView codeIconImageView;
    private TextView codeTitleView;
    private TextView codeCodeTextView;
    private ImageView codeStatusIconImageView;
    private ImageView codeMoreDetailsIconImageView;
    private ImageView requestFavoriteIconView;

    //guest code
    private GuestCode guestCode;

    //code details dialog
    private MaterialDialog codeItemShowMaterialDialog;

    public CodesListViewHolder(Context context, View view) {
        super(view);

        this.context = context;

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.GUEST_CODES_ACTIVITY_SKIN);

        //view
        this.initView(view);
    }

    //general
    protected void initView(View view) {

        this.view = view;

        //code layout
        this.initCodeLayout(view);

        //icon
        this.initCodeIcon(view);

        //title
        this.initCodeTitle(view);

        //code text
        this.initCodeText(view);

        //status icon
        this.initCodeStatusIcon(view);

        //details icon
        this.initCodeMoreDetailsIcon(view);

        //update code
        this.updateCode();

    }

    private void initCodeLayout(View view) {

        this.codeLayout = (RelativeLayout) view.findViewById(R.id.guest_code_list_item_layout);

    }

    private void initCodeIcon(View view) {

        this.codeIconImageView = (ImageView) view.findViewById(R.id.guest_code_list_item_code_icon);

    }

    private void initCodeTitle(View view) {

        this.codeTitleView = (TextView) view.findViewById(R.id.guest_code_list_item_code_title);

    }

    private void initCodeText(View view) {

        this.codeCodeTextView = (TextView) view.findViewById(R.id.guest_code_list_item_code_text);

    }

    private void initCodeStatusIcon(View view) {

        this.codeStatusIconImageView = (ImageView) view.findViewById(R.id.guest_code_list_item_status_icon);

    }

    private void initCodeMoreDetailsIcon(View view) {

        this.codeMoreDetailsIconImageView = (ImageView) view.findViewById(R.id.guest_code_list_item_more_details_button_icon);

        //set more details icon
        Glide.with(this.context)
                .asDrawable()
                .load(ContextCompat.getDrawable(this.context, R.drawable.ic_more_horiz_black_24dp))
                .into(this.codeMoreDetailsIconImageView);

        //init more details menu
        this.initCodeMoreDetailsMenu();
    }

    private void initCodeMoreDetailsMenu() {

        this.codeMoreDetailsIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(CodesListViewHolder.this.context, CodesListViewHolder.this.codeMoreDetailsIconImageView);

                popup.getMenu().add(Menu.NONE, CODE_ITEM_DROPDOWN_MENU_SHOW_ID, 0, "Show");
                if (CodesListViewHolder.this.guestCode != null
                        && CodesListViewHolder.this.guestCode.getStatus().toLowerCase().equals("connected")) {
                    popup.getMenu().add(Menu.NONE, CODE_ITEM_DROPDOWN_MENU_DISCONNECT_ID, 1, "Disconnect");
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case CODE_ITEM_DROPDOWN_MENU_SHOW_ID:
                                CodesListViewHolder.this.onCodeItemShowClick();
                                return true;
                            case CODE_ITEM_DROPDOWN_MENU_DISCONNECT_ID:
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popup.show();

            }
        });

    }

    private void updateCode() {

        //request icon
        this.updateCodeIcon();

        //title
        this.updateCodeTitle();

        //code text
        this.updateCodeText();

        //status icon
        this.updateStatusIcon();

        //details icon
        this.updateDetailsIcon();


    }

    private void updateCodeIcon() {

        if (this.guestCode != null && this.guestCode.getCode() != null && !this.guestCode.getCode().equals("")) {

            Bitmap generatedBitmap = ImageUtilsManager.get().generateQRBitmap(
                    this.guestCode.getCode(),
                    (int) CodesListViewHolder.this.context.getResources().getDimension(R.dimen.guest_code_list_item_show_guest_code_dialog_qr_code_image_width),
                    (int) CodesListViewHolder.this.context.getResources().getDimension(R.dimen.guest_code_list_item_show_guest_code_dialog_qr_code_image_width),
                    MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_QR_BITMAP_PRIMARY_COLOR, this.screenSkin),
                    MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_QR_BITMAP_SECONDARY_COLOR, this.screenSkin)
            );

            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(CodesListViewHolder.this.context.getResources(), generatedBitmap);
            final float roundPx = (float) generatedBitmap.getWidth() * 0.06f;
            roundedBitmapDrawable.setCornerRadius(roundPx);

            CodesListViewHolder.this.codeIconImageView.setImageDrawable(roundedBitmapDrawable);

        } else {

            Glide.with(this.context)
                    .asDrawable()
                    .load(R.drawable.ic_qr_press_white)
                    .into(this.codeIconImageView);

        }

    }

    private void updateCodeTitle() {

        if (this.guestCode != null && this.guestCode.getTitle() != null && !this.guestCode.getTitle().equals("")) {

            this.codeTitleView.setText(this.guestCode.getTitle());
            this.codeTitleView.setTextColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_FIELD_TEXT_COLOR, this.screenSkin));

        } else {

            this.codeTitleView.setText("No Code");
            this.codeTitleView.setTextColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_FIELD_TEXT_COLOR, this.screenSkin));

        }

    }

    private void updateCodeText() {

        if (this.guestCode != null && this.guestCode.getCode() != null && !this.guestCode.getCode().equals("")) {

            this.codeCodeTextView.setText(this.guestCode.getCode());
            this.codeCodeTextView.setTextColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_FIELD_TEXT_COLOR, this.screenSkin));

        } else {

            this.codeCodeTextView.setText("");
            this.codeCodeTextView.setTextColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_FIELD_TEXT_COLOR, this.screenSkin));

        }

    }

    private void updateStatusIcon() {

        if (this.guestCode != null && this.guestCode.getStatus() != null && !this.guestCode.getStatus().equals("")) {

            int statusIcon = R.drawable.ic_circle_red_24dp;
            if (finals.codeStatusIcons.get(this.guestCode.getStatus()) != null) {
                statusIcon = finals.codeStatusIcons.get(this.guestCode.getStatus().toLowerCase());
            }
            Glide.with(this.context)
                    .asDrawable()
                    .load(ContextCompat.getDrawable(this.context, statusIcon))
                    .into(this.codeStatusIconImageView);

        } else {

            Glide.with(this.context)
                    .asDrawable()
                    .load(ContextCompat.getDrawable(this.context, R.drawable.ic_circle_red_24dp))
                    .into(this.codeStatusIconImageView);

        }

    }

    private void updateDetailsIcon() {

        if (this.guestCode != null && this.guestCode.getCode() != null && !this.guestCode.getCode().equals("")) {

            this.codeMoreDetailsIconImageView.setVisibility(View.VISIBLE);

        } else {

            this.codeMoreDetailsIconImageView.setVisibility(View.GONE);

        }

    }

    //configure guest code
    public void configureGuestCode(GuestCode guestCode) {

        this.guestCode = guestCode;

        this.updateCode();

        if (this.guestCode != null) {

            this.codeLayout.setVisibility(View.VISIBLE);

        } else {

            this.codeLayout.setVisibility(View.GONE);

        }

    }

    //code details
    protected void onCodeItemShowClick() {
        this.codeItemShowMaterialDialog= new MaterialDialog.Builder(this.context)
                .title(this.codeTitleView.getText())
                .titleGravity(finals.dialogGravity.get("center"))
                .customView(this.initCodeDetailsDialogView(), true)
                .backgroundColor(ContextCompat.getColor(this.context, R.color.colorPrimary))
                .show();

        //round corners
        View dialogView = this.codeItemShowMaterialDialog.getWindow().getDecorView();
        GradientDrawable shapeDrawable = new GradientDrawable();
        shapeDrawable.setColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_DIALOG_BACKGROUND_COLOR_PRIMARY, this.screenSkin));
        shapeDrawable.setCornerRadius(UIUtilsManager.get().convertDpToPixels(this.context, 10));
        dialogView.setBackground(shapeDrawable);

        //set width
        ViewGroup.LayoutParams params = this.codeItemShowMaterialDialog.getWindow().getAttributes();
        params.width =  (int) this.context.getResources().getDimension(R.dimen.guest_code_list_item_show_guest_code_dialog_width);
        this.codeItemShowMaterialDialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

    }

    protected View initCodeDetailsDialogView() {

        CodeDetailsDialogView codeDetailsDialogView = new CodeDetailsDialogView(this.context);

        codeDetailsDialogView.setCode(this.guestCode);

        return view;

    }



}