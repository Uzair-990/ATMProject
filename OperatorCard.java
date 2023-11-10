package core_java_topics.exceptionHandling.ATMProject.BankCards;

import core_java_topics.exceptionHandling.ATMProject.customExceptions.InsufficientBalanceException;
import core_java_topics.exceptionHandling.ATMProject.customExceptions.InvalidAmountException;
import core_java_topics.exceptionHandling.ATMProject.customExceptions.NotAOperatorException;
import core_java_topics.exceptionHandling.ATMProject.interfaces.IATMService;

public class OperatorCard implements IATMService {
   private int pinNumber;
   private  long id;
    private String name ;
    private final String type="operator";//declared as final as this can't be changed.
	public OperatorCard(long idn,int pinN,String name) {
		id=idn;
		pinNumber=pinN;
		this.name=name;
	}
	@Override
	public double withDrawAmount(double wthAmt) throws InvalidAmountException, InsufficientBalanceException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void depositAmount(double dptAmt) throws InvalidAmountException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double checkBalance() {
		// TODO Auto-generated method stub
		return 0;
	}
	public String getName() {
		return name;
	}
    public String getType() {
    	return type;
    }
	@Override
	
	public void changePin(int pin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPinNumber() {
		// TODO Auto-generated method stub
		return pinNumber;
	}
	@Override
	public String getUserType() throws NotAOperatorException {
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
		
	}
	@Override
	public int getChances() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void resetChances() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void miniStatement() {
		// TODO Auto-generated method stub
		
	}

}
