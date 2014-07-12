package controller;

import com.sun.jna.NativeLong;
import com.sun.jna.Structure;
import input.Binding;
import main.Main;
import settings.Hotkey;
import settings.WindowSetting;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Set;

/**
 * Created by thb on 28.06.2014.
 */
public class Controller extends Thread{

    private final static int CONTROLLER_PULL_DELAY = 100;

    private int controllerNumber;
    private ControllerHandler.ControllerInterface controllerInterface;
    private boolean active;
    private int guideButtonCounter;
    private boolean connected;
    private ControllerHandler controllerHandler;
    private boolean connectionStatus;
    private List<Hotkey> hotkeys;
    private int[] buttonCounter;

    public Controller(int controllerNumber, ControllerHandler controllerHandler, ControllerHandler.ControllerInterface controllerInterface){
        this.controllerNumber = controllerNumber;
        this.controllerHandler = controllerHandler;
        this.controllerInterface = controllerInterface;
        active = false;
        connected = false;
        connectionStatus = false;
    }

    @Override
    public void run() {
        active = true;
        System.out.println("Controller: " + controllerNumber + " activated");
        ControllerHandler.ControllerStruct buttons = new ControllerHandler.ControllerStruct();
        buttonCounter = new int[10];
        vibrate(400);
        controllerHandler.getMain().showMessageBox("Controller " + (controllerNumber+1) + " Active", true);

        while(active){
            try {
                controllerInterface.getControllerState(controllerNumber, buttons);
                if (controllerHandler.getMain().isGuiActive()) {
                    if(controllerHandler.getMenuController() == controllerNumber) {
                        if (buttons.aButton == 1)
                            controllerHandler.getMain().pressKey(KeyEvent.VK_ENTER);
                        else if (buttons.down == 1 || analogDown(buttons))
                            controllerHandler.getMain().pressKey(KeyEvent.VK_DOWN);
                        else if (buttons.up == 1 || analogUp(buttons))
                            controllerHandler.getMain().pressKey(KeyEvent.VK_UP);
                        else if (buttons.bButton == 1)
                            controllerHandler.getMain().pressKey(KeyEvent.VK_BACK_SPACE);
                    }
                }
                else if(!hotkeys.isEmpty()){
                    int[] buttonArray =  buttons.getArray();
                    for(Hotkey hotkey : hotkeys){
                        if(buttonArray[hotkey.getButtonNumber()] == 1){
                            buttonCounter[hotkey.getButtonNumber()]++;
                        }
                        else
                            buttonCounter[hotkey.getButtonNumber()] = 0;

                        if(buttonCounter[hotkey.getButtonNumber()] == hotkey.getDelayLoops()){
                            controllerHandler.getMain().command(hotkey);
                        }
                    }
                }
                Thread.sleep(CONTROLLER_PULL_DELAY);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }


    public boolean guidePressed(){

        connectionStatus = controllerInterface.getControllerConnected(controllerNumber);

        if(connectionStatus != connected){
            if(connectionStatus) {
                controllerHandler.getMain().showMessageBox("Controller " + (controllerNumber+1) + " Connected", true);
                System.out.println("Controller " + controllerNumber + ": connected");
            }
            else {
                controllerHandler.getMain().showMessageBox("Controller " + (controllerNumber+1) + " Disconnected", true);
                System.out.println("Controller " + controllerNumber + ": disconnected");
                active = false;
            }
            connected = connectionStatus;
        }

        if(connected){
            int guideDown = controllerInterface.getGuideStatus(controllerNumber);
            if(guideDown > 0) {
                guideButtonCounter++;
            }
            else
                guideButtonCounter = 0;

            if(guideButtonCounter == 4) {
                return true;
            }
        }
        return false;
    }

    public int getControllerNumber(){
        return controllerNumber;
    }

    public boolean isActive(){
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public void setHotkeys(List<Hotkey> hotkeys){
        this.hotkeys = hotkeys;
        buttonCounter = new int[10];
    }

    private boolean analogDown(ControllerHandler.ControllerStruct buttons){
        if(buttons.leftStickX < -30000)
            return true;
        return false;
    }

    private boolean analogUp(ControllerHandler.ControllerStruct buttons){
        if(buttons.leftStickX > 30000)
            return true;
        return false;
    }

    public void vibrate(int length){
        (new Thread() {
            public void run() {
                try {
                    controllerInterface.startVibration(controllerNumber, 15000, 15000);
                    Thread.sleep(length);
                    controllerInterface.stopVibration(controllerNumber);
                }
                catch (InterruptedException e){ e.printStackTrace();}
            }
        }).start();
    }
}
