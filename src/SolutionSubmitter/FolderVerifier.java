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
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TreeSet;

/**
 *
 * @author Mike Kent
 */
public class FolderVerifier {

  public static FolderDigest getFolderDigest(File folder)
      throws FileNotFoundException, IOException
  {
    byte[] digest = getDigest(folder);
    if(digest == null) {
      return null;
    }
    return new FolderDigest(digest);
  }

  public static byte[] getDigest(File folder)
      throws FileNotFoundException, IOException
  {
    // TreeSet should ensure that files are listed in same order even on
    // multiple systems.
    TreeSet<File> files = gatherFiles(folder);
    try {
      MessageDigest digest = MessageDigest.getInstance("MD5");
      byte[] buffer = new byte[256];
      for(File f : files) {
        InputStream in = new FileInputStream(f);
        while(in.read(buffer) > 0) {
          digest.update(buffer);
        }
        in.close();
      }
      return digest.digest();
    } catch(NoSuchAlgorithmException ex) {
      System.out.println("There is no such algorithm.");
      ex.printStackTrace();
      return null;
    }
  }

  public static boolean compareDigests(byte[] digest1, byte[] digest2) {
    for(int i=0; i<digest1.length; i++) {
      if(digest1[i] != digest2[i]) {
        return false;
      }
    }
    return true;
  }

  private static TreeSet<File> gatherFiles(File folder) {
    TreeSet<File> files = new TreeSet<File>();
    File[] subfiles = folder.listFiles();
    // go through subfiles, add if file, add all of another gatherFiles if folder
    for(File f : subfiles) {
      if(f.isFile()) {
        files.add(f);
      } else if(f.isDirectory()) {
        files.addAll(gatherFiles(f));
      }
    }
    return files;
  }
}
