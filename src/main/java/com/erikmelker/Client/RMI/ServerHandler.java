package com.erikmelker.Client.RMI;

import com.erikmelker.Common.FileService;
import com.erikmelker.Common.LoginService;
import com.erikmelker.Server.DatabaseHandler.UsernameTakenException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.erikmelker.Common.SimpleJSONParser.ReadListOfFiles;

public class ServerHandler {
    private static int userId = 2;
    private static ServerConnection connection;

    public ServerHandler(){
        connection = new ServerConnection();
    }

    public static void main(String []args){
        ServerHandler serverHandler = new ServerHandler();
        serverHandler.downloadFile(17, 2);
    }
    public static int login(String username, String password){
        LoginService loginService = connection.connectToLogin();
        try {
            // Returns int: (+) ID if correct, (-1) if wrong
            return loginService.login(username, password);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return -2;
    }
    public int registerUser(String username, String password) {
        LoginService loginService = connection.connectToLogin();
        try {
            // Returns int: (+) ID if success, (-1) if fail
            return loginService.registerUser(username, password);
        } catch (RemoteException | UsernameTakenException e) {
            e.printStackTrace();
        }
        return -2;
    }

    private static void deleteFile(int file_id, FileService service) {
        try {
            service.deleteFile(file_id, userId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void uploadFile(String filePath, boolean shared){
        FileService service = connection.connectToFileCatalog();
        byte[] bFile = null;
        int size = 0;
        try {
            File file = new File(filePath);
            String filename = file.getName();
            bFile = Files.readAllBytes(file.toPath());
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            size = (int) attr.size();
            service.uploadFile(filename, size, userId, bFile, shared);
            System.out.println(filename + " was successfully uploaded to the server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(int fid, int user){
        FileService service = connection.connectToFileCatalog();
        byte[] bytes = new byte[0];
        String filename = null;
        HashMap map = null;
        try {
            map = service.getFile(fid, user);
            filename = (String) map.get("fname");
            bytes = (byte[]) map.get("file");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Path path = Paths.get("src/main/java/com/erikmelker/Client/Downloads/"+filename);
        try {
            Files.write(path, bytes);
            System.out.println("Upload was downloaded: " + Files.exists(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList getListOfFilesJSON() {
        FileService fileService = connection.connectToFileCatalog();
        StringBuilder sb = new StringBuilder();
        ArrayList list = null;
        try {
            list =  ReadListOfFiles(fileService.listAllFiles());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean deleteFile(int fid, int userId) {
        FileService fileService = connection.connectToFileCatalog();
        boolean res = false;
        try {
            res = fileService.deleteFile(fid, userId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return res;
    }


    public String getEvents(int lastIndex) {
        FileService fileService = connection.connectToFileCatalog();
        try {
            return fileService.getEvents(lastIndex);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
