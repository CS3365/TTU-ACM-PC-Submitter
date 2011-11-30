/*
 * The MIT License
 *
 * Copyright 2011 Kevin.
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
package PCC;

/**
 *
 * @author Kevin
 */
public class PCCMain {
    private LoginWindow login;
  private MainWindow main;
  private SubmissionWindow submission;
  private WelcomeWindow welcome;

  public PCCMain() {
    this.login = new LoginWindow();
    this.main = new MainWindow();
    main.setVisible(false);
    this.submission = new SubmissionWindow();
    submission.setVisible(false);
    this.welcome = new WelcomeWindow();
    welcome.setVisible(false);
  }

  protected void showWelcome() {
    login.setVisible(false);
    welcome.setVisible(true);
  }

  protected void showMain() {
    welcome.setVisible(false);
    main.setVisible(true);
  }

  protected void showSubmission() {
    submission.setVisible(true);
  }

  protected void hideSubmission() {
    submission.setVisible(false);
  }
  
}
