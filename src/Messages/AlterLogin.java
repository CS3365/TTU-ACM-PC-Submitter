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

import Messages.LoginAttempt.LoginType;
import NetworkIO.Message;

/**
 * This class is intended to ask the server to alter a login credential in some
 * way.
 *
 * @author Mike Kent
 */
public class AlterLogin implements Message {

	/**
	 * The type of alteration to a login to be performed.
	 */
	public enum AlterAction {
		CREATE, DELETE, CHANGE_PASSWORD, DISQUALIFY, CLEAR_MAC, LOGOUT
	}

	private LoginType type;
	private AlterAction action;
	private String name, password;

	/**
	 * Create a new AlterLogin to alter a specific login.
	 *
	 * Note: the password will only be used in the case of type =
	 * AlterAction.CHANGE_PASSWORD
	 *
	 * @param type The type of login that is being altered.
	 * @param action The alteration to be performed
	 * @param name The name of the login account.
	 * @param password The password to set to the account. This will only be
	 * used in the case of type = LoginType.ADMIN, action =
	 * AlterAction.CHANGE_PASSWORD and the computer is currently making the
	 * request logged in as the user attempting to be changed.
	 */
	public AlterLogin(LoginType type, AlterAction action, String name,
			String password) {
		this.type = type;
		this.action = action;
		this.name = name;
		this.password = password;
	}

	/**
	 * Get the type of login.
	 *
	 * @return The type of login.
	 */
	public LoginType getType() {
		return type;
	}

	/**
	 * Get the type of action.
	 *
	 * @return The type of action.
	 */
	public AlterAction getAction() {
		return action;
	}

	/**
	 * Get the name of the team or admin to change.
	 *
	 * @return The username to alter.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the password.
	 *
	 * @return The password specified.
	 */
	public String getPassword() {
		return password;
	}
}
