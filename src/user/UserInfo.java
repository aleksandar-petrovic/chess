//Aleksandar Petrovic

package user;

import java.io.Serializable;

public class UserInfo implements Serializable, Comparable<UserInfo>
{
	private static final long serialVersionUID = 1L;
	private String name;
	private String pass;
	private String avatar;
	private int wins;
	private int losses;
	private String host;

	public UserInfo(String name, String pass, String avatar, int wins, int losses, String host)
	{
		this.name = name;
		this.pass=pass;
		this.avatar = avatar;
		this.wins=wins;
		this.losses=losses;
		this.host = host;
	}

	public String getName()
	{
		return name;
	}
	
	public int getWins()
	{
		return wins;
	}
	
	public void setWins(int wins){
		this.wins=wins;
	}
	
	public void addWin()
	{
		wins++;
	}
	
	public int getLosses()
	{
		return losses;
	}
	
	public void setLosses(int losses){
		this.losses=losses;
	}
	
	public void addLoss()
	{
		losses++;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public void setAvatar(String avatar){
		this.avatar=avatar;
	}
	
	public String getPass()
	{
		return pass;
	}

	public String getAvatar()
	{
		return avatar;
	}

	public String getHost()
	{
		return host;
	}
	
	public void setHost(String host){
		this.host=host;
	}

	@Override
	public int hashCode()
	{
		return host.hashCode();
	}

	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserInfo other = (UserInfo) obj;
		return name.toLowerCase().equals(other.name.toLowerCase());
	}

	@Override
	public int compareTo(UserInfo o) {
		if(getWins()>o.getWins())	
			return -1;
		return 0;
	}
	
}
