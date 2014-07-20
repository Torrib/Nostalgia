package models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

    private List<Function> functions = new ArrayList<>();

    public Functions(){
        functions.add(new Function(KILL, "Kill", "Kills the process"));
        functions.add(new Function(EXIT_NOSTALGIA, "Exit Nostalgia", "Turns off Nostalgia"));
        functions.add(new Function(SHUTDOWN, "Shutdown", "Shutdown the computer"));
        functions.add(new Function(SLEEP, "Sleep", "Puts the computer in sleep mode"));
        functions.add(new Function(TOGGLE_HOTKEYS, "Toggle Hotkeys", "Disables or enables hotkeys for the application)", true));
        functions.add(new Function(TURN_OFF_CONTROLLERS, "Turn off controllers", "Turns off all the controllers(Only works for Xbox360 controllers)"));
    }

    public List<Function> getFunctions() {
        return functions;
    }
}
