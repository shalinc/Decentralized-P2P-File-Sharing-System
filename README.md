# Decentralized-P2P-File-Sharing-System
A Decentralized file sharing system using distributed hash table (DHT) and file replication

<h4><strong>INTRODUCTION:</strong></h4>
	
This implementation is Java based Simple Decentralized P2P file sharing system. Unlike Centralized File Sharing system, 
here there is no centralized Index Server. We have Peers, i.e. both acting as Server as well as Client.
Each of these peers has its Hash table which stores Key/Value (Filename/ PeerID) pairs. 
The components viz. Decentralized Indexing Server, its Peer and its DHT, works as follows:
<ol> 
<li><strong>Decentralized Indexing Server</strong>
		This server indexes the contents of all of the peers that register with it. It also provides search facility to peers, so that peer can interact with others to obtain a file from other peers. Implements registry(peerID, Filename) & search()</li>

<li><strong>A Peer</strong>
		The peer acts as a server and client both. As a Client, it provides interfaces through which users can register a file and view search results where the server returns a list of all other peers that hold the file. The user can pick one such peer and the client then connects to this peer and downloads the file. As a Server, it accepts queries from other peers and sends the requested file when receiving a request.</li>

<li><strong>Distributed Hash Table (DHT)</strong>
		A DHT is a hash table which store values in form of Key/Value pairs. The hash table stores these values into buckets, which is computed by using a hash function. The hash function computes the hash value for the Key and allocates it to a particular bucket in table. In this assignment, each Peer has its own Hash table. The Key/ Value pairs are Filename/ PeerID.</li>
</ol>

<h4><strong>BEFORE EXECUTION:</strong></h4>
<ol>
<li>Edit ConfigureServer.java file,  to change the path according to the system to read the config.xml file, line (51)</li>
<li>Change the config.xml file to change the ServerIP tag to IP address you want.</li>
<li>Edit DHTClient.java file, to change the path according to the system where the p2pSharedFolder will be kept, line (26)</li>
<li>Edit ServerSideImplementation.java file, to change the path according to the system where the p2pSharedFolder will be kept, line (24)</li>
<li>Copy p2pSharedFolder folder in above path, so that Obtain function can work efficiently</li>
</ol>
<h4><strong>STEPS FOR EXECUTION:</strong></h4>

<strong><h5>Using Command Prompt/Terminal:</h5></strong>
1.	Extract the zip file</br>
2.	Open Cmd</br>
3.	Goto path \Decentralized-P2P-File-Sharing-System\src\Server\ </br>
4.	Run the cmd: javac *.java</br>
5.	Now type cd . .</br>
6.	You will be in the path: \Decentralized_p2p_FileSharingSystem \src\ </br>
7.	Now to run <strong>server</strong> code: java Server.ConfigureServer server0 </br> 
(name of the server from 0 to 7, there is no space between server and its number ex. server1)</br>
Pass the servers as the cmd line argument</br>
8.	Repeat step 7, for all the 8 Servers connected to each other.</br>
