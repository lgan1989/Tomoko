package com.ganlu.tomoko;

/**
 * Created by lgan on 7/22/13.
 */
public class Status {
    private long id;
    private long rightCount;
    private long wrongCount;
    private String character;
    private String rome;
    private float ratio;

    public long getId(){
        return id;
    }
    public String getCharacter(){
        return character;
    }
    public String getRome() {return rome;}
    public long getRightCount(){return rightCount;}
    public long getWrongCount(){return wrongCount;}
    public float getRatio(){return ratio;}

    public void setCharacter(String newCharacter){
        character = newCharacter;
    }
    public void setRome(String _rome){rome = _rome;}
    public void setRightCount(long _right){
        rightCount = _right;
    }
    public void setWrongCount(long _wrong){ wrongCount = _wrong;}
    public void setId(long newId){
        id = newId;
    }
    public void setRatio(float _ratio){ratio = _ratio;}
    public void addRight(){rightCount ++;}
    public void addWrong(){wrongCount ++;}

}
