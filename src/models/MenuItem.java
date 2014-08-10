package models;

import interfaces.Item;
import main.Main;

import java.util.ArrayList;
import java.util.List;


public class MenuItem implements Item {

    public static final int COMMAND = 0;
    public static final int SUBMENU = 1;

    private int type = COMMAND;

    private String displayName = "";
    private String message = "";
    private boolean confirmation = false;
    private List<Command> commands = new ArrayList<>();

    private SubMenu subMenu = null;

    public MenuItem(){}

    public MenuItem(String displayName, String message){
        this.type = COMMAND;
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

    public boolean isConfirmation() {
        return confirmation;
    }

    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    public SubMenu getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(SubMenu subMenu) {
        this.type = SUBMENU;
        this.subMenu = subMenu;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString(){

        if(type == SUBMENU)
            return subMenu.toString();

        for(Command command : commands){
            if(command.getCommandType() == Command.FUNCTION){
                if(command.getFunction().isToggle()){
                    if(!command.getEnableMenuText().isEmpty() && !command.getDisableMenuText().isEmpty()){
                        return Main.SETTINGS.isDisableHotkeys() ? command.getEnableMenuText() : command.getDisableMenuText();
                    }
                }
            }
        }

        return displayName;
    }
}
