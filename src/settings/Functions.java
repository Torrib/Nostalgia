package settings;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by thb on 13.07.2014.
 */
public class Functions {

    public static final int KILL = 0;
    public static final int EXIT_NOSTALGIA = 1;
    public static final int SHUTDOWN = 2;
    public static final int SLEEP = 3;
    public static final int TOGGLE_HOTKEYS = 4;
    public static final int TURN_OFF_CONTROLLERS = 5;

    private Map<String, String> functions = new LinkedHashMap<>();

    public Functions(){
        functions.put("Kill", "Kills the process");
        functions.put("Exit Nostalgia", "Turns off Nostalgia");
        functions.put("Shutdown", "Shutdown the computer");
        functions.put("Sleep", "Puts the computer in sleep mode");
        functions.put("Toggle Hotkeys", "Disables or enables hotkeys for the application)");
        functions.put("Turn off controllers", "Turns off all the controllers(Only works for Xbox360 controllers)");
    }

    public Map<String, String> getFunctions() {
        return functions;
    }

    public void setFunctions(Map<String, String> functions) {
        this.functions = functions;
    }
}
