package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import com.sun.jna.PointerType;

import controller.Controller;
import controller.ControllerHandler;
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
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private int windowTextRefreshCounter = 0;
    private ControllerHandler controllerHandler;

    public Main(GuiManager guiManager){
        this.guiManager = guiManager;
    }

    @Override
	public void run()
	{
		log("====================================================================");
        log("Starting");
		createTrayIcon();
        loadControllers();
		outputHandler = new OutputHandler(this);
		osHandler = getOsHandler();
		startWindowPulling(SETTINGS.getWindowPullDelay());
    }
	
	private void loadControllers()
	{
        log("Loading controller handler");
		controllerHandler = new ControllerHandler(this);
        boolean loaded = controllerHandler.load();
        if(!loaded){
            log("Unable to load controller.dll");
            return;
        }
        controllerHandler.start();
        log("Controller handler loaded");
    }
	
	private void startWindowPulling(int sleep)
	{
		ActionListener action = new ActionListener() 
		{
		    public void actionPerformed(ActionEvent e) 
		    {
		    	setActiveWindowSettings();
		    }
		};
		new javax.swing.Timer(sleep, action).start();
	}

	private void createTrayIcon()
	{
		try 
		{
			PopupMenu popMenu= new PopupMenu();
			MenuItem item1 = new MenuItem("Exit");
			MenuItem item2 = new MenuItem("Config");
			popMenu.add(item2);
			popMenu.add(item1);
			item1.addActionListener(new ActionListener() {
	 
	            public void actionPerformed(ActionEvent e)
	            {
	            	log("User exit(tray)");
	                System.exit(0);
	            }
	        });

            item2.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    log("Opening config");
                    guiManager.showConfig();
                }
            });
            File file = new File("src/resources/icon.ico");
			Icon ico = FileSystemView.getFileSystemView().getSystemIcon(file );
			Image img = ((ImageIcon) ico).getImage();
			TrayIcon trayIcon = new TrayIcon(img, "Nostalgia", popMenu);
			SystemTray.getSystemTray().add(trayIcon);
		}
		catch (AWTException e1) 
		{
			log(e1.toString());
			e1.printStackTrace();
            System.exit(1);
        }
	}
	
	private OsHandler getOsHandler()
	{
		//TODO add linux/mac support?
		String os = System.getProperty("os.name");
		OsHandler wh = null;
		if(os.contains("Windows"))
			wh = new windows.WindowsHandler();
		else
		{
			log("Unsupported OS: " + os);
			log("Exiting");
			System.exit(0);
		}
			
		log(os + " window handler loaded");
		return wh;
	}

    public void command(Item item, Controller controller){
        outputHandler.handleKeyCommands(item.getCommands());
        showMessageBox(item.getMessage(), false);
        if(item.vibrate())
            controller.vibrate(400);
    }

    public void command(List<Command> commands){
        outputHandler.handleKeyCommands(commands);
    }

    public void showMenu(int controller) {
        if(guiManager.isMenuShowing())
            guiManager.hideMenu();
        else {
            if(!activeWindowSettings.getMenuItems().isEmpty()) {
                command(activeWindowSettings.getPreMenuComands());
                guiManager.showMenu(activeWindowSettings.getMenuItems(), controller);
            }
        }
    }

    public boolean isGuiActive(){
        return guiManager.isMenuShowing();
    }

	public void pressKey(int key)
	{

		outputHandler.pressKey(key);
	}

	public void showMessageBox(String message, boolean systemMessage)
	{
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

 	private void setActiveWindowSettings()
 	{
        if(isGuiActive())
            return;

 		PointerType hWnd = osHandler.GetForegroundWindow();

 		if(hWnd == null)
 			return;

 		// Do nothing if the window handle if its the same.
 		// Refresh it every know and then(Some windows change text) every 10 sec by default.
 		if(hWnd.equals(activeWindowHandle) && windowTextRefreshCounter < SETTINGS.getWindowPullRefreshCount())
 		{
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

    private WindowSetting getActiveWindowSettings(String windowName)
    {
        for(WindowSetting windowSetting : SETTINGS.getWindowSettings())
            if(windowName.contains(windowSetting.getWindowName()))
                return windowSetting;

        return null;
    }

    private void modifyWindow(){
        if(activeWindowSettings != null)
        {
            if(activeWindowSettings.isRemoveBorders())
            {
                osHandler.removeBorder32(activeWindowHandle);
                osHandler.removeMenu(activeWindowHandle);
            }
            if(activeWindowSettings.isTopmost())
            {
                osHandler.setTopMost(activeWindowHandle);
            }
        }
    }

	private void setWindowFocus(PointerType hwnd)
  	{
  		osHandler.setWindowFocus(hwnd);
  	}
  	
	public void runProgram(Program program)
	{
		try 
		{
			Runtime.getRuntime().exec (program.getPreCommand() + " " + program.getPath() + " " + program.getPostCommand());
		} 
		catch (IOException e) 
		{
			log(e.toString());
		}
	}

    public void focusMenu(){
           osHandler.setWindowFocus(osHandler.getWindowHandle("Nostalgia menu"));
    }
	
	public void log(String message)
	{
		Date date = new Date();
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
		    out.println(dateFormat.format(date) + " - " + message);
		    out.close();
		    System.out.println(message);
		} 
		catch (IOException e) 
		{
		    e.printStackTrace();
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

    public void toggleHotkeys(){
        Main.SETTINGS.setDisableHotkeys(Main.SETTINGS.isDisableHotkeys() ? false : true);
    }


    public void updateControllerStatus(){
        controllerHandler.updateControllers();
    }


}