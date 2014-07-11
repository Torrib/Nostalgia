package settings;

/**
 * Created by thb on 06.07.2014.
 */
public class MenuItem implements ItemInterface{

    private String displayName = "";
    private String message = "";
    private String command = "";

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

    @Override
    public String toString(){
        return displayName;
    }
}
