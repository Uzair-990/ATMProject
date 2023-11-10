package core_java_topics.exceptionHandling.ATMProject.customExceptions;

public class InsufficientBalanceException extends Exception {
	/*creating a custom exception by extending the Exception class.*/

public InsufficientBalanceException(String message) {
	super(message);
}
}
