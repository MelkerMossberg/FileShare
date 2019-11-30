package com.erikmelker.Client.RMI;

import com.erikmelker.Common.FileService;
import com.erikmelker.Common.LoginService;

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
        serverHandler.downloadFile(17);
    }
    public static boolean login(String username, String password){
        LoginService loginService = connection.connectToLogin();
        try {
            // Returns boolean: Am I logged in now?
            return loginService.login(username, password);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void deleteFile(int file_id, FileService service) {
        try {
            service.deleteFile(file_id, userId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void uploadFile(String filePath){
        FileService service = connection.connectToFileCatalog();
        byte[] bFile = null;
        int size = 0;
        try {
            File file = new File(filePath);
            String filename = file.getName();
            bFile = Files.readAllBytes(file.toPath());
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            size = (int) attr.size();
            service.uploadFile(filename, size, userId, bFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(int fid){
        FileService service = connection.connectToFileCatalog();
        byte[] bytes = new byte[0];
        String filename = null;
        HashMap map = null;
        try {
            map = service.getFile(fid);
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
}
