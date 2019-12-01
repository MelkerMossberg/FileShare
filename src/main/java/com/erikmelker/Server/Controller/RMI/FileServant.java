package com.erikmelker.Server.Controller.RMI;

import com.erikmelker.Common.FileService;
import com.erikmelker.Server.DatabaseHandler.FileTableHandler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class FileServant extends UnicastRemoteObject implements FileService {

    FileServant() throws RemoteException {
        super();
    }

    @Override
    public void uploadFile(String filename, int size, int owner, byte[] file, boolean shared) throws RemoteException {
        System.out.println("FileServant!");
        FileTableHandler.addFile(filename, size, owner, file, shared);
    }

    @Override
    public boolean deleteFile(int fid, int userId) throws RemoteException {
        return FileTableHandler.deleteFile(fid, userId);
    }

    @Override
    public String listAllFiles() throws RemoteException {
        return FileTableHandler.listAllFiles();
    }

    @Override
    public HashMap<String, Object> getFile(int fid, int user) throws RemoteException {
        return FileTableHandler.getFile(fid, user);
    }


}
