package util; 

public class AccessToken {

	long expires_on;
	String accesstoken;

	public long getExpires_on()
	{
		return expires_on;
	}
	public void setExpires_on(long expires_on)
	{
		this.expires_on = expires_on;
	}
	public String getAccesstoken()
	{
		return accesstoken;
	}
	public void setAccesstoken(String accesstoken)
	{
		this.accesstoken = accesstoken;
	}

}
