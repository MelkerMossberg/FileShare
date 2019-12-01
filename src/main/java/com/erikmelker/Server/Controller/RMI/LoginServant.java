package com.erikmelker.Server.Controller.RMI;

import com.erikmelker.Common.LoginService;
import com.erikmelker.Server.DatabaseHandler.UserTableHandler;
import com.erikmelker.Server.DatabaseHandler.UsernameTakenException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class LoginServant extends UnicastRemoteObject implements LoginService {
    protected LoginServant() throws RemoteException {
        super();
    }

    @Override
    public int registerUser(String username, String password) throws RemoteException, UsernameTakenException {
        return UserTableHandler.addUser(username,password);
    }

    @Override
    public int login(String username, String password) throws RemoteException {
        int loginSuccess = UserTableHandler.checkPassword(username, password);
        return loginSuccess;
    }
}
