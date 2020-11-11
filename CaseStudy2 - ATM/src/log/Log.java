package log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Log {
	private String password;
	private String username;
	private File log;
	private Scanner fin;
	private FileWriter fout;
	private static DateTimeFormatter format;
	
	public Log()
	{
		log=new File("AccessLog.txt");
		try
		{
			fin=new Scanner(log);
			fout=new FileWriter(log,true);
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		username=fin.next();
		password=fin.next();
		
		format=DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
	}
	private void writeToLog(String username, String password, boolean isValid)
	{
		LocalDateTime loginTime=LocalDateTime.now();
		String content=username+" "+password+" "+loginTime.format(format)+" "+(isValid?"Valid":"Invalid")+"\n";
		try
		{
			fout.write(content);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	public void closeFile()
	{
		try 
		{
			fin.close();
			fout.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	public boolean checkPassword(String username, String password)
	{
		boolean isValid=false;
		if(username.compareTo(this.username)==0)
			if(password.compareTo(this.password)==0)
				isValid=true;
		writeToLog(username,password,isValid);
		return isValid;
	}
	public static String getTime()
	{
		LocalDateTime t=LocalDateTime.now();
		return t.format(format);
	}
}
