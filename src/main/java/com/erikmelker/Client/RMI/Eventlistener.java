package com.erikmelker.Client.RMI;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.erikmelker.Common.SimpleJSONParser.parseEventsJSON;

public class Eventlistener{

    private ServerHandler serverHandler = null;
    int userId = -1;
    int lastIndex = -1;
    Timestamp starTime = null;

    public Eventlistener(ServerHandler serverHandler, int userId){
        this.serverHandler = serverHandler;
        this.userId = userId;
        starTime = getTimeStamp();
    }

    private Timestamp getTimeStamp() {
        java.util.Date javaDate = new java.util.Date();
        long javaTime = javaDate.getTime();
        java.sql.Timestamp timestamp = new java.sql.Timestamp(javaTime);
        return makeTimeStamp(timestamp.toString());
    }

    public String lookForEvents() {
         String JSON = serverHandler.getEvents(lastIndex);
         ArrayList events = parseEventsJSON(JSON);
         events.forEach(e->{
             try {
                 JSONParser parser = new JSONParser();
                 JSONObject jsonObject = (JSONObject) parser.parse((String)e);
                 long owner = (Long) jsonObject.get("toOwner");
                 long user = (Long) jsonObject.get("byUser");
                 String act = (String) jsonObject.get("act");
                 long fid = (Long) jsonObject.get("fid");
                 String time = (String) jsonObject.get("time");
                 Timestamp timestamp = makeTimeStamp(time);
                 if (owner == userId && starTime.before(timestamp)){
                     System.out.println("Activity: " + act + " your file(fid): " + fid + "by user: " + user);
                 }
             } catch (ParseException ex) {
                 ex.printStackTrace();
             }

         });
         return null;
    }
    private Timestamp makeTimeStamp(String timeString){
        Timestamp timestamp = null;
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // you can change format of date
            Date date = formatter.parse(timeString);
            timestamp = new Timestamp(date.getTime());

            return timestamp;
        } catch (java.text.ParseException e) {
            System.out.println("Exception :" + e);
            return null;
        }
    }
}
