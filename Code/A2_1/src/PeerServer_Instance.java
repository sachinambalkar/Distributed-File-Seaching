
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PeerServer_Instance implements Runnable
{
      static Map<String, String> mp = new ConcurrentHashMap<String, String>();
      /*mp=> This ConcurrenHashMap store all key-value pair.
       * 	 And provides respective 'VALUE' for particular 'KEY'for 'GET' request.
       */
	  Socket sock;
	  public void set(Socket server){
		  this.sock=server;
	  }
	  
	  public void run()
	  {
		  InputStream is=null;
		  ObjectInputStream ois=null;
		  ObjectOutputStream oos = null;
		try {
			/*
			 * Here first conenction with peer gets established.
			 * 			 */
			is = sock.getInputStream();
			ois = new ObjectInputStream(is);
		    OutputStream outToServer = sock.getOutputStream();         
	        oos=new ObjectOutputStream(outToServer);	

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*
		 * After successful connection with InputStream and OutPutStream,
		 * it will goes in infinte while loop to provide continue service.
		 */
		
		  while(true){
			  String searchKey;//=new String();
			  String size;
			  StringBuilder sb;
			  try {
				  searchKey=(String)ois.readObject();

				  if(searchKey!=null)
				  {	
					  
					  /*Follwoing IF condtion evaluate to TRUE only
					   * for one time, while connecting with peer at first time. */
					  if(searchKey.equals("request"))
						  	  oos.writeObject("connect");	  
					  else
					  	{
 							  String key=(searchKey.toString()).split("##")[0];
 							  String op=(searchKey.toString()).split("##")[1];
							  
 							  //If Peer requested GET operation then
 							  //folloeing IF condition get executed.
 							  if(op.equals("GET"))
							  {
						          if (mp.get(key)==null)
						        	  oos.writeObject("NotFound");
						          else
						        	  oos.writeObject(mp.get(key));
							  }
							  else if(op.equals("PUT"))
							  {
								  /*KEY and VALUE are appended with delimiter '@@'.
								   * Following code seperates KEY and VALUE from string
								   * and store it in 'keyS' and 'valS' respectively.
								   */
								  String keyS=key.split("@@")[0];
								  String valS=key.split("@@")[1];
								  mp.put(keyS, valS);
						          oos.writeObject("Success");
							  }
							  else if(op.equals("DEL"))
							  {
								  mp.remove(key);
						          oos.writeObject("Deleted");
							  }					  
						  }	
				  }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
	  }	 	  
}