package com.erikmelker.Common;

import com.erikmelker.Server.DatabaseHandler.UsernameTakenException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LoginService extends Remote {
    int registerUser(String username, String password) throws RemoteException, UsernameTakenException;
    int login(String username, String password) throws RemoteException;
}
