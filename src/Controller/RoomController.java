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
public class RoomController {
    private Connection connection;
    private Statement statement;
    private String username;
    private String password;
    private String connectionString;
    
    public RoomController(Connection connection){
        this.connection = connection;
    }
    
    public RoomController(){}
    
    private ResultSet getTable(String tableName) throws SQLException{
        statement = connection.createStatement();
        return statement.executeQuery("SELECT * FROM " + tableName);
    }
    
    public List<Room> getRooms() throws SQLException{
        List<Room> result = new ArrayList<>();
        ResultSet rs = getTable("room");
        while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt("roomid"));
                room.setStatus(rs.getBoolean("status"));
                room.setTip(rs.getString("tip"));
                room.setCost(rs.getDouble("cost"));
                System.out.println(room.toString());
                result.add(room);
        }
        return result;
    }
    
    public void addToRoom(Object[] data) throws SQLException{
        String sql = "INSERT INTO room VALUES (" + data[0] + ", " + data[1] + ", '" + data[2] + "', " + data[3]+ ");";
        connection = DriverManager.getConnection(connectionString, username, password);
        connection.setAutoCommit(false);
        statement = connection.createStatement();
        System.out.println(sql);
        try{
            statement.execute(sql);
            connection.commit();
        }
        catch (SQLException ex){
            ex.printStackTrace();
            connection.rollback();
        }
    }
    
    public void updateRoom(int id, Object[] newData) throws SQLException{
        String sql = "UPDATE room SET status = '" + newData[0] + "'" +
                        ", tip = '" + newData[1] + "'" +
                        ", cost = " + newData[2] +
                        " WHERE roomid=" + id;
        System.out.println(sql);
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
    
    public Object[] getRoomIds() throws SQLException{
        List<Room> list = getRooms();
        Object[] result = new Object[list.size()];
        for(int i = 0; i < list.size(); ++i){
            result[i] = list.get(i).getId();
        }
        return result;
    }
    
    public Room getRoomById(int id) throws SQLException{
        connection = DriverManager.getConnection(connectionString, username, password);
        statement = connection.createStatement();
        ResultSet rs =  statement.executeQuery("SELECT * FROM room where roomid = " + id);
        Room room = new Room();
        rs.next();
        room.setId(rs.getInt("roomid"));
        room.setStatus(rs.getBoolean("status"));
        room.setTip(rs.getString("tip"));
        room.setCost(rs.getDouble("cost"));
        return room;
    }
    
    public void delete(int id) throws SQLException{
        String sql = "DELETE FROM room WHERE roomid = " + id;
        System.out.println(sql);
        connection = DriverManager.getConnection(connectionString, username, password);
        connection.setAutoCommit(false);
        statement = connection.createStatement();
        try{
            statement.executeUpdate(sql);
            connection.commit();
        }
        catch (SQLException ex){
            connection.rollback();
            System.out.println("Удаление данной строки приводит к нарушению целостности базы данных");
        }
    }
}
