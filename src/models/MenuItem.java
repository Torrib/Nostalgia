package models;

import interfaces.Item;
import main.Main;
import models.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thb on 06.07.2014.
 */
public class MenuItem implements Item {

    private String displayName = "";
    private String message = "";

    List<Command> commands = new ArrayList<>();

    public MenuItem(){}

    public MenuItem(String displayName, String message){
        this.displayName = displayName;
        this.message = message;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public boolean vibrate() {
        return false;
    }

    @Override
    public String toString(){

        for(Command command : commands){
            if(command.getCommandType() == Command.FUNCTION){
                if(command.getFunction().isToggle()){
                    if(command.getEnableDisplay() != null && command.getDisableDisplay() != null){
                        return Main.SETTINGS.isDisableHotkeys() ? command.getEnableDisplay() : command.getDisableDisplay();
                    }
                }
            }
        }

        return displayName;
    }
}
