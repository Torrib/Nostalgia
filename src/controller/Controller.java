package controller;

import jgamepad.enums.Analog;
import jgamepad.enums.Button;
import jgamepad.interfaces.ButtonListener;
import jgamepad.listeners.ButtonHoldListener;
import jgamepad.listeners.ButtonPressedListener;
import main.Main;
import models.Hotkey;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Controller extends jgamepad.Controller {

    private static final int STICK_ZONE = 10000;

    private List<ButtonListener> menuListeners = createMenuList();
    private Main main;
    private List<ButtonListener> systemListeners = createSystemListeners();
    private Thread analogThread;
    private List<ButtonListener> hotkeyListeners = new ArrayList<>();
    private Presser presser;

    public Controller(int controller, Main main) {
        super(controller);
        this.main = main;
    }

    public void activate(){
        clearButtonListeners();
        vibrate();
        setSystemListeners();
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
        analogThread.interrupt();
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

    private List<ButtonListener> createSystemListeners(){
        List<ButtonListener> listeners = new ArrayList<>();

        for(Hotkey hotkey : Main.SETTINGS.getSystemHotkeys()){
            listeners.add(new ButtonHoldListener(hotkey.getButton(), hotkey.getDelay(),
                    () -> main.command(hotkey, this)));
        }
        return listeners;
    }

    public void setSystemListeners(){
        this.removeButtonListener(systemListeners);
        systemListeners = createSystemListeners();
        this.addButtonListener(systemListeners);
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
