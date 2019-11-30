package com.erikmelker.Client.RMI;

import com.erikmelker.Common.FileService;
import com.erikmelker.Common.LoginService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ServerConnection {

    public FileService connectToFileCatalog() {
        FileService fileService = null;
        try {
            fileService = (FileService) Naming.lookup("rmi://localhost:5099/filecatalog");
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return fileService;
    }
    public LoginService connectToLogin() {
        LoginService loginService = null;
        try {
            loginService = (LoginService) Naming.lookup("rmi://localhost:5099/login");
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return loginService;
    }
}
