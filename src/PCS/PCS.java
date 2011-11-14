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
import NetworkIO.Message;
import NetworkIO.ServerBase;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Mike Kent
 */
public class PCS {
  public final String serverDirectory = "Server Data";
  public final String settingsFile = serverDirectory+"/settings.txt";
	private ServerBase server;
	private int serverPort = 1923;
  private String dbFileName = "ProgrammingCompetition.db";
  private String saveDirectory = "Submissions";
  private PCSDatabase db;
  public HashMap<String, LanguageImplementation> langs;

	public PCS() {
		parseSettings();
    parseLanguages();
    server = new ServerBase(serverPort);
    db = new PCSDatabase(dbFileName);
	}

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
        "/Problem"+problem.getProblemOrder());
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
}