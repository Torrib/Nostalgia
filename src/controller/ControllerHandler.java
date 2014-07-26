package controller;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import main.Main;
import models.Hotkey;

import java.util.*;

public class ControllerHandler extends Thread{

    private Main main;
    private ControllerInterface controllerInterface;
    private List<Controller> controllers;
    private int menuController;

    public ControllerHandler(Main main) {
        this.main = main;
        try{
            if(Platform.is64Bit()) {
                this.controllerInterface = (ControllerInterface) Native.loadLibrary("Controller64.dll", ControllerInterface.class);
                main.log("Controller64.dll loaded");
            }
            else {
                this.controllerInterface = (ControllerInterface) Native.loadLibrary("Controller.dll", ControllerInterface.class);
                main.log("Controller.dll loaded");
            }
        }
        catch (Exception e){
            main.log(e.getMessage());
            main.log(e.toString());
        }

        controllers = new ArrayList<>();
        controllers.add(new Controller(0, this, controllerInterface));
        if(!Main.SETTINGS.isDisableControllers()) {
            controllers.add(new Controller(1, this, controllerInterface));
            controllers.add(new Controller(2, this, controllerInterface));
            controllers.add(new Controller(3, this, controllerInterface));
        }
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
}

