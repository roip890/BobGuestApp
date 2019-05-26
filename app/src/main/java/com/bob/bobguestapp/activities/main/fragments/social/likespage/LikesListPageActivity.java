package com.bob.bobguestapp.activities.main.fragments.social.likespage;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.social.likespage.likes.likeslist.adapter.PostPageLikesListAdapter;
import com.bob.bobguestapp.tools.database.objects.Post;
import com.bob.bobguestapp.tools.database.objects.PostLike;
import com.bob.bobguestapp.tools.parsing.MyGsonParser;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.http.requests.JsonServerRequest;
import com.bob.toolsmodule.http.serverbeans.ApplicativeResponse;
import com.bob.toolsmodule.parsing.GsonParser;
import com.bob.uimodule.recyclerview.MyRecyclerView;
import com.bob.uimodule.theme.ThemeUtilsManager;
import com.bob.uimodule.video.exoplayer.ExoPlayerFullScreenListener;
import com.bob.uimodule.views.loadingcontainer.ManagementActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.view.View.VISIBLE;
import static com.bob.uimodule.recyclerview.MyRecyclerView.RECYCLER_VIEW;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.FAILURE_MESSAGE;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.LOADING;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.NONE_MESSAGE;

public class LikesListPageActivity extends ManagementActivity {

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";

    //get all posts url
    private static final String GET_ALL_LIKES_OF_POST_URL = BOB_SERVER_MOBILE_SERVICES_URL + "/feed/guest/like/page";

    //post page arguments
    public static final String ARG_EXTRA_LIKES_PAGE_POST_JSON = "bob:mobile:postpage:postjson";  // Int

    //main view screen states
    public static final int POST = 10;

    //views
    private ConstraintLayout postPageLayout;

    //ikes
    private SwipeRefreshLayout postLikesSwipeRefreshLayout;
    private MyRecyclerView postLikesRecyclerView;
    private PostPageLikesListAdapter postPageLikesListAdapter;
    private ArrayList<PostLike> likesList;
    private Post post;

    //lock like
    protected boolean likeLocked = false;

    //animations
    private Animation touchDownAnimation;
    private Animation touchUpAnimation;

    //exo full screen listener
    private ExoPlayerFullScreenListener exoPlayerFullScreenListener;


    public LikesListPageActivity() {

        //theme
        this.appTheme = ThemeUtilsManager.DEFAULT_THEME;
        this.screenSkin = ThemeUtilsManager.LIGHT_COLOR_SKIN;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.makeRefreshPostLikesRequest();

        this.managementViewContainer.setScreenState(POST);

    }

    @Override
    protected View onCreateMainView(LayoutInflater inflater, ViewGroup container,
                                    Bundle savedInstanceState) {

        //background

        //view
        View view = inflater.inflate(R.layout.activity_likes_list_page, container, false);

        //layouts
        this.initMainViewLayout(view);

        //animation
        this.initAnimations();
        
        //status bar color
        this.initStatusBarColor();

        //navigation bar color
        this.initNavigationBarColor();

        //update post
        this.updatePost();

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initStatusBarColor() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.primary_color));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initNavigationBarColor() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.primary_color));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public int getNavigationBarSize() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && getResources().getIdentifier("navigation_bar_height", "dimen", "android") > 0) {

            return getResources().getDimensionPixelSize(
                    getResources().getIdentifier("navigation_bar_height", "dimen", "android")
            );

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            this.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }

        return 0;

    }

    //arguments
    protected void fillPostPageParameters(Bundle savedInstanceState) {

        Bundle bundle = savedInstanceState;

        if (bundle == null) bundle = getIntent().getExtras();

        if (bundle != null) {

            String postJson = bundle.getString(ARG_EXTRA_LIKES_PAGE_POST_JSON);

            try {

                this.post = GsonParser.getParser().create().fromJson(postJson, Post.class);

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }

    //management
    @Override
    protected void preOnCreate() {


    }

    @Override
    protected void postOnCreate() {


    }

    @Override
    protected void handleSavedInstanceState(Bundle savedInstanceState) {

        this.fillPostPageParameters(savedInstanceState);

    }

    @Override
    protected void setMainViewScreenState(int screenState) {

        this.postPageLayout.setVisibility(View.INVISIBLE);

        switch (screenState) {

            case POST:
                this.postPageLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //init views
    protected void initMainViewLayout(View view) {

        this.postPageLayout = (ConstraintLayout) view.findViewById(R.id.likes_list_page_activity_background_layout);

        this.initPostLikesSwipeRefreshLayout(view);

    }

    //likes list
    private void initPostLikesSwipeRefreshLayout(View view) {

        this.postLikesSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.likes_list_page_activity_likes_recycler_view_swipe_refresh_layout);

        this.postLikesSwipeRefreshLayout.setColorSchemeColors(MyAppThemeUtilsManager.get(this).getColor(
                MyAppThemeUtilsManager.DEFAULT_CIRCULAR_PROGRESS_BAR_PRIMARY_COLOR,
                this.screenSkin
        ), MyAppThemeUtilsManager.get(this).getColor(
                MyAppThemeUtilsManager.DEFAULT_CIRCULAR_PROGRESS_BAR_SECONDARY_COLOR,
                this.screenSkin
        ));

        this.postLikesSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(MyAppThemeUtilsManager.get(this).getColor(
                MyAppThemeUtilsManager.DEFAULT_CIRCULAR_PROGRESS_BAR_BACKGROUND_PRIMARY_COLOR,
                this.screenSkin
        ));

        this.postLikesSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                LikesListPageActivity.this.postLikesRecyclerView.setScreenState(LOADING);
                LikesListPageActivity.this.makeRefreshPostLikesRequest();

                LikesListPageActivity.this.postLikesSwipeRefreshLayout.setRefreshing(false);
            }
        });

        this.initPostLikesRecyclerView(view);

    }

    private void initPostLikesRecyclerView(View view) {

        this.postLikesRecyclerView = (MyRecyclerView) view.findViewById(R.id.likes_list_page_activity_likes_recycler_view);

        this.postLikesRecyclerView.setRecyclerViewOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LikesListPageActivity.this.makeGetPostLikesRequest();

            }
        });

        this.postLikesRecyclerView.setScreenSkin(ThemeUtilsManager.LIGHT_COLOR_SKIN);

        this.postLikesRecyclerView.getRecyclerView().setBackgroundColor(
                ContextCompat.getColor(this, R.color.light_primary_color)
        );

        this.postLikesRecyclerView.setPadding(
                0,
                0,
                0,
                this.getNavigationBarSize()
        );

        //horizontal line divider between items
//        this.setHorizontalDivider();

        this.setAdapter();

    }

    private void setAdapter() {

        if (this.likesList != null) {
            this.postPageLikesListAdapter = new PostPageLikesListAdapter(this, this.likesList);
        } else {
            this.postPageLikesListAdapter = new PostPageLikesListAdapter(this, new ArrayList<PostLike>());
        }

        this.postPageLikesListAdapter.setScreenSkin(this.screenSkin);
        
        this.postLikesRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false) {

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

        });
        
        this.postLikesRecyclerView.getRecyclerView().setAdapter(this.postPageLikesListAdapter);

    }

    //update views
    public void updatePost() {

        if (this.post != null) {

        }

    }

    //http requests
    private void makeRefreshPostLikesRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

                LikesListPageActivity.this.postLikesRecyclerView.setMessage("Loading...", NONE_MESSAGE);
                LikesListPageActivity.this.postLikesRecyclerView.setLoadMore(true, "Loading...");
                LikesListPageActivity.this.postLikesRecyclerView.setLoadingItems(true);

                LikesListPageActivity.this.postLikesSwipeRefreshLayout.setEnabled(false);
                LikesListPageActivity.this.postLikesSwipeRefreshLayout.setRefreshing(true);

            }

            @Override
            protected boolean requestCondition() {

                String guestId = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestId", null);
                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", null);

                return LikesListPageActivity.this.post != null && guestId != null && hotelId != null;

            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected void setRequestParams(HashMap<String, String> params) {

            }

            @Override
            protected String getRequestUrl() {

                String url = GET_ALL_LIKES_OF_POST_URL;

                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", null);
                url += "?hotel=" + hotelId;

                url += "&post=" + Long.toString(LikesListPageActivity.this.post.getPostId());

                if (LikesListPageActivity.this.postLikesRecyclerView != null
                        && LikesListPageActivity.this.postLikesRecyclerView.getRecyclerView() != null
                        && LikesListPageActivity.this.postLikesRecyclerView.getRecyclerView().getAdapter() != null
                        && LikesListPageActivity.this.postLikesRecyclerView.getRecyclerView().getAdapter() instanceof PostPageLikesListAdapter
                        && ((PostPageLikesListAdapter) LikesListPageActivity.this.postLikesRecyclerView.getRecyclerView().getAdapter()).getFilter() != null) {

                    PostPageLikesListAdapter adapter = ((PostPageLikesListAdapter) LikesListPageActivity.this.postLikesRecyclerView.getRecyclerView().getAdapter());

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

                    if (response.getJSONObject("response").has("likes")) {

                        PostLike[] postLikes = new PostLike[0];
                        if (response.getJSONObject("response").optJSONArray("likes") != null) {
                            String postLikesString = response.getJSONObject("response").getJSONArray("likes").toString();
                            postLikes = customGson.fromJson(postLikesString, PostLike[].class);
                        } else if (response.getJSONObject("response").optJSONObject("likes") != null) {
                            String postLikesString = response.getJSONObject("response").getJSONObject("likes").toString();
                            postLikes = new PostLike[] {
                                    customGson.fromJson(postLikesString, PostLike.class)
                            };
                        }

                        if (postLikes != null && postLikes.length > 0) {

                            if (LikesListPageActivity.this.postLikesRecyclerView.getRecyclerView().getAdapter() != null
                                    && LikesListPageActivity.this.postLikesRecyclerView.getRecyclerView().getAdapter() instanceof PostPageLikesListAdapter) {

                                ArrayList<PostLike> postsLikesFromServer = new ArrayList<PostLike>(Arrays.asList(postLikes));
                                ((PostPageLikesListAdapter) LikesListPageActivity.this.postLikesRecyclerView.getRecyclerView().getAdapter()).setObjects(postsLikesFromServer);
                                LikesListPageActivity.this.postLikesRecyclerView.getRecyclerView().getAdapter().notifyDataSetChanged();

                            }

                            LikesListPageActivity.this.postLikesRecyclerView.setHasMoreItems(true);

                        } else {

                            LikesListPageActivity.this.postLikesRecyclerView.setFinishLoading(true, "-End-");
                            LikesListPageActivity.this.postLikesRecyclerView.setHasMoreItems(false);

                        }


                    } else {

                        LikesListPageActivity.this.postLikesRecyclerView.setFinishLoading(true, "-End-");
                        LikesListPageActivity.this.postLikesRecyclerView.setHasMoreItems(false);

                    }

                    LikesListPageActivity.this.postLikesRecyclerView.setScreenState(RECYCLER_VIEW);

                } catch (JSONException e) {
                    e.printStackTrace();
                    this.onDefaultError("error in parsing response");
                }
            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    LikesListPageActivity.this.postLikesRecyclerView.setMessage(message, FAILURE_MESSAGE);
                } else {
                    LikesListPageActivity.this.postLikesRecyclerView.setMessage("Getting Posts List Error!", FAILURE_MESSAGE);
                }
            }

            @Override
            protected void postRequest() {

                LikesListPageActivity.this.postLikesSwipeRefreshLayout.setRefreshing(false);
                LikesListPageActivity.this.postLikesSwipeRefreshLayout.setEnabled(true);

                LikesListPageActivity.this.postLikesRecyclerView.setLoadMore(false);
                LikesListPageActivity.this.postLikesRecyclerView.setLoadingItems(false);

            }

        };

        jsonServerRequest.makeRequest();

    }

    private void makeGetPostLikesRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

                LikesListPageActivity.this.postLikesRecyclerView.setLoadingItems(true);
                LikesListPageActivity.this.postLikesRecyclerView.setLoadMore(true, "Loading...");

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

                String url = GET_ALL_LIKES_OF_POST_URL;

                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", null);
                url += "?hotel=" + hotelId;

                String guestId = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestId", null);
                url += "&post=" + guestId;

                if (LikesListPageActivity.this.postLikesRecyclerView != null
                        && LikesListPageActivity.this.postLikesRecyclerView.getRecyclerView() != null
                        && LikesListPageActivity.this.postLikesRecyclerView.getRecyclerView().getAdapter() != null
                        && LikesListPageActivity.this.postLikesRecyclerView.getRecyclerView().getAdapter() instanceof PostPageLikesListAdapter
                        && ((PostPageLikesListAdapter) LikesListPageActivity.this.postLikesRecyclerView.getRecyclerView().getAdapter()).getFilter() != null) {

                    PostPageLikesListAdapter adapter = ((PostPageLikesListAdapter) LikesListPageActivity.this.postLikesRecyclerView.getRecyclerView().getAdapter());

                    //offset & amount
                    if (adapter.getAllObjects() != null && adapter.getAllObjects().size() > 0) {
                        url += "&offset=" + adapter.getAllObjects().get(adapter.getAllObjects().size() - 1).getLikeId();
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

                    if (response.getJSONObject("response").has("likes")) {

                        PostLike[] postLikes = new PostLike[0];
                        if (response.getJSONObject("response").optJSONArray("likes") != null) {
                            String postLikesString = response.getJSONObject("response").getJSONArray("likes").toString();
                            postLikes = customGson.fromJson(postLikesString, PostLike[].class);
                        } else if (response.getJSONObject("response").optJSONObject("likes") != null) {
                            String postLikesString = response.getJSONObject("response").getJSONObject("likes").toString();
                            postLikes = new PostLike[] {
                                    customGson.fromJson(postLikesString, PostLike.class)
                            };
                        }

                        if (postLikes != null && postLikes.length > 0) {

                            if (LikesListPageActivity.this.postLikesRecyclerView.getRecyclerView().getAdapter() != null
                                    && LikesListPageActivity.this.postLikesRecyclerView.getRecyclerView().getAdapter() instanceof PostPageLikesListAdapter) {

                                ArrayList<PostLike> postLikesToInsert = new ArrayList<PostLike>();
                                ArrayList<PostLike> postLikesFromServer = new ArrayList<PostLike>(Arrays.asList(postLikes));

                                List<PostLike> allPostsLikes = ((PostPageLikesListAdapter) LikesListPageActivity.this.postLikesRecyclerView.getRecyclerView().getAdapter()).getAllObjects();

                                ArrayList<Long> allPostsLikesIds = new ArrayList<Long>();
                                for (PostLike postLike : allPostsLikes) {
                                    if (postLike != null) {
                                        allPostsLikesIds.add(postLike.getLikeId());
                                    }
                                }

                                for (PostLike postLikeFromServer : postLikesFromServer) {
                                    if (postLikeFromServer != null && !allPostsLikesIds.contains(postLikeFromServer.getLikeId())) {
                                        postLikesToInsert.add(postLikeFromServer);
                                    }
                                }

                                ((PostPageLikesListAdapter) LikesListPageActivity.this.postLikesRecyclerView.getRecyclerView().getAdapter()).addObjects(postLikesToInsert);
                                LikesListPageActivity.this.postLikesRecyclerView.getRecyclerView().getAdapter().notifyDataSetChanged();

                            }

                            LikesListPageActivity.this.postLikesRecyclerView.setHasMoreItems(true);

                        } else {

                            LikesListPageActivity.this.postLikesRecyclerView.setFinishLoading(true, "-End-");
                            LikesListPageActivity.this.postLikesRecyclerView.setHasMoreItems(false);

                        }


                    } else {

                        LikesListPageActivity.this.postLikesRecyclerView.setFinishLoading(true, "-End-");
                        LikesListPageActivity.this.postLikesRecyclerView.setHasMoreItems(false);

                    }

                    LikesListPageActivity.this.postLikesRecyclerView.setScreenState(RECYCLER_VIEW);

                } catch (JSONException e) {
                    e.printStackTrace();
                    this.onDefaultError("error in parsing response");
                }
            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    LikesListPageActivity.this.postLikesRecyclerView.setMessage(message, FAILURE_MESSAGE);
                } else {
                    LikesListPageActivity.this.postLikesRecyclerView.setMessage("Getting Posts List Error!", FAILURE_MESSAGE);
                }
            }

            @Override
            protected void postRequest() {

                LikesListPageActivity.this.postLikesRecyclerView.setLoadMore(false);
                LikesListPageActivity.this.postLikesRecyclerView.setLoadingItems(false);

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
        this.touchDownAnimation.setInterpolator(new OvershootInterpolator());

    }

    private void initTouchUpAnimation() {

        this.touchUpAnimation = new ScaleAnimation(0.80f, 1f, 0.80f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        this.touchUpAnimation.setDuration(300);
        this.touchUpAnimation.setInterpolator(new OvershootInterpolator());

    }

}
