package com.erikmelker.Common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileService extends Remote {
    void uploadFile(String filename, int size, int owner, byte[] file) throws RemoteException;
    void deleteFile(int fid, int userId) throws RemoteException;
    String listAllFiles()throws RemoteException;
}
