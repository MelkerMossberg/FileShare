package com.erikmelker.Client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

class View {

    void printStart() {
        System.out.println("Welcome to FileShare\n[1] Login \t\t [2] Register ");
    }

    void askForUsername() {
        System.out.println("Enter username:");
    }

    void askForPassword() {
        System.out.println("Enter password:");
    }

    void printFailedLogin() {
        System.out.println("Wrong combination of username and password");
    }

    void printListOfFiles(ArrayList files) {
        System.out.println("***********************************************************************************************************");

        files.forEach(f -> {
            JSONParser parser = new JSONParser();
            try {
                JSONObject jsonObject = (JSONObject) parser.parse(f.toString());
                System.out.print("Id: " + jsonObject.get("fid") + "\t\t");
                System.out.print("Name: " + jsonObject.get("filename") + "\t\t\t");
                System.out.print("Size: " + jsonObject.get("size") + "\t\t\t");
                System.out.print("Owner: " + jsonObject.get("user") + "\t\t\t");
                System.out.println("Shared: " + jsonObject.get("shared") + "\t\t");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        System.out.println("***********************************************************************************************************");
        System.out.println("Options: [1] Download file,  [2] Upload file, [3] Delete file");

    }

    void askForNewUsername() {
        System.out.println("Please enter a username");
    }

    void askForNewPassword() {
        System.out.println("Please enter a password");
    }
}
