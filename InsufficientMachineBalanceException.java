package core_java_topics.exceptionHandling.ATMProject.customExceptions;

public class InsufficientMachineBalanceException extends Exception {
	public InsufficientMachineBalanceException(String message) {
		super(message);
	}

}
