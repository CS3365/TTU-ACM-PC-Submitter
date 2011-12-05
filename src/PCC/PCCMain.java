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
import Messages.SubmissionAck;
import Messages.SubmissionGradingStarted;
import Messages.SubmissionInit;
import Messages.SubmissionResult;
import NetworkIO.ClientBase;
import NetworkIO.Message;
import NetworkIO.NetworkListener;
import PCS.PCS;
import PCS.Problem;
import SolutionSubmitter.FolderSender;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
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
  private Object defaultLanguage;
  protected String problemlist = "";
  private int nextTransmissionID = 0;
  private HashMap<Integer, File> submissions;
  private HashMap<Integer, Problem> submissionProblems;
  private ArrayList<Problem> problems;

  public PCCMain() {
    submissions = new HashMap<Integer, File>();
    submissionProblems = new HashMap<Integer, Problem>();
    loginWindow = new LoginWindow(this);
    mainWindow = new MainWindow(this);
    //submissionWindow = new SubmissionWindow(this);
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

  protected void sendSubmission(Problem prob, File folder, String lang) {
    System.out.println("Sending SubmissionInit("+prob+", "+folder+", "+lang+")");
    int transmissionID = nextTransmissionID++;
    submissions.put(transmissionID, folder);
    submissionProblems.put(transmissionID, prob);
    
    // send the request to transfer a submission
    try {
      System.out.println("Sending SubmissionInit");
      client.send(new SubmissionInit(prob, transmissionID, lang));
    } catch(IOException ex) {
      System.out.println("There was an IOException while attempt to send a "+
          "SubmissionInit for problem: "+prob.getProblemTitle());
      ex.printStackTrace();
    }
  }

  private void startSubmission(int transmissionID) {
    System.out.println("Starting the FolderSender with id: "+
        transmissionID);
    new FolderSender(client, submissions.get(transmissionID),
        transmissionID).start();
  }

  public void processInput(Message m, Socket sok) {
    if (m instanceof LoginStatus) {
      System.out.println("Got LoginStatus");
      processLoginStatus((LoginStatus) m);
    } else if (m instanceof ProblemsList) {
      System.out.println("Got ProblemsList");
      processProblemsList((ProblemsList) m);
    } else if(m instanceof SubmissionAck) {
      System.out.println("got SubmissionAck");
      startSubmission(((SubmissionAck)m).getTransmissionID());
    } else if(m instanceof SubmissionResult) {
      SubmissionResult result = (SubmissionResult)m;
      System.out.println("got SubmissionResult\n\t"+
          "result: "+result.getSuccess());
      mainWindow.processSubmissionResult(result,
          submissionProblems.get(result.getTransmissionID()));
    } else if(m instanceof SubmissionGradingStarted) {
      SubmissionGradingStarted msg = (SubmissionGradingStarted)m;
      mainWindow.processSubmissionGradingStarted(
          submissionProblems.get(msg.getTransmissionID()));
    }
  }

  private void processLoginStatus(LoginStatus status) {
    switch (status.getResponse()) {
      // both ALREADY_LOGGED_IN and LOGIN_SUCCESS should continue to the next
      // window.
      case ALREADY_LOGGED_IN:
      case LOGIN_SUCCESS:
        loginWindow.setVisible(false);

        // register languages
        welcomeWindow.setLanguages(status.getLanguages());
        mainWindow.setLanguages(status.getLanguages());
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
    // ProblemsList is typically received before the default language is set
    this.problems = problems.getProblems();
    if(defaultLanguage != null) {
      mainWindow.setProblemsList(problems.getProblems());
    }
  }

  public void setDefaultLanguage(Object lang) {
    // ProblemsList is typically received before the default language is set
    System.out.println("Default Language set to: " + lang);
    defaultLanguage = lang;
    if(problems != null) {
      mainWindow.setProblemsList(problems);
    }
  }

  public Object getDefaultLanguage() {
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
