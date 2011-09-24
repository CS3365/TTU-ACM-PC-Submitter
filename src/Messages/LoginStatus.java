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
 *
 * @author Mike Kent
 */
public class LoginStatus implements Message {
	/**
	 * The types of responses the server could send to a login request.
	 */
	public enum LoginResponse {
		LOGIN_SUCCESS, ALREADY_LOGGED_IN, LOGIN_FAILURE
	}

	private LoginStatus status;
	private String message;

	/**
	 * Create a new LoginStatus message.
	 * @param status The status of the login attempt.
	 */
	public LoginStatus(LoginStatus status) {
		this(status,"");
	}

	/**
	 * Create a new LoginStatus response with a specific message.
	 * @param status The status of the login attempt.
	 * @param message The message to send to clients.
	 */
	public LoginStatus(LoginStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	/**
	 * Get the status of the login attempt.
	 * @return The status of the login attempt.
	 */
	public LoginStatus getStatus() {
		return status;
	}

	/**
	 * Get the message.
	 * @return The message (if one was sent).
	 */
	public String message() {
		return message;
	}
}
