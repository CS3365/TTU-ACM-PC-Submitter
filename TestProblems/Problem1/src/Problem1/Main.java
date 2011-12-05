/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Problem1;

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
    BufferedReader in = new BufferedReader(new FileReader("Problem1Input.txt"));
    BufferedWriter out = new BufferedWriter(
        new FileWriter("Output.txt"));
    int lines = Integer.parseInt(in.readLine());
    in.close();
    for(int i=1;i<=lines;i++) {
      out.write((i*2)+"\n");
    }
    out.flush();
    out.close();
  }

}
