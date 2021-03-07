package client.error;

@SuppressWarnings("serial")
public class AccountAndPasswordErrorException extends Exception {
	private String message = null;
	
	public AccountAndPasswordErrorException(int index){
		if(index == 0)
			message = "账号与密码错误";
		if(index == 1)
			message = "账号与密码不能为空";
		if(index == 2)
			message = "其他人已注册此账号";
	}
	
	public String getMessage(){
		return message;
	}
}
