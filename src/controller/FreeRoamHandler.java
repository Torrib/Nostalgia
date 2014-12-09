package controller;

import jgamepad.enums.*;
import jgamepad.enums.Button;
import jgamepad.interfaces.ButtonListener;
import jgamepad.listeners.ButtonHoldListener;
import jgamepad.listeners.ButtonPressedListener;
import main.Logger;
import main.Main;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class FreeRoamHandler extends Thread{

    private Controller controller;
    private boolean run = true;
    private double speedFactor = 1200;
    private Robot robot = null;
    private List<ButtonListener> buttonListeners = new ArrayList<>();
    private Main main;

    public FreeRoamHandler(Controller controller, Main main){
        this.controller = controller;
        this.main = main;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            Logger.log("Unable to load robot");
            Logger.log(e.getMessage());
        }

        registerListeners();
    }

    public void run(){

        int x;
        int y;

        while(run){
            try {
                x = controller.getAnalogValue(Analog.leftStickX, true);
                y = controller.getAnalogValue(Analog.leftStickY, true);

                x /= speedFactor;
                y /= speedFactor;

                if(y != 0 || x != 0){
                    Point point = MouseInfo.getPointerInfo().getLocation();
                    robot.mouseMove((int) point.getX() + x, (int) point.getY() - y);
                }

                if(speedFactor < 2000)
                    speedFactor += (controller.getAnalogValue(Analog.L2, true) / 30);
                if(speedFactor > 400)
                    speedFactor -= (controller.getAnalogValue(Analog.R2, true) / 30);

                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        clearListeners();
    }

    public void end(){
        run = false;
    }

    private void registerListeners(){

        buttonListeners.add(new ButtonHoldListener(Button.GUIDE, 1000, () -> {
            main.stopFreeRoam();
        }));

        buttonListeners.add(new ButtonPressedListener(Button.A, pressed -> {
            if(pressed) {
                robot.mousePress(InputEvent.BUTTON1_MASK);
            }
            else{
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
            }
        }));

        buttonListeners.add(new ButtonPressedListener(Button.DOWN, pressed ->{
            if(pressed)
                robot.keyPress(KeyEvent.VK_DOWN);
        }));

        buttonListeners.add(new ButtonPressedListener(Button.UP, pressed ->{
            if(pressed)
                robot.keyPress(KeyEvent.VK_UP);
        }));

        buttonListeners.add(new ButtonPressedListener(Button.LEFT, pressed ->{
            if(pressed)
                robot.keyPress(KeyEvent.VK_LEFT);
        }));

        buttonListeners.add(new ButtonPressedListener(Button.RIGHT, pressed ->{
            if(pressed)
                robot.keyPress(KeyEvent.VK_RIGHT);
        }));


        controller.addButtonListener(buttonListeners);
    }

    private void clearListeners(){
        controller.removeButtonListener(buttonListeners);
    }

}
