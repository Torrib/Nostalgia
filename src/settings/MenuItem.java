package settings;

import interfaces.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thb on 06.07.2014.
 */
public class MenuItem implements Item {

    private String displayName = "";
    private String message = "";
    private String command = "";

    List<Command> commands = new ArrayList<>();

    public MenuItem(){}

    public MenuItem(String displayName, String message, String command){
        this.displayName = displayName;
        this.message = message;
        this.command = command;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
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
        return displayName;
    }
}
