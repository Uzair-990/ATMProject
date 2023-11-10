package core_java_topics.exceptionHandling.ATMProject.customExceptions;

public class InvalidAmountException extends Exception {
	/*creating a custom exception by extending the Exception class.*/
	public InvalidAmountException(String message) {
		super(message);
	}
}
