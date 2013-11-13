package com.ganlu.tomoko;

/**
 * Created by lgan on 7/22/13.
 */
public class Status {
    private long id;
    private float status;
    private String character;

    public long getId(){
        return id;
    }
    public String getCharacter(){
        return character;
    }
    public float status(){
        return status;
    }
    public void setCharacter(String newCharacter){
        character = newCharacter;
    }
    public void setStatus(float newStatus){
        status = newStatus;
    }
    public void setId(long newId){
        id = newId;
    }

}
