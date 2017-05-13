/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subd;

import java.util.Date;

/**
 *
 * @author Админ
 */
public class ServiceOrder {
    private int id;
    private int roomOrderId;
    private int serviceId;
    private Date OrderDate;
    
    private RoomOrder roomOrder;
    private Service service;

    public int getId() {
        return id;
    }

    public int getRoomOrderId() {
        return roomOrderId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public Date getOrderDate() {
        return OrderDate;
    }

    public RoomOrder getRoomOrder() {
        return roomOrder;
    }

    public Service getService() {
        return service;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRoomOrderId(int roomOrderId) {
        this.roomOrderId = roomOrderId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setOrderDate(Date OrderDate) {
        this.OrderDate = OrderDate;
    }

    public void setRoomOrder(RoomOrder roomOrder) {
        this.roomOrder = roomOrder;
    }

    public void setService(Service service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "";
    }
    
    
}
