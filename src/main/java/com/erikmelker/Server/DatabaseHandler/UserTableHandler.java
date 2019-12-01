package com.erikmelker.Server.DatabaseHandler;

import com.erikmelker.Server.DatabaseHandler.Models.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static com.erikmelker.Server.DatabaseHandler.DatabaseHandler.getEntityManager;

public class UserTableHandler {
    public static void main( String[] args ) {
        //getAllUsers().forEach(s->System.out.println(s));
        System.out.println(getUserId("melker"));
    }

    private static ArrayList getAllUsers() {
        EntityManager em = getEntityManager();
        String strQuery1 = "SELECT u FROM User u WHERE u.id IS NOT NULL";
        TypedQuery<User> tq1 = em.createQuery(strQuery1, User.class);
        List<User> users;
        ArrayList<String> userArray = new ArrayList<>();
        try {
            users = tq1.getResultList();
            for (User u : users) { userArray.add(u.getUsername()); }
        }
        catch(NoResultException ex) {
            ex.printStackTrace();
        }
        finally {
            em.close();
        }
        return userArray;
    }

    private static int getUserId(String name) {
        EntityManager em = getEntityManager();
        String strQuery1 = "SELECT u FROM User u WHERE u.username = :name";
        TypedQuery<User> tq = em.createQuery(strQuery1, User.class);
        tq.setParameter("name", name);
        User user;
        try {
            user = tq.getSingleResult();
            int id = user.getId();
            return id;
        }
        catch(NoResultException ex) {
            ex.printStackTrace();
        }
        finally {
            em.close();
        }
        return -1;
    }

    public static int addUser(String username, String password) throws UsernameTakenException {
        if (getAllUsers().contains(username))
            throw new UsernameTakenException(username);

        EntityManager em = getEntityManager();
        EntityTransaction et = null;
        int userID = -1;
        try {
            et = em.getTransaction();
            et.begin();

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);

            em.persist(user);
            et.commit();

            userID = getUserId(username);

        } catch (Exception ex) {
            if (et != null) {
                et.rollback();
            }
            ex.printStackTrace();
        } finally {
            em.close();
        }
        return userID;
    }

    public static int checkPassword(String username, String password) {
        EntityManager em = getEntityManager();
        String strQuery1 = "SELECT u FROM User u WHERE u.id IS NOT NULL";
        TypedQuery<User> tq1 = em.createQuery(strQuery1, User.class);
        List<User> users;
        try {
            users = tq1.getResultList();
            for (User u : users) {
                if (u.getUsername().equals(username) && u.getPassword().equals(password)){
                    return u.getId();
                }
            }
        }
        catch(NoResultException ex) {
            ex.printStackTrace();
        }
        finally {
            em.close();
        }
        return -1;
    }
}
