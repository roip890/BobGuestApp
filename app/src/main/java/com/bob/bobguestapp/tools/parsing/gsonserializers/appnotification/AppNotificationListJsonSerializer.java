package com.bob.bobguestapp.tools.parsing.gsonserializers.appnotification;

import com.bob.bobguestapp.tools.database.objects.GuestAppNotification;
import com.bob.toolsmodule.parsing.gsonserializers.BaseListJsonSerializer;

public class AppNotificationListJsonSerializer extends BaseListJsonSerializer<GuestAppNotification> {

    @Override
    protected void initObjectSerializer() {
        this.objectSerializer = new AppNotificationJsonSerializer();
    }

    @Override
    protected GuestAppNotification[] getArrayOfType(int arraySize) {
        return new GuestAppNotification[arraySize];
    }

}
