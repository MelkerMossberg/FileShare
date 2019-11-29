package com.erikmelker.Common;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class SimpleJSONParser {

    public static void main(String []args){
        String json1 = PackageFile("file1", 898765, "melker");
        String json2 = PackageFile("file2", 432, "erik");
        String json3 = PackageFile("hejsan", 22222, "erik");
        String allFiles = PackageAllFileInfo(json1+"&"+json2+"&"+json3);

        String files = ReadListOfFiles(allFiles);
        System.out.println(files);

    }

    public static String PackageFile(String filename, int size, String userName){
        JSONObject obj = new JSONObject();
        obj.put("filename", filename);
        obj.put("size", size);
        obj.put("user", userName);
        return obj.toJSONString();
    }
    public static String PackageAllFileInfo(String allFilesSepByComma){
        String[] indFiles =  allFilesSepByComma.split("&");
        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();
        list.addAll(Arrays.asList(indFiles));
        obj.put("files",list);
        return obj.toJSONString();
    }

    public static String ReadListOfFiles(String JSON){
        JSONParser parser = new JSONParser();
        try {
            JSONObject container = (JSONObject) parser.parse(JSON);
            JSONArray msg = (JSONArray) container.get("files");
            Iterator iterator = msg.iterator();
            int count = 0;
            ArrayList files= new ArrayList();
            while (iterator.hasNext()) {
                String text =  iterator.next().toString();
                files.add(text);
            }
            return files.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "ReadListOfFiles did not work";
    }


}
