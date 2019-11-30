package com.erikmelker.Server.DatabaseHandler;

import com.erikmelker.Server.DatabaseHandler.Models.User;
import com.erikmelker.Server.DatabaseHandler.Models.UserFile;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import static com.erikmelker.Common.SimpleJSONParser.PackageJSONFiles;
import static com.erikmelker.Common.SimpleJSONParser.JSONFile;

public class DatabaseHandler {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("FileShare");;

    public static void main( String[] args ) {
        //addUser("melker", "pass");
        //testUploadFile();
        //String username = getUsername(2);
        //System.out.println(username);
        //System.out.println(listAllFiles());

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

    private static String getUsername(int id){
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        String query = "SELECT u FROM User u WHERE u.id = :uid";
        TypedQuery<User> tq = em.createQuery(query, User.class);
        tq.setParameter("uid", id);
        User user = null;
        try{
            user = tq.getSingleResult();
        }catch (NoResultException e){
            System.out.println(e);
            return null;
        }finally {
            em.close();
        }
        assert user != null;
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
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static String listAllFiles() {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        String strQuery1 = "SELECT f FROM UserFile f WHERE f.id IS NOT NULL";
        TypedQuery<UserFile> tq1 = em.createQuery(strQuery1, UserFile.class);
        List<UserFile> files;
        String JSON = null;

        try {
            files = tq1.getResultList();
            StringBuilder sb = new StringBuilder();
            for (UserFile f : files) {
                String owner = getUsername(f.getOwner());
                if (owner != null) sb.append(JSONFile(f.getFname(), f.getFsize(), owner)).append("&");
                else continue;
            }
            JSON = PackageJSONFiles(sb.toString());
        }
        catch(NoResultException ex) {
            ex.printStackTrace();
        }
        finally {
            em.close();
        }
        return JSON;
    }


}
