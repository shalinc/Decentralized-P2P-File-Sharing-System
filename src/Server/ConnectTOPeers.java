package Server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectTOPeers implements Runnable 
{
	private ConcurrentHashMap<String, Socket> socketMapping;
	private String serverName;
	private String serverHostAddress;
	private int serverPort;
	private Socket socket;

	public ConnectTOPeers(int serverPort, String serverName, String serverHostAddress,
			ConcurrentHashMap<String, Socket> socketMapping) 
	{
		// TODO Auto-generated constructor stub
		this.socketMapping = socketMapping;
		this.serverPort = serverPort;
		this.serverName = serverName;
		this.serverHostAddress = serverHostAddress;
	}

	/**
	 * @param args
	 */
	public void run()
	{
		try
		{
			//System.out.println("Before Sleep");
			Thread.sleep(40000);
			socket = new Socket(serverHostAddress, serverPort);
			//System.out.println("\nClient Connected to "+serverName);
		}catch(IOException | InterruptedException e)
		{
			e.printStackTrace();
		}
		//System.out.println(serverName+" "+socket);
		socketMapping.put(serverName, socket);
	}
}
