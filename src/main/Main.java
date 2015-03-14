package main;

import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;

import com.sun.jna.PointerType;

import controller.Controller;
import controller.ControllerHandler;
import controller.FreeRoamHandler;
import interfaces.Item;
import interfaces.OsHandler;
import models.Command;
import models.Hotkey;
import models.Program;
import settings.Settings;
import settings.WindowSetting;


public class Main extends Thread
{
    public static final Settings SETTINGS = Settings.load();

	private OutputHandler outputHandler;
	private GuiManager guiManager;
	private PointerType activeWindowHandle;
    private WindowSetting activeWindowSettings;
	private OsHandler osHandler;
	private int windowTextRefreshCounter = 0;
    private ControllerHandler controllerHandler;
    private FreeRoamHandler freeRoamHandler;
    private String activeWindowName;

    public Main(GuiManager guiManager){
        this.guiManager = guiManager;
    }

    @Override
	public void run(){
        loadControllers();
		outputHandler = new OutputHandler(this);
		osHandler = getOsHandler();
		startWindowPulling(SETTINGS.getWindowPullDelay());
    }

	private void loadControllers(){
        Logger.log("Loading controller handler");
		controllerHandler = new ControllerHandler(this);
        controllerHandler.start();
        Logger.log("controller handler loaded");
    }
	
	private void startWindowPulling(int sleep){
		ActionListener action = e ->  setActiveWindowSettings();
		new javax.swing.Timer(sleep, action).start();
	}
	
	private OsHandler getOsHandler(){
		//TODO add linux/mac support?
		String os = System.getProperty("os.name");
		OsHandler wh = null;
		if(os.contains("Windows"))
			wh = new windows.WindowsHandler();
		else{
			Logger.log("Unsupported OS: " + os);
			Logger.log("Exiting");
			System.exit(0);
		}
			
		Logger.log(os + " window handler loaded");
		return wh;
	}

    public void command(Item item, Controller controller){
        outputHandler.doCommand(item.getCommands(), controller);
        showMessageBox(item.getMessage(), false);
        if(item.vibrate())
            controller.vibrate(400);
    }

    public void command(Hotkey hotkey, Controller controller){
        if(!Main.SETTINGS.isDisableHotkeys()) {
            outputHandler.doCommand(hotkey.getCommands(), controller);
            showMessageBox(hotkey.getMessage(), false);
            if (hotkey.vibrate())
                controller.vibrate(400);
        }
    }

    public void command(List<Command> commands){
        outputHandler.doCommand(commands, null);
    }

    public void command(List<Command> commands, Controller controller){
        outputHandler.doCommand(commands, controller);
    }

    public void showMenu(Controller controller) {
        if(guiManager.isMenuShowing()) {
            Logger.log("Menu showing: Hiding");
            guiManager.hideMenu();
        }
        else {
            if(!activeWindowSettings.getMenuItems().isEmpty()) {
                Logger.log("Showing menu");
                controller.activateMenu();
                command(activeWindowSettings.getPreMenuComands(), controller);
                guiManager.showMenu(activeWindowSettings.getMenuItems(), controller);
            }
        }
    }

    public boolean isGuiActive(){
        return guiManager.isMenuShowing();
    }

	public void pressKey(int key){
		Output.pressKey(key);
	}

	public void showMessageBox(String message, boolean systemMessage){
        if(systemMessage && SETTINGS.isDisableSystemMessages())
            return;

        if(activeWindowSettings != null && !SETTINGS.isDisableMessages()) {
            if (!activeWindowSettings.isDisableMessages()) {
                if (message != null && !message.isEmpty()) {
                    guiManager.showMessageBox(message);
                }
            }
        }
	}

    public void returnFocus(){
        osHandler.setWindowFocus(activeWindowHandle);
    }

 	private void setActiveWindowSettings(){
        if(isGuiActive() || isMessageBoxShowing())
            return;

 		PointerType hWnd = osHandler.GetForegroundWindow();

 		if(hWnd == null)
 			return;

 		// Do nothing if the window handle if its the same.
 		// Refresh it every know and then(Some windows change text) every 10 sec by default.
 		if(hWnd.equals(activeWindowHandle) && windowTextRefreshCounter < SETTINGS.getWindowPullRefreshCount()){
 			windowTextRefreshCounter++;
 			return;
 		}
 		windowTextRefreshCounter = 0;

 		activeWindowName = osHandler.getWindowText(hWnd);

 		activeWindowSettings = getActiveWindowSettings(activeWindowName);
 		activeWindowHandle = hWnd;

 		// If no bindings are found, a default is set, as long as useDefaultWindowBindings is true
 		if(activeWindowSettings == null)
            activeWindowSettings = getActiveWindowSettings("~Default");

        if(activeWindowSettings == null)
            activeWindowSettings = new WindowSetting(false);

        controllerHandler.setHotkeys(activeWindowSettings.getHotkeys());
        modifyWindow();
 	}

    private WindowSetting getActiveWindowSettings(String windowName){
        for(WindowSetting windowSetting : SETTINGS.getWindowSettings())
            if(windowName.contains(windowSetting.getWindowName()))
                return windowSetting;

        return null;
    }

    private void modifyWindow(){
        if(activeWindowSettings != null){
            if(activeWindowSettings.isRemoveBorders()){
                osHandler.removeBorder32(activeWindowHandle);
                osHandler.removeMenu(activeWindowHandle);
            }
            if(activeWindowSettings.isTopmost()){
                osHandler.setTopMost(activeWindowHandle);
            }
        }
    }

	public void runProgram(Program program){
		try {
			Runtime.getRuntime().exec (program.getPreCommand() + " " + program.getPath() + " " + program.getPostCommand());
		}
		catch (IOException e) {
			Logger.log(e.toString());
		}
	}

    public WindowSetting getActiveWindowSettings(){
        return activeWindowSettings;
    }

    public void killProcess(){
        osHandler.killProcess(activeWindowHandle);
    }

    public void exit(){
        System.exit(0);
    }

    public void shutdown(){
        osHandler.shutdown();
    }

    public void sleep(){
        osHandler.sleep();
    }

    public void turnOffControllers(){
        controllerHandler.turnOffControllers();
    }

    public void increaseVolume(){
        osHandler.increaseVolume();
    }

    public void decreaseVolume(){
        osHandler.decreaseVolume();
    }

    public void mute(){
        osHandler.mute();
    }

    public void startFreeRoam(Controller controller){
        if(freeRoamHandler == null) {
            showMessageBox("Freeroam enabled", true);
            freeRoamHandler = new FreeRoamHandler(controller, this);
            freeRoamHandler.start();
        }
        else{
            stopFreeRoam();
        }
    }

    public void stopFreeRoam(){
        showMessageBox("Freeroam disabled", true);
        freeRoamHandler.end();
        freeRoamHandler = null;
    }

    public void toggleHotkeys(){
        Main.SETTINGS.setDisableHotkeys(!Main.SETTINGS.isDisableHotkeys());
    }

    public void updateControllers(){
        controllerHandler.update();
    }

    public void updateMenuSettings(){
        guiManager.updateMenuSettings();
    }

    public void handleRunOnStartup() {
        osHandler.handleRunOnStartup(SETTINGS.isRunOnStartup());
    }

    public boolean isMessageBoxShowing(){
        return guiManager.isMessageBoxShowing();
    }

    public void logActiveWindowText(){
        Logger.log(activeWindowName);
    }
}