package core_java_topics.exceptionHandling.ATMProject.mainApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

import core_java_topics.exceptionHandling.ATMProject.BankCards.AxisBank;
import core_java_topics.exceptionHandling.ATMProject.BankCards.ICICIBank;
import core_java_topics.exceptionHandling.ATMProject.BankCards.OperatorCard;
import core_java_topics.exceptionHandling.ATMProject.BankCards.SBIBank;
import core_java_topics.exceptionHandling.ATMProject.customExceptions.IncorrectPinLimitReachedException;
import core_java_topics.exceptionHandling.ATMProject.customExceptions.InsufficientBalanceException;
import core_java_topics.exceptionHandling.ATMProject.customExceptions.InsufficientMachineBalanceException;
import core_java_topics.exceptionHandling.ATMProject.customExceptions.InvalidAmountException;
import core_java_topics.exceptionHandling.ATMProject.customExceptions.InvalidCardException;
import core_java_topics.exceptionHandling.ATMProject.customExceptions.InvalidPinException;
import core_java_topics.exceptionHandling.ATMProject.customExceptions.NotAOperatorException;
import core_java_topics.exceptionHandling.ATMProject.interfaces.IATMService;

public class BankOperations {
	// variable displays the amount of money available in the machine.
	public static double ATM_MACHINE_BALANCE = 100000l;
	public static int ATM_SECRET_CODE=7861;
	// shows all the money transactions took place from this machine.
	public static ArrayList<String> ACTIVITY = new ArrayList<>();
	// for storing all the user data act as a runtime database.
	public static HashMap<Long, IATMService> database = new HashMap<>();
	public static boolean MACHINE_ON = true;
	public static IATMService card = null;
    /*this function can only be accessed by in operator mode.
     * WORKING:
     * 1)ask the operator for the atm machines secret code.
     * 2)validate the atm machine secret code and clear the atm machines activity history.*/
	public static void clearAcitivtyLogs() {
		Scanner scn = new Scanner(System.in);
		System.out.println("Please enter the atm machines secret code to reset the activity logs.");
		int secret_code = scn.nextInt();
		if(secret_code==ATM_SECRET_CODE) {
			ACTIVITY.clear();
			ACTIVITY.add("Accessed by: " + card.getUserName()
				+ "\nActivity:Cleared the ATM LOGS. ");
	 	}else {
	 		ACTIVITY.add("Accessed by: " + card.getUserName()
				+ "\nActivity:Failed to clear atm Activity Logs.");
	 	}
		
	}
	// it search the card if it is present in the database if not then throws card
	// not compatible exception indicating that card
	// cannot be used to complete transactions .
	public static IATMService validateCard(long cardNumber) throws InvalidCardException {
		if (database.containsKey(cardNumber)) {
			return database.get(cardNumber);
		} else {
			
			throw new InvalidCardException("This Card is not compatible.");
		}
	}

	// show all the activities that took place in the machines from latest to
	// oldest.
	public static void checkMachineActivity() {
		
		System.out.println("ACTIVITIES:");
		for (String i : ACTIVITY) {
			System.out.println("=================================");
			System.out.println(i);
		}
		System.out.println("=================================");
	}

	/*
	 * if a user exceeds the number of attempts to put wrong pin number then it can
	 * be manually reset by the operator using this function.
	 */
	// can only be accessed in operator mode only
	public static boolean resetUserAttempts(IATMService operatorCard) {
		IATMService card = null;
		long number;
		int pin;
		Scanner scn = new Scanner(System.in);
		// operator needs to provide the card number of the user.
		System.out.println("please enter user card number");
		number = scn.nextLong();
		try {
			// card number entered by the operator is validated before it is used to reset.
			card = validateCard(number);
			card.resetChances();
		} catch (InvalidCardException ice) {
			ACTIVITY.add("Accessed by: " + operatorCard.getUserName()
				+ "\nActivity: failed to reset the number of attempts of invalid pin.\nfor CARD :" + number+"\nStatus:Failed \nReason:card not registered with the machine.");
			System.out.println(ice.getMessage());
			return false;
		}
		ACTIVITY.add("Accessed by: " + operatorCard.getUserName()
				+ "\nActivity: to reset the number of attempts of invalid pin.\nfor user :" + card.getUserName());

	   return true;
	}

	/*
	 * once the input his card number and the pin this function does three things
	 * 1)check if the card is valid or not using the validate card function. 2)check
	 * if it is a operator card. if yes the return the operator card object.after
	 * checking if the operator card pin is correct. 3)if card user is normal user
	 * then it checks the pin that is supplied . 4)if user has not exhausted
	 * entering the wrong pin number it return the card object.else it throws the
	 * attempts limit exhausted exception. 5) if the pin is incorrect than it gives
	 * the invalid pin number exception.
	 */
	public static IATMService validateCredentials(long cardNumber, int pinNumber)
			throws InvalidPinException, InvalidCardException, IncorrectPinLimitReachedException {
		//IATMService card;
		if (database.containsKey(cardNumber)) {
			card = database.get(cardNumber);
		} else {
			throw new InvalidCardException("This Card is not compatible.");
		}
		try {
			if (card.getUserType().equals("operator")) {
				if (card.getPinNumber() != pinNumber) {
					throw new InvalidPinException("Dear operator please enter a valid pin.");
				} else {
					return card;
				}
			}
		} catch (NotAOperatorException e) {

			e.printStackTrace();
		}
		if (card.getChances() <= 0) {
			throw new IncorrectPinLimitReachedException(
					"You have exceded the number of attempts tp access your account.\n please try after 24hours.");
		}
		if (card.getPinNumber() != pinNumber) {
			card.decChances();
			
			throw new InvalidPinException(
					"You Have Entered an invalid pin number.\n number of attempts remaining: "+card.getChances() );
		} else {
			return card;
		}
	}

	/*
	 * before the amount is given from the atm machine it is checked if the atm
	 * machines has sufficient amount of money to process the user transaction. if
	 * the amount to be withdrawn exceeds the amount of money in atm machines it
	 * throws insufficient machine balance exception.
	 */
	public static void validateAmount(double amount) throws InsufficientMachineBalanceException {

		if (amount > ATM_MACHINE_BALANCE) {
			throw new InsufficientMachineBalanceException(
					"We are unable to process your transaction due to insufficient funds int the ATM Machine."
							+ "\n Sorry for the inconvinince please use another Machine.");
		}

	}

	/*
	 * before and amount is deposited inside the machine it is checked if there is
	 * enough space in the machines to accommodate more amount using this function
	 * .if limit to hold money of the machines is reached it throws an exception.
	 */
	public static void validateDepositAmount(double amount) throws InsufficientMachineBalanceException {
		if (amount+ATM_MACHINE_BALANCE > 1000000.0d) {
			ACTIVITY.add("Activity: unable to deposit amount capacity reached.");
			throw new InsufficientMachineBalanceException(
					"Machine capacity is reached.Please deposit amount from next nearest ATM.");
		}
	}
	/*
	 * operator mode is for users having card type as operator.it can perform the
	 * following tasks. 1)the operator can check the amount present within the
	 * machine. 2)the operator can deposit the amount in machine. 3)operator can
	 * help a users who has exceeded the number of attempts of incorrect password by
	 * reseting it.
	 */

	public static void operatorMode(IATMService card) {
		Scanner scn = new Scanner(System.in);
		System.out.println("WELCOME TO OPERATOR MODE");
		double amount;
		boolean flag = true;
		while (flag) {
			System.out.println("OPERATOR MODE\n operator name:" + card.getUserName());
			System.out.println("================================");
			System.out.println("||  0. Swith Off Machine.     ||");
			System.out.println("||  1. ATM Machine Balance.   ||");
			System.out.println("||  2. Deposit Money into ATM ||");
			System.out.println("||  3. Reset a users attempts ||");
			System.out.println("||  4. check machine activity ||");
			System.out.println("||  5. clear Machine activity ||");
			System.out.println("||  6. Exit operator mode     ||");
			System.out.println("================================");

			System.out.println("please enter your choice:");
			int option = scn.nextInt();
			switch (option) {
			case 0:
				System.out.println("enter the secret atm machine pin.");
				int sercret_pin = scn.nextInt();
				if(sercret_pin==ATM_SECRET_CODE) {
					MACHINE_ON = false;
					flag = false;
					ACTIVITY.add(
							"Accessed by: " + card.getUserName() + "\nActivity: Turn off the machine");
					break;
				}else {
					System.out.println("Invalid machine pin.");
					ACTIVITY.add(
							"Accessed by: " + card.getUserName() + "\nActivity:Failed to Turn off the machine");
				}
				
				
				
				
				
				break;
			case 1:
				ACTIVITY.add("Accessed by: " + card.getUserName() + "\nActivity: " + "checked cash in the atm.");
				System.out.println("================================");
				System.out.println("the atm currentlty has Rs" + ATM_MACHINE_BALANCE + " cash.");
				System.out.println("================================");
				break;
			case 2:
				System.out.println("please enter amount.");
				amount = scn.nextDouble();
				ATM_MACHINE_BALANCE += amount;
				ACTIVITY.add("Accessed by: " + card.getUserName() + "\nActivity: " + "added cash Rs" + amount
						+ "in the atm.");
				System.out.println("================================");
				System.out.println("amount updated successully");
				System.out.println("================================");
				break;
			case 3:
				boolean status =resetUserAttempts(card);
				if(!status) {
					System.out.println("================================");
					System.out.println("Attemps restoration failed");
					System.out.println("================================");
					break;
				}
				System.out.println("================================");
				System.out.println("Attemps restored successfully");
				System.out.println("================================");
				break;
			case 4:
				checkMachineActivity();
				break;
			case 5:
				clearAcitivtyLogs();
				break;
			case 6:
				flag = false;
				break;

			default:
				System.out.println("please enter a valid number.");
			}

		}

	}

	public static void main(String[] args) throws InvalidAmountException, InsufficientBalanceException {
		/* supplying some inputs to the data base. */
		database.put(546789123l, new ICICIBank("Uzair Ahmed", 546789123l, 150000.0d, 5463));
		database.put(465132465l, new SBIBank("Shakil Ahmed", 465132465l, 45000.0d, 7820));
		database.put(566546512l, new AxisBank("Shoeb Ahmed", 566546512l, 90000.0d, 8945));
		database.put(10101l, new OperatorCard(10101l, 0000, "operator 1"));
		/* declaring various variables to be used during working of ATM machines. */
		int option = 0;
		String nextOption = null;
		long cardNumber = 0;
		double depositAmount = 0;
		double withdrawAmount = 0;
		int pin = 0;
		
		// scanner class to take inputs
		Scanner scn = new Scanner(System.in);
		while (MACHINE_ON) {
			System.out.println("please insert your debit card.");
			// taking cardnumber as input from the user.
			cardNumber = scn.nextLong();
			try {
				System.out.println("Please Enter your pin: \n");
				// taking pin number of the user for authorization.
				pin = scn.nextInt();
				card = validateCredentials(cardNumber, pin);
				ACTIVITY.add("Accessed by: " + card.getUserName() + "\nStatus: Access Granted.");
				System.out.println("WELCOME " + card.getUserName());

			} catch (InvalidPinException ipe) {
 				ACTIVITY.add("Accessed by: " + card.getUserName()+"\nActivity: incorrect pin." + "\nStatus: Access Denied.");
				System.out.println(ipe.getMessage());
				continue;
			} catch (InvalidCardException ipe) {
				ACTIVITY.add("Accessed by: " + cardNumber +"\nActivity: CardNot compatible." +  "\nStatus: Access Denied.");
				System.out.println(ipe.getMessage());
				continue;
			} catch (IncorrectPinLimitReachedException ipe) {
				ACTIVITY.add("Accessed by: " + card.getUserName() +"\nActivity: incorrect pin limit reached." +  "\nStatus: Access Denied.");
				System.out.println(ipe.getMessage());
				continue;
			}
			try {
				// if the card is a operator card then entering operator mode.
				if (card.getUserType().equals("operator")) {
					
					operatorMode(card);
					continue;
				}
				start: while (true) {
					System.out.println("=================================");
					System.out.println("|| 1.Withdraw Amount            ||");
					System.out.println("|| 2.Deposit Amount             ||" );
					System.out.println("|| 3.check balance              ||");
					System.out.println("|| 4.Change Pin                 ||");
					System.out.println("|| 5.get last five transactions ||");
					System.out.println("================================= \n");
					System.out.println("Please select a choice:");
					//taking user input
					option = scn.nextInt();

					try {

						switch (option) {
						case 1:
							System.out.println("please enter the amount you want to withdraw.");
							withdrawAmount = scn.nextDouble();
							card.withDrawAmount(withdrawAmount);
							validateAmount(withdrawAmount);
							ATM_MACHINE_BALANCE -= withdrawAmount;

							ACTIVITY.add("Accessed by: " + card.getUserName() + "\nActivity: withdrawn amount Rs"
									+ withdrawAmount + "\nStatus :transaction successfull.");
							System.out.println("==========================");
							System.out.println(
									"Amount of " + withdrawAmount + " is successfully debited from your account.");
							System.out.println("==========================");
							break;
						case 2:
							System.out.println("please enter the amount you want to deposit.");
							depositAmount = scn.nextDouble();
							validateDepositAmount(depositAmount);
							ATM_MACHINE_BALANCE += depositAmount;
							card.depositAmount(depositAmount);
							ACTIVITY.add("Accessed by: " + card.getUserName() + "\nActivity: deposited amount Rs"
									+ depositAmount + "\nStatus :transaction successfull.");
							System.out.println("==========================");
							System.out.println(
									"Amount of " + depositAmount + " is successfully credited to your account.");
							System.out.println("==========================");
							break;
						case 3:
							System.out.println("==========================");
							ACTIVITY.add("Accessed by: " + card.getUserName() + "\nActivity: checked balance");
							System.out.println("Your Current Account Balance is " + card.checkBalance());
							System.out.println("==========================");
							break;
						case 4:
							System.out.println("enter new pin number: ");
							pin = scn.nextInt();
							card.changePin(pin);
							System.out.println("==========================");
							ACTIVITY.add("Accessed by: " + card.getUserName() + "\nActivity: changed the pin.");
							System.out.println("pin changed successfully!");
							break;
						case 5:
							ACTIVITY.add("Accessed by: " + card.getUserName() + "\nActivity: viewed mini statement.");
							card.miniStatement();
							break;
						default:
							System.out.println("please enter a valid input.");
							break;

						}
						System.out.println("do you want to continue.\n press Y for yes,press N for no");
						nextOption = scn.next();
						if (nextOption.equalsIgnoreCase("N")) {
							break start;

						}
					} catch (InvalidAmountException iae) {
						ACTIVITY.add("Accessed by: " + card.getUserName()
								+ "\nActivity: entered and invalid amount.\nStatus :transaction declined.");
						System.out.println(iae.getMessage());
					} catch (InsufficientBalanceException ibe) {
						ACTIVITY.add("Accessed by: " + card.getUserName()
								+ "\nActivity: had insufficient balance \nStatus :transaction declined.");
						System.out.println(ibe.getMessage());
					} catch (InsufficientMachineBalanceException imbe) {
						ACTIVITY.add("Accessed by: " + card.getUserName()
								+ "\nActivity: Machine did not had enough money.\nStatus :transaction declined.");
						card.depositAmount(withdrawAmount);
						System.out.println(imbe.getMessage());
					}
				}
			} catch (Exception iae) {
				System.out.println(iae.getMessage());
			}
			System.out.println("=======================================================");
			System.out.println("=========Thanks for visiting HDFC ATM Machine==========");
			System.out.println("=======================================================");
		}
		System.out.println("Turning Off the Machine.");

	}

}
