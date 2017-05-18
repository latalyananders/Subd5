/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.util.Date;

/**
 *
 * @author Админ
 */
public class RoomOrder {
    private int id;
    private int guestId;
    private int roomId;
    private int countDay;
    private Date orderDate;
    private double chek;
    
    private Guest guest;
    
    public void setId(int id){
        this.id = id;
    }
    
    public int getId(){
        return id;
    }
    
    public void setGuestId(int guestId){
        this.guestId = guestId;
    }
    
    public int getGuestId(){
        return guestId;
    }
    
    public void setRoomId(int roomId){
        this.roomId = roomId;
    }
    
    public int getRoomId(){
        return roomId;
    }
    
    public void setCountDay(int countDay){
        this.countDay = countDay;
    }
    
    public int getCountDay(){
        return countDay;
    }
    
    public void setOrderDate(Date orderDate){
        this.orderDate = orderDate;
    }
    
    public Date getOrderDate(){
        return orderDate;
    }
    
    public void setChek(double chek){
        this.chek = chek;
    }
    
    public double getChek(){
        return chek;
    }
    
    public void setGuest(Guest guest){
        this.guest = guest;
    }
    
    public Guest getGuest(){
        return guest;
    }

    @Override
    public String toString() {
        return guest + " " + orderDate;
    }
}
