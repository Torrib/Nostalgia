package controller;

import jgamepad.enums.Analog;
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
    private double scrollFactor = 8000;
    private double tempSpeedFactor;
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
            run = false;
        }

        registerListeners();
    }

    public void run(){

        int scrollCounter = 0;

        while(run){
            try {
                handleMouse();
                handleSpeedChange();
                if(scrollCounter % 15 == 0) {
                    handleScrolling();
                    scrollCounter = 0;
                }
                else{
                    scrollCounter++;
                }

                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        clearListeners();
    }

    private void handleSpeedChange() {
        if(speedFactor < 2000)
            speedFactor += (controller.getAnalogValue(Analog.L2, true) / 30);
        if(speedFactor > 400)
            speedFactor -= (controller.getAnalogValue(Analog.R2, true) / 30);
    }

    private void handleMouse() {
        int x = controller.getAnalogValue(Analog.leftStickX , true);
        int y = controller.getAnalogValue(Analog.leftStickY, true);

        x /= speedFactor;
        y /= speedFactor;

        if(y != 0 || x != 0){
            Point point = MouseInfo.getPointerInfo().getLocation();
            robot.mouseMove((int) point.getX() + x, (int) point.getY() - y);
        }
    }

    private void handleScrolling(){
        int y = controller.getAnalogValue(Analog.rightStickY, true);

        y /= scrollFactor;
        y *= -1;

        robot.mouseWheel(y);
    }

    public void end(){
        run = false;
    }

    private void registerListeners(){

        buttonListeners.add(new ButtonPressedListener(Button.A, pressed -> {
            if(pressed) {
                robot.mousePress(InputEvent.BUTTON1_MASK);
            }
            else{
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
            }
        }));

        buttonListeners.add(new ButtonPressedListener(Button.Y, pressed -> {
            if(pressed) {
                robot.mousePress(InputEvent.BUTTON3_MASK);
            }
            else{
                robot.mouseRelease(InputEvent.BUTTON3_MASK);
            }
        }));

        buttonListeners.add(new ButtonPressedListener(Button.B, pressed -> {
            if(pressed)
                pressKey(KeyEvent.VK_ESCAPE);
        }));

        buttonListeners.add(new ButtonPressedListener(Button.DOWN, pressed ->{
            if(pressed)
                pressKey(KeyEvent.VK_DOWN);
        }));

        buttonListeners.add(new ButtonPressedListener(Button.UP, pressed ->{
            if(pressed)
                pressKey(KeyEvent.VK_UP);
        }));

        buttonListeners.add(new ButtonPressedListener(Button.LEFT, pressed ->{
            if(pressed)
                pressKey(KeyEvent.VK_LEFT);
        }));

        buttonListeners.add(new ButtonPressedListener(Button.RIGHT, pressed ->{
            if(pressed)
                pressKey(KeyEvent.VK_RIGHT);
        }));

        buttonListeners.add(new ButtonPressedListener(Button.R3, pressed ->{
            if(pressed) {
                tempSpeedFactor = speedFactor;
                speedFactor = 3000;
            }
            else {
                speedFactor = tempSpeedFactor;
            }
        }));


        controller.addButtonListener(buttonListeners);
    }

    private void clearListeners(){
        controller.removeButtonListener(buttonListeners);
    }

    private void pressKey(int key){
        robot.keyPress(key);
        robot.keyRelease(key);
    }

}
