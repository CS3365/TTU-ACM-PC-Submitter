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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Mike Kent
 */
public class FileSplitter {

	private FileInfo info;
	private File file;
	private int numParts, curPart;
	private long curLocation;
	private FileInputStream in;

	public FileSplitter(FileInfo info) throws FileNotFoundException {
		this.info = info;
		file = new File(info.getSourcePath());
		if(!file.exists()) {
			throw new FileNotFoundException();
		}
		numParts = (int)file.length()/FilePart.PART_SIZE;
		curPart = 0;
		curLocation = 0l;
		in = new FileInputStream(file);
	}

	/**
	 * Get the next FilePart of the specified file
	 *
	 * @return The next FilePart of the specified or null if there is a read
	 * error.
	 */
	public FilePart next() throws IOException {
		if(!hasNext()) {
			return null;
		}
		FilePart part = new FilePart(info.clone(), curLocation, curPart++, numParts);
		//set data length and read data into part.data
		int len = in.read(part.getData());
		part.setDataLength(len);
		curLocation += len<0 ? 1 : len;
		return part;
	}

	public boolean hasNext() {
		return curLocation <= file.length();
	}
}
