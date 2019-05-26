package com.bob.bobguestapp.tools.parsing.gsonserializers.appnotification;

import com.bob.bobguestapp.tools.database.objects.GuestAppNotification;
import com.bob.toolsmodule.parsing.gsonserializers.BaseJsonSerializer;
import com.google.gson.JsonObject;

public class AppNotificationJsonSerializer extends BaseJsonSerializer<GuestAppNotification> {

    @Override
    public JsonObject createJsonObject(GuestAppNotification object) {
        JsonObject appNotificationJson = new JsonObject();

        appNotificationJson.addProperty("id", object.getId());
        appNotificationJson.addProperty("content", object.getContent());
        appNotificationJson.addProperty("value", object.getValue());
        appNotificationJson.addProperty("ts", object.getTimeStamp());
        appNotificationJson.addProperty("icon", object.getIconUrl());
        appNotificationJson.addProperty("type", object.getType());
        appNotificationJson.addProperty("isRead", object.isRead());
        appNotificationJson.addProperty("guestId", object.getGuestId());

        return appNotificationJson;
    }

    @Override
    public GuestAppNotification createObject(JsonObject jsonObject) {
        GuestAppNotification appNotification = new GuestAppNotification();

        if (jsonObject.get("id") != null) {
            appNotification.setId(jsonObject.get("id").getAsLong());
        }
        if (jsonObject.get("content") != null) {
            appNotification.setContent(jsonObject.get("content").getAsString());
        }
        if (jsonObject.get("value") != null) {
            appNotification.setValue(jsonObject.get("value").getAsString());
        }
        if (jsonObject.get("ts") != null) {
            appNotification.setTimeStamp(jsonObject.get("ts").getAsString());
        }
        if (jsonObject.get("icon") != null) {
            appNotification.setIconUrl(jsonObject.get("icon").getAsString());
        }
        if (jsonObject.get("type") != null) {
            appNotification.setType(jsonObject.get("type").getAsString());
        }
        if (jsonObject.get("isRead") != null) {
            appNotification.setRead(jsonObject.get("isRead").getAsBoolean());
        }
        if (jsonObject.get("guestId") != null) {
            appNotification.setGuestId(jsonObject.get("guestId").getAsLong());
        }

        return appNotification;
    }
}
