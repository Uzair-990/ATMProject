package core_java_topics.exceptionHandling.ATMProject.customExceptions;

public class IncorrectPinLimitReachedException extends Exception {
	
	public IncorrectPinLimitReachedException(String message){
		super(message);
	}
}
