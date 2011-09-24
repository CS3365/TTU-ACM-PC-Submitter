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
import PCS.Leaderboard;

/**
 * This class is used to update the clients as to the status of the leaderboard.
 * It can also be used to send the specifics about a particular team's attempt
 * status. The difference will lie in exactly what information is given in the
 * Leaderboard class.
 *
 * @author Mike Kent
 */
public class LeaderboardUpdate implements Message {
	private Leaderboard leaderboard;

	/**
	 * Create a new LeaderboardUpdate message to send to clients.
	 * @param leaderboard The updated Leaderboard.
	 */
	public LeaderboardUpdate(Leaderboard leaderboard) {
		this.leaderboard = leaderboard;
	}

	/**
	 * Get the leaderboard.
	 * @return The leaderboard.
	 */
	public Leaderboard getLeaderboard() {
		return leaderboard;
	}
}
