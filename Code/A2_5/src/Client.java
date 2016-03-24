import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.awt.Choice;
import java.awt.List;
import java.io.*;

import javax.swing.plaf.SliderUI;

public class Client
{
	public static String localIpandPortAddress;
	static ArrayList<ObjectInputStream> ois_list;
	static ArrayList<ObjectOutputStream> oos_list;
	
	   public static void main(String [] args)
	   {	   	  
		    Properties property=new Properties();
		    Boolean runCode=true;
		 	try 
		 	{
			      ois_list=new ArrayList<ObjectInputStream>();
			      oos_list=new ArrayList<ObjectOutputStream>();			    	
				 /*	ois_list & oos_list =>
				  * Two arraylist each for ObjectInputStream and 
				  * ObjectOutputStream are created.
				  * All connected socket details are stored in this two array list. 
				  * */
			      PeerServer peerServer = new PeerServer();
			      peerServer.start();
				  property.load(new FileInputStream(new File("config.property")));

				   
				  int  totalPeer=Integer.parseInt(property.getProperty("TotalPeer"));	      	
					/* totalPeer =>	
					 * 		1.	As per instruction, we need to connect to total 8 peers.
					 * 			The value is fetched from "config.property" file.
					 * 		2.	If we changed value of this variable to 5,
					 * 			then it will connect to only 5 peers.
					 * 		3.  Depending on value of this variable, hashcode decides
					 * 			which server should select to PUT/GET/DELETE key.
							4. 	Following code is at line 147.
											
									long hashcode= new Client().ComputeHashCode(key2search);			          			     
							   		int ServerSelected=(int)(hashcode%totalPeer);
							   							        	
							     	Here 'hashcode' is gets compute based on key value.
							     	Then on this value, we perform '%' (mod) operation,
							     	to get specific server.
					 */
			       
			       
			      for(int index=1;index<(totalPeer+1);index++){
			    	  runCode=true;
			    	  String ClientSocketAddress=property.getProperty("Client"+index);
			    	  /*ClientSocketAddress=> It retrives peer's IP and PORT address
			    	   * 					  for 'config.property' file.			    	   * 
			    	   */
			    	  
				      String  clientIP=ClientSocketAddress.split(":")[0];
				      int  clientPort=Integer.parseInt(ClientSocketAddress.split(":")[1]);
				      Socket clientSocket=null;
			    	  try{
			          clientSocket = new Socket(clientIP,clientPort);
			    	  }catch(Exception e){
			    		  
			    		  /*Following conding are added to continously perform same
			    		   * operation untill gets connected to all peers.
			    		   **/
			    		  runCode=false;
			    		  index--;
			    		  try {
							Thread.sleep(10000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			    	  }
			    	  if(runCode){

			    		  
							/*
							 * Follwoing code establish connection with
							 * all peers listed in 'config.property' file.			    		  
							 */
			     		      OutputStream outToServer = clientSocket.getOutputStream();         
					          ObjectOutputStream oos=new ObjectOutputStream(outToServer);	
					          oos.writeObject("request");					          
					    	  InputStream is = clientSocket.getInputStream();
					    	  ObjectInputStream ois = new ObjectInputStream(is);
					    	  String returnResult=(String)ois.readObject();
     					      System.out.println("Connected to Server "+index);			    	  					    	  
					    	  ois_list.add(index-1,ois);
					    	  oos_list.add(index-1,oos);
			    	  }
			      }
			/* After successful connection with all peers,
			 * following options will display :
			 * 
			 * 		1.Get
			 * 		2.Put
			 * 		3.Delete
			 * 		Enter choice: 			      
			 */
		       Scanner sc=new Scanner(System.in);	
		       String key2search = null;
		       StringBuffer SearchKey = null,value=null,choice;
		       int OptionSelected;
		       do
		       {
		          System.out.println("1.GET\n2.PUT\n3.DELETE");
		          System.out.print("Enter choice : ");
		          try{
		          choice=new StringBuffer(sc.next());
			 OptionSelected=Integer.parseInt(choice.toString());
		          }catch(Exception e){
					OptionSelected=5;
		          }		          
		          switch(OptionSelected)
		          {
			          case 1: System.out.print("Enter key : ");
			          		  key2search=sc.next();
			          		  SearchKey=new StringBuffer(key2search);		        	  
			          		  SearchKey.append("##"+"GET");
			          		  break;
			          case 2: System.out.print("Enter key : ");
			          		  key2search=sc.next();
			          		  SearchKey=new StringBuffer(key2search);		        	  
			          		  System.out.print("Enter Value : ");
			          		  value=new StringBuffer(sc.next());
			          		  SearchKey.append("@@"+value+"##"+"PUT");
			        	  	  break;
			          case 3: System.out.print("Enter key to delete : ");
			          		  key2search=sc.next();
		          		 	  SearchKey=new StringBuffer(key2search);		
			          		  SearchKey.append("##"+"DEL");
		          		 	  break;
			          case 4: System.out.println("Good Bye");
	        		 		  break;
			          default:
			        	  System.out.println("Invalid choice !!! ");
			        	  	break;		        	  	
		          }

	        	 long startTime = System.nanoTime();    
	        	 StringBuffer returnResult = null;
	        	 if(SearchKey!=null)
	        	 {
		        	  	long hashcode= new Client().ComputeHashCode(key2search);			          			     
		        		int ServerSelected=(int)(hashcode%totalPeer);
		        	  	ServerSelected++;
		       		  	(oos_list.get(ServerSelected-1)).writeObject(SearchKey.toString());			          
	        		  	returnResult=new StringBuffer((String)(ois_list.get(ServerSelected-1)).readObject());		          		      		          		          		        	 
		        	  	System.out.println(" Result : "+returnResult.toString());
		          }
	        	 
		       }while(OptionSelected!=4);
		       
	       } catch (IOException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	   }
	   
	   /*
	    * Following function calculates HashCode for given 'key'.
	    * This function, calculates hashcode based on ASCII value of charactes.
	    */   
	   long ComputeHashCode(String key)
	   {
		   long hashcode=0,multiplier=10;		   
		   for(int i=0;i<key.length();i++)
		   {
			   hashcode+=key.charAt(i)+Math.pow(multiplier, i);		
		   }
		   	return hashcode;
	   }
}
