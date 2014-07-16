package settings;

import interfaces.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thb on 06.07.2014.
 */
public class Hotkey implements Item {

    private int button = 0;
    private String message = "";
    private int displayTime = 2000;
    private boolean vibrate = true;

    List<Command> commands = new ArrayList<>();

    private int delayLoops = -1;

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(int displayTime) {
        this.displayTime = displayTime;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public int getDelayLoops() {
        return delayLoops;
    }

    public void setDelayLoops(int delayLoops) {
        this.delayLoops = delayLoops;
    }

    @Override
    public List<Command> getCommands() {
        return commands;
    }

    @Override
    public boolean vibrate() {
        return vibrate;
    }

    public String[] getButtonList(){
        return new String[] { "A/Cross", "B/Circle", "Y/Triangle", "X/Square", "L1", "R1", "Back/Select", "Start", "L3", "R3"};
    }

    @Override
    public String toString(){
        return getButtonList()[button];
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }


}
