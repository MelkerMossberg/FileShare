package com.erikmelker.Client.RMI;

import com.erikmelker.Common.FileService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ServerConnection {
    FileService service;

    public FileService connectTo(String page) {
        try {
            service = (FileService) Naming.lookup("rmi://localhost:5099/" + page);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return service;
    }
}
