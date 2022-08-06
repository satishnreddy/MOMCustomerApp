package com.mom.momcustomerapp.networkservices;

/*
 * Created by Nishant on 16-06-2016.
 */
public class ParseUtils {


    public static boolean isValidString(String stringFromServer) {
        return stringFromServer != null && !stringFromServer.isEmpty() && !stringFromServer.equals("null");
    }
}
