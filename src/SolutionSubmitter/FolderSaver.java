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
import NetworkIO.NetworkListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;

/**
 * This class is designed to save files sent from either the server or client
 * into a specified folder. The files are saved in the same relative path as
 * they were sent from the other machine.
 *
 * @author Mike Kent
 */
public class FolderSaver implements NetworkListener {
	private File folder;

	/**
	 * Create a new FolderSaver. Each machine only needs one FileSaver per
	 * connection to save files. If the folder to save the files to needs to be
	 * changed, then changeFolder() can be used.
	 *
	 * @see FolderSaver#changeFolder(java.io.File)
	 * @param folder
	 */
	public FolderSaver(File folder) {
		this.folder = folder;
	}

	/**
	 * Saves the FileParts sent from other machines.
	 *
	 * @param message The message sent from the other computer.
	 * @param sok The socket associated with the connection.
	 */
	public void processInput(Message message, Socket sok) {
		if(message instanceof FilePart) {
			FilePart part = (FilePart)message;
			FileInfo info = part.getInfo();
			File file = new File(folder.getAbsolutePath()+"/"+
					part.getInfo().getRelativePath());
			try {
				// make directories and create file if necessary
				if(!file.exists()) {
					file.getParentFile().mkdirs();
				}
				RandomAccessFile out = new RandomAccessFile(file,"rw");
				out.seek(part.getLocation());
				out.write(part.getData());
				out.close();
			} catch(FileNotFoundException ex) {
				System.out.println("Could not find file: "+file.getAbsolutePath());
				ex.printStackTrace();
			} catch(IOException ex) {
				System.out.println("Could not write to file: "+file.getAbsolutePath());
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Changes the folder where the files are to be saved.
	 *
	 * @param folder The new folder to save files to.
	 * @return True if the folder was successfully changed. False otherwise.
	 */
	public boolean changeFolder(File folder) {
		if(folder.isDirectory()) {
			this.folder = folder;
			return true;
		}
		return false;
	}
}
