package com.yafieimam.nuhanaapp;

/**
 * Created by YAFIE IMAM A on 1/18/2018.
 */

public class AksaraJawa {

    //private variables
    int _id;
    String _nama_aksara_jawa;

    // Empty constructor
    public AksaraJawa(){
    }

    // constructor
    public AksaraJawa(int id, String nama_aksara_jawa){
        this._id = id;
        this._nama_aksara_jawa = nama_aksara_jawa;
    }

    // constructor
    public AksaraJawa(String nama_aksara_jawa){
        this._nama_aksara_jawa = nama_aksara_jawa;
    }

    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting aksara jawa
    public String getAksaraJawa(){
        return this._nama_aksara_jawa;
    }

    // setting aksara jawa
    public void setAksaraJawa(String nama_aksara_jawa){
        this._nama_aksara_jawa = nama_aksara_jawa;
    }
}
