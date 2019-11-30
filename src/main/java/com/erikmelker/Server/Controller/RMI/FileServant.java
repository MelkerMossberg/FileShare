package com.erikmelker.Server.Controller.RMI;

import com.erikmelker.Common.FileService;
import com.erikmelker.Server.DatabaseHandler.FileTableHandler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class FileServant extends UnicastRemoteObject implements FileService {

    protected FileServant() throws RemoteException {
        super();
    }

    @Override
    public void uploadFile(String filename, int size, int owner, byte[] file) throws RemoteException {
        System.out.println("FileServant!");
        FileTableHandler.addFile(filename, size, owner, file);
    }

    @Override
    public void deleteFile(int fid, int userId) throws RemoteException {
        //Todo: (1) Get file info.
        //Todo: (2) Does this  user have access?
        //Todo: (2) Delete
        FileTableHandler.deleteFile(fid);
        System.out.println("DeleteFile Success");
    }

    @Override
    public String listAllFiles() throws RemoteException {
        return FileTableHandler.listAllFiles();
    }

    @Override
    public HashMap<String, Object> getFile(int fid) throws RemoteException {
        return FileTableHandler.getFile(fid);
    }


}
