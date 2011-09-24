/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package networking;

import NetworkIO.Message;
import NetworkIO.NetworkListener;
import java.net.Socket;

/**
 *
 * @author mike
 */
public class TestListener implements NetworkListener {

    private String name;

    public TestListener(String name) {
        this.name = name;
    }

    public void processInput(Message msg, Socket sok) {
        if(msg instanceof StringMessage) {
            System.out.println(name+": got message: \""+
                    ((StringMessage)msg).getMessage()+"\"");
        }
    }

}
