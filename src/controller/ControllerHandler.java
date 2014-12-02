package controller;

import jgamepad.enums.Button;
import jgamepad.listeners.ButtonHoldListener;
import main.Logger;
import main.Main;
import models.Hotkey;

import java.util.ArrayList;
import java.util.List;

public class ControllerHandler extends Thread{
    private Main main;
    private List<Controller> controllers = new ArrayList<>();
    private List<Hotkey> activeHotkeys = new ArrayList<>();

    public ControllerHandler(Main main){
        this.main = main;
        jgamepad.Controller.dllPath = System.getProperty("user.dir") + "\\windows";
    }

    @Override
    public void run() {
        for(int i = 0; i <= 3; i++) {
            if (!Main.SETTINGS.getControllerDisabledStatus().get(i)) {
                addController(i);
            }
        }
    }

    public void setHotkeys(List<Hotkey> hotkeys){
        activeHotkeys = hotkeys;
        for(Controller controller : controllers) {
            controller.changeHotkeyListeners(hotkeys);
        }
    }

    public void turnOffControllers(){
        for(Controller controller : controllers){
            controller.turnOff();
        }
    }

    public void updateControllerDisabledStatus(){
        List<Boolean> statuses = Main.SETTINGS.getControllerDisabledStatus();

        //Disable currently running controllers that have been disabled
        for(int i = 0; i < controllers.size(); i++){
            Controller controller = controllers.get(i);
            if(statuses.get(controller.getControllerNumber())){
                Logger.log("Disabling controller " + i);
                controller.stop();
                controllers.remove(controller);
                i--;
            }
        }

        //Enable previously disabled controllers
        for(int i = 0; i < statuses.size(); i++){
            if(!statuses.get(i) && controllerDisabled(i)){
                Logger.log("Enabling controller " + i);
                addController(i);
            }
        }
    }

    private boolean controllerDisabled(int i){
        for(Controller controller : controllers){
            if(controller.getControllerNumber() == i){
                return false;
            }
        }
        return true;
    }

    private void addController(int controllerNumber){
        Controller controller = new Controller(controllerNumber, main);

        controller.addConnectionChangedEvent(connected -> {
            if(connected)
                main.showMessageBox("Controller " + (controllerNumber +1) + " connected", true);
            else
                main.showMessageBox("Controller " + (controllerNumber +1) + " disconnected", true);
        });

        if (Main.SETTINGS.isRequireActivate()) {
            controller.addButtonListener(new ButtonHoldListener(Button.GUIDE, 2000,
                    () -> activateController(controller)));
        } else
            activateController(controller);
    }

    private void activateController(Controller controller){
        controllers.add(controller);
        controller.activate();
        controller.changeHotkeyListeners(activeHotkeys);
    }
}
