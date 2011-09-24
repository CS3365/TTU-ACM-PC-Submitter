/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package networking;

import NetworkIO.Message;

/**
 *
 * @author mike
 */
public class StringMessage implements Message {
    private String message;

    public StringMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
