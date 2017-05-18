/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Entity.RoomOrder;
import Entity.ServiceOrder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Админ
 */
public class ServiceOrderController {
    private Connection connection;
    private Statement statement;
    private String username;
    private String password;
    private String connectionString;
    private RoomOrderController roomOrderController;
    private ServiceController serviceController;
    
    public ServiceOrderController(Connection connection) {
        this.connection = connection;
        roomOrderController = new RoomOrderController(connection);
        serviceController = new ServiceController(connection);
    }
    
    private ResultSet getTable(String tableName) throws SQLException{
        statement = connection.createStatement();
        return statement.executeQuery("SELECT * FROM " + tableName);
    }
    
    public List<ServiceOrder> getServiceOrders() throws SQLException{
        List<ServiceOrder> result = new ArrayList<>();
        ResultSet rs = getTable("serviceorder");
        while(rs.next()){
            ServiceOrder serviceOrder = new ServiceOrder();
            serviceOrder.setId(rs.getInt("serviceorderid"));
            serviceOrder.setRoomOrderId(rs.getInt("orderid"));
            serviceOrder.setServiceId(rs.getInt("serviceid"));
            serviceOrder.setOrderDate(rs.getDate("orderdate"));
            serviceOrder.setRoomOrder(roomOrderController.getRoomOrderById(rs.getInt("orderid")));
            serviceOrder.setService(serviceController.getServiceById(rs.getInt("serviceid")));
            result.add(serviceOrder);
        }
        return result;
    }
    
    public void addServiceOrder(int orderId, int serviceId, String orderdate) throws SQLException{
        double cost = serviceController.getServiceById(serviceId).getCost();
        roomOrderController.addToChek(orderId, cost);
        connection = DriverManager.getConnection(connectionString, username, password);
        connection.setAutoCommit(false);
        statement = connection.createStatement();
        try{
            Object[] data = {orderId, serviceId, orderdate};
            String sql = "INSERT INTO serviceorder VALUES (nextval('seq_serviceorder'), " + orderId + 
                    ", " + serviceId + ", '" + orderdate + "');";
            statement.execute(sql);
            connection.commit();
        }
        catch (SQLException ex){
            ex.printStackTrace();
            connection.rollback();
        }
    }
}
