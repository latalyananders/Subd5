/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    private Connection connection;
    private Statement statement;
    
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
            setConnection(true);
        }
        finally{
            if (connection != null) {
                    connection.close();
                    setConnection(false);
            }
        }
        
        setConnection(true);
    }
    
    private void createConnectionString(){
        connectionString = "jdbc:postgresql://127.0.0.1:";
        connectionString += port + '/' + dataBaseName;
    }
    
    public void disconnect() throws SQLException{
        if (connection != null) {
                connection.close(); // Каскадное закрытие statement, resultSet
                setConnection(false);
            }
    }
    
    private ResultSet getTable(String tableName) throws SQLException{
        connection = DriverManager.getConnection(connectionString, username, password);
        statement = connection.createStatement();
        return statement.executeQuery("SELECT * FROM " + tableName);
    }
    
    //Получение справочников
    public List<Guest> getGuests() throws SQLException{
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
            roomOrder.setGuest(getGuestById(rs.getInt("guestId")));
            result.add(roomOrder);
        }
        return result;
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
    
    public List<ServiceOrder> getServiceOrders() throws SQLException{
        List<ServiceOrder> result = new ArrayList<>();
        ResultSet rs = getTable("serviceorder");
        while(rs.next()){
            ServiceOrder serviceOrder = new ServiceOrder();
            serviceOrder.setId(rs.getInt("serviceorderid"));
            serviceOrder.setRoomOrderId(rs.getInt("orderid"));
            serviceOrder.setServiceId(rs.getInt("serviceid"));
            serviceOrder.setOrderDate(rs.getDate("orderdate"));
            serviceOrder.setRoomOrder(getRoomOrderById(rs.getInt("orderid")));
            serviceOrder.setService(getServiceById(rs.getInt("serviceid")));
            result.add(serviceOrder);
        }
        return result;
    }
    //Получение справочников
    
    public void addToDataBase(String tableName, Object[] data) throws SQLException{
        String sql = "INSERT INTO " + tableName + " VALUES (";
        switch(tableName){
            case "guest":
                sql+= data[0] +", '" + data[1] + "', '" + data[2] + "', '" + data[3] + "'";
                break;
            case "room":
                sql += data[0] + ", '" + data[1] + "', '" + data[2] + "', " + data[3];
                break;
            case "roomorder":
                sql+= data[0] + ", " + data[1] + ", " + data[2] + ", " + data[3] + ", '" + data[4] + "', " + data[5];
                break;
            case "employee":
                sql += data[0] + ", '" + data[1] + "', '" + data[2] + "', '" + data[3] + "', '" + data[4] + 
                        "', " + data[5];
                break;
            case "service":
                sql += data[0] + ", " + data[1] + ", '" + data[2] + "', " + data[3];
                break;
            case "serviceorder":
                sql+= data[0] + ", " + data[1] + ", " + data[2] + ", '" + data[3] + "'";
                break;
        }
        sql += ");";
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
    
    public Object[] getEmployeeList() throws SQLException{
        List<Employee> list = getEmployees();
        Object[] result = new Object[list.size()];
        for(int i = 0; i < list.size(); ++i){
            result[i] = list.get(i).toString();
        }
        return result;
    }
    
    public void updateDataBase(String tableName, Object[] oldData, Object[] newData) throws SQLException{
        int id = 0;
        String sql = "UPDATE " + tableName + " SET ";
        switch(tableName){
            case "guest":
                List<Guest> listGuest = getGuests();
                for(int i = 0; i < listGuest.size(); ++i){
                    if(listGuest.get(i).getSurname().equals(oldData[0]) &&
                            listGuest.get(i).getName().equals(oldData[1]) &&
                            listGuest.get(i).getPatr().equals(oldData[2]))
                        id = listGuest.get(i).getId();
                }
                sql += "surname = '" + newData[0] + "'" +
                        ", name = '" + newData[1] + "'" +
                        ", patr = '" + newData[2] + "'" +
                        " WHERE " +
                        "guestid = " + id;
                break;
            case "room":
                sql += "status = '" + newData[1] + "'" +
                        ", tip = '" + newData[2] + "'" +
                        ", cost = " + newData[3] +
                        " WHERE " +
                        "roomid = " + oldData[0] +
                        "AND status = '" + oldData[1] + "' " +
                        "AND tip = '" + oldData[2] + "' " +
                        "AND cost = " + oldData[3];
                break;
            case "employee":
                List<Employee> listEmp = getEmployees();
                for(int i = 0; i < listEmp.size(); ++i){
                    if(listEmp.get(i).getSurname().equals(oldData[0]) &&
                            listEmp.get(i).getName().equals(oldData[1]) &&
                            listEmp.get(i).getPatr().equals(oldData[2]) &&
                            listEmp.get(i).getPosition().equals(oldData[3]))
                        id = listEmp.get(i).getId();
                }
                sql += "surname = '" + newData[0] +
                        "', name = '" + newData[1] +
                        "', patr = '" + newData[2] +
                        "', position = '" + newData[3] + 
                        "', money = " + newData[4] +
                        " WHERE employeeid = " + id;
                break;
            case "service":
                List<Service> listService = getServices();
                for(int i = 0; i < listService.size(); ++i){
                    if(oldData[0].equals(listService.get(i).getEmployee().getId()) &&
                            listService.get(i).getName().equals(oldData[1]) &&
                            oldData[2].equals(listService.get(i).getCost()))
                        id = listService.get(i).getId();
                }
                sql += "employeeid = " + newData[0] +
                        ", name = '" + newData[1] +
                        "', cost = " + newData[2] +
                        " WHERE serviceid = " + id;
                break;
        }
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
    
    public int getEmployeeByName(String name) throws SQLException{
        List<Employee> list = getEmployees();
        for(int i = 0; i < list.size(); ++i){
            if(list.get(i).toString().equals(name))
                return list.get(i).getId();
        }
        return 1;
    }
    
    public void deleteFromDataBase(String tableName, int id) throws SQLException{
        String sql = "DELETE FROM " + tableName + " WHERE " + tableName + "id = " + id;
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
            sendMessage("Удаление данной строки приводит к нарушению целостности базы данных");
        }
    }
    
    //Получение по id
    private Guest getGuestById(int id) throws SQLException{
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
    
    private Room getRoomById(int id) throws SQLException{
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
    
    private RoomOrder getRoomOrderById(int id) throws SQLException{
        connection = DriverManager.getConnection(connectionString, username, password);
        statement = connection.createStatement();
        ResultSet rs =  statement.executeQuery("SELECT * FROM roomorder where orderid = " + id);
        RoomOrder roomOrder = new RoomOrder();
        rs.next();
        roomOrder.setId(rs.getInt("orderid"));
        roomOrder.setGuestId(rs.getInt("guestId"));
        roomOrder.setRoomId(rs.getInt("roomid"));
        roomOrder.setCountDay(rs.getInt("countday"));
        roomOrder.setOrderDate(rs.getDate("orderdate"));
        roomOrder.setChek(rs.getDouble("chek"));
        roomOrder.setGuest(getGuestById(rs.getInt("guestId")));
        return roomOrder;
    }
    
    private Service getServiceById(int id) throws SQLException{
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
    
    private int getIdGuest(String guest) throws SQLException{
        List<Guest> list = getGuests();
        for(int i = 0; i < list.size(); ++i){
            String string = list.get(i).getSurname() + ' ' + list.get(i).getName() + ' ' + list.get(i).getPatr();
            if(string.equals(guest)){
                return list.get(i).getId();
            }
        }
        return 1;
    }
    
    public Object[] getRoomList() throws SQLException{
        List<Room> rooms = getRooms();
        Object[] result = new Object[rooms.size()];
        for(int i = 0; i < rooms.size(); ++i){
            result[i] = rooms.get(i).toString();
        }
        return result;
    }
    
    public Object[] getRoomOrderList() throws SQLException{
        List<RoomOrder> list = getRoomOrders();
        Object[] result = new Object[list.size()];
        for(int i = 0; i < list.size(); ++i){
            result[i] = list.get(i).toString();
        }
        return result;
    }
    
    public Object[] getServiceList() throws SQLException{
        List<Service> list = getServices();
        Object[] result = new Object[list.size()];
        for(int i = 0; i < list.size(); ++i){
            result[i] = list.get(i).toString();
        }
        return result;
    } 
    
    //public void addRoomOrder(int id, int guestId, int roomId, int countDay, String orderDate) throws SQLException{
    public void addRoomOrder(String guest, int roomId, int countDay, String orderDate) throws SQLException{
        Room room = getRoomById(roomId);
        if(room.getBooleanStatus()){
            sendMessage("Данный номер сейчас занят!");
            return;
        }
        List<RoomOrder> list = getRoomOrders();
        int max = 0;
        for(int i = 0; i < list.size(); ++i){
            if(list.get(i).getId() > max)
                max = list.get(i).getId();
        }
        ++max;
        connection = DriverManager.getConnection(connectionString, username, password);
        connection.setAutoCommit(false);
        statement = connection.createStatement();
        try{
            Object[] oldRoomData = {room.getId(), room.getBooleanStatus(), room.getTip(), room.getCost()};
            Object[] newRoomData = {room.getId(), true, room.getTip(), room.getCost()};
            updateDataBase("room", oldRoomData, newRoomData);
            double cost = room.getCost() * countDay;
            Object[] data = {max, getIdGuest(guest), roomId, countDay, orderDate, cost};
            addToDataBase("roomorder", data);
            sendMessage("Добавление прошло успешно");
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
    
    private void addToChek(int roomOrderId, double cost) throws SQLException{
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
    
    
    
    private int getOrderByName(String order) throws SQLException {
        List<RoomOrder> list = getRoomOrders();
        for(int i = 0; i < list.size(); ++i){
            if(list.get(i).toString().equals(order))
                return list.get(i).getId();
        }
        return 1;
    }
    
    public void addServiceOrder(String order, int serviceId, String date) throws SQLException{
        List<ServiceOrder> list = getServiceOrders();
        int max = 0;
        for(int i = 0; i < list.size(); ++i){
            if(list.get(i).getId() > max)
                max = list.get(i).getId();
        }
        ++max;
        double cost = getServiceById(serviceId).getCost();
        int orderId = getOrderByName(order);
        addToChek(orderId, cost);
        connection = DriverManager.getConnection(connectionString, username, password);
        connection.setAutoCommit(false);
        statement = connection.createStatement();
        try{
            Object[] data = {max, orderId, serviceId, date};
            addToDataBase("serviceorder", data);
            connection.commit();
        }
        catch (SQLException ex){
            ex.printStackTrace();
            connection.rollback();
        }
    }
    
    public Object[] getGuestList() throws SQLException{
        List<Guest> guests = getGuests();
        Object[] result = new Object[guests.size()];
        for(int i = 0; i < guests.size(); ++i){
            result[i] = guests.get(i).toString();
        }
        return result;
    }
    
    public int getServiceIdByName(String service) throws SQLException{
        List<Service> list = getServices();
        for(int i = 0; i < list.size(); ++i){
            if(list.get(i).getName().equals(service))
                return list.get(i).getId();
        }
        return 1;
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
    
    private void setConnection(boolean connected){
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
