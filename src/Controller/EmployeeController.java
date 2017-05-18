/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Entity.Employee;
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
public class EmployeeController {
    private Connection connection;
    private Statement statement;
    private String username;
    private String password;
    private String connectionString;
    
    public EmployeeController(Connection connection) {
        this.connection = connection;
    }
    
    private ResultSet getTable(String tableName) throws SQLException{
        statement = connection.createStatement();
        return statement.executeQuery("SELECT * FROM " + tableName);
    }
    
    public List<Employee> getEmployees() throws SQLException{
        List<Employee> result = new ArrayList<>();
        ResultSet rs = getTable("employee");
        while(rs.next()){
            Employee employee = new Employee();
            employee.setId(rs.getInt("employeeid"));
            employee.setSurname(rs.getString("surname"));
            employee.setName(rs.getString("name"));
            employee.setPatr(rs.getString("patr"));
            employee.setPosition(rs.getString("position"));
            employee.setMoney(rs.getDouble("money"));
            result.add(employee);
        }
        return result;
    }
    
    public void addToEmployee(Object[] data) throws SQLException{
        String sql = "INSERT INTO employee VALUES (nextval('seq_employee'), '" + data[0] + "', '" + data[1] + "', '" + data[2] + "', '" + data[3] + "', '" + data[4] + 
                        "');";
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
    
    public void updateEmployee(int id, Object[] newData) throws SQLException{
        String sql = "UPDATE employee SET surname = '" + newData[0] +
                        "', name = '" + newData[1] +
                        "', patr = '" + newData[2] +
                        "', position = '" + newData[3] +
                        "', money = " + newData[4] +
                        " WHERE employeeid = " + id;
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
    
    public Object[] getEmlployeeIds() throws SQLException{
        List<Employee> list = getEmployees();
        Object[] result = new Object[list.size()];
        for(int i = 0; i < list.size(); ++i){
            result[i] = list.get(i).getId();
        }
        return result;
    }
    
    public void delete(int id) throws SQLException{
        String sql = "DELETE FROM employee WHERE employeeid = " + id;
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
