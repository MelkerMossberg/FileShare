package com.erikmelker.Common;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class SimpleJSONParser {

    public static void main(String []args){
        String json1 = JSONFile("file1", 898765, "melker");
        String json2 = JSONFile("file2", 432, "erik");
        String json3 = JSONFile("hejsan", 22222, "erik");
        String allFiles = PackageJSONFiles(json1+"&"+json2+"&"+json3);

        ReadListOfFiles(allFiles).forEach(s ->{
            System.out.println(s);
        });


    }

    public static String JSONFile(String filename, int size, String userName){
        JSONObject obj = new JSONObject();
        obj.put("filename", filename);
        obj.put("size", size);
        obj.put("user", userName);
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


}
