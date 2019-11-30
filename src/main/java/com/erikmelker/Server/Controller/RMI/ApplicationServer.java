package com.erikmelker.Server.Controller.RMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ApplicationServer {
    public static void main(String args[]) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(5099);
        registry.rebind("login", new LoginServant());
        registry.rebind("filecatalog", new FileServant());
        System.out.println("Server should be up and running");
    }
}

