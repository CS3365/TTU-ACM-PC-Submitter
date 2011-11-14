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
import java.util.HashMap;

/**
 * This class is designed to save files sent from either the server or client
 * into a specified folder. The files are saved in the same relative path as
 * they were sent from the other machine.
 *
 * @author Mike Kent
 */
public class FolderSaver implements NetworkListener {
	private File folder;
  private HashMap<FileInfo,boolean[]> partsReceived;
  private FolderDigest digest;
  private int transmissionID;
  private SaverHandler callback;

  private enum VerifyStatus{NO_DIGEST, NOT_FINISHED, SUCCESS, FAILURE};

	/**
	 * Create a new FolderSaver. Each machine only needs one FileSaver per
	 * connection to save files. If the folder to save the files to needs to be
	 * changed, then changeFolder() can be used.
	 *
	 * @see FolderSaver#changeFolder(java.io.File)
	 * @param folder
	 */
	public FolderSaver(File folder, int transmissionID, SaverHandler callback) {
		this.folder = folder;
    this.transmissionID = transmissionID;
    this.callback = callback;
    partsReceived = new HashMap<FileInfo,boolean[]>();
	}

	/**
	 * Saves the FileParts sent from other machines.
	 *
	 * @param message The message sent from the other computer.
	 * @param sok The socket associated with the connection.
	 */
	public synchronized void processInput(Message message, Socket sok) {
		if(message instanceof FilePart) {
			FilePart part = (FilePart)message;
			FileInfo info = part.getInfo();
      // only process FileParts that match the transmissionID
      if(info.getTransmissionID() != this.transmissionID) {
        return;
      }
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
        accountForPart(part);
			} catch(FileNotFoundException ex) {
				System.out.println("Could not find file: "+file.getAbsolutePath());
				ex.printStackTrace();
			} catch(IOException ex) {
				System.out.println("Could not write to file: "+file.getAbsolutePath());
				ex.printStackTrace();
			}
		}
    else if(message instanceof FolderDigest) {
      digest = (FolderDigest)message;
    }

    // Attempt to verify the file after every FilePart has been received, this
    // is done order is indeterminate.
    try {
      // tell the callback the folder is finished.
      VerifyStatus status = verifyTransfer();
      if(status == VerifyStatus.SUCCESS) {
        callback.transmissionSuccess(transmissionID);
      } else if(status == VerifyStatus.FAILURE) {
        callback.transmissionFailed(transmissionID);
      }
    } catch(FileNotFoundException ex) {
      System.out.println("There was a FileNotFoundException that occurred "+
          "while attempting to verify the submission transmission.");
      ex.printStackTrace();
    } catch(IOException ex) {
      System.out.println("There was an IOException while attempting to verify"+
          "the submission transmission.");
      ex.printStackTrace();
    }
	}

  /**
   * Accounts for reception of a FilePart.
   *
   * This function will create an entry in partsReceived if none previously
   * existed.
   *
   * @param part The FilePart to account for.
   */
  private synchronized void accountForPart(FilePart part) {
    FileInfo info = part.getInfo();
    if(!partsReceived.containsKey(info)) {
      partsReceived.put(info, new boolean[part.getTotalParts()]);
    }
    partsReceived.get(info)[part.getPartNumber()] = true;
  }

  /**
   * Verifies the transmitted folder for completeness and accuracy.
   *
   * @return True if the FolderDigest has been transmitted, all FileParts have
   * been received for all known files, and the digest matches the local files.
   * @throws FileNotFoundException
   * @throws IOException
   */
  private synchronized VerifyStatus verifyTransfer() throws FileNotFoundException, IOException {
    // Cannot verify transfer without a digest
    if(digest == null) {
      return VerifyStatus.NO_DIGEST;
    }
    // Transfer is incomplete until all the FileParts have been received for
    // all known files.
    for(boolean[] parts : partsReceived.values()) {
      for(int i=0; i<parts.length; i++) {
        if(!parts[i]) {
          return VerifyStatus.NOT_FINISHED;
        }
      }
    }
    // All information needed is available, verify transmission
    if(FolderVerifier.compareDigests(digest.getDigest(),
        FolderVerifier.getDigest(folder))) {
      return VerifyStatus.SUCCESS;
    }
    return VerifyStatus.FAILURE;
  }

  /**
   * This method resets the saver so that it can be ready to receive files
   * again. This is generally only needed in the event of a transmission
   * failed tranmission.
   */
  public void resetSaver() {
    this.digest = null;
    this.partsReceived = new HashMap<FileInfo,boolean[]>();
  }

  /**
   * Get the directory to which files are saved.
   *
   * @return The directory where the files are saved.
   */
  public File getSaveDirectory() {
    return folder;
  }
}
