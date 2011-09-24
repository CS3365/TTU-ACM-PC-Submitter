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

import NetworkIO.ClientBase;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Mike Kent
 */
public class FolderSender extends Thread {
	private ClientBase client;
	private File folder;
	private ArrayList<File> files;

	public FolderSender(ClientBase client, File folder) {
		this.client = client;
		this.folder = folder;
		files = new ArrayList<File>();
		gatherFileList(folder);
	}

	private void gatherFileList(File folder) {
		File[] subfiles = folder.listFiles();
		for(File file : subfiles) {
			if(file.isDirectory()) {
				gatherFileList(file);
			} else {
				files.add(file);
			}
		}
	}

	@Override
	public void run() {
		for(File file : files) {
			try {
				FileSplitter splitter = new FileSplitter(
						new FileInfo(file.getName(),
							getRelativePath(file),
							file.getAbsolutePath()));
				while(splitter.hasNext()) {
					client.send(splitter.next());
				}
			} catch(FileNotFoundException ex) {
				System.out.println("Could not find file: "+file.getAbsolutePath());
			} catch(IOException ex) {
				System.out.println("Error sending a part of the file: "+
						file.getAbsolutePath());
			}
		}

	}

	private String getRelativePath(File file) {
		return file.getAbsolutePath().substring(
				folder.getAbsolutePath().length()+1);
	}
}
