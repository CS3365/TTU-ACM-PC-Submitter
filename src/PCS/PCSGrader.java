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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
  }

  public void grade() {
    try {
      compile();
      this.start();
    } catch(CompilationFailureException ex) {
      // TODO: report this back to the groupConnection
      ArrayList<String> output = ex.getOutput();
      System.out.println("\nCompile finished with: "+ex.getErrorCode());
      for(String s : output) {
        System.out.println(s);
      }
      System.out.println("");
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
          new InputStreamReader(compilation.getErrorStream()));
      String line;
      while((line = in.readLine()) != null) {
        output.add(line);
      }
      int resultCode = compilation.waitFor();
      if(resultCode != 0) {
        throw new CompilationFailureException(output,resultCode);
      }
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
      copyInputFile();
      int resultCode = run.waitFor();
      boolean passed = diffOutputFiles();
      System.out.println("Graded code: "+passed);
      // TODO: return the result to the groupConnection
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

  private void copyInputFile() {
    File origPath = new File(pcs.serverDirectory+"/Problems/"+
        submission.getProblem().getProblemOrder()+"/input.txt");
    File subPath = new File(submission.getDirectory()+"/"+
        submission.getProblem().getProblemTitle()+"Input.txt");
    try {
      BufferedReader in = new BufferedReader(new FileReader(origPath));
      BufferedWriter out = new BufferedWriter(new FileWriter(subPath));
      String line;
      while((line = in.readLine()) != null) {
        out.write(line);
      }
      out.flush();
      out.close();
      in.close();
    } catch(FileNotFoundException ex) {
      System.out.println("FileNotFileException while trying to copy the "+
          "input file for problem: "+submission.getProblem().getProblemTitle()+
          " to directory "+submission.getDirectory());
      ex.printStackTrace();
    } catch(IOException ex) {
      System.out.println("IOException while trying to copy the "+
          "input file for problem: "+submission.getProblem().getProblemTitle()+
          " to directory "+submission.getDirectory());
      ex.printStackTrace();
    }
  }

  private boolean diffOutputFiles() {
    File origPath = new File(pcs.serverDirectory+"/Problems/"+
        submission.getProblem().getProblemOrder()+"/output.txt");
    File subPath = new File(submission.getDirectory()+"/"+
        submission.getProblem().getProblemTitle()+"Output.txt");
    try {
      BufferedReader known = new BufferedReader(new FileReader(origPath));
      BufferedReader trial = new BufferedReader(new FileReader(subPath));
      String knownLine = null, trialLine = null;
      while((knownLine = known.readLine()) != null &&
          (trialLine = trial.readLine()) != null) {
        if(!knownLine.equals(trialLine)) {
          return false;
        }
      }
      // Finish reading trial to check for EOF since the && above will stop
      // as soon as knownLines == null and stop from reading the next trialLine
      if(knownLine == null && trialLine != null) {
        trialLine = trial.readLine();
      }
      // if knownLine == null, then trial file finished before known
      // if trialLine == null, then known file finished before trial
      // either way, they are not exactly the same.
      if(knownLine != null || trialLine != null) {
        return false;
      }
      return true;
    } catch(FileNotFoundException ex) {
      System.out.println("FileNotFileException while trying to copy the "+
          "input file for problem: "+submission.getProblem().getProblemTitle()+
          " to directory "+submission.getDirectory());
      ex.printStackTrace();
    } catch(IOException ex) {
      System.out.println("IOException while trying to copy the "+
          "input file for problem: "+submission.getProblem().getProblemTitle()+
          " to directory "+submission.getDirectory());
      ex.printStackTrace();
    }
    // return false if there was an exception
    return false;
  }
}
