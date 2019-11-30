package com.erikmelker.Server.Controller.RMI;

import com.erikmelker.Common.LoginService;
import com.erikmelker.Server.DatabaseHandler.UserTableHandler;
import com.erikmelker.Server.DatabaseHandler.UsernameTakenException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class LoginServant extends UnicastRemoteObject implements LoginService {
    protected LoginServant() throws RemoteException {
        super();
    }

    @Override
    public void registerUser(String username, String password) throws RemoteException, UsernameTakenException {
        UserTableHandler.addUser(username,password);
    }

    @Override
    public boolean login(String username, String password) throws RemoteException {
        return UserTableHandler.checkPassword(username, password);
    }
}
