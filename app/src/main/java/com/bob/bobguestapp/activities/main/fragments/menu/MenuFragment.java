package com.bob.bobguestapp.activities.main.fragments.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.intro.connector.IntroConnector;
import com.bob.bobguestapp.activities.intro.fragments.login.LoginFragment;
import com.bob.bobguestapp.activities.main.MainActivity;
import com.bob.bobguestapp.activities.main.connector.MainConnector;
import com.bob.bobguestapp.tools.database.MyDBUtilsManager;
import com.bob.bobguestapp.tools.database.MyRealmController;
import com.bob.bobguestapp.tools.http.serverbeans.guestrequest.CreateGuestRequestResponse;
import com.bob.bobguestapp.tools.myFinals;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.database.objects.FormItem;
import com.bob.toolsmodule.database.objects.FormItemProperty;
import com.bob.toolsmodule.database.objects.GuestRequest;
import com.bob.toolsmodule.database.objects.MenuNode;
import com.bob.toolsmodule.database.objects.MenuNodeProperty;
import com.bob.toolsmodule.database.objects.RequestItem;
import com.bob.toolsmodule.http.ServerRequest;
import com.bob.toolsmodule.http.requests.JsonServerRequest;
import com.bob.toolsmodule.http.serverbeans.ApplicativeResponse;
import com.bob.toolsmodule.http.serverbeans.Guest;
import com.bob.bobguestapp.tools.parsing.MyGsonParser;
import com.bob.toolsmodule.http.serverbeans.menu.RelationalMenuResponse;
import com.bob.uimodule.UIModuleManager;
import com.bob.uimodule.menu.DynamicMenuView;
import com.bob.uimodule.menu.adapters.MenuFormItemsAdapter;
import com.bob.uimodule.menu.adapters.MenuNodesAdapter;
import com.bob.uimodule.theme.ThemeUtilsManager;
import com.bob.uimodule.views.MyView;
import com.bob.uimodule.views.loadingcontainer.ManagementFragment;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmList;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.FAILURE_MESSAGE;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.LOADING;


public class MenuFragment extends ManagementFragment {

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";
    private static String BOB_SERVER_DESIGN_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_DESIGN_PORT;

    //menu design
    private static final String GET_ALL_MENU_URL = BOB_SERVER_DESIGN_SERVICES_URL +"/design/getRelationalTree/";

    //create wish url
    private static final String CREATE_REQUEST_URL = BOB_SERVER_MOBILE_SERVICES_URL + "/wishes/create";

    //main view screen states
    public static final int MENU = 10;

    //intro commands
    private MainConnector mainConnector;

    //menu
    private RelativeLayout menuLayout;
    private MyDynamicMenuView dynamicMenuView;

    public MenuFragment() {

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.MENU_FRAGMENT_SKIN);

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

        //get menu
        this.makeGetAllMenuRequest();

        return view;

    }


    @Override
    public View onCreateMainView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //init management skin
        this.managementViewContainer.setScreenSkin(this.screenSkin);

        //view
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        //dynamic menu view
        this.initMenuLayout(view);

        return view;

    }

    //screen state
    @Override
    protected void setMainViewScreenState(int screenState) {

        this.menuLayout.setVisibility(INVISIBLE);

        switch (screenState) {

            case MENU:
                this.menuLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //main commands
    public void setMainConnector(MainConnector mainConnector) {
        this.mainConnector = mainConnector;
    }

    //menu
    private void initMenuLayout(View view) {

        //menu main layout
        this.initMenuMainLayout(view);

        //dynamic menu view


    }

    private void initMenuMainLayout(View view) {

        this.menuLayout = (RelativeLayout) view.findViewById(R.id.menu_fragment_menu_layout);

        this.initDynamicMenuView();

    }

    private void initDynamicMenuView() {

        this.createDynamicMenuView();

        this.addDynamicMenuView();

    }

    private void createDynamicMenuView() {
        XmlPullParser menuRecyclerViewParser = getResources().getXml(com.bob.uimodule.R.xml.view_default_attribute);
        try {
            menuRecyclerViewParser.next();
            menuRecyclerViewParser.nextTag();
        } catch (Exception e) {
            e.printStackTrace();
        }
        AttributeSet menuRecyclerViewAttrs = Xml.asAttributeSet(menuRecyclerViewParser);
        this.dynamicMenuView = new MyDynamicMenuView(this.getContext(),menuRecyclerViewAttrs);
    }

    private void addDynamicMenuView() {
        RelativeLayout.LayoutParams dynamicMenuViewParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        dynamicMenuViewParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        this.dynamicMenuView.setPaddingRelative(
                (int) this.getContext().getResources().getDimension(R.dimen.menu_fragment_menu_start_padding),
                (int) this.getContext().getResources().getDimension(R.dimen.menu_fragment_menu_top_padding),
                (int) this.getContext().getResources().getDimension(R.dimen.menu_fragment_menu_end_padding),
                (int) this.getContext().getResources().getDimension(R.dimen.menu_fragment_menu_bottom_padding)
        );

        this.menuLayout.addView(this.dynamicMenuView, dynamicMenuViewParams);
    }

    //http requests
    private void makeCreateRequestRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {
                MenuFragment.this.managementViewContainer.setScreenState(LOADING);
            }

            @Override
            protected boolean requestCondition() {
                String guestEmail = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestEmail", null);
                if (MenuFragment.this.dynamicMenuView.getMenuViewState() == DynamicMenuView.FORM_STATE && guestEmail != null) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected JSONObject getJsonObject() {
                try {
                    String guestEmail = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestEmail", null);


                    //guest request
                    GuestRequest guestRequest = MenuFragment.this.dynamicMenuView.getFormRequest();
                    JsonElement jsonGuestRequest = MyGsonParser.getParser().create().toJsonTree(guestRequest, GuestRequest.class);

                    //guest
                    Guest guest = new Guest();
                    guest.setEmail(guestEmail);
                    JsonElement jsonGuest = MyGsonParser.getParser().create().toJsonTree(guest, Guest.class);

                    //request
                    JsonObject jsonRequest = new JsonObject();
                    jsonRequest.add("guest", jsonGuest);
                    jsonRequest.add("wish", jsonGuestRequest);

                    //json to send
                    JsonObject jsonCreateGuestRequest = new JsonObject();
                    jsonCreateGuestRequest.add("request", jsonRequest);

                    return new JSONObject( new Gson().toJson(jsonCreateGuestRequest));
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected String getRequestUrl() {
                return CREATE_REQUEST_URL;
            }

            @Override
            protected ApplicativeResponse getApplicativeResponse(JSONObject response) {
                try {
                    CreateGuestRequestResponse createGuestRequestResponse = new Gson().fromJson(response.getJSONObject("response").toString(), CreateGuestRequestResponse.class);
                    return createGuestRequestResponse.getStatusResponse();
                } catch (JSONException e) {
                    e.printStackTrace();
                    this.onDefaultError("error in parsing response");
                    return null;
                }
            }

            @Override
            protected void onSuccess(JSONObject response) {
                MenuFragment.this.managementViewContainer.setMessage("Success creating request!");
            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    MenuFragment.this.managementViewContainer.setMessage(message);
                } else {
                    MenuFragment.this.managementViewContainer.setMessage("Error creating request!");
                }
            }

        };

        jsonServerRequest.makeRequest();

    }

    //menu
    private void makeGetAllMenuRequest() {
        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {
                MenuFragment.this.managementViewContainer.setScreenState(LOADING);
            }

            @Override
            protected boolean requestCondition() {
                String email = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestEmail", null);
                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", "1");
                if ((email != null) && (hotelId != null)) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected String getRequestUrl() {
                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", "1");
                if (hotelId != null) {
                    return GET_ALL_MENU_URL + hotelId;
                } else {
                    return null;
                }
            }

            @Override
            protected ApplicativeResponse getApplicativeResponse(JSONObject response) {
                try {
                    Gson customGson = MyGsonParser.getParser().create();
                    String statusResponse = response.getJSONObject("statusResponse").toString();

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
                RelationalMenuResponse relationalMenuResponse = new Gson().fromJson(response.toString(), RelationalMenuResponse.class);
                MyDBUtilsManager.get().insertMenuToDB(relationalMenuResponse.getTree().getNodes(),
                        relationalMenuResponse.getTree().getNodesProperties(),
                        relationalMenuResponse.getTree().getBullets(),
                        relationalMenuResponse.getTree().getBulletsProperties());

                MenuNode rootMenuNode = MyRealmController.with(BOBGuestApplication.get()).getMenuNodeById(0L);
                if (rootMenuNode != null) {
                    MenuFragment.this.dynamicMenuView.setCurMenuNode(rootMenuNode);
                } else {
                    MenuNode menuNode = new MenuNode();
                    menuNode.setParentId(-2L);
                    menuNode.setId(-2L);
                    menuNode.setForm(false);
                    MenuFragment.this.dynamicMenuView.setCurMenuNode(menuNode);
                }

            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    MenuFragment.this.managementViewContainer.setMessage(message, FAILURE_MESSAGE);
                } else {
                    MenuFragment.this.managementViewContainer.setMessage("Getting Menu Error", FAILURE_MESSAGE);
                }
            }

            @Override
            protected void onException(Exception e) {
                e.printStackTrace();
                this.onDefaultError(e.toString());
            }

        };

        jsonServerRequest.makeRequest();
    }

    public void refreshMenu() {

        this.makeGetAllMenuRequest();

    }

    //back pressed handler
    public void onBackPressed() {

        MenuNode menuNode = this.dynamicMenuView.getCurMenuNode();
        if (menuNode != null && menuNode.getParentId() >= 0) {
            MenuNode parentMenuNode = MyRealmController.with(BOBGuestApplication.get()).getMenuNodeById(menuNode.getParentId());
            if (parentMenuNode != null) {
                this.dynamicMenuView.setCurMenuNode(parentMenuNode);
            }
        }

    }

    //my dynamic menu view
    public class MyDynamicMenuView extends DynamicMenuView<MenuNode, MenuNodeProperty, FormItem, FormItemProperty, GuestRequest, RequestItem> {

        //constructors
        public MyDynamicMenuView(Context context) {
            this(context, null);
        }

        public MyDynamicMenuView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public MyDynamicMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);

        }


        @Override
        protected int getTheme() {
            return UIModuleManager.get().getSharedPreferencesManager().getSecureSharedPreferences().getInt("appTheme", ThemeUtilsManager.DEFAULT_THEME);
        }

        @Override
        protected int getSkin() {
            return ThemeUtilsManager.get(this.getContext()).getSkin(this.getTheme(), MyAppThemeUtilsManager.MENU_FRAGMENT_MENU_SKIN);
        }

        @Override
        protected int getDialogSkin() {
            return ThemeUtilsManager.get(this.getContext()).getSkin(this.getTheme(), MyAppThemeUtilsManager.MENU_FRAGMENT_MENU_DIALOG_SKIN);
        }

        @Override
        protected void onStartLoading() {
            MenuFragment.this.managementViewContainer.setScreenState(LOADING);
        }

        @Override
        protected void onFinishLoading() {
            MenuFragment.this.managementViewContainer.setScreenState(MENU);
        }

        @Override
        protected void onError(String error) {
            MenuFragment.this.managementViewContainer.setMessage(error);
        }

        @Override
        protected void initMenuNodesAdapter() {
            this.menuNodesAdapter = new MenuNodesAdapter(this.getContext(), this.getTheme(), this.getSkin(), this.getDialogSkin()) {

                @Override
                protected HashMap<String, String> getMenuNodeProperties(int position) {

                    HashMap<String, String> menuItemProperties = new HashMap<String, String>();
                    if (this.menuItems != null && position < this.menuItems.size() && this.menuItems.get(position) != null) {
                        MenuNode menuNode = this.menuItems.get(position);
                        if (menuNode != null) {
                            List<MenuNodeProperty> menuNodePropertiesObjects = MyDynamicMenuView.this.getPropertiesOfMenuNode(menuNode);
                            menuItemProperties.put("id", String.valueOf(menuNode.getId()));
                            for (MenuNodeProperty property : menuNodePropertiesObjects) {
                                menuItemProperties.put(property.getKey(), property.getValue());
                            }
                        }
                    }

                    return menuItemProperties;

                }

                @Override
                public void onMenuNodeClick(int position) {

                    if (this.menuItems != null && position < this.menuItems.size() && this.menuItems.get(position) != null) {
                        MenuNode menuNode = this.menuItems.get(position);
                        if (menuNode != null) {
                            MyDynamicMenuView.this.setCurMenuNode(menuNode);
                        }
                    }

                }

            };
        }

        @Override
        protected void initFormItemsAdapter() {

            this.menuFormItemsAdapter = new MenuFormItemsAdapter(this.getContext(), this.getTheme(), this.getSkin(), this.getDialogSkin()) {
                @Override
                protected HashMap<String, String> getFormItemProperties(int position) {

                    HashMap<String, String> formItemProperties = new HashMap<String, String>();
                    if (this.formItems != null && position < this.formItems.size() && this.formItems.get(position) != null) {
                        FormItem formItem = this.formItems.get(position);
                        List<FormItemProperty> formItemPropertiesObjects = MyDynamicMenuView.this.getPropertiesOfFormItem(formItem);
                        formItemProperties.put("id", String.valueOf(formItem.getId()));
                        for (FormItemProperty property : formItemPropertiesObjects) {
                            formItemProperties.put(property.getKey(), property.getValue());
                        }
                    }

                    return formItemProperties;

                }
            };

        }

        @Override
        protected void initFormRequest() {

            this.formRequest = new GuestRequest();

        }

        @Override
        protected void initCurMenuItem() {

            this.isSubmittable = false;
            this.curMenuNode = null;

//            if (MenuFragment.this.getArguments() != null && MenuFragment.this.getArguments().get("curMenuNodeId") != null) {
//                long curMenuNodeId = MenuFragment.this.getArguments().getLong("curMenuNodeId", -1L);
//                if (curMenuNodeId != -1L) {
//                    this.curMenuNode = MyRealmController.with(BOBGuestApplication.get()).getMenuNodeById(curMenuNodeId);
//                }
//            }

            if (MyRealmController.with(BOBGuestApplication.get()).getMenuNodeById(0) != null) {
                this.curMenuNode = MyRealmController.with(BOBGuestApplication.get()).getMenuNodeById(0);
            }

        }

        @Override
        protected ArrayList<RequestItem> generateRequestItem(ArrayList<MyView> formItemsViews) {

            ArrayList<RequestItem> requestItems = new ArrayList<RequestItem>();

            if (formItemsViews != null) {

                for (MyView myView : formItemsViews) {

                    RequestItem requestItem = new RequestItem();

                    requestItem.setKey(myView.getKey());
                    requestItem.setValue(myView.getValue());
                    requestItem.setOrder(myView.getViewOrder());
                    requestItem.setType(myFinals.viewsCodes.get(myView.getViewCodeType()));

                    requestItems.add(requestItem);
                }

            }

            return requestItems;
        }

        @Override
        protected void fillFormRequest() {

            RealmList<RequestItem> requestItems = new RealmList<RequestItem>();
            requestItems.addAll(this.getRequestItems());
            this.formRequest.setRequestItems(requestItems);

        }

        @Override
        protected void setCurMenuItemInfo(MenuNode menuItem) {

            if (menuItem != null) {
                HashMap<String, String> properties = new HashMap<String, String>();
                List<MenuNodeProperty> menuNodeProperties = this.getPropertiesOfMenuNode(menuItem);
                properties.put("id", String.valueOf(menuItem.getId()));
                for (MenuNodeProperty property : menuNodeProperties) {
                    properties.put(property.getKey(), property.getValue());
                }
                String curProperty;
                if ((curProperty = properties.get("title")) != null) {
                    this.formRequest.setTitle(curProperty);
                }
                if ((curProperty = properties.get("img")) != null) {
                    this.formRequest.setIconUrl(curProperty);
                }
                this.formRequest.setStatus("waiting");

                if ((curProperty = properties.get("submittable")) != null) {
                    if (curProperty.equals("true")) {
                        this.isSubmittable = true;
                    } else if (curProperty.equals("false")) {
                        this.isSubmittable = false;
                    }

                }
            }

        }

        @Override
        protected List<FormItem> getFormItemsOfMenuNode(MenuNode menuItem) {

            if (menuItem != null) {
                return MyRealmController.with(BOBGuestApplication.get()).getFormItemsOfMenuNode(menuItem.getId());
            } else {
                return new ArrayList<FormItem>();
            }

        }

        @Override
        protected List<MenuNode> getSubMenuNodesOfMenuNode(MenuNode menuItem) {

            if (menuItem != null) {
                return MyRealmController.with(BOBGuestApplication.get()).getSubMenuNodes(menuItem.getId());
            } else {
                return new ArrayList<MenuNode>();
            }

        }

        @Override
        protected List<MenuNodeProperty> getPropertiesOfMenuNode(MenuNode menuItem) {

            if (menuItem != null) {
                return MyRealmController.with(BOBGuestApplication.get()).getPropertiesOfMenuNode(menuItem.getId());
            } else {
                return new ArrayList<MenuNodeProperty>();
            }

        }

        @Override
        protected List<FormItemProperty> getPropertiesOfFormItem(FormItem formItem) {

            if (formItem != null) {
                return MyRealmController.with(BOBGuestApplication.get()).getPropertiesOfFormItem(formItem.getId());
            } else {
                return new ArrayList<FormItemProperty>();
            }

        }

        @Override
        protected boolean checkIfForm(MenuNode menuItem) {
            return menuItem != null && menuItem.isForm();

        }

        @Override
        public void onSubmitButtonClick() {

            MenuFragment.this.makeCreateRequestRequest();

        }

        public MenuNode getCurMenuNode() {

            return this.curMenuNode;

        }

    }

}
