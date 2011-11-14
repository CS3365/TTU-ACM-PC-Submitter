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

import NetworkIO.ClientBase;
import NetworkIO.Message;
import NetworkIO.NetworkListener;
import SolutionSubmitter.FolderSaver;
import SolutionSubmitter.SaverHandler;
import Messages.SubmissionAck;
import Messages.SubmissionCompilationFailure;
import Messages.SubmissionInit;
import Messages.SubmissionOvertimeFailure;
import Messages.SubmissionResult;
import Messages.SubmissionRuntimeFailure;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class should be able to manage all things a group would like to do
 *
 * @author Mike Kent
 */
public class PCSGroupConnection implements NetworkListener, SaverHandler {
  protected ClientBase client;
  private Team team;
  private PCS pcs;
  private Queue<ProblemSubmission> submissions;
  private PCSGrader checker;

  public enum SubmissionStatus{IN_PROGRESS, SUCCESS, FAILED};
  private class SubmissionStats {
    public SubmissionStatus status;
    public FolderSaver saver;
    public Problem problem;
    public int transferAttempts;
    public Socket socket;
    public String language;

    public SubmissionStats(SubmissionStatus status, FolderSaver saver,
        Problem problem, Socket socket, String language) {
      this.status = status;
      this.saver = saver;
      this.problem = problem;
      this.socket = socket;
      this.transferAttempts = 0;
      this.language = language;
    }
  }
  private HashMap<Integer,SubmissionStats> transfers;

  public PCSGroupConnection(PCS pcs, ClientBase cb, Team team) {
    submissions = new LinkedList<ProblemSubmission>();
    transfers = new HashMap<Integer,SubmissionStats>();
    this.pcs = pcs;
    this.team = team;
    this.client = cb;
    System.out.println("PCSGroupConnection created, adding to network "+
        "listeners");
    this.client.addNetworkListener(this);
  }

  public void processInput(Message m, Socket sok) {
    if(m instanceof ProblemSubmission) {
      ProblemSubmission submission = (ProblemSubmission)m;
    }
    else if(m instanceof SubmissionInit) {
      System.out.println("Got SubmissionInit");
      processSubmissionInit((SubmissionInit)m, sok);
    }
  }

  private void processSubmissionInit(SubmissionInit init, Socket sok) {
    File saveDirectory =
        pcs.getProblemAttemptSaveDirectory(init.getProblem(), team);
    FolderSaver saver =
        new FolderSaver(saveDirectory,init.getTransmissionID(),this);
    transfers.put(init.getTransmissionID(),
        new SubmissionStats(
          SubmissionStatus.IN_PROGRESS,
          saver,
          init.getProblem(),
          sok,
          init.getLanguage()));
    client.addNetworkListener(saver);
    // send the acknoledgement and rediness to recieve the submission
    try {
      pcs.send(new SubmissionAck(init.getTransmissionID()), sok);
    } catch(IOException ex) {
      System.out.println("An IOException occurred while attempting to send "+
          "an acknoledgement of a submission for team "+team.getTeamName()+
          " and problem "+init.getProblem().getProblemTitle()+".");
      ex.printStackTrace();
    }
  }

  private void enqueueSubmission(ProblemSubmission submission) {
    submissions.add(submission);
    if(checker == null) {
      runSubmission();
    }
  }

  private void runSubmission() {
    if(!submissions.isEmpty() && checker == null) {
      checker = new PCSGrader(pcs,this,submissions.remove());
    }
  }

  public void sendCompilationFailure(ProblemSubmission submission,
      ArrayList<String> message, int errorCode) {
    try {
      client.send(new SubmissionCompilationFailure(message,errorCode,
          submission.getTransmissionID()));
      pcs.registerGradeResult(team, submission.getProblem(), false);
    } catch(IOException ex) {
      System.out.println("There was an error while sending a compilation "+
          "failure message to "+team.getTeamName()+ " for problem: "+
          submission.getProblem().getProblemTitle());
      ex.printStackTrace();
    }
  }

  public void sendRuntimeFailure(ProblemSubmission submission,
      ArrayList<String> message, int errorCode) {
    try {
      client.send(new SubmissionRuntimeFailure(message,errorCode,
          submission.getTransmissionID()));
      pcs.registerGradeResult(team, submission.getProblem(), false);
    } catch(IOException ex) {
      System.out.println("There was an error while sending a runtime "+
          "failure message to "+team.getTeamName()+ " for problem: "+
          submission.getProblem().getProblemTitle());
      ex.printStackTrace();
    }
  }

  public void sendOvertimeFailure(ProblemSubmission submission) {
    try {
      client.send(new SubmissionOvertimeFailure(submission.getTransmissionID()));
      pcs.registerGradeResult(team, submission.getProblem(), false);
    } catch(IOException ex) {
      System.out.println("There was an error while sending a overtime "+
          "failure message to "+team.getTeamName()+ " for problem: "+
          submission.getProblem().getProblemTitle());
      ex.printStackTrace();
    }
  }

  public void gradingCompleted(ProblemSubmission submission, boolean success) {
    try {
      client.send(new SubmissionResult(submission.getTransmissionID(),success));
      pcs.registerGradeResult(team, submission.getProblem(), true);
    } catch(IOException ex) {
      System.out.println("There was an error while sending a submission "+
          "result message to "+team.getTeamName()+ " for problem: "+
          submission.getProblem().getProblemTitle());
      ex.printStackTrace();
    }
  }

  public void transmissionSuccess(int transmissionID) {
    // enqueue the submission to be graded
    System.out.println("transmission success");
    SubmissionStats stats = transfers.get(transmissionID);
    FolderSaver saver = stats.saver;
    File dir = saver.getSaveDirectory();
    enqueueSubmission(
        new ProblemSubmission(transmissionID,
          stats.problem,
          dir,
          stats.language));
    client.removeNetworkListener(saver);
  }

  public void transmissionFailed(int transmissionID) {
    // increment the attempt count for this tranmission, then try again if
    // number of attempts is less than 3.
    System.out.println("transmission failed in PCSGroupConnection");
    SubmissionStats stats = transfers.get(transmissionID);
    if(stats.transferAttempts++ < 3) {
      stats.saver.resetSaver();
      // ask for a resubmission
      try {
        pcs.send(new SubmissionAck(transmissionID), stats.socket);
      } catch(IOException ex) {
        System.out.println("An IOException occurred while attempting to "+
            " request a re-submission from team "+team.getTeamName()+
            " and problem "+stats.problem.getProblemTitle()+".");
        ex.printStackTrace();
      }
    }
  }
}
