import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.*;

public class Login {

	public static void main(String args[]){
		
		final JFrame frame = new JFrame("Login");
		JPanel panel = new JPanel();
		final JTextField tf =new JTextField(20);
		JButton enter = new JButton("Login");
		
		panel.add(tf);
		panel.add(enter);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setSize(300, 110);
		frame.add(panel);
		
		enter.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(tf.getText().length()>0){
						ChatClient cc = new ChatClient(tf.getText());
						frame.setVisible(false);
						frame.dispose();
					}
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
						ChatClient cc = new ChatClient(tf.getText());
						frame.setVisible(false);
						frame.dispose();
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
	}
}
