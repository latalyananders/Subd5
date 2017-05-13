/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subd;

/**
 *
 * @author Админ
 */
public class Guest {
    private int id;
    private String surname;
    private String name;
    private String patr;
    
    public Guest(int id, String surname, String name, String patr){
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.patr = patr;
    }
    
    public Guest(){}
    
    public void setId(int id){
        this.id = id;
    }
    
    public int getId(){
        return id;
    }
    
    public void setSurname(String surname){
        this.surname = surname;
    }
    
    public String getSurname(){
        return surname;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public void setPatr(String patr){
        this.patr = patr;
    }
    
    public String getPatr(){
        return patr;
    }
    
    @Override
    public String toString(){
        return surname + " " + name + " " + patr;
    }
}
