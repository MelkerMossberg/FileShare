package com.erikmelker.Common;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class SimpleJSONParser {

    public static void main(String []args){
    }

    public static String JSONFile(int fid, String filename, int size, String userName, boolean shared){
        JSONObject obj = new JSONObject();
        obj.put("fid", fid);
        obj.put("filename", filename);
        obj.put("size", size);
        obj.put("user", userName);
        obj.put("user", userName);
        obj.put("shared", shared);
        return obj.toJSONString();
    }
    public static String PackageJSONFiles(String allFilesSepByComma){
        String[] indFiles =  allFilesSepByComma.split("&");
        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();
        list.addAll(Arrays.asList(indFiles));
        obj.put("files",list);
        return obj.toJSONString();
    }

    public static ArrayList ReadListOfFiles(String JSON){
        JSONParser parser = new JSONParser();
        ArrayList files= new ArrayList();
        try {
            JSONObject container = (JSONObject) parser.parse(JSON);
            JSONArray msg = (JSONArray) container.get("files");
            Iterator iterator = msg.iterator();
            int count = 0;
            while (iterator.hasNext()) {
                String text =  iterator.next().toString();
                files.add(text);
            }
            return files;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return files;
    }

    public static String packageEvent(int id, String act, int fid, int byUser, int toOwner, String time){
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("act", act);
        obj.put("fid", fid);
        obj.put("byUser", byUser);
        obj.put("toOwner", toOwner);
        obj.put("time", time);
        return obj.toJSONString();
    }

    public static String PackageAllEvents(String input){
        String[] indEvents =  input.split("&");
        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();
        list.addAll(Arrays.asList(indEvents));
        obj.put("events",list);
        return obj.toJSONString();
    }

    public static ArrayList parseEventsJSON(String JSON){
        JSONParser parser = new JSONParser();
        ArrayList events= new ArrayList();
        try {
            JSONObject container = (JSONObject) parser.parse(JSON);
            JSONArray msg = (JSONArray) container.get("events");
            Iterator iterator = msg.iterator();
            while (iterator.hasNext()) {
                String text =  iterator.next().toString();
                events.add(text);
            }
            return events;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return events;
    }


}
