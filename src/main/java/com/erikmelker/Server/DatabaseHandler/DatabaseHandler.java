package com.erikmelker.Server.DatabaseHandler;

import javax.persistence.*;

public class DatabaseHandler {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("FileShare");;

    public static EntityManager getEntityManager(){
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }
}
