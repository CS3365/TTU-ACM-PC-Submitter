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

/**
 * This class facilitates login attempts by both admin users and teams. It
 * should be responded by the server with a LoginStatus.
 *
 * @see LoginStatus
 *
 * @author Mike Kent
 */
public class LoginAttempt implements Message {
	public enum LoginType {ADMIN, TEAM}

	private LoginType type;
	private String name, password, macAddress;

	/**
	 * Create a new login attempt for an admin.
	 *
	 * @param name The username of the admin.
	 * @param pass The password of the admin.
	 */
	public LoginAttempt(String name, String pass) {
		this(LoginType.ADMIN, name, pass, "");
	}

	/**
	 * Create a new login attempt for a team.
	 *
	 * @param name The name of the team.
	 * @param pass The team's password.
	 * @param mac The MAC address of the computer being logged in from.
	 */
	public LoginAttempt(String name, String pass, String mac) {
		this(LoginType.TEAM, name, pass, mac);
	}

	private LoginAttempt(LoginType type, String name, String pass,
			String mac) {
		this.type = type;
		this.name = name;
		password = pass;
		macAddress = mac;
	}

	/**
	 * Get the login type.
	 *
	 * @return The type of login attempt.
	 */
	public LoginType getType() {
		return type;
	}

	/**
	 * Get the name of the account attempting login.
	 *
	 * @return The name of the account attempting login.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the password of the account attempting login.
	 *
	 * @return The password of the account attempting login.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Get the MAC address of the computer attempting login. This only returns
	 * a meaningful MAC address if a team is attempting to log in.
	 *
	 * @return The MAC address of the computer attempting login if this is a
	 * team login. Returns null otherwise.
	 */
	public String getMACAddress() {
		if(type == LoginType.TEAM)
			return macAddress;
		return null;
	}

  /**
   * Compare LoginAttempts to each other
   *
   * @param attempt The other attempt.
   * @return Returns true if the name, password, and mac addresses all match.
   */
  @Override
  public boolean equals(Object attempt) {
    if(attempt instanceof LoginAttempt) {
      LoginAttempt other = (LoginAttempt)attempt;
      return other.name == this.name &&
          other.password == this.password &&
          other.macAddress == this.macAddress;
    }
    return false;
  }
}
