package controller;

import main.Logger;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class FreeRoamHandler extends Thread{

    private static final int SCROLL_FACTOR = 10000;

    private int controller;
    private boolean enabled;
    private int speedFactor = 900;
    private Robot robot;
    private boolean mouseButton1Pressed;
    private boolean mouseButton3Pressed;
    private boolean escPressed;
    private boolean enterPressed;
    private Process keyboard = null;
    private boolean backButtonReleased = true;
    private int pressDelayCounter = 0;

    public FreeRoamHandler(int controller){
        this.controller = controller;
        enabled = true;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            Logger.log(e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        
        Logger.log("Starting free roam for controller: " + controller+1);

        while(enabled){
            try {
                handleInput();
                Thread.sleep(25);
            } catch (InterruptedException e) {
                Logger.log(e.toString());
                e.printStackTrace();
            }
        }
    }

    private void handleInput(){
        ControllerStructure cs = new ControllerStructure();
        ControllerInput.ci.getControllerState(controller, cs);

        moveMouse(cs);

        pressDelayCounter++;

        if(pressDelayCounter == 4) {
            pressDelayCounter = 0;

            handleSpeedChange(cs);
            HandleSrolling(cs);
            handleAClick(cs);
            handleYClick(cs);
            handleBClick(cs);
            handleStart(cs);
        }
//        handleBackButton(cs);
    }

    private void handleStart(ControllerStructure cs) {
        if(cs.start == 1 && !enterPressed){
            enterPressed = true;
            robot.keyPress(KeyEvent.VK_ENTER);
        }
        else if(cs.start == 0 && enterPressed){
            enterPressed = false;
            robot.keyRelease(KeyEvent.VK_ENTER);
        }
    }

    private void handleBClick(ControllerStructure cs) {
        if(cs.bButton == 1 && !escPressed){
            escPressed = true;
            robot.keyPress(KeyEvent.VK_ESCAPE);
        }
        else if(cs.bButton == 0 && escPressed){
            escPressed = false;
            robot.keyRelease(KeyEvent.VK_ESCAPE);
        }
    }

    private void handleYClick(ControllerStructure cs) {
        if(cs.yButton == 1 && !mouseButton3Pressed){
            mouseButton3Pressed = true;
            robot.mousePress(InputEvent.BUTTON3_MASK);
        }
        else if(cs.yButton == 0 && mouseButton3Pressed){
            mouseButton3Pressed = false;
            robot.mouseRelease(InputEvent.BUTTON3_MASK);
        }
    }

    private void handleAClick(ControllerStructure cs) {
        if(cs.aButton == 1 && !mouseButton1Pressed){
            mouseButton1Pressed = true;
            robot.mousePress(InputEvent.BUTTON1_MASK);
        }
        else if(cs.aButton == 0 && mouseButton1Pressed){
            mouseButton1Pressed = false;
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
        }
    }

    private void HandleSrolling(ControllerStructure cs) {
        int scrollDistance = 0;

        if(cs.rightStickX > 10000 || cs.rightStickX < -10000){
            scrollDistance = (cs.rightStickX / SCROLL_FACTOR) * -1;
        }

        if(scrollDistance != 0) {
            robot.mouseWheel(scrollDistance);
        }
    }

    private void moveMouse(ControllerStructure cs) {
        int yDiff = 0;
        int xDiff = 0;

        if(cs.leftStickY > 5000 || cs.leftStickY < -5000){
            yDiff = cs.leftStickY / speedFactor;
        }

        if(cs.leftStickX > 5000 || cs.leftStickX < -5000){
            xDiff = cs.leftStickX / speedFactor;
        }

        if(yDiff != 0 || xDiff != 0) {
            Point point = MouseInfo.getPointerInfo().getLocation();

            int newX = (int) (point.getX() + yDiff);
            int newY = (int) (point.getY() - xDiff);

            robot.mouseMove(newX, newY);
        }
    }

    private void handleSpeedChange(ControllerStructure cs) {
        if(cs.l2 != 0){
            alterSpeed(cs.l2 / 50);
        }
        else if(cs.r2 != 0){
            alterSpeed(-(cs.r2 / 50));
        }
    }

    private void alterSpeed(int speed){
        speedFactor += speed;
    }

    private void handleBackButton(ControllerStructure cs) {
        if(cs.back == 1){
            if(backButtonReleased) {
                backButtonReleased = false;
                if (keyboard == null) {

                    try {
                        keyboard = Runtime.getRuntime().exec("cmd /C " + System.getenv("SystemRoot") + "/system32/osk.exe");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Destroying");
                    keyboard.destroy();
                    keyboard = null;
                }
            }
        }
        else if(!backButtonReleased){
            backButtonReleased = true;
        }
    }

    public void disable(){
        enabled = false;
    }
}
