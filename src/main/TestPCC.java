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
package main;

import Messages.LoginAttempt;
import Messages.LoginStatus;
import Messages.SubmissionAck;
import Messages.SubmissionInit;
import Messages.SubmissionResult;
import NetworkIO.ClientBase;
import NetworkIO.Message;
import NetworkIO.NetworkListener;
import PCS.PCS;
import PCS.Problem;
import SolutionSubmitter.FolderSender;
import java.io.File;
import java.net.Socket;

/**
 * The goal of this class is to create a simple PCC which establishes a
 * connection, attempts to login, then send a sample submission for grading.
 *
 * @author Mike Kent
 */
public class TestPCC implements NetworkListener {
  private ClientBase cb;
  
  public TestPCC(PCS pcs) {
    try {
      cb = new ClientBase("localhost",pcs.serverPort);
      cb.addNetworkListener(this);
      loginToServer();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void loginToServer() {
    try {
      cb.send(new LoginAttempt("name","pass","mac"));
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void sendSubmission() {
    try {
      cb.send(new SubmissionInit(
          new Problem("Problem0"),
          0,
          "Java"));
      System.out.println("Sent submission init");
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void processInput(Message m, Socket s) {
    if(m instanceof LoginStatus) {
      LoginStatus status = (LoginStatus)m;
      System.out.println("got login status: "+status.getResponse().name());
      System.out.println("attempting to send problem");
      sendSubmission();
    }
    else if(m instanceof SubmissionAck) {
      SubmissionAck ack = (SubmissionAck)m;
      System.out.println("Got submission acknologement");
      new FolderSender(cb,new File("TestProblems/Problem0"),
          ack.getTransmissionID()).start();
    }
    else if(m instanceof SubmissionResult) {
      SubmissionResult result = (SubmissionResult)m;
      System.out.println("Result of grading: "+result.getSuccess());
    }
  }

  public void disconnect() {
    cb.disconnect();
  }
}
