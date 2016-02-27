import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class ChatClient extends JFrame implements Runnable {

	JTextArea ta;
	JTextField tf;
	JButton send,logout;
	Thread thread;
	String loginName;

	Socket socket;
	DataInputStream din;
	DataOutputStream dout;
	
	ChatClient(String login) throws UnknownHostException, IOException{
		super(login);
		loginName = login;
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				try {
					dout.writeUTF(loginName+" "+"LOGOUT");
					System.exit(1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		ta = new JTextArea(18,50);
		tf = new JTextField(50);
		send = new JButton("Send");
		logout = new JButton("Logout");
		send.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(tf.getText().length()>0)
						dout.writeUTF(loginName+" DATA: "+tf.getText().toString());
					tf.setText("");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});
		tf.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent key) {
				if(key.getKeyCode()==KeyEvent.VK_ENTER){
					try {
						if(tf.getText().length()>0)
						dout.writeUTF(loginName+" DATA: "+tf.getText().toString());
						tf.setText("");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
			}
			
		});
		logout.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					dout.writeUTF(loginName+" "+"LOGOUT");
					System.exit(1);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});
		
		socket = new Socket("localhost",5217);
		
		din = new DataInputStream(socket.getInputStream());
		dout = new DataOutputStream(socket.getOutputStream());
		
		dout.writeUTF(loginName);
		dout.writeUTF(loginName+" "+"LOGIN");
		
		thread = new Thread(this);
		thread.start();
		setup();
	}
	
	public void setup(){
		setSize(600,400);
		JPanel panel = new JPanel();
		panel.add(new JScrollPane(ta));
		panel.add(tf);
		panel.add(send);
		panel.add(logout);
		add(panel);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void run(){
		while(true){
			try {
				ta.append("\n"+din.readUTF());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
