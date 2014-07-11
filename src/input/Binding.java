package input;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Binding {

    private final static int CONTROLLER_PULL_DELAY = 100;

	private String name;
	private Map<String, Boolean> settings;
	private Map<String, String> bindings;
	private Map<String, String> bindingMessages;
	private Map<Integer, String> hotkeys;
	private Map<Integer, String> hotkeyMessages;
	private Map<Integer, Integer> hotkeyDelay;
	
	public Binding(String name)
	{
		this.name = name;
		settings = new HashMap<String, Boolean>();
		bindings = new LinkedHashMap<String, String>();
		bindingMessages = new HashMap<String, String>();
		hotkeys = new LinkedHashMap<Integer, String>();
		hotkeyMessages = new HashMap<Integer, String>();
        hotkeyDelay = new HashMap<Integer, Integer>();
	}
	
	public void addBinding(String key, String value)
	{
		bindings.put(key, value);
	}
	
	public String getBinding(String key)
	{
		return bindings.get(key);
	}
	
	public void addMessage(String key, String value)
	{
		bindingMessages.put(key, value);
	}
	
	public String getMessage(String key)
	{
		return bindingMessages.get(key);
	}
	
	public void addHotkey(int key, String value)
	{
		hotkeys.put(key, value);
	}
	
	public String getHotkey(int key)
	{
		return hotkeys.get(key);
	}
	
	public boolean hasHotkeys()
	{
		return !hotkeys.isEmpty();
	}
	
	public Set<Integer> getHotkeys()
	{
		return hotkeys.keySet();
	}
	
	public void addHotkeyMessage(Integer key, String value)
	{
		hotkeyMessages.put(key, value);
	}
	
	public String getHotkeyMessage(Integer key)
	{
		return hotkeyMessages.get(key);
	}

    public void addHotkeyDelay(Integer key, Integer value)
    {
        hotkeyDelay.put(key, (int)value / CONTROLLER_PULL_DELAY);
    }

    public Integer getHotkeyDelay(Integer key)
    {
        return hotkeyDelay.get(key);
    }

	public String getName()
	{
		return name;
	}

	public Set<String> getCommands()
	{
		return bindings.keySet();
	}
	
	public void addSettings(String key, Boolean value)
	{
		settings.put(key, value);
	}
	
	public Boolean getSetting(String key)
	{
		if(settings.containsKey(key))
			return settings.get(key);
		return false;
	}
}
