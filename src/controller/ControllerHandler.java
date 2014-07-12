package controller;

import com.sun.jna.*;
import input.Binding;
import main.Main;
import settings.Hotkey;

import java.util.*;

public class ControllerHandler extends Thread{

    private Main main;
    private ControllerInterface controllerInterface;
    private List<Controller> controllers;
    private int menuController;

    public ControllerHandler(Main main) {
        this.main = main;
        try{
            this.controllerInterface = ControllerInterface.INSTANCE;
        }
        catch (Exception e){
            main.log(e.getMessage());
            main.log(e.toString());
        }

        controllers = new ArrayList<Controller>();
        controllers.add(new Controller(0, this, controllerInterface));
        if(!main.getSettings().isDisableControllers()) {
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
        main.log("Controller handler started");
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
                e.printStackTrace();
            }
        }
    }

    public void setHotkeys(List<Hotkey> hotkeys){
        for(Controller controller : controllers)
            controller.setHotkeys(hotkeys);
    }

    public interface ControllerInterface extends Library {
        ControllerInterface INSTANCE = (ControllerInterface) Native.loadLibrary("Controller.dll", ControllerInterface.class);
        boolean initController();
        boolean getControllerConnected(int controller);
        int getGuideStatus(int controller);
        int getControllerState(int controller, ControllerStruct controllerStruct);
        void startVibration(int controller, int leftMotor, int rightMotor);
        void stopVibration(int controller);
        void turnControllerOff(int controller);
        void close();
    }

    public static class ControllerStruct extends Structure{
        public int up, down, left, right, start, back;
        public int aButton, bButton, xButton, yButton, guideButton;
        public int l1, l2, l3, r1, r2, r3;
        public int leftStickY, leftStickX, rightStickY, rightStickX;

        public int[] getArray(){
            return new int[] {aButton, bButton, yButton, xButton, l1, r1, back, start, l3, r3 };
        }
        protected List getFieldOrder() {
            return Arrays.asList(new String[] {"up", "down",  "left", "right", "start", "back",
                    "aButton", "bButton", "xButton", "yButton", "guideButton",
                    "l1", "l2", "l3", "r1", "r2", "r3",
                    "leftStickY", "leftStickX", "rightStickY", "rightStickX"

            });
        }
    }

    public Main getMain(){
        return main;
    }

    public int getMenuController(){
        return menuController;
    }
}

