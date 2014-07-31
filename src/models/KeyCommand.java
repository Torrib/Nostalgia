package models;

import javafx.scene.input.KeyCode;

public class KeyCommand {
    private boolean ctrl = false;
    private boolean alt = false;
    private boolean shift = false;
    private boolean windows = false;
    //TODO other OS buttons
    private KeyCode keyCode;

    public KeyCommand(){}

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

    public boolean isWindows() {
        return windows;
    }

    public void setWindows(boolean windows) {
        this.windows = windows;
    }

    @Override
    public String toString(){
        String result = "";
        result += ctrl ? "CTRL+" : "";
        result += alt ? "ALT+" : "";
        result += shift ? "Shift+" : "";
        result += windows ? "Windows+" : "";
        result += keyCode.toString();
        return result;
    }
}
