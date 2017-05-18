/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

/**
 *
 * @author Админ
 */
public class Service {
    private int id;
    private int employeeId;
    private String name;
    private double cost;
    
    private Employee employee;

    public int getId() {
        return id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return name;
    }
}
