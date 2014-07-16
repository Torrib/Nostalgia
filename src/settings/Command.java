package settings;

import com.sun.javafx.scene.input.KeyCodeMap;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Created by thb on 13.07.2014.
 */
public class Command {

    public static final int KEY = 0;
    public static final int FUNCTION = 1;
    public static final int PROGRAM = 2;

    private int commandType = 0;

    private boolean ctrl = false;
    private boolean alt = false;
    private boolean shift = false;
    private KeyCode keyCode;

    private int function = 0;
    private String functionName = "";

    private int delay = 200;

    public Command(){

    }

    public Command(boolean ctrl, boolean alt, boolean shift, KeyCode keyCode, int delay) {
        this.commandType = 0;
        this.ctrl = ctrl;
        this.alt = alt;
        this.shift = shift;
        this.keyCode = keyCode;
        this.delay = delay;
    }

    public Command(int function, int delay, String functionName){
        this.commandType = 1;
        this.function = function;
        this.delay = delay;
        this.functionName = functionName;
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

    public int getFunction() {
        return function;
    }

    public void setFunction(int function) {
        this.function = function;
    }



    public String toString(){
        String result = "";

        if(commandType == 0) {
            result += ctrl ? "CTRL+" : "";
            result += alt ? "ALT+" : "";
            result += shift ? "Shift+" : "";
            result += keyCode.toString();
            return result;
        }
        else if(commandType == 1){
            return functionName + "()";
        }
        else
            return "-";
    }
}
