package com.gymhomie.supplements;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;

public class Utility {

    static String timestamptoStr(Timestamp timestamp)
    {
       return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
    }
}
