package cashdispenser;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CashDispenser
{
	private static int[] denomination;
	public static Map<Integer,Integer> indexValue;
	public static boolean lessCash;
	public static boolean isModified;
	public static long maxWithdraw;
	public CashDispenser(int[] d)
	{
		denomination=new int[d.length];
		for(int i=0;i<d.length;i++)
			denomination[i]=d[i];
		indexValue=new HashMap<Integer,Integer>();
		indexValue.put(0, 10);
		indexValue.put(1, 20);
		indexValue.put(2, 50);
		indexValue.put(3, 100);
		indexValue.put(4, 200);
		indexValue.put(5, 500);
		indexValue.put(6, 2000);
		lessCash=false;
		isModified=false;
		
		maxWithdraw=40000;
	}
	public static int[] returnBalance()
	{
		return denomination;
	}
	public static void showBalance()
	{
		long balance=0;
		System.out.println("Number of Notes of Denomination :-");
		for(int i=0;i<denomination.length;i++)
		{
			int v=indexValue.get(i),n=denomination[i];
			System.out.println(v+"\t : "+n);
			balance+=n*v;
		}
		System.out.println("Available Balance : "+balance); 
		System.out.println("Max Withdraw Amount : "+maxWithdraw);
	}
	public static long availableAmount(int A[])
	{
		long balance=0;
		for(int i=0;i<denomination.length;i++)
			balance+=A[i]*indexValue.get(i);
		return balance;
	}
	public static void addCash(int d[])
	{
		for(int i=0;i<denomination.length;i++)
			denomination[i]+=d[i];
		if(!isModified)
			isModified=true;
	}
	public static void addCash(int d[],char ch)
	{
		if(ch=='-')
			for(int i=0;i<denomination.length;i++)
				denomination[i]-=d[i];
	}
	public static void deposit(Scanner sc)
	{
		int d[]=new int[7];
		System.out.println("Please add cash to the Dispensor\nEnter no of notes of denomination 10, 20, 50, 100, 200, 500, 2000 respectively");
		for(int i=0;i<7;i++)
			d[i]=sc.nextInt();
		addCash(d);
	}
	public static int[] returnCount(double amount)
	{
		lessCash=false;
		long cash=(int)Math.floor(amount);
		long dummy=cash;
		int A[]=new int[indexValue.size()];
		int availableAmount=0;
		for(int i=denomination.length-1;i>=0;i--)
		{
			if(denomination[i]>0) 
			{
				int num=(int)Math.floorDiv(dummy, indexValue.get(i));
				A[i]=(int)(num<denomination[i]?num:denomination[i]);
				availableAmount+=A[i]*indexValue.get(i);
				dummy-=A[i]*indexValue.get(i);
			}	
		}
		if(availableAmount<cash)
		{
			lessCash=true;
		}
		return A;
	}
}