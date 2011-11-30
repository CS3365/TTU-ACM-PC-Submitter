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
import java.util.ArrayList;

/**
 *
 * @author Mike Kent
 */
public class PCSDatabase {
	private String dbFileName = "ProgrammingCompetition";
  // TODO: remove problemAcc for actual implementation, also in getProblemAttemptNumber
  private int problemAcc;

	public PCSDatabase(String fileName) {
		dbFileName = fileName;
		initDatabase();
	}

	private void initDatabase() {
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
    return null;
  }

  protected Problem getPracticeProblem() {
    // TODO: get practice problem
    return null;
  }

  protected ArrayList<Problem> getAllProblems() {
    // TODO: get all problems
    return new ArrayList<Problem>();
  }

	public boolean canLogIn(LoginAttempt attempt) {
    if(attempt.getName() == "user" && attempt.getPassword() == "user") {
      return true;
    }
    // otherwise return false
    return false;
	}
}
