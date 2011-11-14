/*
 * ServerBase.java
 *
 * Created on August 4, 2005, 11:32 PM
 */

package NetworkIO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * A ServerBase will received, and can make, connections to other servers, or
 * clients that use this NetworkIO package.
 * @author Michael Kent
 */
public class ServerBase extends Thread {
	
	private static int number = 0;
	protected NetworkLogger logger = new NetworkLogger("ServerBase"+number);
	private HashSet<ConnectionListener> connectionListeners;
  private HashSet<NetworkListener> networkListeners;
	protected ServerSocket serverSocket;
	protected HashMap<Socket,ClientBase> connections;
	protected int port;
  private boolean running;
	
	/**
	 * Creates a new instance of ServerBase
	 * @param port The port on which to host this server.
	 */
	public ServerBase(int port) {
		number++;
		this.port = port;
		connectionListeners = new HashSet<ConnectionListener>();
		networkListeners = new HashSet<NetworkListener>();
		connections = new HashMap<Socket,ClientBase>();
		setPriority(Thread.MIN_PRIORITY);
		start();
	}
	
	/**
	 * Receives and processes all incoming connections.
	 */
	public void run() {
    running = true;
		try {
			serverSocket = new ServerSocket(port);
			while(running) {
				Socket s = serverSocket.accept();
				addConnection(new ClientBase(s));
			}
		} catch(Exception e) {
			logger.log("There was an error while accepting a connection - "+e.getMessage());
		}
	}

  /**
   * Stops the server and disconnects all connections.
   */
  public void stopServer() {
    running = false;
    for(ClientBase cb : connections.values()) {
      cb.disconnect();
    }
  }
	
	private synchronized void addConnection(ClientBase cb) {
		connections.put(cb.getSocket(),cb);
		for(Iterator itr = networkListeners.iterator();itr.hasNext();) {
			cb.addNetworkListener((NetworkListener)itr.next());
		}
		ConnectionListener lis;
		for(Iterator itr = connectionListeners.iterator();itr.hasNext();) {
			lis = (ConnectionListener)itr.next();
			cb.addConnectionListener(lis);
			lis.gotConnection(cb.getSocket());
		}
	}
	
	/**
	 * Adds a connection.
	 * @param host The host computer running another server.
	 * @param port The port of the computer on which to connect to.
	 */
	public void addConnection(String host, int port) {
		ClientBase cb = null;
		try {
			cb = new ClientBase(host,port);
		} catch(Exception e) {
			logger.log("There was an Exception while creating a ClientBase(String,int) - "+e.getMessage());
			return;
		}
		addConnection(cb);
	}
	
	/**
	 * Sends a message to a specific connection.
	 * @param message The Message to send.
	 * @param socket The Socket connection to send the Message to.
	 * @throws Exception Any exception that occurs while sending the message.
	 */
	public void send(Message message, Socket socket) throws IOException {
		((ClientBase)connections.get(socket)).send(message);
	}
	
	/**
	 *Returns the ClientBase associated with the Socket connection.
	 *@param socket The Socket associated with the ClientBase.
	 *@return The ClientBase 
	 */
	public ClientBase getClientBase(Socket socket) {
		return (ClientBase)connections.get(socket);
	}
	
	/**
	 * Sends a Message to all connected clients.
	 * @param message The Message to send.
	 * @throws Exception Any exception that occurs while trying to send the Message.
	 */
	public void sendToAll(Message message) throws IOException {
		for(Iterator itr = connections.keySet().iterator();itr.hasNext();) {
			send(message,(Socket)itr.next());
		}
	}
	
	/**
	 * Adds a NetworkListener to process any incomming Messages.
	 * @param listener The NetworkListener to listen to all incomming Messages.
	 */
	public void addNetworkListener(NetworkListener listener) {
		networkListeners.add(listener);
		for(Iterator itr = connections.keySet().iterator();itr.hasNext();) {
			((ClientBase)connections.get(itr.next())).addNetworkListener(listener);
		}
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
	 * Adds a ConnectionListener to listen to any new connections or lost connections.
	 * @param listener The ConnectionListener to listen to any new connections or lost connections.
	 */
	public void addConnectionListener(ConnectionListener listener) {
		connectionListeners.add(listener);
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
