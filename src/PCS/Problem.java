/*
 *  The MIT License
 * 
 *  Copyright 2011 TTU ACM.
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
import java.util.ArrayList;

/**
 *
 * @author Mike Kent
 */
public class Problem implements Comparable<Problem>, Message {
	private String problemTitle, description;
  private int order;
  private int pointValue;
  private int phase;

  public Problem(String name) {
    this(name, "", 0, 0, 0);
  }

	public Problem(String name, String description, int order, int value,
      int phase) 
  {
		problemTitle = name;
    this.description = description;
    this.order = order;
    pointValue = value;
    this.phase = phase;
	}

  /**
   * Gets the title of the problem.
   *
   * @return The problem title.
   */
  public String getProblemTitle() {
    return problemTitle;
  }

  /**
   * Gets the order in which this problem should be displayed.
   *
   * @return The order of the problem;
   */
	public int getProblemOrder() {
		return order;
	}

  public int getPointValue() {
    return pointValue;
  }

  public String getDescription() {
    return description;
  }

  public int getPhase() {
    return phase;
  }

  @Override
  public boolean equals(Object o) {
    if(o instanceof Problem) {
      Problem p = (Problem)o;
      return p.description.equals(this.description) &&
          p.order == this.order &&
          p.pointValue == this.pointValue &&
          p.problemTitle == this.problemTitle;
    }
    return false;
  }

  /**
   * Compares the order of two problems to each other.
   *
   * @param p The other problem.
   * @return The order of this problem minus that of the other problem.
   */
	public int compareTo(Problem p) {
		return getProblemOrder() - p.getProblemOrder();
	}

  /**
   * Print the title of this problem.
   * @return The title of the problem.
   */
  @Override
  public String toString() {
    return problemTitle;
  }
}
