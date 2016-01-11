package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;

public class ServerSideImplementation implements Runnable
{
	private Socket clientSocket;
	public static ConcurrentHashMap<String, ArrayList<String>> distributedHashTable = new ConcurrentHashMap<String, ArrayList<String>>();
	private boolean loop = true;
	
	//Make a Change of File Path here for the Obtain Method
	public static String PATH_OF_FILE = "G:/p2pSharedFolder/";

	public ServerSideImplementation(Socket clientSocket) 
	{
		// TODO Auto-generated constructor stub
		this.clientSocket = clientSocket;
	}

	public void run()
	{
		try
		{
			//for communication over sockets between Client and server
			DataInputStream dIn = new DataInputStream(clientSocket.getInputStream());
			DataOutputStream dOut = new DataOutputStream(clientSocket.getOutputStream());

			while(loop)
			{
				try
				{
					//read the choice according to MENU
					String choice = dIn.readUTF();

					switch(choice)
					{
					case "1":

						//Perform REGISTRY Operation
						//read the key value pair from client
						String readKeyValue = dIn.readUTF();

						//split the value from Key and Value by ";"
						String[] hashKeyValue = readKeyValue.split(";");
						//System.out.println(hashKeyValue);
						boolean resultofPut = reigstry(hashKeyValue[0],hashKeyValue[1]);

						//write the result from registry
						dOut.writeUTF(String.valueOf(resultofPut));
						break;

					case "2":	

						//Perform SEARCH Operation

						String key = dIn.readUTF();

						try
						{
							List<String> value = search(key);	//results where file is present
							String searchDetails="";

							//Filename, with multiple locations
							ListIterator<String> iterator = value.listIterator();
							while(iterator.hasNext())
							{
								searchDetails+=iterator.next()+"\n";
							}

							String sendDetails = "The file is present with these Peers: "+searchDetails;
							dOut.writeUTF(sendDetails);	//send the locations where file can be found
							dOut.flush();

						}
						catch(Exception e)
						{
							dOut.writeUTF("File is not Registered");
							System.out.println("File is not Registered");
							break;
						}
						break;

					case "3":	//Perform OBTAIN Operation

						//get the filename to be obtained from the client
						String fileName = dIn.readUTF();

						//call to obtain the file
						fileObtain(fileName,clientSocket);
						break;

					case "4":	//Exit

						System.out.println("Client Disconnected");
						loop = false;	//exit from the while(true) loop
						//System.exit(0);
						break;
					}

				}catch(IOException e)
				{
					//e.printStackTrace();
					//System.out.println("Server Disconnected");
					break;
				}
			}//end of while
		}
		catch(IOException e)
		{
			//e.printStackTrace();
		}
	}


	/*
	 * Search(): to search for Filename & its location(s) 
	 */
	public static List<String> search(String fileName) throws IOException
	{
		List <String> filePeers = new ArrayList<String>();

		fileName = fileName.substring(0,23).replace("*","");
		filePeers = distributedHashTable.get(fileName);	//get the Value(s) i.e location, for the Key(Filename)

		return filePeers;
	}

	/*
	 * Registry(): to register the files requested by the client
	 * Update in DHT the values
	 */
	public static synchronized boolean reigstry(String key, String value)
	{
		//obtain the actual key by removing the padded values from the Key & value
		key = key.substring(0,23).replace("*","");
		value = value.substring(0,999).replace("*", "");

		//create temporary lists to store the new PeerID's for the files
		ArrayList<String> peers = new ArrayList<String>();
		ArrayList<String> check = new ArrayList<String>();

		//add peers to the list
		peers.add(value);

		String fileName = key;

		check = distributedHashTable.get(key);	//get the value(PeerID) for the Key(Filename)

		//check if file is already registered by the same peer, if not register it
		if(check == null || check.isEmpty())
		{
			distributedHashTable.put(fileName, peers);
			System.out.println("Registered "+key+ " Successfully");
		}
		else	//already Registered checking ...
		{
			Iterator<String> iterator = check.listIterator();

			while(iterator.hasNext())
			{
				String chkPid = iterator.next();

				if(chkPid.equals(value))
				{
					//System.out.println("Already Registered !!!");
					return true;
				}
			}

			//add new PeerID to existing FileName
			check.add(value);
			distributedHashTable.put(fileName,check);

			//FOR DOCUMENTATION PURPOSE DISPLAYING THE DHT	--- will be commented while running in actual
			//System.out.println("The Distributed Hash Table is now: "+distributedHashTable);
			
		}
		//if put into hashtable is successful then true
		return true;
	}

	/*
	 * obtain(): to download file, requested by the Client, to its specified location
	 */
	public static void fileObtain(String fileName, Socket clientSocket)
	{
		//System.out.println("Client connected for File sharing ...");
		try
		{
			DataOutputStream dOut = new DataOutputStream(clientSocket.getOutputStream());
			//DataInputStream dIn = new DataInputStream(clientSocket.getInputStream());

			String peerName = "peer"+(ConfigureServer.serverArgs.substring(ConfigureServer.serverArgs.length()-1));
			
			//System.out.println("Requested file is: "+fileName);
			//String peerForFile = dIn.readUTF();

			//The Path of the file to be downloaded
			File checkFile = new File(PATH_OF_FILE + peerName +"/" + fileName);

			//creating intput streams & buffer streams for reading
			FileInputStream fin = new FileInputStream(checkFile);
			BufferedInputStream buffReader = new BufferedInputStream(fin);

			//check if the file exists, for it to be downloaded
			if (!checkFile.exists()) 
			{
				System.out.println("File doesnot Exists");
				buffReader.close();
				return;
			}

			//get the file size
			int size = (int)checkFile.length();	//convert from long to int

			byte[] buffContent = new byte[8192];

			//send file size
			dOut.writeLong(size);

			//allocate a buffer to store contents of file

			int numOfRead = -1;	//how much is read in each read() call

			BufferedOutputStream buffOut = new BufferedOutputStream(clientSocket.getOutputStream());

			//read the bytes from file in chunks of 8192, until End of File Stream (-1) is not reached
			while((numOfRead = buffReader.read(buffContent)) != -1)
			{
				//write to client in chunks of 8192
				dOut.write(buffContent, 0, numOfRead);
			}

			System.out.println("Transferring File SUCCESS !!!");
			buffReader.close();
		}
		catch(IOException ex)
		{
			System.out.println("Exception in file sharing");
		}
	}
}