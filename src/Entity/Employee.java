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
public class Employee {
    private int id;
    private String surname;
    private String name;
    private String patr;
    private String position;
    private double money;

    public int getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getPatr() {
        return patr;
    }

    public String getPosition() {
        return position;
    }

    public double getMoney() {
        return money;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPatr(String patr) {
        this.patr = patr;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return surname + " " + name + " " + patr;
    }
}
