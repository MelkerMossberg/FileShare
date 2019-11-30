package com.erikmelker.Server.DatabaseHandler;

import javax.persistence.*;

public class DatabaseHandler {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("FileShare");;

    public static void main( String[] args ) {
        //addUser("melker", "pass");
        //testUploadFile();
        //String username = getUsername(2);
        //System.out.println(username);
        //System.out.println(listAllFiles());
        //testDownloadFile(16);
    }

    public static EntityManager getEntityManager(){
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }
}
