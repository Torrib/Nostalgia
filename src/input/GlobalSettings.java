package input;

import java.util.HashMap;
import java.util.Map;

public class GlobalSettings 
{
	private Map<String, String> programBindings;
	private Map<String, String> settings;
	
	public GlobalSettings()
	{
		programBindings = new HashMap<String, String>();
		settings = new HashMap<String, String>();
	}
	
	public void addProgramBinding(String key, String value)
	{
		programBindings.put(key, value);
	}
	
	public String getProgramBinding(String key)
	{
		return programBindings.get(key);
	}

	public void addSetting(String key, String value)
	{
		settings.put(key, value);
	}
	
	public String getSettings(String key)
	{
		return settings.get(key);
	}
	
	public Integer getSettingsInt(String key)
	{
		return Integer.parseInt(settings.get(key));
	}
	
	public boolean getSettingsBoolean(String key)
	{
		return Boolean.parseBoolean(settings.get(key));
	}
}
