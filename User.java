import java.util.*;

public class User {
	private String username;
	private HashSet<String> friends;
	private LinkedHashMap<String, String> values;
	private LinkedHashMap<String, Integer> valueTimestamps;

	public User(String username)
	{
		this.username = username;
		this.friends = new HashSet<String>(); 
		this.values = new LinkedHashMap<String, String>();
		this.valueTimestamps = new LinkedHashMap<String, Integer>();
	}

	public String getUsername() 
	{
		return username;
	}

	public boolean equals(Object obj) 
	{
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		User user = (User) obj;
		return username.equals(user.username);
	}

	public void addFriend(String username) 
	{
		friends.add(username);
	}
	
	public void deleteFriend(String username) 
	{
		friends.remove(username);
	}
}