package com.erikmelker.Server.DatabaseHandler;

import com.erikmelker.Server.DatabaseHandler.Models.Event;
import com.erikmelker.Server.DatabaseHandler.Models.User;
import com.erikmelker.Server.DatabaseHandler.Models.UserFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.erikmelker.Common.SimpleJSONParser.*;
import static com.erikmelker.Server.DatabaseHandler.DatabaseHandler.getEntityManager;

public class FileTableHandler {

    public static void main( String[] args ) {
        //addUser("melker", "pass");
        //testUploadFile();
        //String username = getUsername(2);
        //System.out.println(username);
        //System.out.println(listAllFiles());
        //testDownloadFile(16);
        System.out.println(getEvents(-1));
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
            et.commit();;
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

    private static int getFid(String name){
        EntityManager em = getEntityManager();
        String query = "SELECT f FROM UserFile f WHERE f.fname = :name";
        TypedQuery<UserFile> tq = em.createQuery(query, UserFile.class);
        tq.setParameter("name", name);
        UserFile file = null;
        try{
            file = tq.getSingleResult();
        }catch (NoResultException e){
            System.out.println(e);
            return -1;
        }finally {
            em.close();
        }
        assert file != null;
        return file.getId();
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
            logEvent("upload", getFid(fname),owner,owner);
        } catch (Exception ex) {
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static boolean deleteFile(int fid, int user) {
        EntityManager em = getEntityManager();
        EntityTransaction et = null;
        UserFile file = null;
        boolean success = false;

        try {
            et = em.getTransaction();
            et.begin();

            file = em.find(UserFile.class, fid);
            if (file.getOwner() == user || file.getShared()){
                em.remove(file);
                success = true;
                logEvent("delete", fid,user,file.getOwner());
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

    public static HashMap<String,Object> getFile(int fid, int user) {
        EntityManager em = getEntityManager();
        String query = "SELECT f FROM UserFile f WHERE f.id = :fid";
        TypedQuery<UserFile> tq = em.createQuery(query, UserFile.class);
        tq.setParameter("fid", fid);
        UserFile file = null;
        HashMap<String, Object> map = new HashMap<>();
        try{
            file = tq.getSingleResult();
            logEvent("download", fid,user,file.getOwner());
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

    private static void logEvent(String act, int fid, int user, int owner) {
        EntityManager em = getEntityManager();
        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();

            Event event = new Event();
            event.setAct(act);
            event.setFid(fid);
            event.setByUser(user);
            event.setToUser(owner);
            event.setTime();

            em.persist(event);
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

    public static String getEvents(int lastIndex) {
        EntityManager em = getEntityManager();

        String strQuery1 = "SELECT e FROM Event e WHERE e.id > :pointer";
        TypedQuery<Event> tq1 = em.createQuery(strQuery1, Event.class);
        tq1.setParameter("pointer", lastIndex);
        List<Event> events;
        StringBuilder sb = new StringBuilder();
        String JSON = null;

        try {
            events = tq1.getResultList();
            for (Event e : events) {
                sb.append(
                        packageEvent(e.getId(), e.getAct(), e.getFid(), e.getByUser(), e.getToUser(), e.getTime()))
                        .append("&");
            }
            JSON = PackageAllEvents(sb.toString());
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
