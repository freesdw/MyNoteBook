package client.error;

@SuppressWarnings("serial")
public class AccountAndPasswordErrorException extends Exception {
	private String message = null;
	
	public AccountAndPasswordErrorException(int index){
		if(index == 0)
			message = "�˺����������";
		if(index == 1)
			message = "�˺������벻��Ϊ��";
		if(index == 2)
			message = "��������ע����˺�";
	}
	
	public String getMessage(){
		return message;
	}
}
