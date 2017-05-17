# Distributed-File-System

Please refer design document.
Following are instruction about how to run code:

Detais of config.property are as follows:
ServerPort :   This is Peer's server port number of.
TotalPeer :This number of peers need to active to start program.
If “TotalPeer = 8” then Client1 to Client8 should active to start program.

eg.
If TotalPeer = 2, then Client1 and Client2 should be running to start functionality.

PORT Dependency =>
1. 'config.property' file details are as follows :
ServerPort = 4410 TotalPeer = 8
Client1 = 192.168.0.999:4410
Client2 = 192.168.0.105:4420
Client3 = 192.168.0.105:4430
Client4 = 192.168.0.105:4440
Client5 = 192.168.0.105:4450
Client6 = 192.168.0.105:4460
Client7 = 192.168.0.105:4470
Client8 = 192.168.0.105:4480
Client9 = 192.168.0.105:4490
Client10 = 192.168.0.105:5500


2. Suppose ServerPort values is “ABCD”, then this “ABCD” port should
  be present in listed Client's IP and PORT like.
  ServerPort = ABCD
  TotalPeer = 8
  Client1 = XXXX.YYYY.ZZZZ.MMMM:ABCD

3. So 'XXXX.YYYY.ZZZZ.MMMM' should be the IP address    of PEER.

4. From details mentioned above,
Peer's IP address is    192.168.0.999
Peer's ServerPort is    4410





How to run code :

I have provided total 8 instance code. Project name are from A2_1, A2_2,....,A2_8.
If you just want to test the code for three Servers only, you need make sure you 
make following changes :
1. Change  'TotalPeer' value
2.Make changes in all 'config.property' file present in
3. A2_1, A2_2 and A2_3. run functionality on three servers.
4. Run A2_1,A2_2 and A2_3 server.



Following are details about how to run the servers :
Guidelines :
Client1
Suppose, Project name is “A2_1” .
Suppose, Project path is /home/sachin/Documents/cs550/workspace/A2_1

Location of config.property file will be at :
/home/sachin/Documents/cs550/workspace/A2_1/config.property

Location of build.xml  file will be at:
/home/sachin/Documents/cs550/workspace/A2_1/build.xml

Generated Jar file path will be at :
/home/sachin/Documents/cs550/workspace/A2_1/dist/A2_1-20150920.jar

Command to build project :
/home/sachin/Documents/cs550/workspace/A2_1$ ant
/home/sachin/Documents/cs550/workspace/A2_1$ java -jar dist/A2_1-20150921.jar
