package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class GUI implements ActionListener
{
	JFrame frame;
	JPanel panel;
	JButton login;
	JLabel field1, field2;
	JTextField userinput1;
	JPasswordField userinput2;
	
	JPasswordField pin1;
	JPasswordField pin2;
	
	int i;
	
	private String[] credentials;
	
	public GUI(String buttonLabel,String field1,String field2,int i)
	{
		frame=new JFrame();
		panel=new JPanel();
		login=new JButton(buttonLabel);
		this.field1=new JLabel(field1);
		this.field2=new JLabel(field2);
		userinput1=new JTextField();
		userinput2=new JPasswordField();
		pin1=new JPasswordField();
		pin2=new JPasswordField();
		
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		frame.setTitle("Enter Credentials");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(350,200);
		if(i==0)
		{
			this.field1.setBounds(10,20,100,20);
			this.field2.setBounds(10,50,100,20);
			userinput1.setBounds(120,20,160,20);
			userinput2.setBounds(120,50,160,20);
		}
		else if(i==1)
		{
			this.field1.setBounds(10,20,130,20);
			this.field2.setBounds(10,50,130,20);
			pin1.setBounds(145,20,120,20);
			pin2.setBounds(145,50,120,20);
		}
		
		login.setBounds(120,80,100,25);
		
		panel.setLayout(null);
		frame.add(panel);
		panel.add(this.field1);
		panel.add(this.field2);
		if(i==0)
		{
			panel.add(userinput1);
			panel.add(userinput2);
		}
		else if(i==1)
		{
			panel.add(pin1);
			panel.add(pin2);
		}
		panel.add(login);
		
		login.addActionListener(this);	
		this.i=i;
	}
	public String[] getLoginCredentials()
	{
		System.out.print("");
		return credentials;
	}
	@Override
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		String ip1=null,ip2=null;
		if(i==0) 
		{
			ip1=userinput1.getText();
			ip2=userinput2.getText();
		}
		else if(i==1)
		{
			ip1=pin1.getText();
			ip2=pin2.getText();
		}
		credentials=new String[2];
		credentials[0]=ip1;
		credentials[1]=ip2;
		frame.dispose();
	}
	
}