package com.example.coba.model.Json;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonHelper {
    public static boolean put(JSONObject o, String k, Object v){
        try{
            o.put(k, v);
            return true;
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static Object get(final JSONObject o, String k) {
        try{
            return o.get(k);

        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public static JSONObject getJsonObject(final JSONObject o, String k) {
        try {
            return o.getJSONObject(k);

        }
        catch (JSONException ex){
            ex.printStackTrace();
        }
        return null;
    }
    public static boolean remove(final JSONObject o, String k){
        o.remove(k);
        return true;
    }
}
