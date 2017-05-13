/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subd;

/**
 *
 * @author Админ
 */
public class Room {
    private int id;
    private boolean status;
    private String tip;
    private double cost;
    private String stringStatus;
    
    public Room(int id, boolean status, String tip, double cost){
        this.id = id;
        this.status = status;
        this.tip = tip;
        this.cost = cost;
    }
    
    public Room() { }
    
    public void setId(int id){
        this.id = id;
    }
    
    public int getId(){
        return id;
    }
    
    public void setStatus(boolean status){
        this.status = status;
        if(this.status)
            stringStatus = "Занят";
        else
            stringStatus = "Свободен";
    }
    
    public String getStatus(){
        return stringStatus;
    }
    
    public boolean getBooleanStatus(){
        return status;
    }
    
    public void setTip(String tip){
        this.tip = tip;
    }
    
    public String getTip(){
        return tip;
    }
    
    public void setCost(double cost){
        this.cost = cost;
    }
    
    public double getCost(){
        return cost;
    }
    
    @Override
    public String toString(){
        return id + "";
    }
}
