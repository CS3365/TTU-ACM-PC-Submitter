/*
 * The MIT License
 *
 * Copyright 2011 Mike Kent.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package PCC;

import Messages.LoginStatus;
import NetworkIO.ClientBase;
import NetworkIO.Message;
import NetworkIO.NetworkListener;
import PCS.PCS;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Mike Kent
 */
public class PCCMain implements NetworkListener {
  private ClientBase client;
  private LoginWindow loginWindow;
  private MainWindow mainWindow;
  private SubmissionWindow submissionWindow;
  private WelcomeWindow welcomeWindow;

  public PCCMain() {
    loginWindow = new LoginWindow(this);
    mainWindow = new MainWindow(this);
    submissionWindow = new SubmissionWindow(this);
    welcomeWindow = new WelcomeWindow(this);

    // start client
    loginWindow.setVisible(true);
  }

  public void attemptServerConnection(String ipAddress) {
    try {
      client = new ClientBase(ipAddress, PCS.serverPort);
      client.addNetworkListener(this);
      System.out.println("sending login attempt");
      client.send(loginWindow.getLoginAttempt());
    } catch(IOException ex) {
      System.out.println("There was an IOException while attempt to connect "+
          "to the server at "+ipAddress+":"+PCS.serverPort);
      ex.printStackTrace();
      loginWindow.connectionFailure();
    }
  }

  public void processInput(Message m, Socket sok) {
    System.out.println("got message response!");
    if(m instanceof LoginStatus) {
      LoginStatus status = (LoginStatus)m;
      System.out.println("Got LoginStatus: "+status.getResponse().toString());
      switch(status.getResponse()) {
        case ALREADY_LOGGED_IN:
          break;
        case LOGIN_SUCCESS:
          break;
        case LOGIN_FAILURE:
          break;
      }
    }
  }

  // TODO: remove this main method
  public static void main(String[] args) {
    new PCS();
    new PCCMain();
  }
}
