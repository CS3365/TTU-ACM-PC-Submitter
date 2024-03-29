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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Mike Kent
 */
public class LanguageImplementation {
  public String compile, run;
  Pattern problem, problemDir, arguments;

  public LanguageImplementation() {
    problem = Pattern.compile("\\<problem\\>");
    arguments = Pattern.compile("(?<!\\\\) ");
  }

  public void setCompile(String compile) {
    this.compile = compile;
  }

  public void setRun(String run) {
    this.run = run;
  }

  /*public String getCompileString(ProblemSubmission submission) {
    System.out.println("matching to compile string: "+compile);
    Matcher m = problem.matcher(compile);
    return m.replaceAll(
        submission.getProblem().getProblemTitle().replace(" ", "\\\\ "));
  }*/

  public String[] getCompileArguments(ProblemSubmission submission) {
    String[] args = arguments.split(compile);
    Matcher m;
    for(int i=0; i<args.length; i++) {
      m = problem.matcher(args[i]);
      args[i] = m.replaceAll(
        submission.getProblem().getProblemTitle());//.replace(" ", "\\\\ "));
    }
    return args;
  }

  /*public String getRunString(ProblemSubmission submission) {
    Matcher m = problem.matcher(run);
    return m.replaceAll(
        submission.getProblem().getProblemTitle().replace(" ", "\\\\ "));
  }*/

  public String[] getRunArguments(ProblemSubmission submission) {
    String[] args = arguments.split(run);
    Matcher m;
    for(int i=0; i<args.length; i++) {
      m = problem.matcher(args[i]);
      args[i] = m.replaceAll(
        submission.getProblem().getProblemTitle());//.replace(" ", "\\\\ "));
    }
    return args;
  }
}
