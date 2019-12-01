package com.erikmelker.Server.DatabaseHandler;

import com.erikmelker.Server.DatabaseHandler.Models.User;
import com.erikmelker.Server.DatabaseHandler.Models.UserFile;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;

import static com.erikmelker.Common.SimpleJSONParser.JSONFile;
import static com.erikmelker.Common.SimpleJSONParser.PackageJSONFiles;
import static com.erikmelker.Server.DatabaseHandler.DatabaseHandler.getEntityManager;

public class FileTableHandler {

    public static void main( String[] args ) {
        //addUser("melker", "pass");
        //testUploadFile();
        //String username = getUsername(2);
        //System.out.println(username);
        //System.out.println(listAllFiles());
        //testDownloadFile(16);
    }

    private static void testDownloadFile(int i) {
        byte[] bytes = (byte[]) getFile(i).get("file");
        Path path = Paths.get("src/main/test.png");
        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        addFile( "Mapzi2", size, 1,bFile , true);
    }

    public static void addUser(String username, String password) {
        EntityManager em = getEntityManager();
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
        EntityManager em = getEntityManager();
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

    public static void addFile( String fname, int size, int owner, byte[] file, boolean shared) {
        EntityManager em = getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();

            UserFile userFile = new UserFile();
            userFile.setFname(fname);
            userFile.setFsize(size);
            userFile.setOwner(owner);
            userFile.setFile(file);
            userFile.setShared(shared);

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

    public static boolean deleteFile(int fid, int owner) {
        EntityManager em = getEntityManager();
        EntityTransaction et = null;
        UserFile file = null;
        boolean success = false;

        try {
            et = em.getTransaction();
            et.begin();

            file = em.find(UserFile.class, fid);
            if (file.getOwner() == owner){
                em.remove(file);
                success = true;
            }
            et.commit();
        } catch (Exception ex) {
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        } finally {
            em.close();
        }
        return success;
    }

    public static String listAllFiles() {
        EntityManager em = getEntityManager();

        String strQuery1 = "SELECT f FROM UserFile f WHERE f.id IS NOT NULL";
        TypedQuery<UserFile> tq1 = em.createQuery(strQuery1, UserFile.class);
        List<UserFile> files;
        String JSON = null;

        try {
            files = tq1.getResultList();
            StringBuilder sb = new StringBuilder();
            for (UserFile f : files) {
                String owner = getUsername(f.getOwner());
                if (owner != null) sb.append(JSONFile(f.getId(),f.getFname(), f.getFsize(), owner, f.getShared())).append("&");
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

    public static HashMap<String,Object> getFile(int fid) {
        EntityManager em = getEntityManager();
        String query = "SELECT f FROM UserFile f WHERE f.id = :fid";
        TypedQuery<UserFile> tq = em.createQuery(query, UserFile.class);
        tq.setParameter("fid", fid);
        UserFile file = null;
        HashMap<String, Object> map = new HashMap<>();
        try{
            file = tq.getSingleResult();
        }catch (NoResultException e){
            System.out.println(e);
            return null;
        }finally {
            em.close();
        }
        assert file != null;
        map.put("fname", file.getFname());
        map.put("file", file.getFile());
        return map;
    }
}
