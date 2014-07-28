package controller;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import main.Main;
import models.Hotkey;

import java.util.*;

public class ControllerHandler extends Thread{

    private Main main;
    private ControllerInterface controllerInterface;
    private List<Controller> controllers = new ArrayList<>();
    private int menuController;

    public ControllerHandler(Main main) {
        this.main = main;
        try{
            if(Platform.is64Bit()) {
                this.controllerInterface = (ControllerInterface) Native.loadLibrary("windows/Controller64.dll", ControllerInterface.class);
                main.log("Controller64.dll loaded");
            }
            else {
                this.controllerInterface = (ControllerInterface) Native.loadLibrary("windows/Controller.dll", ControllerInterface.class);
                main.log("Controller.dll loaded");
            }
        }
        catch (Exception e){
            main.log(e.getMessage());
            main.log(e.toString());
        }
        updateControllers();
    }

    public boolean load(){
        main.log("Loading controller interface");
        return controllerInterface.initController();
    }

    @Override
    public void run() {
        while(true) {
            try {
                for (Controller controller : controllers) {
                    if (controller.guidePressed()) {
                        if(controller.isActive()) {
                            menuController = controller.getControllerNumber();
                            main.showMenu(menuController);
                        }
                        else
                            controller.start();
                    }
                }
                Thread.sleep(500);
            }
            catch (InterruptedException e){
                main.log(e.toString());
                e.printStackTrace();
            }
        }
    }

    public void setHotkeys(List<Hotkey> hotkeys){
        for(Controller controller : controllers)
            controller.setHotkeys(hotkeys);
    }

    public Main getMain(){
        return main;
    }

    public int getMenuController(){
        return menuController;
    }

    public void updateControllers(){
        List<Boolean> controllerState = Main.SETTINGS.getControllerDisabledStatus();
        for(int i = 0; i < controllers.size(); i++){
            if(controllerState.get(controllers.get(i).getControllerNumber())){
                controllers.get(i).setActive(false);
                controllers.remove(i);
                i--;
            }
        }

        for(int i = 0; i < controllerState.size(); i++){
            if(!controllerState.get(i)){
                boolean controllerAdded = false;
                for(Controller controller : controllers){
                    if (controller.getControllerNumber() == i)
                        controllerAdded = true;
                }
                if(!controllerAdded)
                    controllers.add(new Controller(i, this, controllerInterface));
            }
        }
    }

    public void turnOffControllers(){
        for(int i = 0; i < 4; i++){
            controllerInterface.turnControllerOff(i);
        }
    }
}

