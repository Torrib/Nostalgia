package controller;

import jgamepad.enums.Button;
import jgamepad.interfaces.ButtonListener;
import jgamepad.listeners.ButtonHoldListener;
import main.Main;
import models.Hotkey;

import java.util.ArrayList;
import java.util.List;

public class ControllerHandler extends Thread{
    private Controller controller;
    private Main main;
    private List<ButtonListener> activeListeners = new ArrayList<>();


    public ControllerHandler(Main main){
        this.main = main;
    }

    @Override
    public void run() {
        jgamepad.Controller.dllPath = "C:\\Users\\thb\\Downloads\\simpleDLL\\x64\\Release";
        controller = new Controller(0, main);

        if(Main.SETTINGS.isRequireActivate())
            controller.addButtonListener(new ButtonHoldListener(Button.GUIDE, 2000, () -> activate()));
        else
            activate();
    }

    private void activate() {
        controller.vibrate();
        controller.enableGuideListener();
    }

    public void setHotkeys(List<Hotkey> hotkeys){
        controller.removeButtonListener(activeListeners);
        activeListeners.clear();

        for(Hotkey hotkey : hotkeys){
            activeListeners.add(new ButtonHoldListener(hotkey.getButton(), hotkey.getDelay(), () -> {
                main.command(hotkey.getCommands(), controller);
            }));
        }

        controller.addButtonListener(activeListeners);
    }
}
