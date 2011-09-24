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

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Mike Kent
 */
public class Leaderboard {
	public Map<Team,Map<Problem,Score>> scores;

	public Leaderboard() {
		scores = new TreeMap<Team,Map<Problem,Score>>();
	}

	private Leaderboard(Map<Team,Map<Problem,Score>> prevScores) {
		scores = prevScores;
	}

	/**
	 * Get a version of the leaderboard with only the specific attempt information
	 * for one team, but the score amounts for the rest of the teams.
	 * @param team The team to include all attempt information for.
	 * @return The abbreviated leaderboard.
	 */
	public Leaderboard cloneForTeam(Team team) {
		TreeMap<Team,Map<Problem,Score>> newLeaderboard =
				new TreeMap<Team,Map<Problem,Score>>();
		TreeMap<Problem,Score> newScores;
		for(Team t : scores.keySet()) {
			if(t == team) {
				newScores = (TreeMap)scores.get(t);
			} else {
				newScores = new TreeMap<Problem,Score>();
				Map<Problem,Score> origScores = scores.get(t);
				for(Problem p : origScores.keySet()) {
					newScores.put(p, new Score(origScores.get(p)));
				}
			}
			newLeaderboard.put(t, newScores);
		}
		return new Leaderboard(newLeaderboard);
	}

	/**
	 * Get the scores for a team.
	 * @param team The team to obtain scores for.
	 * @return The scores for that team.
	 */
	public Map<Problem,Score> getTeamScores(Team team) {
		return scores.get(team);
	}
}
