package core_java_topics.exceptionHandling.ATMProject.BankCards;

import java.util.ArrayList;
import java.util.Collections;

import core_java_topics.exceptionHandling.ATMProject.customExceptions.InsufficientBalanceException;
import core_java_topics.exceptionHandling.ATMProject.customExceptions.InvalidAmountException;
import core_java_topics.exceptionHandling.ATMProject.customExceptions.NotAOperatorException;
import core_java_topics.exceptionHandling.ATMProject.interfaces.IATMService;

public class ICICIBank implements IATMService{
	String name;
	long DebitCardNumber;
    double balance ;
    int pinNumber ;
    int chances;
    ArrayList<String> statement;
    final String type ="user";

    
	public ICICIBank(String name, long debitCardNumber, double balance, int pinNumber) {
		super();
		chances=3;
		this.name = name;
		DebitCardNumber = debitCardNumber;
		this.balance = balance;
		this.pinNumber = pinNumber;
		statement = new ArrayList<>();
	}

	@Override
	public double withDrawAmount(double withDrawAmount) throws InvalidAmountException, InsufficientBalanceException {
		if(withDrawAmount<=0) {
			throw new InvalidAmountException("Dear Customer You Have Entered an Invalid amount.\n please enter a valid amount.");
		}else if(withDrawAmount>balance) {
			throw new InsufficientBalanceException("Dear Customer you have Inusfficient Balance to complete this Transaction.");
		}else {
			balance-=withDrawAmount;
			statement.add("Debited "+withDrawAmount);
			return withDrawAmount;
		}
	}

	@Override
	public void depositAmount(double depositAmount) throws InvalidAmountException {
		if(depositAmount<=0||depositAmount%10!=0) {
			throw new InvalidAmountException("Please deposit a valid amount. ie multiple of 100.");
		}else {
			balance+=depositAmount;
			statement.add("Credit "+depositAmount);
		}
		
	}

	@Override
	public double checkBalance() {
		
		return balance;
	}

	@Override
	public void changePin(int pin) {
		pinNumber = pin;
		
	}

	@Override
	public int getPinNumber() {
		
		return pinNumber;
	}

	@Override
	public String getUserType() {
		// TODO Auto-generated method stub
		return type;
	}

	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void decChances() {
		// TODO Auto-generated method stub
		 --chances;
	}
	public int getChances() {
		// TODO Auto-generated method stub
		 return chances;
	}

	@Override
	public void resetChances() {
		chances=3;
		
	}

	@Override
	public void miniStatement() {
		int count=5;
		if(statement.size()==0) {
			System.out.println("you haven't performed any transaction till date.");
			return;
		}
		System.out.println("Last five transactions are as follows:");
		Collections.reverse(statement);
		for(String i:statement) {
			if(count==0) {break;}
			System.out.println(i);
			count--;
		}
		Collections.reverse(statement);
		
	}

}
