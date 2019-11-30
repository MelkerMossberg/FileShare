package com.erikmelker.Server.DatabaseHandler;

public class UsernameTakenException extends Exception{
    public UsernameTakenException(String username) {
        super("Username: " + username +" is already taken");
    }
}
