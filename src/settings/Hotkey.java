package settings;

/**
 * Created by thb on 06.07.2014.
 */
public class Hotkey implements ItemInterface{

    private String button = "";
    private String command = "";
    private String message = "";
    private int delay = 2000;
    private boolean vibrate = true;

    private int buttonNumber = -1;
    private int delayLoops = -1;

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getMessage() {
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

    public int getButtonNumber() {
        return buttonNumber;
    }

    public void setButtonNumber(int buttonNumber) {
        this.buttonNumber = buttonNumber;
    }

    public int getDelayLoops() {
        return delayLoops;
    }

    public void setDelayLoops(int delayLoops) {
        this.delayLoops = delayLoops;
    }

    @Override
    public boolean vibrate() {
        return vibrate;
    }

    @Override
    public String toString(){
        return button;
    }
}
