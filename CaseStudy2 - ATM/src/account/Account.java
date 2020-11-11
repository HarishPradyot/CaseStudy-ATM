package account;

public class Account {
	private String[] details;
	public Account(String details[])
	{	
		this.details= new String[details.length];
		for(int i=0;i<details.length;i++)
			this.details[i]=details[i];
	}
	public String[] getDetails()
	{
		return details;
	}
	public boolean checkAccNo(String AccNo)
	{
		if(AccNo.compareTo(details[0])==0)
			return true;
		return false;
	}
	public boolean checkPIN(String pin)
	{
		if(pin.compareTo(details[1])==0)
			return true;
		return false;
	}
	public boolean checkPIN(boolean flag,String[] pin)
	{
		if(flag||(pin[0].compareTo(pin[1])==0))
		{
			if(pin[0].compareTo(details[1])!=0)
				return true;
		}
		return false;
	}
	public void displayAccount(char c)
	{
		if(c=='0') 
		{
			System.out.println("Welcome "+details[2]+"\nAccountNumber :"+"XXXXXXXXX"+details[0].substring(details[0].length()-3));
			System.out.println("Press 1 to View Balance, 2 to Withdraw Money, 3 to Deposit Money, 4 to Change Pin, 5 to Provide Transaction Record, 6 for International Money Transfer or Press 'X' to terminate :");
		}
		else if(c=='1')
			System.out.println("Balance :"+details[3]);
	}
	public boolean checkBalance(String amt)
	{
		return (Long.parseLong(amt)<=Long.parseLong(details[3]));
	}
	public void updateBalance(long Amount,char c)
	{
		long balance=Long.parseLong(details[3]);
		if(c=='-')
			balance-=Amount;
		else if(c=='+')
			balance+=Amount;
		details[3]=""+balance;
	}
	public void changePIN(String pin)
	{
		details[1]=pin;
	}
}
