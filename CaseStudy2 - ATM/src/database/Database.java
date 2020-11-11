package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import gui.*;
import log.*;

import account.*;
import cashdispenser.*;

public class Database {
	private File cash;
	private File database;
	private File transaction;
	private FileReader fin;
	private FileReader tin;
	private Scanner cin;
	private FileWriter cout;
	private FileWriter tout;
	private FileWriter fout;
	private FileWriter uout;
	private FileWriter sout;
	private BufferedReader fbr;
	private BufferedReader tbr;
	private Scanner sc;
	private Random rand;
	public boolean isModified;
	
	private int currentAccountIndex;
	private int[] denom;
	public ArrayList<Account> Accounts;
	public Database()
	{
		currentAccountIndex=-1;
		sc=new Scanner(System.in);
		rand=new Random();
		Accounts=new ArrayList<Account>();
		try {
			database=new File("DataBase.csv");
			fin=new FileReader(database);
			fbr=new BufferedReader(fin);
			fout=new FileWriter(database,true);
			
			cash=new File("Cash.txt");
			cin=new Scanner(cash);
			
			transaction=new File("Transaction.csv");
			tin=new FileReader(transaction);
			tbr=new BufferedReader(tin);
			tout=new FileWriter(transaction,true);
			
			denom=new int[7];
			for(int i=0;i<7;i++)
				denom[i]=Integer.parseInt(cin.next());
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	public int[] returnCash()
	{
		return denom;
	}
	public void loadClientData()
	{
		String line;
		try 
		{
			while((line=fbr.readLine())!=null)
			{
				String[] details=line.split(",");
				Account N=new Account(details);
				Accounts.add(N);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void updateData()
	{
		try
		{
			uout=new FileWriter(database,false);
			for(int i=0;i<Accounts.size();i++)
			{
				String details[]=Accounts.get(i).getDetails();
				String line=formatLine(details,',')+"\n";
				uout.write(line);
			}
			uout.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	public void cacheCash(int[] denom)
	{
		String cache=formatLine(denom,' ');
		try 
		{	
			cout=new FileWriter(cash,false);
			cout.write(cache);
			cout.close();
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
			fbr.close();
			fin.close();
			cin.close();
			fout.close();
			tout.close();
			sc.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	private void recordTransaction(Account A,char c,long amount)
	{
		String[] cust=A.getDetails();
		String[] details=new String[6];
		details[0]=Log.getTime();
		details[1]=cust[0];
		details[2]=cust[2];
		details[3]=(c=='2'||c=='6')?""+amount:"-";
		details[4]=(c=='3')?""+amount:"-";
		details[5]=cust[3];
		String writeContent=formatLine(details,',')+"\n";
		try {
			tout.write(writeContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private String generateAcNo()
	{
		String acno="";
		for(int i=0;i<12;i++)
			acno+=rand.nextInt(10);
		return acno;
	}
	private String generatePIN()
	{
		String pin="";
		for(int i=0;i<6;i++)
			pin+=rand.nextInt(10);
		return pin;
	}
	private void notifyCustomer(String d[])
	{
		FileWriter w;
		try 
		{	
			w=new FileWriter("NewClient.txt");
			String S="Account Number\t : "+d[0]+"\nPIN\t\t : "+d[1]+"\nName\t\t : "+d[2]+"\nBalance\t\t : "+d[3];
			w.write(S);
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private String formatLine(String details[],char seperator)
	{
		String line="";
		for(int i=0;i<details.length;i++)
			line+=details[i]+seperator;
		line=line.substring(0,line.length()-1);
		return line;
	}
	private String formatLine(int details[],char seperator)
	{
		String line="";
		for(int i=0;i<details.length;i++)
			line+=""+details[i]+seperator;
		line=line.substring(0,line.length()-1);
		return line;
	}
	public void clearNewClientFile()
	{
		notifyCustomer(new String[] {"","","",""});
	}
	public void addAccount()
	{
		String name;
		long amount;
		System.out.println("Create New Account");
		System.out.println("Enter your Name :");
		name=sc.nextLine();
		amount=deposit();
		boolean isNewAcc=false;
		String acno="",pin="";
		while(!isNewAcc)
		{
			acno=generateAcNo();
			pin=generatePIN();
			isNewAcc=checkNewAcc(acno,pin);
		}
		String[] details=new String[4];
		details[0]=acno;
		details[1]=pin;
		details[2]=name;
		details[3]=""+amount;
		Accounts.add(new Account(details));
		notifyCustomer(details);
		System.out.println("Your Account has been successfully created, Find your AccountNumber and PIN in the NewClient.txt | Enter any character to continue | To change your PIN please request during next Transaction");
		@SuppressWarnings("unused")
		char ch=sc.next().charAt(0);
		String writeContent=formatLine(details,',')+"\n";
		try 
		{
			fout.write(writeContent);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	private long deposit()
	{
		System.out.println("Enter the Amount :");
		long amount=sc.nextLong();
		int[] d=new int[7];
		while(true)
		{
			System.out.println("Amount in denominations of 10, 20, 50, 100, 200, 500, 2000 respectively :");
			for(int i=0;i<7;i++)
				d[i]=sc.nextInt();
			if(checkDenom(amount,d))break;
			System.out.println("Invalid");
		}
		CashDispenser.addCash(d);
		return amount;
	}
	private long withdraw(Account A)
	{
		String amt;
		while(true)
		{
			System.out.println("Enter the Amount :");
			amt=sc.next();
			if(Integer.parseInt(amt)>CashDispenser.maxWithdraw)
				System.out.println("Maximum withdraw limit exceeded. You can only withdraw a maximum of "+CashDispenser.maxWithdraw);
			else
			{
				if(A.checkBalance(amt))
					break;
				System.out.println("Insufficient Balance Try Again");
			}
		}
		int denomination[]=CashDispenser.returnCount(Long.parseLong(amt));
		long availableAmt=CashDispenser.availableAmount(denomination);
		if(CashDispenser.lessCash) {
			System.out.println("Sorry we are out of Cash. Available Amount: "+availableAmt+" Press 'X' to cancel request or enter any other character to continue");
			char ch=sc.next().charAt(0);
			if(ch=='X'||ch=='x')return 0;
		}
		CashDispenser.addCash(denomination,'-');
		return availableAmt;
	}
	private long inMoneyTransfer(Account A)
	{
		Map<String,Float> rates=new HashMap<String,Float>();
		rates.put("USD",75f);
		rates.put("EUR",88f);
		rates.put("JPY",0.7f);
		float rate=0f;
		float process=2f;
		String key;
		long amt;
		while(true)
		{
			System.out.println("Money transfer to USD, EUR or JPY");
			key=sc.next();
			if(rates.containsKey(key))
				{rate=rates.get(key);break;}
			else
				System.out.println("Invalid. Enter Again");
		}
		while(true)
		{
			System.out.println("Enter amount in "+key+" you want to transfer :");
			long inamt=sc.nextLong();
			amt=(long)Math.ceil((rate+process/100)*inamt);
			System.out.println("Amount that will be deducted including "+process+"% processing fee is :"+amt);
			if(A.checkBalance(""+amt))
				break;
			System.out.println("Insufficient Balance Try Again");
		}
		return amt;
	}
	private void bankStatement(Account A,char c)
	{
		String line;
		String[] cust=A.getDetails();
		String writeContent=null;
		boolean flag=false;
		try 
		{	
			sout=new FileWriter("Statement.csv",false);
			if(c=='0')
				writeContent="Account Number,\nName,\n";
			else if(c=='1')
			{
				writeContent="Account Number,"+cust[0]+"\nName,"+cust[2]+"\n\nDate,Withdrawals,Deposits\n";
				while((line=tbr.readLine())!=null)
				{
					String[] details=line.split(",");
					String[] statement=new String[4];
					if(A.checkAccNo(details[1]))
					{
						if(!flag)
							flag=true;
						statement[0]=details[0];
						for(int i=1;i<=3;i++)
							statement[i]=details[i+2];
						writeContent+=formatLine(statement,',')+"\n";
					}
				}
			}
			
			if(!flag)
				writeContent+="No Transaction Records\n";
			sout.write(writeContent);
			sout.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	private boolean changePIN(Account A)
	{
		while(true)
		{
			GUI user=new GUI("Continue","Enter new PIN","Please Re-Enter PIN",1);
			String[] pin;
			while((pin=user.getLoginCredentials())==null);
			boolean isValid=A.checkPIN(false,pin);
			if(isValid)
				for(int i=0;i<Accounts.size();i++)
					if(!Accounts.get(i).checkPIN(true,pin))
						{isValid=false;break;}
			
			if(!isValid)
			{
				System.out.println("Invalid, Please enter a new PIN, Press 'X' to cancel request or any other character to continue");
				char ch=sc.next().charAt(0);
				if(ch=='X'||ch=='x')return false;
			}
			else
			{
				Accounts.get(currentAccountIndex).changePIN(pin[0]);
				return true;
			}
		}
	}
	private boolean checkDenom(long a,int d[])
	{
		long s=0;
		for(int i=0;i<d.length;i++)
			s+=d[i]*CashDispenser.indexValue.get(i);
		if(a==s)
			return true;
		return false;
			
	}
	private boolean checkNewAcc(String acno, String pin)
	{
		boolean flag=true;
		for(int i=0;i<Accounts.size();i++)
			if(Accounts.get(i).checkAccNo(acno)||Accounts.get(i).checkPIN(pin))
				{flag=false;break;}
		return flag;
	}
	public int checkAccount(String accNo,String pin)//0-Valid, 1-Invalid, 2-Account does not exist
	{
		int c=2;
		currentAccountIndex=-1;
		if(Accounts.size()>0)
		{
			for(int i=0;i<Accounts.size();i++)
				if(Accounts.get(i).checkAccNo(accNo))
				{	
					c=1;
					if(Accounts.get(i).checkPIN(pin))
					{	
						c=0;
						currentAccountIndex=i;
					}
					break;
				}
		}
		return c;
	}
	public void newQuery()
	{
		if(currentAccountIndex!=-1)
		{
			Account A=Accounts.get(currentAccountIndex);
			A.displayAccount('0');
			char c=sc.next().charAt(0);
			boolean isModified=false;
			long changeAmount=0;
			switch(c)
			{
				case '1':A.displayAccount(c);break;

				case '2':
					A.displayAccount('1');
					long availableAmt=withdraw(A);
					Accounts.get(currentAccountIndex).updateBalance(availableAmt,'-');
					isModified=true;
					changeAmount=availableAmt;
					break;
				
				case '3':
					A.displayAccount('1');
					long amount=deposit();
					Accounts.get(currentAccountIndex).updateBalance(amount,'+');
					isModified=true;
					changeAmount=amount;
					break;
					
				case '4':
					isModified=changePIN(A);
					break;
					
				case '5':
					bankStatement(A,'1');
					System.out.println("We have generated your Bank Statement in the file Statement.csv | Please create a local copy for the same and Enter any character to continue");
					@SuppressWarnings("unused") 
					char ch=sc.next().charAt(0);
					bankStatement(A,'0');
					break;
					
				case '6':
					long transferAmount=inMoneyTransfer(A);
					Accounts.get(currentAccountIndex).updateBalance(transferAmount,'-');
					isModified=true;
					changeAmount=transferAmount;
					break;
					
				case 'x':
				case 'X':
				default:System.out.println("Thank You Have a Wonderful Day");
			}
			if(isModified)
			{
				if(c=='2'||c=='3'||c=='6')
				{
					if(c=='2')
					{
						System.out.print("Money Withdrawn :");
						System.out.println(changeAmount);
					}
					else if(c=='3')
					{
						System.out.print("Money Deposited :");
						System.out.println(changeAmount);
					}
					else if(c=='6')
						System.out.println("Amount transferred to International Account.");
					recordTransaction(A,c,changeAmount);
					Accounts.get(currentAccountIndex).displayAccount('1');
				}
				else if(c=='4')
					System.out.println("You have Successfully changed your PIN");
				System.out.println("Thank You Have a Wonderful Day");
				if(!this.isModified)
					this.isModified=isModified;
			}
		}
		currentAccountIndex=-1;	
	}
}
