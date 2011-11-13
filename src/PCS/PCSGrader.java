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

import PCS.Exceptions.CompilationFailureException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Mike Kent
 */
public class PCSGrader extends Thread {
  private PCS pcs;
  private PCSGroupConnection groupConnection;
  private ProblemSubmission submission;
  private String solutionDir, submissionDir;
  private Process run;

  public PCSGrader(PCS pcs, PCSGroupConnection groupConnection,
      ProblemSubmission submission) {
    this.pcs = pcs;
    this.groupConnection = groupConnection;
    this.submission = submission;
    grade();
    //this.start();
  }

  private void grade() {
    try {
      compile();
      this.start();
    } catch(CompilationFailureException ex) {
    }
  }

  private void compile() throws CompilationFailureException {
    try {
      String compile =
          pcs.langs.get(submission.getLanguage()).getCompileString(submission);
      Process compilation = Runtime.getRuntime().exec(
          compile,
          new String[0],
          submission.getDirectory());
      ArrayList<String> output = new ArrayList<String>();
      BufferedReader in = new BufferedReader(
          new InputStreamReader(compilation.getInputStream()));
      String line;
      while((line = in.readLine()) != null) {
        output.add(line);
      }
      int resultCode = compilation.waitFor();
    } catch(IOException ex) {
      System.out.println("There was an IOException while attempting to "+
          "compile the submission at: "+
          submission.getDirectory().getAbsolutePath());
      ex.printStackTrace();
    } catch(InterruptedException ex) {
      System.out.println("There was an InterruptedException while attempting "+
          "to compile the submission at: "+
          submission.getDirectory().getAbsolutePath());
      ex.printStackTrace();
    }
  }

  public void run() {
    try {
      String runCommand =
          pcs.langs.get(submission.getLanguage()).getRunString(submission);
      run = Runtime.getRuntime().exec(
          runCommand,
          new String[0],
          submission.getDirectory());
      // TODO: make timeout code (5 mins or whatever the server is set to)
      int resultCode = run.waitFor();
    } catch(IOException ex) {
      System.out.println("There was an IOException while running the "+
          "submission at: "+submission.getDirectory().getAbsolutePath());
      ex.printStackTrace();
    } catch(InterruptedException ex) {
      System.out.println("There was an InterruptedException while running "+
          "the submission at"+
          submission.getDirectory().getAbsolutePath());
      ex.printStackTrace();
    }
  }
}
