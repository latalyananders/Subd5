/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Entity.Employee;
import Entity.Service;
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
public class ServiceController {
     private Connection connection;
    private Statement statement;
    private String username;
    private String password;
    private String connectionString;
    
    public ServiceController(Connection connection) {
        this.connection = connection;
    }
    
    private ResultSet getTable(String tableName) throws SQLException{
        statement = connection.createStatement();
        return statement.executeQuery("SELECT * FROM " + tableName);
    }
    
    public List<Service> getServices() throws SQLException{
        List<Service> result = new ArrayList<>();
        ResultSet rs = getTable("service");
        while(rs.next()){
            Service service = new Service();
            service.setId(rs.getInt("serviceid"));
            service.setEmployeeId(rs.getInt("employeeid"));
            service.setName(rs.getString("name"));
            service.setCost(rs.getDouble("cost"));
            service.setEmployee(getEmployeeById(rs.getInt("employeeid")));
            result.add(service);
        }
        return result;
    }
    
    private Employee getEmployeeById(int id) throws SQLException{
        connection = DriverManager.getConnection(connectionString, username, password);
        statement = connection.createStatement();
        ResultSet rs =  statement.executeQuery("SELECT * FROM employee where employeeid = " + id);
        Employee employee = new Employee();
        rs.next();
        employee.setId(rs.getInt("employeeid"));
        employee.setSurname(rs.getString("surname"));
        employee.setName(rs.getString("name"));
        employee.setPatr(rs.getString("patr"));
        employee.setPosition(rs.getString("position"));
        employee.setMoney(rs.getDouble("money"));
        return employee;
    }
    
    public void addToService(Object[] data) throws SQLException{
        String sql = "INSERT INTO service VALUES (nextval('seq_service'), " + data[0] + ", '" + data[1] + "', " + data[2] + ");";
        System.out.println(sql);
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
    
    public void updateService(int id, Object[] newData) throws SQLException{
        String sql = "UPDATE service SET employeeid = " + newData[0] + 
                        ", name = '" + newData[1] + "'" +
                        ", cost = " + newData[2] + 
                        " WHERE serviceid = " + id;
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

    public Object[] getServicesIds() throws SQLException{
        List<Service> list = getServices();
        Object[] result = new Object[list.size()];
        for(int i = 0; i < list.size(); ++i){
            result[i] = list.get(i).getId();
        }
        return result;
    }
    
    public Service getServiceById(int id) throws SQLException{
        connection = DriverManager.getConnection(connectionString, username, password);
        statement = connection.createStatement();
        ResultSet rs =  statement.executeQuery("SELECT * FROM service where serviceid = " + id);
        Service service = new Service();
        if(rs.next()){
            service.setId(rs.getInt("serviceid"));
            service.setEmployeeId(rs.getInt("employeeid"));
            service.setName(rs.getString("name"));
            service.setCost(rs.getDouble("cost"));
            service.setEmployee(getEmployeeById(rs.getInt("employeeid")));
        }
        return service;
    }
    
    public void delete(int id) throws SQLException{
        String sql = "DELETE FROM service WHERE serviceid = " + id;
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
