package order.data.factory;

public class LoginLogEntity {
	private String userNo;
	private String userName;
	
	public LoginLogEntity(String no,String name)
	{
		this.userNo = no;
		this.userName = name;
	}
	
	public String GetUserNo()
	{
		return userNo;
	}
	
	public String GetUserName()
	{
		return userName;
	}
}
