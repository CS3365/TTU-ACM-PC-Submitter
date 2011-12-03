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
import Messages.ProblemsList;
import NetworkIO.ClientBase;
import NetworkIO.Message;
import NetworkIO.NetworkListener;
import PCS.PCS;
import PCS.Problem;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;

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
  private String defaultLanguage;

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
    } catch (IOException ex) {
      System.out.println("There was an IOException while attempt to connect "
          + "to the server at " + ipAddress + ":" + PCS.serverPort);
      ex.printStackTrace();
      loginWindow.connectionFailure();
    }
  }

  public void processInput(Message m, Socket sok) {
    System.out.println("got message response!");
    if (m instanceof LoginStatus) {
      processLoginStatus((LoginStatus)m);
    } else if(m instanceof ProblemsList) {
      processProblemsList((ProblemsList)m);
    }
  }

  private void processLoginStatus(LoginStatus status) {
    switch (status.getResponse()) {
      // both ALREADY_LOGGED_IN and LOGIN_SUCCESS should continue to the next
      // window.
      case ALREADY_LOGGED_IN:
      case LOGIN_SUCCESS:
        loginWindow.setVisible(false);
        welcomeWindow.setLanguages(status.getLangauges());
        welcomeWindow.setVisible(true);
        break;
      case LOGIN_FAILURE:
        JOptionPane.showMessageDialog(mainWindow,
            "Your credentials did not match any on record.",
            "Login Failure",
            JOptionPane.ERROR_MESSAGE);
        break;
    }
  }

  private void processProblemsList(ProblemsList problems) {
    // TODO: remove the following code and replace it with code to send the
    // list of problems to MainWindow.
    for(Problem p : problems.getProblems()) {
      System.out.println("\tProblem: "+p.getProblemTitle());
    }
  }

  public void setDefaultLanguage(String lang) {
    System.out.println("Default Language set to: " + lang);
    defaultLanguage = lang;
  }

  public String getDefaultLanguage() {
    return defaultLanguage;
  }

  public void switchFromWelcomeToMainWindow() {
    welcomeWindow.setVisible(false);
    mainWindow.setVisible(true);
  }

  // TODO: remove this main method
  public static void main(String[] args) {
    new PCS();
    new PCCMain();
  }
}
