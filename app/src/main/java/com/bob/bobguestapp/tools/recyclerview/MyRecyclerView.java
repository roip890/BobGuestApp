package com.bob.bobguestapp.tools.recyclerview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobguestapp.R;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.uimodule.views.loadingcontainer.ManagementLayout;

public class MyRecyclerView extends ManagementLayout {


    //screen states
    public static final int RECYCLER_VIEW = 10;

    private RelativeLayout recyclerViewLayout;
    private RecyclerView recyclerView;
    private LinearLayout recyclerViewLoadMoreLayout;
    private ProgressBar recyclerViewLoadMoreProgressBar;
    private ImageView recyclerViewLoadMoreProgressIcon;
    private TextView recyclerViewLoadMoreText;

    private boolean hasMoreItems;
    private boolean loadingItems;


    public MyRecyclerView(Context context) {
        this(context, null);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

    }

    @Override
    protected View onCreateMainView() {

        View view = this.initRecyclerViewLayout();

        this.setHasMoreItems(true);

        this.setLoadingItems(true);

        this.setScreenState(RECYCLER_VIEW);

        return view;

    }

    @Override
    protected void setMainViewScreenState(int screenState) {


        this.recyclerViewLayout.setVisibility(INVISIBLE);

        switch (screenState) {
            case RECYCLER_VIEW:
                this.recyclerViewLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //init views
    protected View initRecyclerViewLayout() {

        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View view = inflater.inflate(R.layout.view_my_recycler_view, new RelativeLayout(this.getContext()));
        this.recyclerViewLayout = (RelativeLayout) view.findViewById(R.id.view_my_recycler_view_recycler_view_layout);

        //recycler view
        this.initRecyclerView(view);

        //load more
        this.initRecyclerViewLoadMoreLayout(view);

        return view;

    }

    protected void initRecyclerView(View view) {

        this.recyclerView = (RecyclerView) view.findViewById(R.id.view_my_recycler_view_recycler_view);

    }

    protected void initRecyclerViewLoadMoreLayout(View view) {

        this.recyclerViewLoadMoreLayout = (LinearLayout) view.findViewById(R.id.view_my_recycler_view_load_more_layout);

        //progress bar
        this.initRecyclerViewLoadMoreProgressBar(view);

        //progress icon
        this.initRecyclerViewLoadMoreProgressIcon(view);

        //load more text
        this.initRecyclerViewLoadMoreText(view);

    }

    protected void initRecyclerViewLoadMoreProgressBar(View view) {

        this.recyclerViewLoadMoreProgressBar = (ProgressBar) view.findViewById(R.id.view_my_recycler_view_load_more_progress_bar);

    }

    protected void initRecyclerViewLoadMoreProgressIcon(View view) {

        this.recyclerViewLoadMoreProgressIcon = (ImageView) view.findViewById(R.id.view_my_recycler_view_load_more_progress_icon);

        this.recyclerViewLoadMoreProgressIcon.setVisibility(GONE);
    }

    protected void initRecyclerViewLoadMoreText(View view) {

        this.recyclerViewLoadMoreText = (TextView) view.findViewById(R.id.view_my_recycler_view_load_more_text);

    }

    //functions
    public void setRecyclerViewOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {

        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (MyRecyclerView.this.hasMoreItems && !MyRecyclerView.this.loadingItems) {

                    LinearLayoutManager layoutManager = (LinearLayoutManager) MyRecyclerView.this.recyclerView.getLayoutManager();

                    if (layoutManager.findLastCompletelyVisibleItemPosition()
                            >= layoutManager.getItemCount() - 2) {

                        onScrollListener.onScrolled(rv, dx, dy);

                    }

                }
            }
        });

    }

    public void setLoadMore(boolean loadMoreProgressEnable) {

        this.setLoadMore(loadMoreProgressEnable, null);

    }

    public void setLoadMore(boolean loadMoreProgressEnable, String loadMoreText) {

        if (loadMoreProgressEnable || loadMoreText != null) {

            this.recyclerViewLoadMoreLayout.setVisibility(VISIBLE);

            if (loadMoreText != null) {
                this.recyclerViewLoadMoreText.setVisibility(VISIBLE);
                this.recyclerViewLoadMoreText.setText(loadMoreText);
            } else {
                this.recyclerViewLoadMoreText.setVisibility(GONE);
            }

            if (loadMoreProgressEnable) {
                this.recyclerViewLoadMoreProgressBar.setVisibility(VISIBLE);
            } else {
                this.recyclerViewLoadMoreProgressBar.setVisibility(GONE);
            }

        } else {

            this.recyclerViewLoadMoreLayout.setVisibility(GONE);

        }

    }

    public void setFinishLoading(boolean finishLoading, String finishLoadingText) {

        if (finishLoading || finishLoadingText != null) {

            this.recyclerViewLoadMoreLayout.setVisibility(VISIBLE);

            if (finishLoadingText != null) {
                this.recyclerViewLoadMoreText.setVisibility(VISIBLE);
                this.recyclerViewLoadMoreText.setText(finishLoadingText);
            } else {
                this.recyclerViewLoadMoreText.setVisibility(GONE);
            }

        } else {

            this.recyclerViewLoadMoreLayout.setVisibility(GONE);

        }

    }

    //skins
    protected void updateViewSkin() {

        super.updateViewSkin();

        //load more progress
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            Drawable wrapDrawable = DrawableCompat.wrap(this.recyclerViewLoadMoreProgressBar.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable,
                    MyAppThemeUtilsManager.get(this.getContext()).getColor(
                            MyAppThemeUtilsManager.DEFAULT_CIRCULAR_PROGRESS_BAR_PRIMARY_COLOR,
                            this.screenSkin
                    )
            );
            this.recyclerViewLoadMoreProgressBar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            this.recyclerViewLoadMoreProgressBar.setIndeterminateTintList(ColorStateList.valueOf(
                    MyAppThemeUtilsManager.get(this.getContext()).getColor(
                            MyAppThemeUtilsManager.DEFAULT_CIRCULAR_PROGRESS_BAR_PRIMARY_COLOR,
                            this.screenSkin
                    )
            ));
        }


        this.recyclerViewLoadMoreText.setTextColor(MyAppThemeUtilsManager.get(this.getContext()).getColor(
                MyAppThemeUtilsManager.DEFAULT_BASE_TEXT_COLOR,
                this.screenSkin
        ));
    }

    //getters
    public RelativeLayout getRecyclerViewLayout() {
        return recyclerViewLayout;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public LinearLayout getRecyclerViewLoadMoreLayout() {
        return recyclerViewLoadMoreLayout;
    }

    public ProgressBar getRecyclerViewLoadMoreProgressBar() {
        return recyclerViewLoadMoreProgressBar;
    }

    public ImageView getRecyclerViewLoadMoreProgressIcon() {
        return recyclerViewLoadMoreProgressIcon;
    }

    public TextView getRecyclerViewLoadMoreText() {
        return recyclerViewLoadMoreText;
    }

    public boolean isHasMoreItems() {
        return hasMoreItems;
    }

    public void setHasMoreItems(boolean hasMoreItems) {
        this.hasMoreItems = hasMoreItems;
    }

    public boolean isLoadingItems() {
        return loadingItems;
    }

    public void setLoadingItems(boolean loadingItems) {
        this.loadingItems = loadingItems;
    }
}
