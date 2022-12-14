import java.util.*;

public class User {
	private String username;
	private LinkedHashSet<String> friends;
	private LinkedHashMap<String, String> values;
	private LinkedHashMap<String, Long> valueTimestamps;


	public User(String username)
	{
		this.username = username;
		this.friends = new LinkedHashSet<String>(); 
		this.values = new LinkedHashMap<String, String>();
		this.valueTimestamps = new LinkedHashMap<String, Long>();
	}

	public User(String username, Long timestamp, LinkedHashMap<String, String> values)
	{
		this.username = username;
		this.friends = new LinkedHashSet<String>(); 
		this.values = values;
		this.valueTimestamps = new LinkedHashMap<String, Long>();

		for (Map.Entry<String, String> set: values.entrySet())
		{
			valueTimestamps.put(set.getKey(), timestamp);
		}
	}

	public String getUsername() 
	{
		return username;
	}

	public LinkedHashSet<String> getFriends()
	{
		return friends;
	}

	public LinkedHashMap<String, String> getValues()
	{
		return values;
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

	public boolean hasFriends()
	{
		return friends.size() != 0;
	}

	public LinkedHashMap<String, String> updateValues(Long timestamp, LinkedHashMap<String, String> values)
	{
		LinkedHashMap<String, String> broadcastValueList = new LinkedHashMap<String, String>();

		for (Map.Entry<String, String> set: values.entrySet())
		{
			String key = set.getKey();
			String value = set.getValue(); 

			if (!this.values.containsKey(key))
			{
				this.values.put(key, value);
				this.valueTimestamps.put(key, timestamp);
				broadcastValueList.put(key, value);
			} else {
				if (timestamp > valueTimestamps.get(set.getKey()))
				{
					this.values.replace(key, value);
					this.valueTimestamps.replace(key, timestamp);
					broadcastValueList.put(key, value);
				}
			}
		}

		return broadcastValueList;
	}
}