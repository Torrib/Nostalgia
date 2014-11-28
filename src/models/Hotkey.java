package models;

import interfaces.Item;
import jgamepad.enums.Button;
import main.Main;

import java.util.ArrayList;
import java.util.List;

public class Hotkey implements Item {

    private String name = "";
    private Button button;
    private String message = "";
    private int delay = 2000;
    private boolean vibrate = true;

    List<Command> commands = new ArrayList<>();

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public int getButtonValue(){
        if(button != null){
            return button.value;
        }
        return 0;
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

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
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
            return name + "(" + getButtonList()[getButtonValue()] + ")";
        else
            return getButtonList()[getButtonValue()];
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }


}
