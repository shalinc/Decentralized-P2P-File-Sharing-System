package Server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigureServer 
{
	//Attributes for Config.xml file
	private static String serverName;		/*Contains the Server Name*/
	private static int serverPort;			/*Contains the PORT Number for the Server*/
	private static String serverHostAddress;/*Contains the IP Address*/

	//Variables for fetching data from XML file
	private static Element element;
	private static Node nNode;

	//condition variables
	private static boolean asAServer;
	private static boolean asAClient;

	//Socket constructs
	private static ConcurrentHashMap<String, Socket> socketMapping 
	= new ConcurrentHashMap<String, Socket>();		/*Mapping the ServerName and its Socket*/

	private static ServerSocket serverSocket;
	private static Socket clientSocket;

	public static String serverArgs;
	
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		
		try
		{
			//Change the path here for config file
			//Load the configuration file CONFIG.XML has information about all the servers
			File configFile = new File("config.xml");
			serverArgs = args[0];
			
			System.out.println("****************************");
			System.out.println("\t"+serverArgs);
			System.out.println("****************************");

			//File configFile = new File("C:\\Users\\USER\\Desktop\\PROG2_CHOPRA_SHALIN\\SimpleDistributedHashTable\\src\\config.xml");	
			//Parsing the DOM tree for XML file
			DocumentBuilderFactory docbldFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docbldFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(configFile);

			//normalize the DOM tree
			doc.getDocumentElement().normalize();

			//get the list of servers and their information from XML <Servers> tag
			NodeList nodeList = doc.getElementsByTagName("Servers");

			//Repeat for all the servers in the config.xml file (In our case 8 Servers)
			for (int i = 0; i < nodeList.getLength(); i++) 
			{
				nNode = nodeList.item(i);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) 
				{
					element = (Element) nNode;

					//get all the attributes for each of the servers
					serverName = element.getElementsByTagName("ServerName").item(0).getTextContent();  
					serverPort = Integer.parseInt(element.getElementsByTagName("ServerPort").item(0).getTextContent());
					serverHostAddress = element.getElementsByTagName("ServerIP").item(0).getTextContent();

					//check whether the Command Line Args is same as ServerName,
					//if YES, make it as a server
					if(serverName.equalsIgnoreCase(serverArgs))
					{
						asAServer = true;
						asAClient = false;
						//System.out.println("ServerName: "+serverName+" ServerPort: "+serverPort+" ServerIP: "+serverHostAddress+"\n");

						if(asAServer)
						{
							try 
							{
								//create a server socket
								serverSocket = new ServerSocket(serverPort);

								
								//create a thread for Client and start the client
								Thread clientThread = new Thread(new DHTClient(socketMapping));
								clientThread.start();
								
								/*For Performance Evaluation thread to run Uncomment this and Comment above thread*/
								/*Thread PerfEvlThread = new Thread(new DHTPerformanceEvaluation(socketMapping));
								PerfEvlThread.start();*/
								

							} catch (IOException /*| InterruptedException*/ e) 
							{
								e.printStackTrace();
							}
						}
					}
					else 
					{
						asAServer = false;
						asAClient = true;

						if(asAClient)
						{
							Thread connectServerThread = new Thread(new ConnectTOPeers(serverPort,serverName,serverHostAddress,socketMapping));
							connectServerThread.start();
						}	
					}
				}
			}//end for()

			while(!serverSocket.isClosed())
			{
				//System.out.println("Server waiting for client to accept\n");
				clientSocket = serverSocket.accept();

				//start the server side thread
				Thread serverSide = new Thread(new ServerSideImplementation(clientSocket));
				serverSide.start();
				//System.out.println(serverName +" accepted Connection\n");
			}

			//call ClientSide Thread DHTClient.java
			Thread clientMenu = new Thread(new DHTClient(socketMapping));
			clientMenu.start();
			

		}catch(IOException | ParserConfigurationException | SAXException e)
		{
			e.printStackTrace();
		} 
	}
}
