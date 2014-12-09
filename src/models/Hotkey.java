package models;

import interfaces.Item;
import jgamepad.enums.Button;
import main.Main;

import java.util.ArrayList;
import java.util.List;

public class Hotkey implements Item {

    private String name = "";
    private List<Button> buttons = new ArrayList<>();
    private String message = "";
    private int delay = 2000;
    private boolean vibrate = true;

    private List<Command> commands = new ArrayList<>();

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    public void addButton(Button button){
        buttons.add(button);
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


    @Override
    public String toString(){
        if(!name.isEmpty())
            return name + "(" + getButtonString() + ")";
        else
            return getButtonString();
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    public void addCommand(Command command){
        commands.add(command);
    }

    private String getButtonString(){
        String buttonString = "";
        for(Button button : buttons){
            buttonString += button.toString() + "+";
        }
        if(buttonString.length() > 0){
            buttonString = buttonString.substring(0, buttonString.length() -1);
        }
        else{
            buttonString = "-";
        }
        return buttonString;
    }
}
