/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Entity.Room;
import Entity.RoomOrder;
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
public class RoomOrderController {
    private Connection connection;
    private Statement statement;
    private String username;
    private String password;
    private String connectionString;
    private GuestController guest;
    private RoomController roomController;
    
    public RoomOrderController(Connection connection) {
        this.connection = connection;
        guest = new GuestController(connection);
        roomController = new RoomController(connection);
    }
    
    private ResultSet getTable(String tableName) throws SQLException{
        statement = connection.createStatement();
        return statement.executeQuery("SELECT * FROM " + tableName);
    }
    
    public List<RoomOrder> getRoomOrders() throws SQLException{
        List<RoomOrder> result = new ArrayList<>();
        ResultSet rs = getTable("roomorder");
        while(rs.next()){
            RoomOrder roomOrder = new RoomOrder();
            roomOrder.setId(rs.getInt("orderid"));
            roomOrder.setGuestId(rs.getInt("guestId"));
            roomOrder.setRoomId(rs.getInt("roomid"));
            roomOrder.setCountDay(rs.getInt("countday"));
            roomOrder.setOrderDate(rs.getDate("orderdate"));
            roomOrder.setChek(rs.getDouble("chek"));
            roomOrder.setGuest(guest.getGuestById(rs.getInt("guestId")));
            result.add(roomOrder);
        }
        return result;
    }
    
    public Object[] getGuestIds() throws SQLException{
        List<RoomOrder> list = getRoomOrders();
        Object[] result = new Object[list.size()];
        for(int i = 0; i < list.size(); ++i){
            result[i] = list.get(i).getId();
        }
        return result;
    }
    
    public void addRoomOrder(int guestid, int roomId, int countDay, String orderDate) throws SQLException{
        Room room = roomController.getRoomById(roomId);
        if(room.getBooleanStatus()){
            System.out.println("Данный номер сейчас занят!!!");
            return;
        }
        connection = DriverManager.getConnection(connectionString, username, password);
        connection.setAutoCommit(false);
        statement = connection.createStatement();
        try{
            int id = room.getId();
            Object[] newRoomData = {true, room.getTip(), room.getCost()};
            roomController.updateRoom(id, newRoomData);
            double cost = room.getCost() * countDay;
            Object[] data = {guestid, roomId, countDay, orderDate, cost};
            String sql = "INSERT INTO roomorder VALUES (nextval('seq_roomorder'), " + guestid + ", " + roomId + ", " + countDay +
                    ", '" + orderDate + "', " + cost + ")";
            System.out.println(sql);
            statement.execute(sql);
            connection.commit();
            connection.commit();
        }
        catch (SQLException ex){
            connection.rollback();
        }
    }
    
    public Object[][] getNotEnabledRooms() throws SQLException{
        Object[][] result;
        connection = DriverManager.getConnection(connectionString, username, password);
        statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select guest.surname, roomorder.roomid\n" +
                                                "from guest, roomorder, room\n" +
                                                "where roomorder.guestid = guest.guestid and "
                                     + "room.roomid=roomorder.roomid and room.status = 'true';");
        int count = 0;
        List<String> names = new ArrayList<>();
        List<Integer> rooms = new ArrayList<>();
        while (rs.next()) {
            ++count;
            names.add(rs.getString("surname"));
            rooms.add(rs.getInt("roomId"));
        }
        result = new Object[2][names.size()];
        for(int i = 0; i < count; ++i){
            result[0][i] = names.get(i).toString();
            result[1][i] = rooms.get(i).toString();
        }
        return result;
    }
    
    public void setRoomFree(int roomId) throws SQLException{
        String sql = "UPDATE room SET status = 'false' WHERE roomid = " + roomId;
        connection = DriverManager.getConnection(connectionString, username, password);
        connection.setAutoCommit(false);
        statement = connection.createStatement();
        try{
            statement.executeUpdate(sql);
            connection.commit();
        }
        catch (SQLException ex){
            ex.printStackTrace();
            connection.rollback();
        }
    }
    
    
    public void addToChek(int roomOrderId, double cost) throws SQLException{
        RoomOrder roomOrder = getRoomOrderById(roomOrderId);
        connection = DriverManager.getConnection(connectionString, username, password);
        connection.setAutoCommit(false);
        try{
            statement.executeUpdate("UPDATE roomorder SET chek = " + (roomOrder.getChek() + cost) +
                    "WHERE orderid = " + roomOrderId + ";");
            connection.commit();
        }
        catch (SQLException ex){
            ex.printStackTrace();
            connection.rollback();
        }
    }
    
    public RoomOrder getRoomOrderById(int id) throws SQLException{
        connection = DriverManager.getConnection(connectionString, username, password);
        statement = connection.createStatement();
        ResultSet rs =  statement.executeQuery("SELECT * FROM roomorder where orderid = " + id);
        RoomOrder order = new  RoomOrder();
        rs.next();
        order.setId(rs.getInt("orderid"));
        order.setRoomId(rs.getInt("roomid"));
        order.setGuestId(rs.getInt("guestid"));
        order.setCountDay(rs.getInt("countday"));
        order.setOrderDate(rs.getDate("orderdate"));
        order.setChek(rs.getDouble("chek"));
        order.setGuest(guest.getGuestById(rs.getInt("guestid")));
        return order;
    }
    
    public Object[] getRoomOrderIds() throws SQLException{
        List<RoomOrder> list = getRoomOrders();
        Object[] result = new Object[list.size()];
        for(int i = 0; i < list.size(); ++i){
            result[i] = list.get(i).getId();
        }
        return result;
    }
}
