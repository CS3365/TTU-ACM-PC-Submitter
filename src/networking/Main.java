/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package networking;

import NetworkIO.ClientBase;
import NetworkIO.ServerBase;
import SolutionSubmitter.FileInfo;
import SolutionSubmitter.FolderSaver;
import SolutionSubmitter.FolderSender;
import java.io.File;

/**
 *
 * @author mike
 */
public class Main {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws Exception {
		//testServerClient();
		//testFileSplitter();
		testFolderSenderSaver();
	}

	public static void testFolderSenderSaver() throws Exception {
		File clientdir = new File("Test Data/Client Test Data");
		File serverdir = new File("Test Data/Server Test Data");
		ServerBase server = new ServerBase(1029);
		ClientBase client = new ClientBase("localhost",1029);
		FolderSaver saver = new FolderSaver(serverdir);
		server.addNetworkListener(saver);
		FolderSender sender = new FolderSender(client,clientdir);
		sender.start();
	}

	public static void testFileSplitter() throws Exception {
		File dir = new File(".");
		System.out.println(dir.getAbsolutePath());
		File f = new File("Test Data/Client Test Data");
		System.out.println(f.getAbsolutePath());
		if(f.exists())
			System.out.println("exists");
		else
			System.out.println("error");
		FileInfo info = new FileInfo(f.getName(),f.getAbsolutePath(),
				f.getAbsolutePath().substring(dir.getAbsolutePath().length()-1));
		System.out.println(info.getRelativePath());
		System.out.println(info.getSourcePath());
		ServerBase server = new ServerBase(1029);
		ClientBase client = new ClientBase("localhost",1029);
		FolderSender sender = new FolderSender(client,f);
		sender.start();
	}

	public static void testServerClient() throws Exception {
		pl("Creating Server...");
		ServerBase server = new ServerBase(1029);
		pl("Creating client and connecting to server");
		ClientBase client = new ClientBase("localhost",1029);
		pl("Registering listeners...");
		TestListener serverListener = new TestListener("server"),
						clientListener = new TestListener("client");
		server.addNetworkListener(serverListener);
		client.addNetworkListener(clientListener);
		pl("Sending message");
		client.send(new StringMessage("test message!!!"));
		server.sendToAll(new StringMessage("got it!!!"));
	}

	public static void pl(String msg) {
		System.out.println(msg);
	}
}
