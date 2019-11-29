package com.erikmelker.Server.Controller.RMI;

import com.erikmelker.Server.DatabaseHandler.DatabaseHandler;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ApplicationServer {
    public static void main(String args[]) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(5099);
        registry.rebind("upload", new FileServant());
        System.out.println("Server should be up and running");
    }
}