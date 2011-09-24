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

package SolutionSubmitter;

import NetworkIO.Message;

/**
 * This class represents a file. It transmits the file path relative to some
 * specified folder. This allows the transmission of single files and full
 * directories without compressing them into a single file and then extracting
 * it on the server.
 *
 * @author Mike Kent
 */
public class FileInfo implements Message {
	private String fileName;
	private String relativePath;
	// The path to the file on the originating computer
	private String sourcePath;

	/**
	 * Create a new FileInfo with the specified information.
	 *
	 * @param name The name of the file.
	 * @param rel The relative path of a file.
	 * @param real The full path of the file on the client machine.
	 */
	public FileInfo(String name, String rel, String real) {
		fileName = name;
		relativePath = rel;
		sourcePath = real;
	}

	/**
	 * Get the path of a file relative to some folder.
	 *
	 * @return The relative path.
	 */
	public String getRelativePath() {
		return relativePath;
	}

	/**
	 * Get the original full path of the file on the client machine.
	 *
	 * @return The full path of the file.
	 */
	public String getSourcePath() {
		return sourcePath;
	}
}
