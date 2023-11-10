package core_java_topics.exceptionHandling.ATMProject.interfaces;

import core_java_topics.exceptionHandling.ATMProject.customExceptions.InsufficientBalanceException;
import core_java_topics.exceptionHandling.ATMProject.customExceptions.InvalidAmountException;
import core_java_topics.exceptionHandling.ATMProject.customExceptions.NotAOperatorException;

public interface IATMService {
	
/*these are the services that are to implemented by the specific banks.since implentation of these methods
	 * are bank specific therefore we have left it for the banks to decide as to what will be their specific implementation.*/
   /*this function return the type of the card that is being inserted into the machine*/
   public abstract String getUserType() throws NotAOperatorException;
   /*this function if used to withdraw the money from the users bank account.and throws two exception:-
    * 1)if the amount is 0 or less than 0 throws invalid amount exception indicating amount is too small.
    * 2) if the amount is greater than the current balance of the user then it throws the insufficient amount exception indicating 
    * there are not enough funds for performing the transaction.*/
   public abstract double withDrawAmount(double wthAmt) throws InvalidAmountException,InsufficientBalanceException;
  /*it is used to add money to a users account. takes the money to be credited as the parameters. it throws invalid amount exception if
   * amount if less than or equal to zero.*/
   public abstract void depositAmount(double dptAmt) throws InvalidAmountException;
   /*this function shows the amount of balance present in the account of specified user.*/
   public abstract double checkBalance();
   /*this function helps to change the current pin associated with a card number.*/
   public abstract void changePin(int pin);
   /*this function is used in main application to check if the pin is correct or not.*/
   public abstract int getPinNumber();
   /*this function return the card holder name.*/
   public abstract String getUserName();
   /*this function decrements the number of attempts in case a wrong pin number is entered.*/
   public abstract void decChances();
   //this shows the remaining attempts in case we have entered a wrong pin number.
   public abstract int getChances();
   //this function can be used in operator mode to reset the number of attempts.
   public abstract void resetChances();
   //this functions prints the last five transactions associated with a card.
   public abstract void miniStatement();
   
}
