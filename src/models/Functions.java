package models;

import java.util.ArrayList;
import java.util.List;

public class Functions {

    public static final int KILL = 0;
    public static final int EXIT_NOSTALGIA = 1;
    public static final int SHUTDOWN = 2;
    public static final int SLEEP = 3;
    public static final int TOGGLE_HOTKEYS = 4;
    public static final int TURN_OFF_CONTROLLERS = 5;
    public static final int INCREASE_VOLUME = 6;
    public static final int DECREASE_VOLUME = 7;
    public static final int MUTE = 8;
    public static final int FREE_ROAM = 9;
    public static final int SHOW_MENU = 10;

    public static List<Function> functions = new ArrayList<>();

    static {
        functions.add(new Function(KILL, "Kill", "Kills the process"));
        functions.add(new Function(EXIT_NOSTALGIA, "Exit Nostalgia", "Turns off Nostalgia"));
        functions.add(new Function(SHUTDOWN, "Shutdown", "Shutdown the computer"));
        functions.add(new Function(SLEEP, "Sleep", "Puts the computer in sleep mode"));
        functions.add(new Function(TOGGLE_HOTKEYS, "Toggle Hotkeys", "Disables or enables hotkeys for the application)", true));
        functions.add(new Function(TURN_OFF_CONTROLLERS, "Turn off controllers", "Turns off all the controllers(Only works for Xbox360 controllers)"));
        functions.add(new Function(INCREASE_VOLUME, "Increase Volume", "Increases the master volume"));
        functions.add(new Function(DECREASE_VOLUME, "Decrease Volume", "Decreases the master volume"));
        functions.add(new Function(MUTE, "Toggle Mute", "Mute the sound"));
        functions.add(new Function(FREE_ROAM, "Free Roam", "Enables \"Free Roam\" allowing free control of the controller"));
        functions.add(new Function(SHOW_MENU, "Show menu", "Displays the menu"));
    }
}
