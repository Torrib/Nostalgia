package controller;

import main.Logger;
import main.Main;
import models.Hotkey;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Controller extends Thread{

    private int controllerNumber;
    private boolean active;
    private int guideButtonCounter;
    private boolean connected;
    private ControllerHandler controllerHandler;
    private List<Hotkey> hotkeys = new ArrayList<>();
    private int[] buttonCounter = new int[10];
    private int xCounter = 0;
    private int bCounter = 0;

    public Controller(int controllerNumber, ControllerHandler controllerHandler){
        this.controllerNumber = controllerNumber;
        this.controllerHandler = controllerHandler;
    }

    @Override
    public void run() {
        active = true;
        ControllerStructure buttons = new ControllerStructure();
        vibrate(400);
        messageBox("Controller " + (controllerNumber+1) + " Active");

        while(active){
            try {
                ControllerInput.ci.getControllerState(controllerNumber, buttons);
                if (controllerHandler.getMain().isGuiActive()) {
                    if(controllerHandler.getMenuController() == controllerNumber) {
                        if (buttons.aButton == 1) {
                            if(xCounter == 0) {
                                controllerHandler.getMain().pressKey(KeyEvent.VK_ENTER);
                            }
                            xCounter++;
                        }
                        else if(buttons.aButton != 1)
                            xCounter = 0;
                        if (buttons.down == 1 || analogDown(buttons))
                            controllerHandler.getMain().pressKey(KeyEvent.VK_DOWN);
                        else if (buttons.up == 1 || analogUp(buttons))
                            controllerHandler.getMain().pressKey(KeyEvent.VK_UP);
                        else if (buttons.left == 1)
                            controllerHandler.getMain().pressKey(KeyEvent.VK_LEFT);
                        else if (buttons.right == 1)
                            controllerHandler.getMain().pressKey(KeyEvent.VK_RIGHT);

                        if (buttons.bButton == 1){
                            if(bCounter == 0) {
                                controllerHandler.getMain().pressKey(KeyEvent.VK_BACK_SPACE);
                            }
                            bCounter++;
                        }
                        else
                            bCounter = 0;
                    }
                }
                else if(checkHotkeys()){
                    int[] buttonArray =  buttons.getArray();
                    for(Hotkey hotkey : hotkeys){
                        if(buttonArray[hotkey.getButton()] == 1){
                            buttonCounter[hotkey.getButton()]++;
                        }
                        else
                            buttonCounter[hotkey.getButton()] = 0;

                        if(buttonCounter[hotkey.getButton()] == hotkey.getDelayLoops()){
                            controllerHandler.getMain().command(hotkey, this);
                        }
                    }
                }
                Thread.sleep(Main.SETTINGS.getControllerPullDelay());
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }


    public boolean guidePressed(){

        boolean connectionStatus = ControllerInput.ci.getControllerConnected(controllerNumber);

        if(connectionStatus != connected){
            if(connectionStatus) {
                messageBox("Controller " + (controllerNumber+1) + " Connected");
                if(!Main.SETTINGS.isRequireActivate())
                    this.start();
            }
            else {
                messageBox("Controller " + (controllerNumber+1) + " Disconnected");
                active = false;
            }
            connected = connectionStatus;
        }

        if(connected){
            int guideDown = ControllerInput.ci.getGuideStatus(controllerNumber);
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

    public void setHotkeys(List<Hotkey> hotkeys){
        this.hotkeys = hotkeys;
        buttonCounter = new int[10];
    }

    private boolean analogDown(ControllerStructure buttons){
        return buttons.leftStickX < -30000;
    }

    private boolean analogUp(ControllerStructure buttons){
        return buttons.leftStickX > 30000;
    }

    public void vibrate(int length){
        if(Main.SETTINGS.isDisableVibration())
            return;

        (new Thread() {
            public void run() {
                try {
                    ControllerInput.ci.startVibration(controllerNumber, 15000, 15000);
                    Thread.sleep(length);
                    ControllerInput.ci.stopVibration(controllerNumber);
                }
                catch (InterruptedException e){ e.printStackTrace();}
            }
        }).start();
    }

    public boolean checkHotkeys(){
        return(!hotkeys.isEmpty() &&
                !Main.SETTINGS.isDisableHotkeys() &&
                !controllerHandler.getMain().getActiveWindowSettings().isDisableHotkeys());
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private void messageBox(String message){
        Logger.log(message);
        controllerHandler.getMain().showMessageBox(message, true);
    }
}
