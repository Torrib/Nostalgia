package main;

import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.sun.jna.PointerType;

import controller.Controller;
import controller.ControllerHandler;
import controller.FreeRoamHandler;
import interfaces.Item;
import interfaces.OsHandler;
import models.Command;
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

    public Main(GuiManager guiManager){
        this.guiManager = guiManager;
    }

    @Override
	public void run(){
        loadControllers();
		outputHandler = new OutputHandler(this);
		osHandler = getOsHandler();
		startWindowPulling(SETTINGS.getWindowPullDelay());
        //TODO remove 
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new Restart(), 1, 1, TimeUnit.HOURS);
    }
	
	private void loadControllers()
	{
        Logger.log("Loading controller handler");
		controllerHandler = new ControllerHandler(this);
        controllerHandler.start();
        Logger.log("Controller handler loaded");
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
        outputHandler.doCommand(item.getCommands(), controller.getControllerNumber());
        showMessageBox(item.getMessage(), false);
        if(item.vibrate())
            controller.vibrate(400);
    }

    public void command(List<Command> commands, int controller){
        outputHandler.doCommand(commands, controller);
    }

    public void showMenu(int controller) {
        if(guiManager.isMenuShowing()) {
            Logger.log("Menu showing: Hiding");
            guiManager.hideMenu();
        }
        else {
            if(!activeWindowSettings.getMenuItems().isEmpty()) {
                Logger.log("Showing menu");
                command(activeWindowSettings.getPreMenuComands(), controller);
                guiManager.showMenu(activeWindowSettings.getMenuItems(), controller);
            }
        }
    }

    public boolean isGuiActive(){
        return guiManager.isMenuShowing();
    }

	public void pressKey(int key){
		outputHandler.pressKey(key);
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
        if(isGuiActive())
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

 		String windowName = osHandler.getWindowText(hWnd);

        if(windowName.equals("Nostalgia message"))
            return;

 		activeWindowSettings = getActiveWindowSettings(windowName);
 		activeWindowHandle = hWnd;

 		// If no bindings are found, a default is set, as long as useDefaultWindowBindings is true
 		if(activeWindowSettings == null)
            activeWindowSettings = getActiveWindowSettings("~Default");

        if(activeWindowSettings == null)
            activeWindowSettings = new WindowSetting();

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

    public void freeRoam(int controller){
        if(freeRoamHandler == null) {
            freeRoamHandler = new FreeRoamHandler(controller);
            freeRoamHandler.start();
        }
        else{
            freeRoamHandler.disable();
            freeRoamHandler = null;
        }
    }

    public void toggleHotkeys(){
        Main.SETTINGS.setDisableHotkeys(!Main.SETTINGS.isDisableHotkeys());
    }


    public void updateControllerStatus(){
        controllerHandler.updateControllers();
    }

    public void handleRunOnStartup() {
        osHandler.handleRunOnStartup(SETTINGS.isRunOnStartup());
    }


    class Restart implements Runnable{

        @Override
        public void run() {
            try {
                Runtime.getRuntime().exec ("java -jar nostalgia.jar");
                System.exit(0);
            }
            catch (IOException e) {
                Logger.log(e.toString());
            }
        }
    }
}