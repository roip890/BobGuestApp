package com.bob.bobguestapp.activities.main.fragments.social.postpage.comments.commentslist.viewholder;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.text.TextUtilsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.social.postpage.comments.commentslist.listener.CommentsListEventsListener;
import com.bob.bobguestapp.tools.database.objects.PostComment;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.icons.Icons;
import com.bob.uimodule.popup.MyPowerMenu;
import com.bob.uimodule.popup.PowerMenuListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mikepenz.iconics.IconicsDrawable;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnDismissedListener;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenuItem;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by User on 07/09/2018.
 */

public class PostPageCommentsListViewHolder extends RecyclerView.ViewHolder {


    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";

    private static final int COMMENT_ITEM_DROPDOWN_MENU_COPY_ID = 0;
    private static final int COMMENT_ITEM_DROPDOWN_MENU_EDIT_ID = 1;
    private static final int COMMENT_ITEM_DROPDOWN_MENU_DELETE_ID = 2;
    private static final int COMMENT_ITEM_DROPDOWN_MENU_CANCEL_ID = 3;

    private static final int MAX_COMMENT_LENGTH = 50;
    private static final int MAX_COMMENT_LINES = 5;

    //app theme
    private int appTheme = MyAppThemeUtilsManager.DEFAULT_THEME;
    private int screenSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;

    //context
    protected Context context;

    //views
    protected View view;
    protected ConstraintLayout commentBackgroundLayout;

    //profile picture
    protected ImageView editorProfilePictureImageView;

    //post content layout
    protected RelativeLayout commentContentLayout;

    //post content header layout
    protected RelativeLayout commentContentHeaderLayout;
    protected TextView editorNameTextView;
    protected TextView commentContentTextView;

    //post content footer layout
    protected RelativeLayout commentContentFooterLayout;

    protected RelativeLayout commentTimestampLayout;
    protected ImageView commentTimestampIconImageView;
    protected TextView commentTimestampTextView;
    protected RelativeLayout commentLikesLayout;
    protected ImageView commentLikesButtonImageView;
    protected TextView commentLikesTextView;
    protected RelativeLayout commentCommentsLayout;
    protected ImageView commentCommentsButtonImageView;
    protected TextView commentCommentsTextView;

    //post
    protected PostComment postPageComment;

    //popup
    private View popupAnchor;
    private MyPowerMenu longClickPowerMenu;
    protected PowerMenuItem copyCommentMenuItem;
    protected PowerMenuItem editCommentMenuItem;
    protected PowerMenuItem deleteCommentMenuItem;
    protected PowerMenuItem cancelCommentMenuItem;
    protected PowerMenuListener powerMenuListener;

    public PostPageCommentsListViewHolder(Context context, View view, PowerMenuListener powerMenuListener) {
        super(view);

        this.context = context;

        this.powerMenuListener = powerMenuListener;

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.REQUESTS_FRAGMENT_SKIN);

        this.initView(view);
    }

    public void onResume() {


    }

    public void onPause() {


    }

    //general
    protected void initView(View view) {

        this.view = view;

        this.initCommentBackgroundLayout(view);

    }

    private void initCommentBackgroundLayout(View view) {

        this.commentBackgroundLayout = (ConstraintLayout) view.findViewById(R.id.post_page_comment_view_holder_background_layout);

        this.commentBackgroundLayout.setLongClickable(true);

        this.commentBackgroundLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                PostPageCommentsListViewHolder.this.onCommentHeaderLongClick();

                return true;
            }
        });

        this.commentBackgroundLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PostPageCommentsListViewHolder.this.commentContentTextView.getText().toString().contains(
                        PostPageCommentsListViewHolder.this.postPageComment.getContent())) {

                    if (PostPageCommentsListViewHolder.this.postPageComment.getContent().length() > MAX_COMMENT_LENGTH) {

                        String readMoreText = "Load More";
                        String shortcutContent = PostPageCommentsListViewHolder.this.postPageComment.getContent().substring(
                                0, MAX_COMMENT_LENGTH
                        );
                        String text = shortcutContent + " ... \n" + readMoreText;
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
                        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(PostPageCommentsListViewHolder.this.context, R.color.primary_color))
                                , text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        PostPageCommentsListViewHolder.this.commentContentTextView.setText(
                                spannableStringBuilder
                        );

                    } else if (PostPageCommentsListViewHolder.this.postPageComment.getContent().split("\r\n|\r|\n").length > MAX_COMMENT_LINES) {


                        String readMoreText = "Load More";

                        String[] lines = PostPageCommentsListViewHolder.this.postPageComment.getContent().split("\r\n|\r|\n");
                        String shortcutContent = TextUtils.join("\n", Arrays.copyOfRange(lines, 0, MAX_COMMENT_LINES));

                        String text = shortcutContent + " ... \n" + readMoreText;
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
                        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(PostPageCommentsListViewHolder.this.context, R.color.primary_color))
                                , text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        PostPageCommentsListViewHolder.this.commentContentTextView.setText(
                                spannableStringBuilder
                        );

                    }



                } else {

                    String readLessText = "";
                    String text = PostPageCommentsListViewHolder.this.postPageComment.getContent() + readLessText;
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
                    spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), text.length() - readLessText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(PostPageCommentsListViewHolder.this.context, R.color.primary_color))
                            , text.length() - readLessText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    PostPageCommentsListViewHolder.this.commentContentTextView.setText(
                            spannableStringBuilder
                    );

                }

            }
        });

        this.initEditorProfilePictureImageView(view);

        this.initCommentContentLayout(view);

    }

    //profile picture
    private void initEditorProfilePictureImageView(View view) {

        this.editorProfilePictureImageView = (ImageView) view.findViewById(R.id.post_page_comment_view_holder_editor_profile_picture_view);

    }


    //content layout
    private void initCommentContentLayout(View view) {

        this.commentContentLayout = (RelativeLayout) view.findViewById(R.id.post_page_comment_view_holder_content_layout);

        this.initCommentHeaderLayout(view);

        this.initCommentFooterLayout(view);

        this.initPopupAnchor(view);

        this.initLongClickMenuItems();

    }

    //comment header
    private void initCommentHeaderLayout(View view)  {

        this.commentContentHeaderLayout = (RelativeLayout) view.findViewById(R.id.post_page_comment_view_holder_content_header_layout);

        float[] outerRadii = new float[] {
                30,
                30,
                30,
                30,
                30,
                30,
                30,
                30
        };
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(ContextCompat.getColor(this.context, R.color.primary_color_extra_opacity));

        this.commentContentHeaderLayout.setBackground(shapeDrawable);

        this.initEditorNameTextView(view);

        this.initCommentTextTextViewLayout(view);

    }

    private void initPopupAnchor(View view) {

        this.popupAnchor = view.findViewById(R.id.post_page_comment_view_holder_popup_anchor);

    }

    private void initLongClickMenuItems() {

        this.initCopyCommentMenuItem();

        this.initEditCommentMenuItem();

        this.initDeleteCommentMenuItem();

        this.initCancelCommentMenuItem();

    }

    private void initCopyCommentMenuItem() {

        this.copyCommentMenuItem = new PowerMenuItem("Copy", false);
        this.copyCommentMenuItem.setTag(COMMENT_ITEM_DROPDOWN_MENU_COPY_ID);

    }

    private void onCopyCommentMenuItemClick() {

        if (this.postPageComment != null
                && this.postPageComment.getContent() != null
                && !this.postPageComment.getContent().equals("")) {

            ClipboardManager clipboard = (ClipboardManager) this.context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Comment: ",this.postPageComment.getContent());
            clipboard.setPrimaryClip(clip);

            Toast.makeText(this.context, "Text copied", Toast.LENGTH_SHORT).show();

        }

    }

    private void initEditCommentMenuItem() {

        this.editCommentMenuItem = new PowerMenuItem("Edit", false);
        this.editCommentMenuItem.setTag(COMMENT_ITEM_DROPDOWN_MENU_EDIT_ID);

    }

    private void onEditCommentMenuItemClick() {

        if (this.context instanceof CommentsListEventsListener) {

            CommentsListEventsListener commentsListEventsListener = ((CommentsListEventsListener)this.context);
            commentsListEventsListener.onCommentEdit(this.postPageComment);

        }

    }

    private void initDeleteCommentMenuItem() {

        this.deleteCommentMenuItem = new PowerMenuItem("Delete", false);
        this.deleteCommentMenuItem.setTag(COMMENT_ITEM_DROPDOWN_MENU_DELETE_ID);

    }

    private void onDeleteCommentMenuItemClick() {

        if (this.context instanceof CommentsListEventsListener) {

            CommentsListEventsListener commentsListEventsListener = ((CommentsListEventsListener)this.context);
            commentsListEventsListener.onCommentDelete(this.postPageComment);

        }

    }

    private void initCancelCommentMenuItem() {

        this.cancelCommentMenuItem = new PowerMenuItem("Cancel", false);
        this.cancelCommentMenuItem.setTag(COMMENT_ITEM_DROPDOWN_MENU_CANCEL_ID);

    }

    private void onCancelCommentMenuItemClick() {

    }

    private void onCommentHeaderLongClick() {

        float[] outerRadii = new float[] {
                30,
                30,
                30,
                30,
                30,
                30,
                30,
                30
        };
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(ContextCompat.getColor(PostPageCommentsListViewHolder.this.context, R.color.primary_color_half_opacity));

        PostPageCommentsListViewHolder.this.commentContentHeaderLayout.setBackground(shapeDrawable);

        this.longClickPowerMenu = ((MyPowerMenu) new MyPowerMenu.Builder(this.context)
                .setAnimation(MenuAnimation.SHOW_UP_CENTER)
                .setAutoDismiss(true)
                .addItem(this.copyCommentMenuItem)
                .addItem(this.editCommentMenuItem)
                .addItem(this.deleteCommentMenuItem)
                .addItem(this.cancelCommentMenuItem)
                .setMenuRadius(UIUtilsManager.get().convertDpToPixels(this.context, 10))
                .setMenuShadow(UIUtilsManager.get().convertDpToPixels(this.context, 2))
                .setTextColor(ContextCompat.getColor(this.context, R.color.light_primary_color))
                .setSelectedTextColor(ContextCompat.getColor(this.context, R.color.light_primary_color_half_opacity))
                .setMenuColor(ContextCompat.getColor(this.context, R.color.primary_color))
                .setSelectedMenuColor(ContextCompat.getColor(this.context, R.color.primary_color_half_opacity))
                .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                    @Override
                    public void onItemClick(int position, PowerMenuItem item) {

                        PostPageCommentsListViewHolder.this.onCommentPowerMenuItemClick(position, item);

                    }
                }).setOnDismissListener(new OnDismissedListener() {
                    @Override
                    public void onDismissed() {

                        PostPageCommentsListViewHolder.this.powerMenuListener.notifyPowerMenuShow(
                                PostPageCommentsListViewHolder.this.longClickPowerMenu
                        );

                        float[] outerRadii = new float[] {
                                30,
                                30,
                                30,
                                30,
                                30,
                                30,
                                30,
                                30
                        };
                        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
                        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
                        shapeDrawable.getPaint().setColor(ContextCompat.getColor(PostPageCommentsListViewHolder.this.context, R.color.primary_color_extra_opacity));

                        PostPageCommentsListViewHolder.this.commentContentHeaderLayout.setBackground(shapeDrawable);

                    }
                })
                .build());

        PostPageCommentsListViewHolder.this.powerMenuListener.notifyPowerMenuShow(
                PostPageCommentsListViewHolder.this.longClickPowerMenu
        );

        //screen size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)this.context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;

        //popup location
        int[] popupLocation = new int[2];
        PostPageCommentsListViewHolder.this.commentBackgroundLayout.getLocationOnScreen(popupLocation);
        int popupLocationY = popupLocation[1];

        //popup height
        int popupHeight = this.longClickPowerMenu.getContentViewHeight();

        if (popupLocationY + UIUtilsManager.get().convertDpToPixels(this.context, 12)
                > screenHeight - popupHeight) {

            if (this.isRtl()) {

                this.longClickPowerMenu.setAnimation(MenuAnimation.SHOWUP_BOTTOM_LEFT);

            } else {

                this.longClickPowerMenu.setAnimation(MenuAnimation.SHOWUP_BOTTOM_RIGHT);

            }

            this.longClickPowerMenu.showAsDropDown(this.popupAnchor, 0 ,-popupHeight);

        } else {

            if (this.isRtl()) {

                this.longClickPowerMenu.setAnimation(MenuAnimation.SHOWUP_TOP_LEFT);

            } else {

                this.longClickPowerMenu.setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT);

            }

            this.longClickPowerMenu.showAsDropDown(this.popupAnchor, 0 ,0);

        }

    }

    private void onCommentPowerMenuItemClick(int position, PowerMenuItem item) {

        if (item.getTag().equals(COMMENT_ITEM_DROPDOWN_MENU_COPY_ID)) {

            this.onCopyCommentMenuItemClick();

        } else if (item.getTag().equals(COMMENT_ITEM_DROPDOWN_MENU_EDIT_ID)) {

            this.onEditCommentMenuItemClick();

        } else if (item.getTag().equals(COMMENT_ITEM_DROPDOWN_MENU_DELETE_ID)) {

            this.onDeleteCommentMenuItemClick();

        } else if (item.getTag().equals(COMMENT_ITEM_DROPDOWN_MENU_CANCEL_ID)) {

            this.onCancelCommentMenuItemClick();

        }

    }

    private void initEditorNameTextView(View view) {

        this.editorNameTextView = (TextView) view.findViewById(R.id.post_page_comment_view_holder_editor_name_view);

    }

    private void initCommentTextTextViewLayout(View view) {

        this.commentContentTextView = (TextView) view.findViewById(R.id.post_page_comment_view_holder_post_text_view);

    }

    //comment footer
    private void initCommentFooterLayout(View view) {

        this.commentContentFooterLayout = (RelativeLayout) view.findViewById(R.id.post_page_comment_view_holder_content_footer_layout);

        this.initCommentTimestampLayout(view);

        this.initCommentLikesLayout(view);

        this.initCommentCommentsLayout(view);
    }

    private void initCommentTimestampLayout(View view) {

        this.commentTimestampLayout = (RelativeLayout) view.findViewById(R.id.post_page_comment_view_holder_timestamp_layout);

        this.initCommentTimestampIconImageView(view);

        this.initCommentTimestampTextView(view);

    }

    private void initCommentTimestampIconImageView(View view) {

        this.commentTimestampIconImageView = (ImageView) view.findViewById(R.id.post_page_comment_view_holder_timestamp_icon);

    }

    private void initCommentTimestampTextView(View view) {

        this.commentTimestampTextView = (TextView) view.findViewById(R.id.post_page_comment_view_holder_post_timestamp_text_view);
    }

    private void initCommentLikesLayout(View view) {

        this.commentLikesLayout = (RelativeLayout) view.findViewById(R.id.post_page_comment_view_holder_likes_layout);

        this.initCommentLikesButtonImageView(view);

        this.initCommentLikesTextView(view);

    }

    private void initCommentLikesButtonImageView(View view) {

        this.commentLikesButtonImageView = (ImageView) view.findViewById(R.id.post_page_comment_view_holder_likes_button);

    }

    private void initCommentLikesTextView(View view) {

        this.commentLikesTextView = (TextView) view.findViewById(R.id.post_page_comment_view_holder_likes_text);

    }

    private void initCommentCommentsLayout(View view) {

        this.commentCommentsLayout = (RelativeLayout) view.findViewById(R.id.post_page_comment_view_holder_comments_layout);

        this.initCommentCommentsButtonImageView(view);

        this.initCommentCommentsTextView(view);

    }

    private void initCommentCommentsButtonImageView(View view) {

        this.commentCommentsButtonImageView = (ImageView) view.findViewById(R.id.post_page_comment_view_holder_comments_button);

    }

    private void initCommentCommentsTextView(View view) {

        this.commentCommentsTextView = (TextView) view.findViewById(R.id.post_page_comment_view_holder_comments_text);

    }

    //social feed post
    public void setPostPageComment(PostComment postPageComment) {

        if (postPageComment != null) {

            this.postPageComment = postPageComment;

        }

        this.updateComment();

    }

    //update comment
    public void updateComment() {

        if (this.postPageComment != null) {

            //comment editor
            this.updateEditor();

            //comment timestamp
            this.updatePostTimestampTextView();

            //comment text
            this.updatePostTextTextView();

            //timestamp icon
            this.updatePostTimestampIconImageView();

            //comment like button
            this.updatePostLikesButtonImageView();

            //comment comments button
            this.updatePostCommentsButtonImageView();

            //comment likes text
            this.updatePostLikesTextView();

            //comment comments text
            this.updatePostCommentsTextView();

        }

    }

    private void updateEditor() {

        if (this.postPageComment.getGuest() != null
                || this.postPageComment.getUser() != null) {

            //comment editor profile picture
            this.updateEditorProfilePictureImageView();

            //comment editor name
            this.updateEditorNameTextView();

        }

    }

    //comment editor profile picture
    private void updateEditorProfilePictureImageView() {

        if (this.postPageComment != null && this.postPageComment.getGuest() != null && this.postPageComment.getGuest().getImageUrl() != null) {

            Glide.with(this.context)
                    .asBitmap()
                    .load(this.postPageComment.getGuest().getImageUrl())
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.circleCropTransform())
                    .into(this.editorProfilePictureImageView);

        } else if (this.postPageComment != null && this.postPageComment.getUser() != null && this.postPageComment.getUser().getImageUrl() != null) {

            Glide.with(this.context)
                    .asBitmap()
                    .load(this.postPageComment.getUser().getImageUrl())
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.circleCropTransform())
                    .into(this.editorProfilePictureImageView);

        } else {

            //put an placeholder default image
//            Glide.with(this.context)
//                    .asBitmap()
//                    .load(placeholderimage)
//                    .into(this.editorProfilePictureImageView);

        }

    }

    //comment editor name
    private void updateEditorNameTextView() {

        if (this.postPageComment != null && this.postPageComment.getGuest() != null
                && this.postPageComment.getGuest().getName() != null) {

            this.editorNameTextView.setText(this.postPageComment.getGuest().getName());

        } else if (this.postPageComment != null && this.postPageComment.getUser() != null
                && this.postPageComment.getUser().getFirstName() != null
                && this.postPageComment.getUser().getLastName() != null) {

            this.editorNameTextView.setText(this.postPageComment.getUser().getFirstName()
                    + " " + postPageComment.getUser().getLastName());

        } else {

            this.editorNameTextView.setText("No User!");

        }

        this.editorNameTextView.setTypeface(this.editorNameTextView.getTypeface(), Typeface.BOLD);

    }

    //comment text
    private void updatePostTextTextView() {

        if (this.postPageComment != null && this.postPageComment.getContent() != null) {

            if (PostPageCommentsListViewHolder.this.postPageComment.getContent().length() > MAX_COMMENT_LENGTH) {

                String readMoreText = "Load More";
                String shortcutContent = PostPageCommentsListViewHolder.this.postPageComment.getContent().substring(
                        0, MAX_COMMENT_LENGTH
                );
                String text = shortcutContent + " ... \n" + readMoreText;
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
                spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(PostPageCommentsListViewHolder.this.context, R.color.primary_color))
                        , text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                PostPageCommentsListViewHolder.this.commentContentTextView.setText(
                        spannableStringBuilder
                );

            } else if (PostPageCommentsListViewHolder.this.postPageComment.getContent().split("\r\n|\r|\n").length > MAX_COMMENT_LINES) {


                String readMoreText = "Load More";

                String[] lines = PostPageCommentsListViewHolder.this.postPageComment.getContent().split("\r\n|\r|\n");
                String shortcutContent = TextUtils.join("\n", Arrays.copyOfRange(lines, 0, MAX_COMMENT_LINES));

                String text = shortcutContent + " ... \n" + readMoreText;
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
                spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(PostPageCommentsListViewHolder.this.context, R.color.primary_color))
                        , text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                PostPageCommentsListViewHolder.this.commentContentTextView.setText(
                        spannableStringBuilder
                );

            } else {

                String text = PostPageCommentsListViewHolder.this.postPageComment.getContent();
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);

                PostPageCommentsListViewHolder.this.commentContentTextView.setText(
                        spannableStringBuilder
                );

            }

        } else {

            this.commentContentTextView.setText("");

        }

    }

    //comment timestamp
    private void updatePostTimestampTextView() {

        if (this.postPageComment != null && this.postPageComment.getTimeStamp() != null) {

            PrettyTime prettyTime = new PrettyTime(Locale.getDefault());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

            try {

                this.commentTimestampTextView.setText(
                        prettyTime.format(dateFormat.parse(this.postPageComment.getTimeStamp()))                );

            } catch (ParseException e) {
                this.commentTimestampTextView.setText(this.postPageComment.getTimeStamp());
                e.printStackTrace();
            }

        } else {

            this.commentTimestampTextView.setText("");

        }

        this.commentTimestampTextView.setTextColor(ContextCompat.getColor(this.context, R.color.primary_color_half_opacity));
        this.commentTimestampTextView.setTypeface(this.commentLikesTextView.getTypeface(), Typeface.BOLD);

    }

    //comment likes text
    private void updatePostLikesTextView() {

        if (this.postPageComment != null) {

            this.commentLikesTextView.setText(Integer.toString(Math.max(0, this.postPageComment.getLikes())));

        } else {

            this.commentLikesTextView.setText("0");

        }

        this.commentLikesTextView.setTextColor(ContextCompat.getColor(this.context, R.color.primary_color_half_opacity));
        this.commentLikesTextView.setTypeface(this.commentLikesTextView.getTypeface(), Typeface.BOLD);

    }

    //comment comments text
    private void updatePostCommentsTextView() {

        if (this.postPageComment != null) {

            this.commentCommentsTextView.setText(Integer.toString(Math.max(0, this.postPageComment.getComments())));

        } else {

            this.commentCommentsTextView.setText("");

        }

        this.commentCommentsTextView.setTextColor(ContextCompat.getColor(this.context, R.color.primary_color_half_opacity));
        this.commentCommentsTextView.setTypeface(this.commentCommentsTextView.getTypeface(), Typeface.BOLD);

    }

    //comment like button
    private void updatePostTimestampIconImageView() {

        this.commentTimestampIconImageView.setImageDrawable(
                ((IconicsDrawable) Icons.get().findDrawable(this.context, "faw_clock1"))
                .sizeDp(12).colorRes(R.color.primary_color_half_opacity).alpha(136));

        if (this.postPageComment != null) {

        }

    }

    //comment like button
    private void updatePostLikesButtonImageView() {

        this.commentLikesButtonImageView.setImageDrawable(
                ((IconicsDrawable) Icons.get().findDrawable(this.context, "faw_heart1"))
                .sizeDp(12).colorRes(R.color.primary_color_half_opacity).alpha(136));

        if (this.postPageComment != null) {

        }

    }

    //comment comments button
    private void updatePostCommentsButtonImageView() {

        this.commentCommentsButtonImageView.setImageDrawable(
                ((IconicsDrawable) Icons.get().findDrawable(this.context, "faw_comment1"))
                        .sizeDp(12).colorRes(R.color.primary_color_half_opacity).alpha(136));

        if (this.postPageComment != null) {

        }

    }

    //skins
    public void setScreenSkin(int screenSkin) {
        this.screenSkin = screenSkin;

        this.updateComment();

    }

    protected boolean isRtl() {
        return TextUtilsCompat.getLayoutDirectionFromLocale(
                this.context.getResources().getConfiguration().locale) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

}