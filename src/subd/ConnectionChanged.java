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
public interface ConnectionChanged {
    void onConnectionChanged(boolean connection);
    
    void onMessageSend(String message);
}
