package com.bob.bobguestapp.tools;

import com.bob.bobguestapp.R;
import com.bob.uimodule.finals;

import java.util.HashMap;

public class myFinals extends finals {

    public static final HashMap<String, String> requestStatusStrings;
    static {
        requestStatusStrings = new HashMap<String, String>();
        requestStatusStrings.put("waiting", "Waiting");
        requestStatusStrings.put("in_progress", "In Progress");
        requestStatusStrings.put("done", "Done");
    }

    public static final HashMap<String, String> requestStatusCodes;
    static {
        requestStatusCodes = new HashMap<String, String>();
        requestStatusCodes.put("Waiting", "waiting");
        requestStatusCodes.put("In Progress", "in_progress");
        requestStatusCodes.put("Done", "done");
    }

    public static final HashMap<String, Integer> requestStatusColors;
    static {
        requestStatusColors = new HashMap<String, Integer>();
        requestStatusColors.put("waiting", R.color.waiting_status_color);
        requestStatusColors.put("in_progress", R.color.in_progress_status_color);
        requestStatusColors.put("done", R.color.done_status_color);
    }

}
