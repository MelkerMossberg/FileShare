package com.erikmelker.Client;

import com.erikmelker.Client.RMI.ServerConnection;
import com.erikmelker.Common.LoginService;
import com.erikmelker.Common.FileService;
import com.erikmelker.Server.DatabaseHandler.UsernameTakenException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;

public class Controller {
    private static int userId = 2;

    public static void main( String[] args ) throws RemoteException, NotBoundException, MalformedURLException {
        ServerConnection connection = new ServerConnection();
        //uploadFile("src/main/java/com/erikmelker/Client/FilesToUpload/demoimage.png", service);
        //deleteFile(15, service);
        //ReadListOfFiles(service.listAllFiles()).forEach(s->{ System.out.println(s); });
        //downloadFile(17, service);
        LoginService loginservice = connection.connectToLogin();
        System.out.println(loginservice.login("blä", "pass"));
        //FileService fileservice = connection.connectTo("filecatalog");
    }

    private static void deleteFile(int file_id, FileService service) {
        try {
            service.deleteFile(file_id, userId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void uploadFile(String filePath, FileService service){
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

    public static void downloadFile(int fid, FileService service){
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
