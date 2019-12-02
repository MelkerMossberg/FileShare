package com.erikmelker.Server.DatabaseHandler.Models;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "eventlog")
public class Event implements Serializable {
    private static final long serialVersionUID = 3L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "act", nullable = false)
    private String act;

    @Column(name = "fid", nullable = false)
    private int fid;

    @Column(name = "by_user", nullable = false)
    private int byUser;

    @Column(name = "to_user", nullable = false)
    private int toUser;

    @Column(name = "time", nullable = false)
    private String time;

    public int getId(){
        return id;
    }
    public String getAct(){
        return act;
    }
    public int getFid(){
        return fid;
    }
    public int getByUser(){
        return byUser;
    }
    public int getToUser(){
        return toUser;
    }
    public String getTime(){
        return time;
    }

    public void setAct(String act) {
        this.act = act;
    }
    public void setFid(int fid) {
        this.fid = fid;
    }
    public void setByUser(int byUser) {
        this.byUser = byUser;
    }
    public void setToUser(int toUser) {
        this.toUser = toUser;
    }
    public void setTime() {
        java.util.Date javaDate = new java.util.Date();
        long javaTime = javaDate.getTime();
        java.sql.Timestamp timestamp = new java.sql.Timestamp(javaTime);
        this.time = timestamp.toString();
    }
}
