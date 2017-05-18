/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Entity.Guest;
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
public class GuestController {
    private Connection connection;
    private Statement statement;
    private String username;
    private String password;
    private String connectionString;
    
    
    public GuestController(Connection connection) {
        this.connection = connection;
    }
    
    private ResultSet getTable(String tableName) throws SQLException{
        statement = connection.createStatement();
        return statement.executeQuery("SELECT * FROM " + tableName);
    }
    
    public List<Guest> getList() throws SQLException{
        List<Guest> result = new ArrayList<>();
        ResultSet rs = getTable("guest");
        while (rs.next()) {
                Guest guest = new Guest();
                  guest.setId(rs.getInt("guestid"));
                  guest.setSurname(rs.getString("surname"));
                  guest.setName(rs.getString("name"));
                  guest.setPatr(rs.getString("patr"));
                  System.out.println(guest.toString());
                  result.add(guest);
        }
        return result;
    }
    
    public void add(Object[] data) throws SQLException{
        String sql = "INSERT INTO guest VALUES (nextval('seq_guest'), '" + data[0] + "', '" + data[1] + "', '" + data[2] + "');";
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
    
    public void update(int id, Object[] newData) throws SQLException{
        String sql = "UPDATE guest SET surname = '" + newData[0] +
                        "', name = '" + newData[1] +
                        "', patr = '" + newData[2] +
                        "' WHERE guestid = " + id;
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
    
    public void delete(int id) throws SQLException{
        String sql = "DELETE FROM guest WHERE guestid = " + id;
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
    
    public Object[] getGuestIds() throws SQLException{
        List<Guest> list = getList();
        Object[] result = new Object[list.size()];
        for(int i = 0; i < list.size(); ++i){
            result[i] = list.get(i).getId();
        }
        return result;
    }
    
    public Guest getGuestById(int id) throws SQLException{
        connection = DriverManager.getConnection(connectionString, username, password);
        statement = connection.createStatement();
        ResultSet rs =  statement.executeQuery("SELECT * FROM guest where guestid = " + id);
        Guest guest = new  Guest();
        rs.next();
        guest.setId(rs.getInt("guestid"));
        guest.setSurname(rs.getString("surname"));
        guest.setName(rs.getString("name"));
        guest.setPatr(rs.getString("patr"));
        return guest;
    }
}
