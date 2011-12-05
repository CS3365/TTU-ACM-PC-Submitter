/*
 *  The MIT License
 * 
 *  Copyright 2011 Mike Kent.
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 * 
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 * 
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package PCS;

import Messages.LoginAttempt;
import Messages.LoginStatus;
import NetworkIO.ClientBase;
import NetworkIO.Message;
import NetworkIO.NetworkListener;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Mike Kent
 */
public class PCSLoginListener implements NetworkListener {
	private PCS pcs;
	private enum ConnectionType { GROUP, ADMIN, UNKNOWN };
	private ConnectionType type;
	private boolean loggedIn;
  // the last successful attempt, test against to see if attempts are equal
  private LoginAttempt successfulAttempt;
  private ClientBase client;

	public PCSLoginListener(PCS pcs, ClientBase cb) {
		this.pcs = pcs;
		this.type = ConnectionType.UNKNOWN;
    this.client = cb;
    this.client.addNetworkListener(this);
	}

	public void processInput(Message m, Socket s) {
		// signin has not been established yet, handle messages
		if(type == ConnectionType.UNKNOWN) {
			if(m instanceof LoginAttempt) {
        System.out.println("got login attempt");
        LoginAttempt attempt = (LoginAttempt)m;
        if(loggedIn) {
          System.out.println("Already logged in");
          // Already logged in, simply say so.
          if(successfulAttempt.equals(attempt)) {
            try {
					    pcs.send(
									new LoginStatus(LoginStatus.LoginResponse.ALREADY_LOGGED_IN,
                      pcs.langs.keySet(), null),
									s);
            } catch(IOException e) {
              System.out.println("IOException while attempting to send "+
                  "ALREADY_LOOGED_IN message back to client.");
              e.printStackTrace();
            }
          }
          // TODO: should I log out if they are not equal?
        } else {
          System.out.println("not already logged in");
          // Not already logged in, attempt to do so
          LoginStatus response = pcs.attemptLogin(attempt);
          
          // client requires LoginResponse before Problems, therefore send
          // response before finalizing connection.
          try {
            System.out.println("Sending LoginResponse.");
            client.send(response);
          } catch(IOException ex) {
            System.out.println("There was an IOException while attempting to "+
                "send the LoginResponse to the client.");
            ex.printStackTrace();
          }

          // finalize connection
          if(response.getResponse() ==
              LoginStatus.LoginResponse.LOGIN_SUCCESS) {
            System.out.println("successful login; mac: "+attempt.getMACAddress());
            successfulAttempt = attempt;
            loggedIn = true;
            if(attempt.getMACAddress().equals("")) {
              type = ConnectionType.ADMIN;
              // TODO: add PCSAdminConnection
            } else {
              type = ConnectionType.GROUP;
              System.out.println("\tadding as group");
              new PCSGroupConnection(pcs, client, response.getTeam());
            }
          }
          else {
            System.out.println("Login was not a success, it was: "+
                response.getResponse().toString());
          }
        }
			}
		}
	}
}
