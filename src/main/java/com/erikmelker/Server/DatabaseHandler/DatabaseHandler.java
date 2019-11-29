package com.erikmelker.Server.DatabaseHandler;

import com.erikmelker.Server.DatabaseHandler.Models.User;
import com.erikmelker.Server.DatabaseHandler.Models.UserFile;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;

public class DatabaseHandler {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("FileShare");;

    public static void main( String[] args ) {
        //addUser("melker", "pass");
        //testUploadFile();
        String username = getUsername(2);
        System.out.println(username);

    }
    private static void testUploadFile()  {
        String filePath = "src/main/code.png";
        byte[] bFile = null;
        int size = 0;
        try {
            File file = new File(filePath);
            bFile = Files.readAllBytes(file.toPath());
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            size = (int) attr.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addFile( "Mapzi2", size, 1,bFile );
    }

    public static void addUser(String username, String password) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);

            em.persist(user);
            et.commit();
        } catch (Exception ex) {
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static String getUsername(int id){
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "SELECT u FROM User u WHERE u.id = :uid";
        TypedQuery<User> tq = em.createQuery(query, User.class);
        tq.setParameter("uid", id);
        User user = null;
        try{
            user = tq.getSingleResult();
        }catch (NoResultException e){
            System.out.println(e);
        }finally {
            em.close();
        }
        return user.getUsername();
    }

    public static void addFile( String fname, int size, int owner, byte[] file) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();

            UserFile userFile = new UserFile();
            userFile.setFname(fname);
            userFile.setFsize(size);
            userFile.setOwner(owner);
            userFile.setFile(file);

            em.persist(userFile);
            et.commit();
        } catch (Exception ex) {
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static void deleteFile(int fid) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        EntityTransaction et = null;
        UserFile file = null;

        try {
            et = em.getTransaction();
            et.begin();
            file = em.find(UserFile.class, fid);
            em.remove(file);
            et.commit();
        } catch (Exception ex) {
            // If there is an exception rollback changes
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        } finally {
            // Close EntityManager
            em.close();
        }
    }

    public static String listAllFiles() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        String strQuery = "SELECT f FROM UserFile f WHERE f.id IS NOT NULL";

        TypedQuery<UserFile> tq = em.createQuery(strQuery, UserFile.class);
        List<UserFile> files;

        try {
            files = tq.getResultList();
            files.forEach(file->{
                file.getFname();
            });
        }
        catch(NoResultException ex) {
            ex.printStackTrace();
        }
        finally {
            em.close();
        }
        return "hej";
    }
}
