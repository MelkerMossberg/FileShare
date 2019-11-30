package com.erikmelker.Client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class View {

    public void printStart() {
        System.out.println("Welcome to FileShare\n[1] Login \t\t [2] Register ");
    }

    public void askForUsername() {
        System.out.println("Enter username:");
    }

    public void askForPassword() {
        System.out.println("Enter password:");
    }

    public void printFailedLogin() {
        System.out.println("Wrong combination of username and password");
    }

    public void printListOfFiles(ArrayList files) {
        System.out.println("********************************************************************************");

        files.forEach(f -> {
            JSONParser parser = new JSONParser();
            try {
                JSONObject jsonObject = (JSONObject) parser.parse(f.toString());
                System.out.print("Id: " + jsonObject.get("fid") + "\t\t");
                System.out.print("Name: " + jsonObject.get("filename") + "\t\t\t");
                System.out.print("Size: " + jsonObject.get("size") + "\t\t\t");
                System.out.println("Owner: " + jsonObject.get("user") + "\t\t");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        System.out.println("********************************************************************************");
        System.out.println("Options: [1] Download file,  [2] Upload file");

    }
}
