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

/*
 * MKLeaderboardPanel.java
 *
 * Created on Dec 5, 2011, 8:05:23 AM
 */
package Leaderboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mike Kent
 */
public class MKLeaderboardPanel extends javax.swing.JPanel implements
    ActionListener
{
  private Leaderboard leaderboard;
  private Object[] columns;
  private long startTime;
  private Timer timer;

  /** Creates new form MKLeaderboardPanel */
  public MKLeaderboardPanel() {
    initComponents();
    startTime = System.currentTimeMillis();
    timer = new Timer(1000, this);
    timer.start();
    columns = new Object[3];
    columns[0] = "Team"; columns[1] = "Score"; columns[2] = "Time";
  }

  public void updateLeaderboard(Leaderboard lb) {
    leaderboard = lb;
    ArrayList<LeaderboardEntry> entries = leaderboard.getScores();
    Object[][] treeData = new Object[entries.size()][3];
    LeaderboardEntry entry;
    for(int i=0; i<entries.size();i++) {
      entry = entries.get(i);
      treeData[i][0] = entry.getTeamName();
      treeData[i][1] = entry.getScore();
      treeData[i][2] = entry.getImplementationTime();
    }
    lbTree.setModel(new DefaultTableModel(treeData, columns));
  }

  public void actionPerformed(ActionEvent evt) {
    long runningTime = System.currentTimeMillis() - startTime;
    String strRunningTime = ""+(runningTime/(60*60*1000));
    runningTime %= 60*60*1000;
    strRunningTime += ":"+(runningTime/(60*1000));
    runningTime %= 60*1000;
    strRunningTime += ":"+(runningTime/1000);
    lblRunningTime.setText("Runing Time: "+strRunningTime);
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        lbTree = new javax.swing.JTable();
        lblRunningTime = new javax.swing.JLabel();

        lbTree.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Team", "Score", "Time"
            }
        ));
        jScrollPane1.setViewportView(lbTree);

        lblRunningTime.setText("Running Time:");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 451, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblRunningTime, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(lblRunningTime)
                        .addContainerGap())
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 275, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable lbTree;
    private javax.swing.JLabel lblRunningTime;
    // End of variables declaration//GEN-END:variables
}
