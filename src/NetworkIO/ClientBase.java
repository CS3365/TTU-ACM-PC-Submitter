/*
 * ClientBase.java
 *
 * Created on August 4, 2005, 10:50 PM
 */

package NetworkIO;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;


/**
 * ClientBase maintains a connection with a server of some kind.  It will not allow
 * for any connections to be made to it, nor is it able to connect to more than one
 * server at a time.
 * @author Michael Kent
 */
public class ClientBase extends Thread {
	
	protected NetworkLogger logger = new NetworkLogger("ClientBase");
	protected Socket connection;
	protected HashSet<NetworkListener> networkListeners = new HashSet<NetworkListener>();
	protected HashSet<ConnectionListener> connectionListeners = new HashSet<ConnectionListener>();
	protected ObjectInputStream input;
	protected ObjectOutputStream output;
	protected boolean connected;
	//boolean sendingInput;
	
	/**
	 * Creates a new instance of ClientBase using a String host and int port
	 * to create a socket connection.
	 * @param host The host name of a computer running a valid server.
	 * @param port The port of the computer to connect to.
	 * @throws Exception Any exception that occurs while creating a socket and registering a connection.
	 */
	public ClientBase(String host, int port) throws IOException {
		connection = connect(host,port);
		getStreams();
		setPriority(Thread.MIN_PRIORITY);
		start();
	}
	
	/**
	 * Creates a new instance of ClientBase using a pre-defined Socket
	 * to create a connection.
	 * @param socket The connected socket to another computer.
	 * @throws Exception Any exception occurring while registering a connection.
	 */
	public ClientBase(Socket socket) throws IOException {
		connection = socket;
		getStreams();
		setPriority(Thread.MIN_PRIORITY);
		start();
	}
	
	/**
	 * Creates and returns a socket created from the received host and port.
	 * @return Returns the created Socket.
	 * @param host The host name of the computer running a valid server.
	 * @param port The port of the computer to connect to.
	 * @throws Exception Any exception that occurred while trying to establish a connection to the serving
	 * computer.
	 */
	protected Socket connect(String host, int port) throws IOException {
		return new Socket(InetAddress.getByName(host),port);
	}

	/**
	 * Registers the input and output streams to establish a communication connection.
	 * @throws Exception Any exception occurred while registering input and output streams.
	 */
	protected void getStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		connected = true;
	}
	
	/**
	 * Starts the client's process of receiving Messages and telling NetworkListeners
	 * to process them.
	 */
	public void run() {
		try {
			while(connected) {
				Object o = input.readObject();
				if(o instanceof String)
					logger.log("\tString is: "+(String)o);
				//Message m = (Message)input.readObject();
				Message m = (Message)o;
				Object[] lis = networkListeners.toArray();
				for(int i=0;i<lis.length;i++) {
					((NetworkListener)lis[i]).processInput(m,connection);
				}
			}
		} catch(EOFException e) {
			logger.log("There was an End Of File Exception.");
		} catch(Exception e) {
			logger.log("There was an error while reading an object - "+e.getMessage());
			e.printStackTrace();
		} finally {
			connected = false;
			closeConnection();
		}
	}
	
	private void closeConnection() {
		try {
			for(Iterator itr = connectionListeners.iterator();itr.hasNext();)
				((ConnectionListener)itr.next()).lostConnection(connection);
			logger.log("\tposted all the lost connections.");
			output.close();
			input.close();
			connection.close();
		} catch(Exception e) {
			logger.log("There was an Exception while closing - "+e.getMessage());
		}
	}
	
	/**
	 * Sends the Message to the connected server.
	 * @throws Exception Any exception that occurs while trying to send the message.
	 * @param message The Message to send.
	 */
	public synchronized void send(Message message) throws IOException {
		output.writeObject(message);
	}
	
	/**
	 * Returns the current Socket connection.
	 * @return The current Socket connection.
	 */
	public Socket getSocket() {
		return connection;
	}
	
	/**
	 * Disconnects from the current connected server.
	 */
	public void disconnect() {
		connected = false;
    try {
      connection.close();
    } catch(IOException ex) {
      logger.log("IOException while closing the connection");
      ex.printStackTrace();
    }
	}
	
	/**
	 * Adds a NetworkListener to ClientBase so that any incoming connection will be
	 * sent to it.
	 * @param listener The NetworkListener to add.
	 */
	public void addNetworkListener(final NetworkListener listener) {
		if(connected)
			networkListeners.add(listener);
	}

  /**
   * Removes the NetworkListener if possible.
   * @param listener The NetworkListener to remove.
   */
  public void removeNetworkListener(final NetworkListener listener) {
    if(networkListeners.contains(listener)) {
      networkListeners.remove(listener);
    }
  }
	
	/**
	 *Adds a ConnectionListener to Client base in case of a lost connection.
	 *@param listener The ConnectionListener to add.
	 */
	public void addConnectionListener(ConnectionListener listener) {
		if(connected)
			connectionListeners.add(listener);
		else
			listener.lostConnection(connection);
	}

  /**
   * Removes the ConnectionListeners if possible.
   * @param listener The connectionListener to remove.
   */
  public void removeConnectionListener(ConnectionListener listener) {
    if(connectionListeners.contains(listener)) {
      connectionListeners.remove(listener);
    }
  }
}
