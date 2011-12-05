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
package Leaderboard;

/**
 *
 * @author Mike Kent
 */
public class LeaderboardEntry {
  public String teamName;
  public int score;
  public int implTime;  // implementation time

  public LeaderboardEntry(String name, int score, int time) {
    teamName = name;
    this.score = score;
    implTime = time;
  }
  
  /**
   * Get the name of the team.
   * @return The teams name.
   */
  public String getTeamName() {
    return teamName;
  }

  /**
   * Get the overall score of the team
   * @return The team's score.
   */
  public int getScore() {
    return score;
  }

  /**
   * Get the total time to implementation time with penalties in the format
   * hours:minutes:seconds
   * @return The implementation time with penalties.
   */
  public String getImplementationTime() {
    int tmpTime = implTime;
    String strImplTime = "" + (tmpTime/(60*60));
    tmpTime %= 60*60;
    strImplTime += ":" + (tmpTime/(60));
    tmpTime %= 60;
    strImplTime += ":" + tmpTime;
    return strImplTime;
  }
}
