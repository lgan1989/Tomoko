package com.ganlu.tomoko;

/**
 * Created by lgan on 7/22/13.
 */
public class Status {
    private int id;
    private int rightCount;
    private int wrongCount;
    private int type;
    private String character;
    private String rome;
    private float ratio;

    public int getId(){
        return id;
    }
    public int getType(){return type;}
    public String getCharacter(){
        return character;
    }
    public String getRome() {return rome;}
    public int getRightCount(){return rightCount;}
    public int getWrongCount(){return wrongCount;}
    public float getRatio(){
        int r = rightCount;
        int w = wrongCount;
        float ra = 0;
        if (r + w > 0)ra = r * 1.0f/(r + w);
        return ra;
    }

    public void setType(int _type){type = _type;}
    public void setCharacter(String newCharacter){
        character = newCharacter;
    }
    public void setRome(String _rome){rome = _rome;}
    public void setRightCount(int _right){
        rightCount = _right;
    }
    public void setWrongCount(int _wrong){ wrongCount = _wrong;}
    public void setId(int newId){
        id = newId;
    }
    public void setRatio(float _ratio){ratio = _ratio;}
    public void addRight(){rightCount ++;}
    public void addWrong(){wrongCount ++;}

}
