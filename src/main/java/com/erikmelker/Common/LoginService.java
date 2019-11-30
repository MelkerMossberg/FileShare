package com.erikmelker.Common;

import com.erikmelker.Server.DatabaseHandler.UsernameTakenException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public interface LoginService extends Remote {
    void registerUser(String username, String password) throws RemoteException, UsernameTakenException;
    boolean login(String username, String password) throws RemoteException;
}
