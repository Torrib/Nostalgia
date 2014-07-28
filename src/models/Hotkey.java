package models;

import interfaces.Item;
import main.Main;

import java.util.ArrayList;
import java.util.List;

public class Hotkey implements Item {

    private String name = "";
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
        for(Command command : commands) {
            if (command.getCommandType() == Command.FUNCTION) {
                if (command.getFunction().isToggle()) {
                    if (command.getEnableMessage() != null && command.getDisableMessage() != null) {
                        return Main.SETTINGS.isDisableHotkeys() ? command.getDisableMessage() : command.getEnableMessage();
                    }
                }
            }
        }
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if(!name.isEmpty())
            return name + "(" + getButtonList()[button] + ")";
        else
            return getButtonList()[button];
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }


}
