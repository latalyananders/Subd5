/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import subd.ConnectionChanged;

/**
 *
 * @author Админ
 */
public class Controller {
    private String dataBaseName;
    private String port;
    private String username;
    private String password;
    private String connectionString;
    private boolean connected = false;

    public String getConnectionString() {
        return connectionString;
    }
    
    private Connection connection;
    private Statement statement;

    public Connection getConnection() {
        return connection;
    }
        
    private ArrayList<ConnectionChanged> listeners = new ArrayList<>();
    
    public void connect(String dataBaseName, String port, String username, 
            String password) throws ClassNotFoundException, SQLException{
        setDataBaseName(dataBaseName);
        setPort(port);
        setUsername(username);
        setPassword(password);
        createConnectionString();
        System.out.println(connectionString);
        
        //Подключение к бд
        try{
            connection = null;
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(connectionString, username, password);
            statement = connection.createStatement();
            setConnected(true);
        }
        catch (SQLException ex){
                    setConnected(false);
            }
        
        setConnected(true);
    }
    
    private void createConnectionString(){
        connectionString = "jdbc:postgresql://127.0.0.1:";
        connectionString += port + '/' + dataBaseName;
    }
    
    public void disconnect() throws SQLException{
        if (connection != null) {
                connection.close(); // Каскадное закрытие statement, resultSet
                setConnected(false);
            }
    }
    
    public void setDataBaseName(String dataBaseName){
        this.dataBaseName = dataBaseName;
    }
    
    public String getDataBaseName(){
        return dataBaseName;
    }
    
    public void setPort(String port){
        this.port = port;
    }
    
    public String getPort(){
        return port;
    }
    
    public void setUsername(String username){
        this.username = username;
    }
    
    public String getUsername(){
        return username;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    public String getPassword(){
        return password;
    }
    
    public boolean isConnected(){
        return connected;
    }
    
    private void setConnected(boolean connected){
        this.connected = connected;
        fireListeners(connected);
    }
    
    public void addListener(ConnectionChanged listener){
        listeners.add(listener);
    }
    
    public void removeListener(ConnectionChanged listener){
        listeners.remove(listener);
    }
    
    private void fireListeners(boolean connected){
        for(int i = 0; i <listeners.size(); ++i){
            listeners.get(i).onConnectionChanged(connected);
        }
    }
    
    private void sendMessage(String message){
        for(int i = 0; i <listeners.size(); ++i){
            listeners.get(i).onMessageSend(message);
        }
    }

}
