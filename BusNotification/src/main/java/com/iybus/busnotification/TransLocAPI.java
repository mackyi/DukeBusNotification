package com.iybus.busnotification;

/**
 * Created by intern on 6/11/13.
 */
public class TransLocAPI {
    public String getAgencies(String parameters, MainActivity context){
        String uri = "agencies.json" + parameters;
        new RestTask(context).execute(uri);
        return "";
    }

    public String getRoutes(String parameters, MainActivity context){
        String uri = "routes.json" + parameters;
        new RestTask(context).execute(uri);
        return "";
    }


}
