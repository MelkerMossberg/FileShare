package com.erikmelker.Common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface FileService extends Remote {
    void uploadFile(String filename, int size, int owner, byte[] file, boolean shared) throws RemoteException;
    boolean deleteFile(int fid, int userId) throws RemoteException;
    String listAllFiles()throws RemoteException;
    HashMap getFile(int fid, int user)throws RemoteException;
}
