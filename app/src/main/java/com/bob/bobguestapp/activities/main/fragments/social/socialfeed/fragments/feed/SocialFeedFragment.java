package com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.text.TextUtilsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.social.postpage.PostPageActivity;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.content.PostContentActivity;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.PostMediaActivity;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.connector.SocialFeedConnector;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.adapter.SocialFeedPostsListAdapter;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.listener.PostsListEventsListener;
import com.bob.bobguestapp.tools.database.objects.Post;
import com.bob.bobguestapp.tools.parsing.MyGsonParser;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.enums.MediaItemType;
import com.bob.toolsmodule.http.requests.JsonServerRequest;
import com.bob.toolsmodule.http.serverbeans.ApplicativeResponse;
import com.bob.toolsmodule.http.serverbeans.Guest;
import com.bob.toolsmodule.objects.MediaItem;
import com.bob.toolsmodule.parsing.GsonParser;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.drawable.DrawableUtilsManager;
import com.bob.uimodule.icons.Icons;
import com.bob.uimodule.popup.PowerMenuListener;
import com.bob.uimodule.recyclerview.MyContainerView;
import com.bob.uimodule.theme.ThemeUtilsManager;
import com.bob.uimodule.views.loadingcontainer.ManagementFragment;
import com.google.gson.Gson;
import com.mikepenz.iconics.IconicsDrawable;
import com.skydoves.powermenu.PowerMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.bob.bobguestapp.activities.main.fragments.social.postupload.content.PostContentActivity.ARG_EXTRA_POST_CONTENT_POST_JSON;
import static com.bob.bobguestapp.activities.main.fragments.social.postupload.content.PostContentActivity.RESPONSE_UPLOADED_POST;
import static com.bob.uimodule.recyclerview.MyRecyclerView.RECYCLER_VIEW;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.FAILURE_MESSAGE;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.LOADING;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.NONE_MESSAGE;


public class SocialFeedFragment extends ManagementFragment implements PowerMenuListener, PostsListEventsListener {

    private static final int POSTS_BATCH = 10;

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";

    //get all posts url
    private static final String GET_ALL_POST_URL = BOB_SERVER_MOBILE_SERVICES_URL + "/feed/guest/post/page";
    private static final String DELETE_A_POST = BOB_SERVER_MOBILE_SERVICES_URL + "/feed/guest/post";

    //main view screen states
    public static final int POSTS_LIST = 10;

    //skins
    private int sortDialogSkin = ThemeUtilsManager.PRIMARY_COLOR_SKIN;
    private int filterDialogSkin = ThemeUtilsManager.PRIMARY_COLOR_SKIN;

    //intro commands
    private SocialFeedConnector socialFeedConnector;

    //posts layout
    private RelativeLayout postsListLayout;
    private ImageButton uploadPostButton;
    private View.OnTouchListener onUploadPostButtonTouchListener;
    private SwipeRefreshLayout postsListContainerSwipeRefreshLayout;
    private MyContainerView socialFeedPostsListContainer;
    private SocialFeedPostsListAdapter socialFeedPostsListAdapter;
    private ArrayList<Post> postsList;
    private PowerMenu postPowerMenu;

    //animations
    private Animation touchDownAnimation;
    private Animation touchUpAnimation;

    public SocialFeedFragment() {

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.REQUESTS_FRAGMENT_SKIN);
        this.sortDialogSkin = MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.REQUESTS_FRAGMENT_SORT_DIALOG_SKIN);
        this.filterDialogSkin = MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.REQUESTS_FRAGMENT_FILTER_DIALOG_SKIN);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        this.socialFeedPostsListContainer.setScreenState(LOADING);

        this.makeRefreshPostsRequest();

//        this.creteDemoPosts();

        return view;

    }


    @Override
    public View onCreateMainView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //init management skin
        this.managementViewContainer.setScreenSkin(this.screenSkin);

        //view
        View view = inflater.inflate(R.layout.fragment_social_feed, container, false);

        //init posts list layouts
        this.initPostsLayout(view);

        //animation
        this.initAnimations();

        this.managementViewContainer.setScreenState(POSTS_LIST);

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void notifyPowerMenuShow(PowerMenu powerMenu) {

        this.postPowerMenu = powerMenu;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        final int unmaskedRequestCode = requestCode & 0x0000ffff;

        Log.i("onActivityResultYo", "SocialFeedFragment - ReqCode: " + Integer.toString(unmaskedRequestCode)
                + "ResCode: " + Integer.toString(resultCode));

        if (unmaskedRequestCode == RESPONSE_UPLOADED_POST) {

            if(resultCode == Activity.RESULT_OK){

                if (data.hasExtra(ARG_EXTRA_POST_CONTENT_POST_JSON)) {

                    String postJson = data.getStringExtra(ARG_EXTRA_POST_CONTENT_POST_JSON);

                    try {

                        Post post = GsonParser.getParser().create().fromJson(postJson, Post.class);

                        if (post != null) {

                            if (data.hasExtra("DELETE")) {

                                this.deletePost(post);

                            } else {

                                this.addOrUpdatePost(post);

                            }

                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                }

            } else if (resultCode == Activity.RESULT_CANCELED) {

                //Write your code if there's no result

            }

        }

    }

    //screen state
    @Override
    protected void setMainViewScreenState(int screenState) {

        this.postsListLayout.setVisibility(INVISIBLE);

        switch (screenState) {

            case POSTS_LIST:
                this.postsListLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //social feed commands
    public void setSocialFeedConnector(SocialFeedConnector socialFeedConnector) {
        this.socialFeedConnector = socialFeedConnector;
    }

    public void addOrUpdatePost(Post post) {

        if (post != null && this.socialFeedPostsListAdapter != null) {

            List<Post> currentPosts = this.socialFeedPostsListAdapter.getAllObjects();

            for (Post currentPost : currentPosts ) {

                if (currentPost.getPostId() == post.getPostId()) {

                    this.socialFeedPostsListAdapter.getAllObjects().remove(currentPost);

                    break;

                }

            }

            this.socialFeedPostsListAdapter.getAllObjects().add(post);

            this.socialFeedPostsListAdapter.updateObjects();

            this.socialFeedPostsListAdapter.notifyDataSetChanged();

        }

    }

    public void deletePost(Post post) {

        if (post != null && this.socialFeedPostsListAdapter != null) {

            List<Post> currentPosts = this.socialFeedPostsListAdapter.getAllObjects();

            for (Post currentPost : currentPosts ) {

                if (currentPost.getPostId() == post.getPostId()) {

                    this.socialFeedPostsListAdapter.getAllObjects().remove(currentPost);

                    break;

                }

            }

            this.socialFeedPostsListAdapter.updateObjects();

            this.socialFeedPostsListAdapter.notifyDataSetChanged();

        }

    }

    //posts list
    private void initPostsLayout(View view) {

        //request list
        this.initPostsList();

        //request main layout
        this.initPostsMainLayout(view);

        //upload post button
        this.initUploadPostButton(view);

        //request recycler view swipe refresh layout
        this.initPostsListContainerSwipeRefreshLayout(view);

    }

    private void initPostsList() {
        this.postsList = new ArrayList<Post>();
    }

    private void initPostsMainLayout(View view) {

        this.postsListLayout = (RelativeLayout) view.findViewById(R.id.social_feed_posts_list_fragment_posts_layout);

    }

    private void initUploadPostButton(View view) {

        this.uploadPostButton = (ImageButton) view.findViewById(R.id.social_feed_posts_list_fragment_upload_post_button);

        this.uploadPostButton.setBackground(
                DrawableUtilsManager.get().getRoundSelectableDrawable(ContextCompat.getColor(this.getContext(), R.color.primary_color))
        );

        ViewCompat.setElevation(
                this.uploadPostButton,
                UIUtilsManager.get().convertDpToPixels(this.getContext(), 10)
        );

        this.uploadPostButton.setImageDrawable(
                ((IconicsDrawable) Icons.get().findDrawable(this.getContext(), "gmd_add"))
                        .sizeDp(24).colorRes(R.color.light_primary_color)
        );

        int margin = UIUtilsManager.get().convertDpToPixels(SocialFeedFragment.this.getContext(), 5);

        if (isRtl()) {

            this.uploadPostButton.setTranslationX(margin);

        } else {

            this.uploadPostButton.setTranslationX(-margin);

        }

        this.uploadPostButton.setTranslationY(-margin);

        this.initOnTouchUploadPostButtonListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchUploadPostButtonListener() {

        this.onUploadPostButtonTouchListener = new View.OnTouchListener() {

            float dX;
            float dY;
            float startX;
            float startY;
            int lastAction;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:

                        v.setPressed(true);

                        this.dX = v.getX() - event.getRawX();
                        this.dY = v.getY() - event.getRawY();
                        this.startX = event.getRawX();
                        this.startY = event.getRawY();
                        this.lastAction = MotionEvent.ACTION_DOWN;

                        break;
                    case MotionEvent.ACTION_MOVE:

                        int margin = UIUtilsManager.get().convertDpToPixels(SocialFeedFragment.this.getContext(), 5);

                        if (event.getRawX() + this.dX > SocialFeedFragment.this.postsListLayout.getX()+ margin
                                && event.getRawX() + this.dX + v.getWidth() + margin < SocialFeedFragment.this.postsListLayout.getX() + SocialFeedFragment.this.postsListLayout.getWidth()) {

                            v.setX(event.getRawX() + this.dX);

                        }

                        if (event.getRawY() + this.dY > SocialFeedFragment.this.postsListLayout.getY() + margin
                                && event.getRawY() + this.dY + v.getHeight() + margin < SocialFeedFragment.this.postsListLayout.getY() + SocialFeedFragment.this.postsListLayout.getHeight()) {

                            v.setY(event.getRawY() + this.dY);

                        }

                        this.lastAction = MotionEvent.ACTION_MOVE;
                        break;
                    case MotionEvent.ACTION_UP:

                        v.setPressed(false);

                        if (Math.abs(startX - event.getRawX()) < 10 && Math.abs(startY - event.getRawY()) < 10){

                            SocialFeedFragment.this.onUploadPostButtonClicked();

                        }
                        break;
                    default:
                        return false;
                }
                return true;


            }
        };

        this.uploadPostButton.setOnTouchListener(this.onUploadPostButtonTouchListener);

    }

    private void onUploadPostButtonClicked() {

        Intent intent = new Intent(this.getContext(), PostMediaActivity.class);

        this.startActivityForResult(intent, RESPONSE_UPLOADED_POST);

    }

    private void initPostsListContainerSwipeRefreshLayout(View view) {

        this.postsListContainerSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.social_feed_posts_list_fragment_posts_list_recycler_view_swipe_refresh_layout);

        this.postsListContainerSwipeRefreshLayout.setColorSchemeColors(MyAppThemeUtilsManager.get(this.getContext()).getColor(
                MyAppThemeUtilsManager.DEFAULT_CIRCULAR_PROGRESS_BAR_PRIMARY_COLOR,
                this.screenSkin
        ), MyAppThemeUtilsManager.get(this.getContext()).getColor(
                MyAppThemeUtilsManager.DEFAULT_CIRCULAR_PROGRESS_BAR_SECONDARY_COLOR,
                this.screenSkin
        ));

        this.postsListContainerSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(MyAppThemeUtilsManager.get(this.getContext()).getColor(
                MyAppThemeUtilsManager.DEFAULT_CIRCULAR_PROGRESS_BAR_BACKGROUND_PRIMARY_COLOR,
                this.screenSkin
        ));

        this.postsListContainerSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                SocialFeedFragment.this.socialFeedPostsListContainer.setScreenState(LOADING);
                SocialFeedFragment.this.makeRefreshPostsRequest();
//                SocialFeedFragment.this.creteDemoPosts();
                postsListContainerSwipeRefreshLayout.setRefreshing(false);
            }
        });

        //recycler view
        this.initPostsRecyclerView(view);

    }

    private void initPostsRecyclerView(View view) {

        this.socialFeedPostsListContainer = (MyContainerView) view.findViewById(R.id.social_feed_posts_list_fragment_posts_list_container);

        this.socialFeedPostsListContainer.setRecyclerViewOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                SocialFeedFragment.this.makeGetPostsRequest();

            }
        });

        this.socialFeedPostsListContainer.setScreenSkin(ThemeUtilsManager.LIGHT_COLOR_SKIN);

        this.socialFeedPostsListContainer.getContainer().setBackgroundColor(
                ContextCompat.getColor(this.getContext(), R.color.dark_primary_color_extra_opacity)
        );

        //horizontal line divider between items
//        this.setHorizontalDivider();

        this.setAdapter();

    }

    private void setHorizontalDivider() {

        DividerItemDecoration horizontalDecorationOpacity = new DividerItemDecoration(this.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDividerOpacity = ContextCompat.getDrawable(getActivity(), R.drawable.horizontal_divider).mutate();
        horizontalDividerOpacity.setColorFilter(new PorterDuffColorFilter(
                MyAppThemeUtilsManager.get(this.getContext()).getColor(MyAppThemeUtilsManager.DEFAULT_RECYCLER_VIEW_SEPARATOR_COLOR, this.screenSkin)
                ,PorterDuff.Mode.SRC_ATOP));
        horizontalDecorationOpacity.setDrawable(horizontalDividerOpacity);
        this.socialFeedPostsListContainer.getRecyclerView().addItemDecoration(horizontalDecorationOpacity);

        //        this.requestsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));

    }

    private void setAdapter() {

        if (this.postsList != null) {
            this.socialFeedPostsListAdapter = new SocialFeedPostsListAdapter(this.getContext(), this.getChildFragmentManager(), this.postsList, this, this);
        } else {
            this.socialFeedPostsListAdapter = new SocialFeedPostsListAdapter(this.getContext(), this.getChildFragmentManager(), new ArrayList<Post>(), this, this);
        }

        this.socialFeedPostsListAdapter.setScreenSkin(this.screenSkin);
        this.socialFeedPostsListContainer.getContainer().setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false) {

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

        });
        this.socialFeedPostsListContainer.getContainer().setAdapter(this.socialFeedPostsListAdapter);

    }

    private void onRequestRefreshClick() {

        this.socialFeedPostsListContainer.setScreenState(LOADING);

//        this.makeRefreshPostsRequest();

    }

    @Override
    public void onPostDelete(Post post) {

        this.makeDeletePostRequest(post);

    }

    @Override
    public void onPostEdit(Post post) {

        if (post != null) {

            Intent intent = new Intent(this.getContext(), PostContentActivity.class);

            String postJson = GsonParser.getParser().create().toJson(post, Post.class);

            intent.putExtra(ARG_EXTRA_POST_CONTENT_POST_JSON, postJson);

            this.startActivityForResult(intent, RESPONSE_UPLOADED_POST);

        }

    }

    @Override
    public void showPostPage(Post post) {

        Intent intent = new Intent(this.getContext(), PostPageActivity.class);

        String postJson = GsonParser.getParser().create().toJson(post, Post.class);

        intent.putExtra(PostPageActivity.ARG_EXTRA_POST_PAGE_POST_JSON, postJson);

        this.startActivityForResult(intent, RESPONSE_UPLOADED_POST);

    }

    //http requests
    private void makeRefreshPostsRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

                SocialFeedFragment.this.socialFeedPostsListContainer.setMessage("Loading...", NONE_MESSAGE);
                SocialFeedFragment.this.socialFeedPostsListContainer.setLoadMore(true, "Loading...");
                SocialFeedFragment.this.socialFeedPostsListContainer.setLoadingItems(true);

                SocialFeedFragment.this.postsListContainerSwipeRefreshLayout.setEnabled(false);
                SocialFeedFragment.this.postsListContainerSwipeRefreshLayout.setRefreshing(true);

            }

            @Override
            protected boolean requestCondition() {

                String guestId = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestId", null);
                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", null);

                return guestId != null && hotelId != null;

            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected void setRequestParams(HashMap<String, String> params) {
                String guestId = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestId", null);
                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", null);
//                params.put("guest", guestId);
//                params.put("hotel", hotelId);
                params.put("guest", "1");
                params.put("hotel", "1");
            }

            @Override
            protected String getRequestUrl() {

                String url = GET_ALL_POST_URL;

                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", null);
                url += "?hotel=" + hotelId;

                String guestId = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestId", null);
                url += "&guest=" + guestId;

                if (SocialFeedFragment.this.socialFeedPostsListContainer != null
                        && SocialFeedFragment.this.socialFeedPostsListContainer.getContainer() != null
                        && SocialFeedFragment.this.socialFeedPostsListContainer.getContainer().getAdapter() != null
                        && SocialFeedFragment.this.socialFeedPostsListContainer.getContainer().getAdapter() instanceof SocialFeedPostsListAdapter
                        && ((SocialFeedPostsListAdapter) SocialFeedFragment.this.socialFeedPostsListContainer.getContainer().getAdapter()).getFilter() != null) {

                    SocialFeedPostsListAdapter adapter = ((SocialFeedPostsListAdapter) SocialFeedFragment.this.socialFeedPostsListContainer.getContainer().getAdapter());

                    //offset & amount
                    url += "&offset=0";

                    url += "&amount=20";

                    //sorting type
                    String sortingType = adapter.getSortingType();
                    if (sortingType != null && !sortingType.equals("")) {

                        url += "&sort=" + adapter.getSortingType();

                    }

                }


                return url;
//                return BOB_SERVER_WEB_SERVICES_URL + "/wishes/getAllByUser?user=15";
            }

            @Override
            protected int getMethod() {
                return Request.Method.GET;
            }

            @Override
            protected ApplicativeResponse getApplicativeResponse(JSONObject response) {
                try {
                    Gson customGson = MyGsonParser.getParser().create();
                    String statusResponse = response.getJSONObject("response").getJSONObject("statusResponse").toString();

                    return customGson.fromJson(statusResponse, ApplicativeResponse.class);
                } catch (JSONException e) {
                    ApplicativeResponse statusResponse = new ApplicativeResponse();
                    statusResponse.setStatus("Failure");
                    statusResponse.setCode(ApplicativeResponse.FAILURE);
                    statusResponse.setMessage("error in parsing response");
                    return statusResponse;
                }
            }

            @Override
            protected void onSuccess(JSONObject response) {
                try {
                    Gson customGson = MyGsonParser.getParser().create();

                    if (response.getJSONObject("response").has("posts")) {

                        Post[] socialFeedPosts = new Post[0];
                        if (response.getJSONObject("response").optJSONArray("posts") != null) {
                            String socialFeedPostsString = response.getJSONObject("response").getJSONArray("posts").toString();
                            socialFeedPosts = customGson.fromJson(socialFeedPostsString, Post[].class);
                        } else if (response.getJSONObject("response").optJSONObject("posts") != null) {
                            String socialFeedPostsString = response.getJSONObject("response").getJSONObject("posts").toString();
                            socialFeedPosts = new Post[] {
                                    customGson.fromJson(socialFeedPostsString, Post.class)
                            };
                        }

                        if (socialFeedPosts != null && socialFeedPosts.length > 0) {

                            if (SocialFeedFragment.this.socialFeedPostsListContainer.getContainer().getAdapter() != null
                                    && SocialFeedFragment.this.socialFeedPostsListContainer.getContainer().getAdapter() instanceof SocialFeedPostsListAdapter) {

                                ArrayList<Post> postsFromServer = new ArrayList<Post>(Arrays.asList(socialFeedPosts));
                                ((SocialFeedPostsListAdapter) SocialFeedFragment.this.socialFeedPostsListContainer.getContainer().getAdapter()).setObjects(postsFromServer);
                                SocialFeedFragment.this.socialFeedPostsListContainer.getContainer().getAdapter().notifyDataSetChanged();

                            }

                            SocialFeedFragment.this.socialFeedPostsListContainer.setHasMoreItems(true);

                        } else {

                            SocialFeedFragment.this.socialFeedPostsListContainer.setFinishLoading(true, "-End-");
                            SocialFeedFragment.this.socialFeedPostsListContainer.setHasMoreItems(false);

                        }


                    } else {

                        SocialFeedFragment.this.socialFeedPostsListContainer.setFinishLoading(true, "-End-");
                        SocialFeedFragment.this.socialFeedPostsListContainer.setHasMoreItems(false);

                    }

                    SocialFeedFragment.this.socialFeedPostsListContainer.setScreenState(RECYCLER_VIEW);

                } catch (JSONException e) {
                    e.printStackTrace();
                    this.onDefaultError("error in parsing response");
                }
            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    SocialFeedFragment.this.socialFeedPostsListContainer.setMessage(message, FAILURE_MESSAGE);
                } else {
                    SocialFeedFragment.this.socialFeedPostsListContainer.setMessage("Getting Posts List Error!", FAILURE_MESSAGE);
                }
            }

            @Override
            protected void postRequest() {

                SocialFeedFragment.this.postsListContainerSwipeRefreshLayout.setRefreshing(false);
                SocialFeedFragment.this.postsListContainerSwipeRefreshLayout.setEnabled(true);

                SocialFeedFragment.this.socialFeedPostsListContainer.setLoadMore(false);
                SocialFeedFragment.this.socialFeedPostsListContainer.setLoadingItems(false);

            }

        };

        jsonServerRequest.makeRequest();

    }

    private void makeGetPostsRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

                SocialFeedFragment.this.socialFeedPostsListContainer.setLoadingItems(true);
                SocialFeedFragment.this.socialFeedPostsListContainer.setLoadMore(true, "Loading...");

            }

            @Override
            protected boolean requestCondition() {

                String guestId = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestId", null);
                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", null);

                return guestId != null && hotelId != null;

            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected void setRequestParams(HashMap<String, String> params) {
                String guestId = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestId", null);
                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", null);
                params.put("guestId", guestId);
                params.put("hotelId", hotelId);
            }

            @Override
            protected int getMethod() {
                return Request.Method.GET;
            }

            @Override
            protected String getRequestUrl() {

                String url = GET_ALL_POST_URL;

                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", null);
                url += "?hotel=" + hotelId;

                String guestId = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestId", null);
                url += "&guest=" + guestId;

                if (SocialFeedFragment.this.socialFeedPostsListContainer != null
                        && SocialFeedFragment.this.socialFeedPostsListContainer.getContainer() != null
                        && SocialFeedFragment.this.socialFeedPostsListContainer.getContainer().getAdapter() != null
                        && SocialFeedFragment.this.socialFeedPostsListContainer.getContainer().getAdapter() instanceof SocialFeedPostsListAdapter
                        && ((SocialFeedPostsListAdapter) SocialFeedFragment.this.socialFeedPostsListContainer.getContainer().getAdapter()).getFilter() != null) {

                    SocialFeedPostsListAdapter adapter = ((SocialFeedPostsListAdapter) SocialFeedFragment.this.socialFeedPostsListContainer.getContainer().getAdapter());

                    //offset & amount
                    if (adapter.getAllObjects() != null && adapter.getAllObjects().size() > 0) {
                        url += "&offset=" + adapter.getAllObjects().get(adapter.getAllObjects().size() - 1).getPostId();
                    } else {
                        url += "&offset=0";
                    }

                    url += "&amount=20";

                    //sorting type
                    String sortingType = adapter.getSortingType();
                    if (sortingType != null && !sortingType.equals("")) {

                        url += "&sort=" + adapter.getSortingType();

                    }

                }

                return url;

            }

            @Override
            protected ApplicativeResponse getApplicativeResponse(JSONObject response) {
                try {
                    Gson customGson = MyGsonParser.getParser().create();
                    String statusResponse = response.getJSONObject("response").getJSONObject("statusResponse").toString();

                    return customGson.fromJson(statusResponse, ApplicativeResponse.class);
                } catch (JSONException e) {
                    ApplicativeResponse statusResponse = new ApplicativeResponse();
                    statusResponse.setStatus("Failure");
                    statusResponse.setCode(ApplicativeResponse.FAILURE);
                    statusResponse.setMessage("error in parsing response");
                    return statusResponse;
                }
            }

            @Override
            protected void onSuccess(JSONObject response) {
                try {
                    Gson customGson = MyGsonParser.getParser().create();

                    if (response.getJSONObject("response").has("posts")) {

                        Post[] socialFeedPosts = new Post[0];
                        if (response.getJSONObject("response").optJSONArray("posts") != null) {
                            String socialFeedPostsString = response.getJSONObject("response").getJSONArray("posts").toString();
                            socialFeedPosts = customGson.fromJson(socialFeedPostsString, Post[].class);
                        } else if (response.getJSONObject("response").optJSONObject("posts") != null) {
                            String socialFeedPostsString = response.getJSONObject("response").getJSONObject("posts").toString();
                            socialFeedPosts = new Post[] {
                                    customGson.fromJson(socialFeedPostsString, Post.class)
                            };
                        }

                        if (socialFeedPosts != null && socialFeedPosts.length > 0) {

                            if (SocialFeedFragment.this.socialFeedPostsListContainer.getContainer().getAdapter() != null
                                    && SocialFeedFragment.this.socialFeedPostsListContainer.getContainer().getAdapter() instanceof SocialFeedPostsListAdapter) {

                                ArrayList<Post> postsToInsert = new ArrayList<Post>();
                                ArrayList<Post> postsFromServer = new ArrayList<Post>(Arrays.asList(socialFeedPosts));

                                List<Post> allPosts = ((SocialFeedPostsListAdapter) SocialFeedFragment.this.socialFeedPostsListContainer.getContainer().getAdapter()).getAllObjects();

                                ArrayList<Long> allPostsIds = new ArrayList<Long>();
                                for (Post post : allPosts) {
                                    if (post != null) {
                                        allPostsIds.add(post.getPostId());
                                    }
                                }

                                for (Post postFromServer : postsFromServer) {
                                    if (postFromServer != null && !allPostsIds.contains(postFromServer.getPostId())) {
                                        postsToInsert.add(postFromServer);
                                    }
                                }

                                ((SocialFeedPostsListAdapter) SocialFeedFragment.this.socialFeedPostsListContainer.getContainer().getAdapter()).addObjects(postsToInsert);
                                SocialFeedFragment.this.socialFeedPostsListContainer.getContainer().getAdapter().notifyDataSetChanged();

                            }

                            SocialFeedFragment.this.socialFeedPostsListContainer.setHasMoreItems(true);

                        } else {

                            SocialFeedFragment.this.socialFeedPostsListContainer.setFinishLoading(true, "-End-");
                            SocialFeedFragment.this.socialFeedPostsListContainer.setHasMoreItems(false);

                        }


                    } else {

                        SocialFeedFragment.this.socialFeedPostsListContainer.setFinishLoading(true, "-End-");
                        SocialFeedFragment.this.socialFeedPostsListContainer.setHasMoreItems(false);

                    }

                    SocialFeedFragment.this.socialFeedPostsListContainer.setScreenState(RECYCLER_VIEW);

                } catch (JSONException e) {
                    e.printStackTrace();
                    this.onDefaultError("error in parsing response");
                }
            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    SocialFeedFragment.this.socialFeedPostsListContainer.setMessage(message, FAILURE_MESSAGE);
                } else {
                    SocialFeedFragment.this.socialFeedPostsListContainer.setMessage("Getting Posts List Error!", FAILURE_MESSAGE);
                }
            }

            @Override
            protected void postRequest() {

                SocialFeedFragment.this.socialFeedPostsListContainer.setLoadMore(false);
                SocialFeedFragment.this.socialFeedPostsListContainer.setLoadingItems(false);

            }

        };

        jsonServerRequest.makeRequest();

    }

    private void makeDeletePostRequest(Post post) {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

            }

            @Override
            protected boolean requestCondition() {

                String guestId = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestId", null);
                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", null);

                return guestId != null && hotelId != null && post != null
                        && post.getPostId() != -1L;

            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected JSONObject getJsonObject() {

//                try {
//
//                    Guest guest = new Guest();
//                    guest.setId(1);
//                    post.setGuest(guest);
//
//                    Hotel hotel = new Hotel();
//                    hotel.setId(1);
//                    post.setHotel(hotel);
//
//                    JsonElement jsonPost = MyGsonParser.getParser().create().toJsonTree(post, Post.class);
//
//                    JsonObject jsonRequest = new JsonObject();
//                    jsonRequest.add("post", jsonPost);
//
//                    JsonObject jsonLoginRequest = new JsonObject();
//                    jsonLoginRequest.add("request", jsonRequest);
//
//                    return new JSONObject( new Gson().toJson(jsonLoginRequest));
//
//                } catch (JSONException e) {
//
//                    e.printStackTrace();
//                    return null;
//                }

                return null;

            }

            @Override
            protected String getRequestUrl() {

                String url = DELETE_A_POST;

                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", null);
                url += "?hotel=" + hotelId;

                String guestId = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestId", null);
                url += "&guest=" + guestId;

                url += "&post=" + Long.toString(post.getPostId());

                return url;

            }

            @Override
            protected int getMethod() {
                return Request.Method.DELETE;
            }

            @Override
            protected ApplicativeResponse getApplicativeResponse(JSONObject response) {
                try {

                    Gson customGson = MyGsonParser.getParser().create();
                    String statusResponse = response.getJSONObject("response").getJSONObject("statusResponse").toString();

                    return customGson.fromJson(statusResponse, ApplicativeResponse.class);

                } catch (JSONException e) {

                    ApplicativeResponse statusResponse = new ApplicativeResponse();
                    statusResponse.setStatus("Failure");
                    statusResponse.setCode(ApplicativeResponse.FAILURE);
                    statusResponse.setMessage("error in parsing response");
                    return statusResponse;

                }
            }

            @Override
            protected void onSuccess(JSONObject response) {

                if (SocialFeedFragment.this.socialFeedPostsListContainer.getContainer().getAdapter() != null
                        && SocialFeedFragment.this.socialFeedPostsListContainer.getContainer().getAdapter() instanceof SocialFeedPostsListAdapter) {

                    SocialFeedPostsListAdapter socialFeedPostsListAdapter =
                            ((SocialFeedPostsListAdapter)SocialFeedFragment.this.socialFeedPostsListContainer.getContainer().getAdapter());

                    long postId = post.getPostId();

                    List<Post> socialFeedPosts = socialFeedPostsListAdapter.getAllObjects();

                    List<Post> socialFeedPostsToRemove = new ArrayList<Post>();

                    for (Post post : socialFeedPosts) {

                        if (post != null && post.getPostId() == postId) {

                            socialFeedPostsToRemove.add(post);

                        }

                    }

                    socialFeedPostsListAdapter.getAllObjects().removeAll(socialFeedPostsToRemove);

                    socialFeedPostsListAdapter.updateObjects();

                    socialFeedPostsListAdapter.notifyDataSetChanged();

                }


            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {

                    Toast toast=Toast.makeText(SocialFeedFragment.this.getContext(), message, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    //some error message

                } else {

                    Toast toast=Toast.makeText(SocialFeedFragment.this.getContext(),"Error deleting comment", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    //some default error message

                }
            }

            @Override
            protected void postRequest() {

            }

        };

        jsonServerRequest.makeRequest();


    }

    //animations
    private void initAnimations() {

        this.initTouchDownAnimation();

        this.initTouchUpAnimation();

    }

    private void initTouchDownAnimation() {

        this.touchDownAnimation = new ScaleAnimation(1f, 0.80f, 1f, 0.80f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        this.touchDownAnimation.setDuration(300);
        this.touchDownAnimation.setFillEnabled(true);
        this.touchDownAnimation.setFillAfter(true);
        this.touchDownAnimation.setInterpolator(new OvershootInterpolator());

    }

    private void initTouchUpAnimation() {

        this.touchUpAnimation = new ScaleAnimation(0.80f, 1f, 0.80f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        this.touchUpAnimation.setDuration(300);
        this.touchUpAnimation.setFillEnabled(true);
        this.touchUpAnimation.setFillAfter(true);
        this.touchUpAnimation.setInterpolator(new OvershootInterpolator());

    }

    //back pressed handler
    public void onBackPressed() {

        if (this.postPowerMenu != null && this.postPowerMenu.isShowing()) {

            this.postPowerMenu.dismiss();

        } else {

        }
    }

    protected boolean isRtl() {
        return TextUtilsCompat.getLayoutDirectionFromLocale(
                this.getResources().getConfiguration().locale) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    private void creteDemoPosts() {

        Guest guest = new Guest();
        guest.setName("Deadpool");
        guest.setId(10);
        guest.setImageUrl("https://avatarfiles.alphacoders.com/129/thumb-129487.jpg");

        MediaItem[] mediaItems1 = new MediaItem[] {
                new MediaItem("https://cdn.empireonline.com/jpg/70/0/0/640/480/aspectfit/0/0/0/0/0/0/c/articles/58fcdbf8baffe22f387bcaec/deadpool-2.jpg", MediaItemType.IMAGE_TYPE, false),
                new MediaItem("https://cdn1.thr.com/sites/default/files/imagecache/scale_crop_768_433/2018/03/deadpool_2016_28.jpg", MediaItemType.IMAGE_TYPE, false),
                new MediaItem("bo_efYhYU2A", MediaItemType.YOUTUBE_TYPE, false),
                new MediaItem("d8MYugmXmGY", MediaItemType.YOUTUBE_TYPE, false),
                new MediaItem("izTMmZ9WYlE", MediaItemType.YOUTUBE_TYPE, false),
                new MediaItem("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", MediaItemType.VIDEO_TYPE, false),
                new MediaItem("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4", MediaItemType.VIDEO_TYPE, false),
                new MediaItem("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4", MediaItemType.VIDEO_TYPE, false),
                new MediaItem("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4", MediaItemType.VIDEO_TYPE, false),
                new MediaItem("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4", MediaItemType.VIDEO_TYPE, false),
                new MediaItem("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4", MediaItemType.VIDEO_TYPE, false)
        };

        MediaItem[] mediaItems2 = new MediaItem[] {
                new MediaItem("https://cdn.empireonline.com/jpg/70/0/0/640/480/aspectfit/0/0/0/0/0/0/c/articles/58fcdbf8baffe22f387bcaec/deadpool-2.jpg", MediaItemType.IMAGE_TYPE, false),
                new MediaItem("https://cdn1.thr.com/sites/default/files/imagecache/scale_crop_768_433/2018/03/deadpool_2016_28.jpg", MediaItemType.IMAGE_TYPE, false),
                new MediaItem("bo_efYhYU2A", MediaItemType.YOUTUBE_TYPE, false),
                new MediaItem("d8MYugmXmGY", MediaItemType.YOUTUBE_TYPE, false),
                new MediaItem("izTMmZ9WYlE", MediaItemType.YOUTUBE_TYPE, false),
                new MediaItem("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4", MediaItemType.VIDEO_TYPE, false),
                new MediaItem("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4", MediaItemType.VIDEO_TYPE, false),
                new MediaItem("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4", MediaItemType.VIDEO_TYPE, false),
                new MediaItem("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4", MediaItemType.VIDEO_TYPE, false),
                new MediaItem("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/VolkswagenGTIReview.mp4", MediaItemType.VIDEO_TYPE, false),
                new MediaItem("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4", MediaItemType.VIDEO_TYPE, false),
                new MediaItem("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4", MediaItemType.VIDEO_TYPE, false)
        };


        Post post = new Post(0, "רק ביבי!", mediaItems1, 8, 10, null, null, guest, new Timestamp(System.currentTimeMillis()).toString(), false);
        Post post1 = new Post(1, "Yo", mediaItems2, 8, 10, null, null, guest, new Timestamp(System.currentTimeMillis()).toString(), false);
        Post post2 = new Post(2, "Yo", mediaItems1, 8, 10, null, null, guest, new Timestamp(System.currentTimeMillis()).toString(), false);
        Post post3 = new Post(3, "Yo", mediaItems2, 8, 10, null, null, guest, new Timestamp(System.currentTimeMillis()).toString(), false);
        Post post4 = new Post(4, "Yo", mediaItems1, 8, 10, null, null, guest, new Timestamp(System.currentTimeMillis()).toString(), false);

        ArrayList<Post> posts = new ArrayList<Post>();

        posts.add(post);
        posts.add(post1);
        posts.add(post2);
        posts.add(post3);
        posts.add(post4);

        if (this.socialFeedPostsListContainer.getContainer().getAdapter() instanceof SocialFeedPostsListAdapter) {

            SocialFeedPostsListAdapter adapter = ((SocialFeedPostsListAdapter) this.socialFeedPostsListContainer.getContainer().getAdapter());

            adapter.setObjects(posts);
            adapter.notifyDataSetChanged();

        }

        this.managementViewContainer.setScreenState(POSTS_LIST);
        this.socialFeedPostsListContainer.setScreenState(RECYCLER_VIEW);

    }

}
