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

import Leaderboard.Leaderboard;
import Messages.LoginAttempt;
import Messages.LoginStatus;
import NetworkIO.ConnectionListener;
import NetworkIO.Message;
import NetworkIO.ServerBase;
import PCS.UI.PCSLeaderboard;
import PCS.UI.PCSSettings;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Mike Kent
 */
public class PCS implements ConnectionListener {
  public final String serverDirectory = "Server Data";
  public final String settingsFile = serverDirectory+"/settings.txt";
	private ServerBase server;
	public static int serverPort = 1923;
  protected final int incorrectPenality = 5; // in minutes
  private String dbFileName = "database.db";
  private String saveDirectory = "Submissions";
  private PCSDatabase db;
  public HashMap<String, LanguageImplementation> langs;
  private enum Phase { PRE_PRACTICE, PRACTICE, PRACTICE_STOPPED, PRE_TOURNIMENT,
    TOURNIMENT, TOURNIMENT_STOPPED };
  private Phase phase;
  private PCSSettings settingsWindow;
  private PCSLeaderboard leaderboardGUI;
  private long phaseStartTime;

	public PCS() {
		parseSettings();
    //parseProblems();
    parseLanguages();
    db = new PCSDatabase(this,dbFileName);
    settingsWindow = new PCSSettings(this);
    leaderboardGUI = new PCSLeaderboard(this);
    settingsWindow.setVisible(true);
	}

  public void startServer() {
    server = new ServerBase(serverPort);
    server.addConnectionListener(this);
    settingsWindow.setVisible(false);
    leaderboardGUI.setVisible(true);
  }

  public ArrayList<String> getUsers() {
    return db.getUsers();
  }

  public void setUsers(ArrayList<String> users) {
    db.setUsers(users);
  }

  /**
   * TODO: Will be replaced with Database implementation wherein there will be
   * a new UI created to manage the problems/administrators/etc that will
   * populate the database rather than everything be directory based.
   */
	private void parseSettings() {
		//TODO: parse settings
		try {
			BufferedReader in = new BufferedReader(
          new FileReader(settingsFile));
      String line, setting, value;
      Pattern exp = Pattern.compile("^(\\w+): ?(.*)$",Pattern.MULTILINE);
      Matcher matcher;
      while((line = in.readLine()) != null) {
        /* use regular expression parsing to parse the following format:
         *   setting: value
         *
         * ^(\w+): ?(.*)$
         */
        matcher = exp.matcher(line);
        if(matcher.matches()) {
					setting = matcher.group(1);
					value = matcher.group(2);
					// parse serverPort
					// parse dbFileName
          // parse saveDirectory
        }
      }
      in.close();
		} catch(FileNotFoundException ex) {
			System.out.println("File \""+settingsFile+
				"\" not found while attempting to read server "+
				"settings");
      ex.printStackTrace();
		} catch(IOException ex) {
      System.out.println("IOException while reading file \""+settingsFile+
          "\" to parse the server settings.");
      ex.printStackTrace();
    }
    
    // Check if there is a Problem 0
    File problem0 = new File(serverDirectory+"/Problems/0");
    if(problem0.exists()) {
      phase = Phase.PRE_PRACTICE;
    } else {
      phase = Phase.PRE_TOURNIMENT;
    }
	}

  /**
   * TODO: Will be replaced with Database implementation wherein there will be
   * a new UI created to manage the problems/administrators/etc that will
   * populate the database rather than everything be directory based.
   */
  private void parseProblems() {
    File problemsDir = new File(serverDirectory+"/Problems");
    // TODO: parse the problemsDir. Make sure that the order does not matter
    // also make sure that even if numbers are skipped that all are still parsed
    for(File problem : problemsDir.listFiles()) {
      try {
        int problemOrder = Integer.parseInt(problem.getName());
        // TODO: check with team how exactly to implement problem settings.
        // Should we use XML???
      } catch(NumberFormatException ex) {
        System.out.println("There was a NumberFormatException that occurred "+
            "while attempting to parse the problems in "+problem.getPath());
        ex.printStackTrace();
      }
    }
  }

  private void parseLanguages() {
    langs = new HashMap<String, LanguageImplementation>();
    File langFiles = new File(serverDirectory+"/Languages");
    Pattern exp = Pattern.compile("^(\\w+): ?(.*)$",Pattern.MULTILINE);
    Matcher matcher;
    String line;
    for(File lang : langFiles.listFiles()) {
      try {
        LanguageImplementation impl = new LanguageImplementation();
        BufferedReader in = new BufferedReader(new FileReader(lang));
        while((line = in.readLine()) != null) {
          matcher = exp.matcher(line);
          if(matcher.matches()) {
            if(matcher.group(1).equals("compile")) {
              impl.setCompile(matcher.group(2));
            } else if(matcher.group(1).equals("run")) {
              impl.setRun(matcher.group(2));
            }
          }
        }
        in.close();
        langs.put(lang.getName().substring(0,lang.getName().length()-4), impl);
      } catch(IOException ex) {
        System.out.println("An IOException occured while attempting to "+
            "load the language file: "+lang.getName());
        ex.printStackTrace();
      }
    }
  }

  /**
   * Attempt a login to the server
   *
   * @param attempt The attempt to login.
   * @return The result of attempting to login.
   */
  protected LoginStatus attemptLogin(LoginAttempt attempt) {
    if(db.canLogIn(attempt)) {
      return new LoginStatus(LoginStatus.LoginResponse.LOGIN_SUCCESS);
    } else {
      return new LoginStatus(LoginStatus.LoginResponse.LOGIN_FAILURE,
          "You are not authorized to log in.");
    }
  }

  protected File getProblemAttemptSaveDirectory(Problem problem, Team team) {
    return new File(
        saveDirectory+"/"+
        team.getTeamName()+
        "/Problem"+problem.getProblemOrder()+
        "/"+db.getProblemAttemptNumber(team, problem));
  }

  protected void registerGradeResult(Team team, Problem problem,
      boolean success) {
  }

  /**
   * Sends a message via the server.
   *
   * @param m The message to send.
   * @param s The socket to send it through.
   * @throws IOException
   */
  public void send(Message m, Socket s) throws IOException {
    server.send(m,s);
  }

  public Set<String> getLanguages() {
    return langs.keySet();
  }

  public void stopServer() {
    server.stopServer();
  }

  public long getPhaseStartTime() {
    return phaseStartTime;
  }

  private void nextPhase() {
    switch(phase) {
      case PRE_PRACTICE:
        // TODO: Send all information to teams: Leaderboard and problem list
        phase = Phase.PRACTICE;
        break;
      case PRACTICE:
        // TODO: send stop signal to all teams
        phase = Phase.PRACTICE_STOPPED;
        break;
      // both PRACTICE_STOPPED and PRE_TOURNIMENT are the same thing as far as
      // changing the phase is concerned. The difference is entirely semantic
      // in that PRACTICE_STOPPED occurs only when there was a practice round
      // and PRE_TOURNIMENT occurs only when there is no practice round.
      case PRACTICE_STOPPED:
      case PRE_TOURNIMENT:
        // TODO: Send all information to teams: Leaderboard and problem list
        phase = Phase.TOURNIMENT;
        phaseStartTime = System.currentTimeMillis();
        break;
      case TOURNIMENT:
        // TODO: Send stop signal to all teams
        phase = Phase.TOURNIMENT_STOPPED;
        break;
      // TODO: is the last case needed?
      //case TOURNIMENT_STOPPED:
    }
  }

  private void leaderboardUpdated() {
    Leaderboard lb = db.getLeaderboard();
    try {
      server.sendToAll(lb);
    } catch(IOException ex) {
      System.out.println("IOException while attempting to send the updated "+
          "leaderboard to all teams");
      ex.printStackTrace();
    }
  }

  protected ArrayList<Problem> getAllProblems() {
    /*ArrayList<Problem> problems = null;
    if(phase == Phase.PRACTICE) {
      problems = new ArrayList<Problem>();
      problems.add(db.getPracticeProblem());
    } else if(phase == Phase.TOURNIMENT) {
      problems = db.getAllProblems();
      // Remove problem 0, if there is one
      if(problems.get(0).getProblemOrder() == 0) {
        problems.remove(0);
      }
    }
    return problems;
    */
    // The database will contain everything!!!
    return db.getAllProblems();
  }

  public void gotConnection(Socket s) {
    System.out.println("got connection, adding login listener");
    new PCSLoginListener(this, server.getClientBase(s));
  }

  public void lostConnection(Socket s) {
    // do nothing
  }
}