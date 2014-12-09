package models;

public class Command {

    public static final int KEY = 0;
    public static final int FUNCTION = 1;
    public static final int PROGRAM = 2;

    private int commandType = KEY;

    private KeyCommand keyCommand;

    private Function function;

    private String enableMenuText = "";
    private String disableMenuText = "";
    private String enableMessage = "";
    private String disableMessage = "";

    private Program program;

    private int delay = 200;

    public Command(){

    }

    public Command(KeyCommand keyCommand, int delay) {
        this.commandType = KEY;
        this.keyCommand = keyCommand;
        this.delay = delay;
    }

    public Command(Function function, int delay){
        this.commandType = FUNCTION;
        this.function = function;
        this.delay = delay;
    }

    public Command(Program program, int delay){
        this.commandType = PROGRAM;
        this.program = program;
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    public int getCommandType() {
        return commandType;
    }

    public Function getFunction() {
        return function;
    }


    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.commandType = PROGRAM;
        this.program = program;
    }

    public void setKeyCommand(KeyCommand keyCommand){
        this.commandType = KEY;
        this.keyCommand = keyCommand;
    }

    public void setFunction(Function function){
        this.commandType = FUNCTION;
        this.function = function;
    }

    public void setDelay(int delay){
        this.delay = delay;
    }

    public String getEnableMenuText() {
        return enableMenuText;
    }

    public void setEnableMenuText(String enableMenuText) {
        this.enableMenuText = enableMenuText;
    }

    public String getDisableMenuText() {
        return disableMenuText;
    }

    public void setDisableMenuText(String disableMenuText) {
        this.disableMenuText = disableMenuText;
    }

    public String getEnableMessage() {
        return enableMessage;
    }

    public void setEnableMessage(String enableMessage) {
        this.enableMessage = enableMessage;
    }

    public String getDisableMessage() {
        return disableMessage;
    }

    public void setDisableMessage(String disableMessage) {
        this.disableMessage = disableMessage;
    }

    public KeyCommand getKeyCommand() {
        return keyCommand;
    }

    public String toString(){
        switch (commandType) {
            case KEY:
                return keyCommand.toString();
            case FUNCTION:
                return function.getName() + "()";
            case PROGRAM:
                return "Run: " + program.toString();
        }
        return "-";
    }
}
