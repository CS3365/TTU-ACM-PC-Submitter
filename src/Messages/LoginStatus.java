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
import PCS.Team;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This class is a response from the server after receiving a LoginAttempt.
 * It should tell the client the status of the LoginAttempt.
 *
 * @see LoginAttempt
 *
 * @author Mike Kent
 */
public class LoginStatus implements Message {
	/**
	 * The types of responses the server could send to a login request.
	 */
	public enum LoginResponse {
		LOGIN_SUCCESS, ALREADY_LOGGED_IN, LOGIN_FAILURE
	};

	private String message;
	private LoginResponse response;
  private Collection<String> languages;
  private Team team;

	/**
	 * Create a new LoginStatus message.
	 *
	 * @param status The status of the login attempt.
	 */
	public LoginStatus(LoginResponse response, Collection<String> languages,
      Team team)
  {
		this(response,languages,team,"");
	}

	/**
	 * Create a new LoginStatus response with a specific message.
	 *
	 * @param status The status of the login attempt.
	 * @param message The message to send to clients.
	 */
	public LoginStatus(LoginResponse response, Collection<String> languages,
      Team team,String message)
  {
		this.message = message;
    // ArrayList is serializable
    this.languages = new ArrayList(languages);
		this.response = response;
    this.team = team;
	}

	/**
	 * Get the message.
	 *
	 * @return The message (if one was sent).
	 */
	public String message() {
		return message;
	}

	/**
	 * Gets the response type.
	 *
	 * @return The response.
	 */
	public LoginResponse getResponse() {
		return response;
	}

  /**
   * Get the languages allowed by the server.
   * @return The list of allowed languages.
   */
  public Collection<String> getLanguages() {
    return languages;
  }


  /**
   * Get the Team that this login is associated with (or user in the case of an
   * admin)
   * @return The team this login allows.
   */
  public Team getTeam() {
    return team;
  }
}
