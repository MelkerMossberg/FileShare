package com.erikmelker.Client;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class App {
    public static void main( String[] args ) throws RemoteException, NotBoundException, MalformedURLException {
        new Controller();
    }
}
