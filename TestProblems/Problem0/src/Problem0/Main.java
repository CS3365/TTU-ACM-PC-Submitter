/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Problem0;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 *
 * @author Mike Kent
 */
public class Main {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws Exception {
    BufferedReader in = new BufferedReader(new FileReader("Problem0Input.txt"));
    BufferedWriter out = new BufferedWriter(
        new FileWriter("Problem0Output.txt"));
    int lines = Integer.parseInt(in.readLine());
    in.close();
    for(int i=1;i<=lines;i++) {
      out.write(i+"\n");
    }
    out.flush();
    out.close();
  }

}
