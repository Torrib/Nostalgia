package models;

import javafx.scene.input.KeyCode;

public class Command {

    public static final int KEY = 0;
    public static final int FUNCTION = 1;
    public static final int PROGRAM = 2;

    private int commandType = KEY;

    private boolean ctrl = false;
    private boolean alt = false;
    private boolean shift = false;
    private KeyCode keyCode;

    private Function function;

    private String enableDisplay = "";
    private String disableDisplay = "";
    private String enableMessage = "";
    private String disableMessage = "";

    private Program program;

    private int delay = 200;

    public Command(){

    }

    public Command(boolean ctrl, boolean alt, boolean shift, KeyCode keyCode, int delay) {
        this.commandType = KEY;
        this.ctrl = ctrl;
        this.alt = alt;
        this.shift = shift;
        this.keyCode = keyCode;
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


    public boolean isCtrl() {
        return ctrl;
    }

    public void setCtrl(boolean ctrl) {
        this.ctrl = ctrl;
    }

    public boolean isAlt() {
        return alt;
    }

    public void setAlt(boolean alt) {
        this.alt = alt;
    }

    public boolean isShift() {
        return shift;
    }

    public void setShift(boolean shift) {
        this.shift = shift;
    }

    public KeyCode getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(KeyCode keyCode) {
        this.keyCode = keyCode;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getCommandType() {
        return commandType;
    }

    public void setCommandType(int commandType) {
        this.commandType = commandType;
    }

    public Function getFunction() {
        return function;
    }


    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public String getEnableDisplay() {
        return enableDisplay;
    }

    public void setEnableDisplay(String enableDisplay) {
        this.enableDisplay = enableDisplay;
    }

    public String getDisableDisplay() {
        return disableDisplay;
    }

    public void setDisableDisplay(String disableDisplay) {
        this.disableDisplay = disableDisplay;
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

    public String toString(){
        String result = "";

        switch (commandType) {
            case KEY:
                result += ctrl ? "CTRL+" : "";
                result += alt ? "ALT+" : "";
                result += shift ? "Shift+" : "";
                result += keyCode.toString();
                return result;
            case FUNCTION:
                return function.getName() + "()";
            case PROGRAM:
                return "Run: " + program.toString();
        }
        return "-";
    }
}
