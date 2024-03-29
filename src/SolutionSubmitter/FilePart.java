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
 * This class contains a part of a file and can be transmitted through the
 * network.
 *
 * @author Mike Kent
 */
public class FilePart implements Message {
	public static final int PART_SIZE = 256;
	private byte[] data;
	private FileInfo fileInfo;
	// The actual length of data[] - useful in eof case
	private int dataLength, partNumber, totalParts;
	private long dataLocation;

	/**
	 * Creates a new FilePart for a file with no initial data.
	 *
	 * @param info The information of the file.
	 * @param location The location of data relative to the beginning of the file.
	 * @param index The order of the FilePart.
	 * @param total The total number of parts for this file.
	 */
	public FilePart(FileInfo info, long location, int index, int total) {
		dataLocation = location;
		fileInfo = info;
		data = new byte[PART_SIZE];
		dataLength = PART_SIZE;
		partNumber = index;
		totalParts = total;
	}

	/**
	 * Gets the data for the file.
	 *
	 * @return The data for the file.
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Gets the info of the file.
	 *
	 * @return The info of the file.
	 */
	public FileInfo getInfo() {
		return fileInfo;
	}

	/**
	 * Gets the length of data
	 * @return The length of data in bytes.
	 */
	public int length() {
		return dataLength;
	}

	/**
	 * Sets the number of bytes of actual data to write.
	 *
	 * @param length The number of bytes in data that are actually part of the
	 * file.
	 */
	public void setDataLength(int length) {
		dataLength = length;
		if(dataLength < PART_SIZE) {
			if(dataLength <= 0) {
				data = new byte[0];
			} else {
				byte[] newData = new byte[dataLength];
				for(int i=0;i<dataLength;i++) {
					newData[i] = data[i];
				}
				data = newData;
			}
		}
	}

	/**
	 * Gets the order of the this part.
	 * @return The order of this part.
	 */
	public int getPartNumber() {
		return partNumber;
	}

	/**
	 * Get the total number of parts for this file.
	 * @return The total number of parts for this file.
	 */
	public int getTotalParts() {
		return totalParts;
	}

	/**
	 * Get the location where the data starts relative to the beginning of the
	 * file.
	 * @return The location of the beginning of the file.
	 */
	public long getLocation() {
		return dataLocation;
	}
}
