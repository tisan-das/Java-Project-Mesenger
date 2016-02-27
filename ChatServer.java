import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.StringTokenizer;
import java.util.Vector;

public class ChatServer {

	private static Vector clientSockets;
	private static Vector loginNames;
	
	ChatServer() throws IOException{
		ServerSocket server = new ServerSocket(5217);
		clientSockets = new Vector();
		loginNames = new Vector();
		
		while(true){
			Socket client = server.accept();
			AcceptClient acptclient = new AcceptClient(client);
		}
	}
	
	public static void main(String args[]) throws IOException{
		ChatServer cs = new ChatServer();
	}
	
	class AcceptClient extends Thread{
		Socket clientSocket;
		DataInputStream din;
		DataOutputStream dout;
		
		AcceptClient(Socket client) throws IOException{
			clientSocket = client;
			din = new DataInputStream(client.getInputStream());
			dout = new DataOutputStream(client.getOutputStream());
			
			String loginName = din.readUTF();
			
			loginNames.add(loginName);
			clientSockets.add(clientSocket);
		
			start();
		}
		
		public void run(){
			while(true){
				
				try {
					String msgFromClient = din.readUTF();
					StringTokenizer st = new StringTokenizer(msgFromClient);
					String logName = st.nextToken();
					String msgType = st.nextToken();
					int io =-1;
					String msg = "";
					while(st.hasMoreTokens()){
						msg = msg+" "+st.nextToken();
					}
					
					if(msgType.equals("LOGIN")){
						for(int i=0;i<loginNames.size();i++){
							Socket pSocket = (Socket) clientSockets.elementAt(i);
							DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
							pOut.writeUTF(logName+" has logged in.");
						}
					}
					else if(msgType.equals("LOGOUT")){
						for(int i=0;i<loginNames.size();i++){
							if(logName.equals(loginNames.elementAt(i)))
								io=i;
							Socket pSocket = (Socket) clientSockets.elementAt(i);
							DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
							pOut.writeUTF(logName+" has logged out.");
							if(io>=0){
								loginNames.removeElementAt(io);
								clientSockets.removeElementAt(io);
							}
						}
					}
					else{
						for(int i=0;i<loginNames.size();i++){
							Socket pSocket = (Socket) clientSockets.elementAt(i);
							DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
							pOut.writeUTF(logName+": "+msg);
						}
					}
					if(msgType.equals("LOGOUT"))
						break;
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
