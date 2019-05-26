package com.bob.bobguestapp.activities.codes.codeslist.codedetails;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
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
import com.bob.bobguestapp.activities.codes.codeslist.CodesListViewHolder;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.database.objects.GuestCode;
import com.bob.uimodule.image.ImageUtilsManager;
import com.bob.uimodule.views.loadingcontainer.ManagementLayout;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class CodeDetailsDialogView extends ManagementLayout {

    //main view screen states
    public static final int CODE_DETAILS = 10;

    //views
    private RelativeLayout codeDetailsDialogLayout;
    private ImageView codeImageView;
    private RelativeLayout codeTextLayout;
    private TextView codeTitleTextView;
    private TextView codeTextView;

    //guest code
    private GuestCode guestCode;


    //constructors
    public CodeDetailsDialogView(Context context) {
        this(context, null);
    }

    public CodeDetailsDialogView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CodeDetailsDialogView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

    }


    @Override
    protected View onCreateMainView() {

        View view = this.initCodeDetailsDialogLayout();

        this.setScreenState(CODE_DETAILS);

        return view;

    }

    @Override
    protected void setMainViewScreenState(int screenState) {


    }

    //views
    private View initCodeDetailsDialogLayout() {

        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View view = inflater.inflate(R.layout.dialog_code_details, new RelativeLayout(this.getContext()));
        this.codeDetailsDialogLayout = (RelativeLayout) view.findViewById(R.id.code_details_dialog_layout);

        //init code image
        this.initCodeImageView(view);

        //init code text
        this.initCodeTextLayout(view);

        return view;
    }

    private void initCodeImageView(View view) {

        this.codeImageView = (ImageView) view.findViewById(R.id.code_details_dialog_code_image_view);

    }

    private void initCodeTextLayout(View view) {

        this.codeTextLayout= (RelativeLayout) view.findViewById(R.id.code_details_dialog_code_text_layout);

        //title
        this.initCodeTitleTextView(view);

        //code
        this.initCodeTextView(view);

    }

    private void initCodeTitleTextView(View view) {

        this.codeTitleTextView = (TextView) view.findViewById(R.id.code_details_dialog_code_title);

    }

    private void initCodeTextView(View view) {

        this.codeTitleTextView = (TextView) view.findViewById(R.id.code_details_dialog_code_text);

    }


    private void updateCode() {

        //background
        this.updateBackground();

        //code image
        this.updateCodeImageView();

        //code title
        this.updateCodeTitle();

        //code text
        this.updateCodeText();

    }

    private void updateBackground() {

        this.codeDetailsDialogLayout.setBackgroundColor(MyAppThemeUtilsManager.get(this.getContext()).getColor(MyAppThemeUtilsManager.DEFAULT_VIEW_BACKGROUND_COLOR_PRIMARY, this.screenSkin));


    }

    private void updateCodeImageView() {

        if (this.guestCode != null && this.guestCode.getCode() != null && !this.guestCode.getCode().equals("")) {

            Bitmap generatedBitmap = ImageUtilsManager.get().generateQRBitmap(
                    this.guestCode.getCode(),
                    (int) this.getContext().getResources().getDimension(R.dimen.guest_code_list_item_show_guest_code_dialog_qr_code_image_width),
                    (int) this.getContext().getResources().getDimension(R.dimen.guest_code_list_item_show_guest_code_dialog_qr_code_image_width),
                    MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_QR_BITMAP_PRIMARY_COLOR, this.screenSkin),
                    MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_QR_BITMAP_SECONDARY_COLOR, this.screenSkin)

            );

            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(this.getContext().getResources(), generatedBitmap);
            final float roundPx = (float) generatedBitmap.getWidth() * 0.06f;
            roundedBitmapDrawable.setCornerRadius(roundPx);

            this.codeImageView.setImageDrawable(roundedBitmapDrawable);

        } else {

            Glide.with(this.getContext())
                    .asDrawable()
                    .load(R.drawable.ic_qr_press_white)
                    .into(this.codeImageView);

        }


    }

    private void updateCodeTitle() {

        MyAppThemeUtilsManager.get().initTextView(this.codeTitleTextView, "Code:", this.getContext(), this.screenSkin);
        this.codeTitleTextView.setTextSize(this.getContext().getResources().getDimension(R.dimen.guest_code_list_popup_title_text_size));
        this.codeTitleTextView.setPaintFlags(this.codeTitleTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

    }

    private void updateCodeText() {

        if (this.guestCode != null && this.guestCode.getCode() != null && !this.guestCode.getCode().equals("")) {

            MyAppThemeUtilsManager.get().initTextView(this.codeTextView, this.guestCode.getCode(), this.getContext(), this.screenSkin);
            this.codeTextView.setTextSize(this.getContext().getResources().getDimension(R.dimen.guest_code_list_popup_code_text_size));

        } else {

            MyAppThemeUtilsManager.get().initTextView(this.codeTextView, "No Code", this.getContext(), this.screenSkin);
            this.codeTextView.setTextSize(this.getContext().getResources().getDimension(R.dimen.guest_code_list_popup_code_text_size));

        }

    }

    //code
    public void setCode(GuestCode guestCode) {

        this.guestCode = guestCode;

        //update views
        this.updateCode();

    }

}
