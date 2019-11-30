package com.erikmelker.Client;

import com.erikmelker.Client.RMI.ServerConnection;
import com.erikmelker.Common.FileService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static com.erikmelker.Common.SimpleJSONParser.ReadListOfFiles;

public class Controller {
    private static int userId = 2;

    public static void main( String[] args ) throws RemoteException, NotBoundException, MalformedURLException {
        ServerConnection connection = new ServerConnection();
        FileService service = connection.connectTo("upload");
        //uploadFile("src/main/code.png", service);
        //deleteFile(15, service);
        ReadListOfFiles(service.listAllFiles()).forEach(s->{
            System.out.println(s);
        });
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
            bFile = Files.readAllBytes(file.toPath());
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            size = (int) attr.size();
            service.uploadFile("ClientMap10", size, userId, bFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
