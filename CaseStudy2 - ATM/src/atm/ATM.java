package atm;

import java.util.Scanner;
import cashdispenser.*;
import database.*;
import log.*;
import gui.*;

public class ATM
{
	Database Db;
	Log l;
	CashDispenser C;
	private Scanner sc=new Scanner(System.in);
	private boolean isActive;
	
	public ATM()
	{
		Db=new Database();
		l=new Log();
		isActive=false;
		loginTerminal('1');
	}
	private void loginTerminal(char ch)
	{
		GUI admin=new GUI("Sign In","Username","Password",0);
		String[] credentials;
		while((credentials=admin.getLoginCredentials())==null);
		if(l.checkPassword(credentials[0], credentials[1]))
		{
			System.out.println("Remote Admin Access");
			if(!isActive)
			{
				System.out.println("Press any character to Continue. Please review Existing Balance and Add Cash if required");
				isActive=true;
				ch=sc.next().charAt(0);
				C=new CashDispenser(Db.returnCash());
			}
			else
			{
				System.out.println("Enter 'X' to shutdown machine or enter any other character to display Available Balance");
				ch=sc.next().charAt(0);
				if(ch=='x'||ch=='X')
				{
					isActive=false;
					return;
				}
			}
			char c;
			CashDispenser.showBalance();
			System.out.println("Do you want to add Cash [Y/N]");
			c=sc.next().charAt(0);
			if(c=='Y'||c=='y')
				CashDispenser.deposit(sc);
			System.out.println("Do you want to change maximum Withdraw Amount [Y/N]");
			c=sc.next().charAt(0);
			if(c=='Y'||c=='y') 
			{
				System.out.print("Enter the maximum Withdraw Amount :");
				CashDispenser.maxWithdraw=sc.nextLong();
			}
		}
		else 
		{
			System.out.println("Invalid Credentials. Try Again");
			loginTerminal('1');
		}
	}
	private int loginTerminal()
	{
		GUI user=new GUI("Continue","Account Number","PIN",0);
		String[] credentials;
		while((credentials=user.getLoginCredentials())==null);
		int c=Db.checkAccount(credentials[0], credentials[1]);
		return c;
	}
	public void Start()
	{
		Db.loadClientData();
		do
		{
			System.out.println("Enter 1 for Remote Admin Access or enter any other Number to continue with Transactions");
			char ch=sc.next().charAt(0);
			Db.clearNewClientFile();
			if(ch=='1')
				loginTerminal(ch);
			else
			{
				switch(loginTerminal())
				{
					
					case 0:Db.newQuery();break;
					case 1:System.out.println("Invalid Credentials Try Again");break;
					case 2:System.out.println("Sorry, you currently don't have your Account linked in our ATM");
						System.out.print("Do you want to create a new Account in our Bank [Y/N] :");
						char c=sc.next().charAt(0);
						if(!(c=='Y'||c=='y'))break;
						Db.addAccount();
				}
			}
		}while(isActive);
		l.closeFile();
		if(Db.isModified)
		{
			Db.updateData();
			Db.cacheCash(CashDispenser.returnBalance());
		}
		else if(CashDispenser.isModified)
			Db.cacheCash(CashDispenser.returnBalance());
		Db.closeFile();
	}
}