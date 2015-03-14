package main;

import models.KeyCommand;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Output {

    private static Robot robot = null;

    static {
        try {
            robot = new Robot();
            robot.setAutoDelay(150);

        } catch (AWTException e) {
            Logger.log("Unable to initiate Robot");
            Logger.log(e.getMessage());
            System.exit(1);
        }
    }

    public static void pressKey(KeyCommand keyCommand){
        if(keyCommand == null || keyCommand.getKeyCode() == null)
            return;

        if (keyCommand.isCtrl()) robot.keyPress(KeyEvent.VK_CONTROL);
        if (keyCommand.isAlt()) robot.keyPress(KeyEvent.VK_ALT);
        if (keyCommand.isShift()) robot.keyPress(KeyEvent.VK_SHIFT);
        if (keyCommand.isWindows()) robot.keyPress(KeyEvent.VK_WINDOWS);

        pressKey(keyCommand.getKeyCode().impl_getCode());

        if (keyCommand.isCtrl()) robot.keyRelease(KeyEvent.VK_CONTROL);
        if (keyCommand.isAlt()) robot.keyRelease(KeyEvent.VK_ALT);
        if (keyCommand.isShift()) robot.keyRelease(KeyEvent.VK_SHIFT);
        if (keyCommand.isWindows()) robot.keyRelease(KeyEvent.VK_WINDOWS);
    }

    public static void pressKey(int key){
        robot.keyPress(key);
        robot.keyRelease(key);
    }

    public static void mouseMove(int x, int y){
        robot.mouseMove(x, y);
    }

    public static void mouseWheel(int value){
        robot.mouseWheel(value);
    }

    public static void mousePress(int button){
        robot.mousePress(button);
    }

    public static void mouseRelease(int button){
        robot.mousePress(button);
    }
}
