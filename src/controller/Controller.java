package controller;

import javafx.scene.input.KeyCode;
import jgamepad.enums.Analog;
import jgamepad.enums.Button;
import jgamepad.interfaces.ButtonListener;
import jgamepad.listeners.ButtonHoldListener;
import jgamepad.listeners.ButtonPressedListener;
import main.Main;
import models.Hotkey;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Controller extends jgamepad.Controller {

    private static final int STICK_ZONE = 10000;

    private List<ButtonListener> menuListeners = createMenuList();
    private Main main;
    private ButtonListener guideListener;
    private Thread analogThread;
    private List<ButtonListener> hotkeyListeners = new ArrayList<>();
    private Presser presser;

    public Controller(int controller, Main main) {
        super(controller);
        this.main = main;
        guideListener = new ButtonHoldListener(Button.GUIDE, 2000, () -> main.showMenu(this));
    }

    public void activate(){
        clearButtonListeners();
        vibrate();
        enableMenuListener();
    }

    private List<ButtonListener> createMenuList(){
        List<ButtonListener> listeners = new ArrayList<>();

        listeners.add(new ButtonPressedListener(Button.DOWN, pressed -> {
            if(pressed) {
                presser = new Presser(KeyEvent.VK_DOWN);
                presser.start();
            }
            else
                presser.end();
        }));

        listeners.add(new ButtonPressedListener(Button.UP, pressed -> {
            if(pressed) {
                presser = new Presser(KeyEvent.VK_UP);
                presser.start();
            }
            else
                presser.end();
        }));

        listeners.add(new ButtonPressedListener(Button.LEFT, pressed -> {
            if (pressed) {
                main.pressKey(KeyEvent.VK_LEFT);
            }
        }));

        listeners.add(new ButtonPressedListener(Button.RIGHT, pressed -> {
            if (pressed) {
                main.pressKey(KeyEvent.VK_RIGHT);
            }
        }));

        listeners.add(new ButtonPressedListener(Button.A, pressed -> {
            if (pressed) {
                main.pressKey(KeyEvent.VK_ENTER);
            }
        }));

        listeners.add(new ButtonPressedListener(Button.B, pressed -> {
            if(pressed){
                main.pressKey(KeyEvent.VK_BACK_SPACE);
            }
        }));

        return listeners;
    }

    public void activateMenu(){
        this.addButtonListener(menuListeners);

        Runnable analogListener = () -> {
            while(true){
                int leftStickY = getAnalogValue(Analog.leftStickY);
                if(leftStickY > STICK_ZONE){
                    main.pressKey(KeyEvent.VK_UP);
                }
                else if(leftStickY < -STICK_ZONE){
                    main.pressKey(KeyEvent.VK_DOWN);
                }

                int leftStickX = getAnalogValue(Analog.leftStickX);
                if(leftStickX > STICK_ZONE){
                    main.pressKey(KeyEvent.VK_RIGHT);
                }
                else if(leftStickX< -STICK_ZONE){
                    main.pressKey(KeyEvent.VK_LEFT);
                }

                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    break;
                }
            }
        };

        analogThread = new Thread(analogListener);
        analogThread.start();
    }

    public void deactivateMenu() {
        this.removeButtonListener(menuListeners);
        this.enableMenuListener();
        analogThread.interrupt();
    }

    public void enableMenuListener(){
        this.addButtonListener(guideListener);
    }

    public void changeHotkeyListeners(List<Hotkey> hotkeys){
        removeButtonListener(hotkeyListeners);
        hotkeyListeners.clear();

        for (Hotkey hotkey : hotkeys) {
            hotkeyListeners.add(new ButtonHoldListener(hotkey.getButton(), hotkey.getDelay(),
                    () -> main.command(hotkey, this)));
        }
        addButtonListener(hotkeyListeners);
    }

    class Presser extends Thread{

        private boolean run = true;
        int keyCode;

        public Presser(int keyCode) {
            this.keyCode = keyCode;
        }

        public void run(){
            while(run){
                main.pressKey(keyCode);
            }
        }

        public void end(){
            run = false;
        }
    }
}
