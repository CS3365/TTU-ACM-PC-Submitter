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
import Leaderboard.LeaderboardEntry;
import Messages.LoginAttempt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Mike Kent
 */
public class PCSDatabase {
	private String dbFileName = "database.db";
  // TODO: remove problemAcc for actual implementation, also in getProblemAttemptNumber
  private int problemAcc;
  Connection db;
  private PCS pcs;

	public PCSDatabase(PCS pcs, String fileName) {
		dbFileName = fileName;
    this.pcs = pcs;
		initDatabase();
	}

	private void initDatabase() {
    try {
      Class.forName("org.sqlite.JDBC");
      db = DriverManager.getConnection("jdbc:sqlite:"+
          pcs.serverDirectory + "/" + dbFileName);
    } catch(SQLException ex) {
      System.out.println("There was an SQLException while attempt to connect "+
          "to the database.");
      ex.printStackTrace();
    } catch(ClassNotFoundException ex) {
      System.out.println("There was a ClassNotFoundException while attempting "+
          "to create the database connection.");
      ex.printStackTrace();
    }
	}

  protected int getProblemAttemptNumber(Team team, Problem problem) {
    // TODO: use the database and remove this the problemAcc variable.
    return problemAcc++;
  }

  protected void registerGradeResult(Team team, Problem problem,
      boolean success) {
    // TODO: fill in database code
  }

  protected Leaderboard getLeaderboard() {
    Leaderboard leaderboard = null;
    try {
      PreparedStatement stmt = db.prepareStatement("");
      /* query:
       * SELECT name, SUM(score) AS score, SUM(implTime) AS implTime
       * FROM
       *   (
       *     SELECT name, pointVal AS score, (COUNT(probID)-1)*60*5 + MAX(time) AS implTime
       *     FROM
       *       (
       *         SELECT uID, probID, name
       *         FROM SUBMISSION JOIN USER USING(uID)
       *         WHERE status = "success"
       *       )
       *       JOIN SUBMISSION USING(uID, probID) JOIN PROBLEM USING(probID)
       *     GROUP BY uID, probID
       *   )
       * GROUP BY uID
       * ORDER BY score DESC, implTime DESC;
       */
      ResultSet result = stmt.executeQuery(
          "SELECT name, SUM(score) AS score, SUM(implTime) AS implTime "+
          "FROM "+
            "("+
              "SELECT name, pointVal AS score, "+
                "(COUNT(probID) - 1) * 60 * " + pcs.incorrectPenality + " + " +
                  "MAX(time) AS implTime "+
              "FROM "+
                "("+
                  "SELECT uID, probID, name "+
                  "FROM SUBMISSION JOIN USER USING(uID) "+
                  "WHERE status = \"success\""+
                ")"+
                "JOIN SUBMISSION USING(uID, probID) JOIN PROBLEM USING(probID) "+
              "GROUP BY uID, probID"+
            ") "+
          "GROUP BY uID "+
          "ORDER BY score DESC, implTime DESC"
          );
      ArrayList<LeaderboardEntry> scores = new ArrayList<LeaderboardEntry>();
      while(result.next()) {
        scores.add(new LeaderboardEntry(
            result.getString("name"),
            result.getInt("score"),
            result.getInt("implTime")));
      }
      leaderboard = new Leaderboard(scores,pcs.getPhaseStartTime());
    } catch(SQLException ex) {
      System.out.println("There was an SQLException while attempting to "+
          "create a leaderboard.");
      ex.printStackTrace();
    }
    return leaderboard;
  }

  protected Problem getPracticeProblem() {
    // TODO: get practice problem
    return null;
  }

  protected ArrayList<String> getUsers() {
    ArrayList<String> users = new ArrayList<String>();
    try {
      Statement stmt = db.createStatement();
      ResultSet qUsers = stmt.executeQuery("SELECT name FROM USER;");
      while(qUsers.next()) {
        users.add(qUsers.getString("name"));
      }
    } catch(SQLException ex) {
      System.out.println("There was an SQLException while attempting to "+
          "retrieve the list of users.");
      ex.printStackTrace();
    }
    return users;
  }

  protected void setUsers(ArrayList<String> users) {
    // TODO: set users in the database
    ArrayList<String> currentUsers = getUsers();
    try {
      PreparedStatement stmt = db.prepareStatement(
          "INSERT INTO USER(name, passwd, isAdmin) VALUES(?, ?, 0)");
      db.setAutoCommit(false);
      for(String newUser : users) {
        if(!currentUsers.contains(newUser)) {
          stmt.setString(1, newUser);
          stmt.setString(2, newUser);
          stmt.addBatch();
        }
      }
      stmt.executeBatch();
      db.setAutoCommit(true);
    } catch(SQLException ex) {
      System.out.println("There was an SQLException while attempting to set "+
          "the users");
      ex.printStackTrace();
    }
  }

  protected ArrayList<Problem> getAllProblems() {
    // TODO: get all problems
    ArrayList<Problem> problems = new ArrayList<Problem>();
    try {
      PreparedStatement stmt = db.prepareStatement("");
      ResultSet results = stmt.executeQuery("SELECT * FROM PROBLEM");
      while(results.next()) {
        problems.add(new Problem(
            results.getString("title"),
            results.getString("description"),
            results.getInt("order"),
            results.getInt("pointVal"),
            results.getInt("phase")));
      }
    } catch(SQLException ex) {
      System.out.println("There was an SQLException while attempt to get "+
          "a list of all the problems in the competition");
      ex.printStackTrace();
    }
    return problems;
  }

	public boolean canLogIn(LoginAttempt attempt) {
    System.out.println("login attempt: " + attempt.getName() + " - "+
        attempt.getPassword());
    if(attempt.getName().equals("user") && attempt.getPassword().equals("user")) {
      return true;
    }
    // otherwise return false
    return false;
	}
}
