package Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class CreaterRandomFiles 
{

	public static void main(String args[]) throws IOException
	{
		
		/*for(int i=200000;i<210000;i++)
		{
			File file = new File("/home/shalin/p2psharedFolder/peer1/"+i);
			RandomAccessFile rndmAccFile = new RandomAccessFile(file, "rw");
			rndmAccFile.setLength(10000);
			rndmAccFile.close();
		}
		
		for(int i=300000;i<310000;i++)
		{
			File file = new File("/home/shalin/p2psharedFolder/peer2/"+i);
			RandomAccessFile rndmAccFile = new RandomAccessFile(file, "rw");
			rndmAccFile.setLength(10000);
			rndmAccFile.close();
		}
*/		
		for(int i=400000;i<410000;i++)
		{
			File file = new File("/home/shalin/p2psharedFolder/peer3/"+i);
			RandomAccessFile rndmAccFile = new RandomAccessFile(file, "rw");
			rndmAccFile.setLength(10000);
			rndmAccFile.close();
		}
		
		/*for(int i=500000;i<510000;i++)
		{
			File file = new File("/home/shalin/p2psharedFolder/peer4/"+i);
			RandomAccessFile rndmAccFile = new RandomAccessFile(file, "rw");
			rndmAccFile.setLength(10000);
			rndmAccFile.close();
		}
		
		for(int i=600000;i<610000;i++)
		{
			File file = new File("/home/shalin/p2psharedFolder/peer5/"+i);
			RandomAccessFile rndmAccFile = new RandomAccessFile(file, "rw");
			rndmAccFile.setLength(10000);
			rndmAccFile.close();
		}
		
		for(int i=700000;i<710000;i++)
		{
			File file = new File("/home/shalin/p2psharedFolder/peer6/"+i);
			RandomAccessFile rndmAccFile = new RandomAccessFile(file, "rw");
			rndmAccFile.setLength(10000);
			rndmAccFile.close();
		}
		*/
		for(int i=800000;i<810000;i++)
		{
			File file = new File("/home/shalin/p2psharedFolder/peer7/"+i);
			RandomAccessFile rndmAccFile = new RandomAccessFile(file, "rw");
			rndmAccFile.setLength(10000);
			rndmAccFile.close();
		}
	}
}
