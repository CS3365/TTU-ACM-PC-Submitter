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

import NetworkIO.Message;
import java.io.File;

/**
 * This class should only be sent once a problem source files has been
 * completed.
 *
 * @author Mike Kent
 */
public class ProblemSubmission implements Message {
  private int transmissionID;
  private Problem problem;
  private String language;
  private File directory;

  public ProblemSubmission(int transmissionID, Problem problem,
      File directory, String language) {
    this.transmissionID = transmissionID;
    this.problem = problem;
    this.directory = directory;
    this.language = language;
  }

  /**
   * Get the transmission ID for this submission.
   *
   * @return The submission's transmission ID.
   */
  public int getTransmissionID() {
    return transmissionID;
  }

  /**
   * Get the problem this submission is solving.
   *
   * @return The problem this submission is solving.
   */
  public Problem getProblem() {
    return problem;
  }

  /**
   * Get the directory this submission is in.
   *
   * @return The directory the submission was saved to.
   */
  public File getDirectory() {
    return directory;
  }

  /**
   * Get the language that this submission was written in.
   *
   * @return The programming language.
   */
  public String getLanguage() {
    return language;
  }
}
