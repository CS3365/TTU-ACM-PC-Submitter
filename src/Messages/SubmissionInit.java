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

package Messages;

import NetworkIO.Message;
import PCS.Problem;

/**
 *
 * @author Mike Kent
 */
public class SubmissionInit implements Message {
  private Problem problem;
  private int transmissionID;
  private String language;

  public SubmissionInit(Problem problem, int transmissionID, String language) {
    this.problem = problem;
    this.transmissionID = transmissionID;
    this.language = language;
  }

  /**
   * Get the problem this solution is for.
   *
   * @return The Problem this solution is solving.
   */
  public Problem getProblem() {
    return problem;
  }

  /**
   * Gets the transmission ID to identify the solution.
   *
   * @return The transmission ID.
   */
  public int getTransmissionID() {
    return transmissionID;
  }

  /**
   * Get the language this submission is written in.
   *
   * @return The submission's programming language.
   */
  public String getLanguage() {
    return language;
  }
}
