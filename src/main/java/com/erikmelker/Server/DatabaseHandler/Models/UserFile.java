package com.erikmelker.Server.DatabaseHandler.Models;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "file")
public class UserFile implements Serializable{
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fid", updatable = false, unique = true)
    private int id;

    @Column(name = "fname", nullable = false)
    private String fname;

    @Column(name = "file", nullable = false)
    private byte[] file;

    @Column(name = "fsize", nullable = false)
    private int fsize;

    @Column(name = "owner", nullable = false)
    private int owner;

    public int getId() {
        return id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public int getFsize() {
        return fsize;
    }

    public void setFsize(int size) {
        this.fsize = size;
    }
    public int getOwner(){
        return owner;
    }
    public void setOwner(int owner) {
        this.owner = owner;
    }
}

