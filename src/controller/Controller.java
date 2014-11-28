package controller;

import jgamepad.enums.Analog;
import jgamepad.enums.Button;
import jgamepad.interfaces.ButtonListener;
import jgamepad.listeners.ButtonHoldListener;
import jgamepad.listeners.ButtonPressedListener;
import main.Main;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Controller extends jgamepad.Controller {

    private static final int STICK_ZONE = 10000;

    private List<ButtonListener> menuListeners = createMenuList();
    private Main main;
    private ButtonListener guideListener;
    private Thread analogThread;

    public Controller(int controller, Main main) {
        super(controller);
        this.main = main;
        guideListener = new ButtonHoldListener(Button.GUIDE, 2000, () -> main.showMenu(this));
    }

    private List<ButtonListener> createMenuList(){
        List<ButtonListener> listeners = new ArrayList<>();

        listeners.add(new ButtonPressedListener(Button.DOWN, pressed -> {
            if (pressed)
                main.pressKey(KeyEvent.VK_DOWN);
        }
        ));

        listeners.add(new ButtonPressedListener(Button.UP, pressed -> {
            if (pressed) {
                main.pressKey(KeyEvent.VK_UP);
            }
        }
        ));

        listeners.add(new ButtonPressedListener(Button.A, pressed -> {
            if (pressed) {
                main.pressKey(KeyEvent.VK_ENTER);
            }
        }
        ));

        listeners.add(new ButtonPressedListener(Button.B, pressed -> {
            if(pressed){
                main.pressKey(KeyEvent.VK_BACK_SPACE);
            }
        }
        ));

        listeners.add(new ButtonPressedListener(Button.GUIDE, pressed -> {
            if (pressed)
                main.pressKey(KeyEvent.VK_BACK_SPACE);
        }
        ));

        return listeners;
    }

    public void activateMenu(){
        this.disableGuideListener();
        this.addButtonListener(menuListeners);

        Runnable analogListener = () -> {
            while(true){
                int leftStickValue = getAnalogValue(Analog.leftStickY);
                if(leftStickValue > STICK_ZONE){
                    main.pressKey(KeyEvent.VK_UP);
                }
                else if(leftStickValue < -STICK_ZONE){
                    main.pressKey(KeyEvent.VK_DOWN);
                }
                try {
                    Thread.sleep(200);
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
        this.enableGuideListener();
        analogThread.interrupt();
    }

    public void enableGuideListener(){
        this.addButtonListener(guideListener);
    }

    public void disableGuideListener(){
        this.removeButtonListener(guideListener);
    }
}
