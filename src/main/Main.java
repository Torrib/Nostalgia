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

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import com.sun.jna.PointerType;

import controller.ControllerHandler;
import settings.ItemInterface;
import settings.Settings;
import settings.WindowSetting;


public class Main extends Thread
{	
	private OutputHandler outputHandler;
	private GuiManager guiManager;
	private PointerType activeWindowHandle;
    private WindowSetting activeWindowSettings;
	private WindowHandler windowHandler;
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private int windowTextRefreshCounter = 0;
    private ControllerHandler controllerHandler;
    private Settings settings;

    public Main(GuiManager guiManager){
        this.guiManager = guiManager;
        settings = Settings.load();
    }

    @Override
	public void run()
	{
		log("====================================================================");
        log("Starting");
		createTrayIcon();
		outputHandler = new OutputHandler(this);
		windowHandler = getWindowHandler();
		startWindowPulling(settings.getWindowPullDelay());
		loadControllers();
	}
	
	private void loadControllers()
	{
		controllerHandler = new ControllerHandler(this);
        boolean loaded = controllerHandler.load();
        if(!loaded){
            log("Unable to load controller.dll");
            System.err.println("Unable to load controller.dll");
            return;
        }
        controllerHandler.start();
    }
	
	private void startWindowPulling(int sleep)
	{
		ActionListener action = new ActionListener() 
		{
		    public void actionPerformed(ActionEvent e) 
		    {
		    	setActiveWindowSettings();
		    	if(activeWindowSettings != null)
		    	{
			 		removeBorders(activeWindowHandle);
			 		setTopMost(activeWindowHandle);
		    	}
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
            File file = new File("icon.ico");
			Icon ico = FileSystemView.getFileSystemView().getSystemIcon(file );
			Image img = ((ImageIcon) ico).getImage();
			TrayIcon trayIcon = new TrayIcon(img, "Nostalgia", popMenu);
			SystemTray.getSystemTray().add(trayIcon);
		}
		catch (AWTException e1) 
		{
			log(e1.toString());
			e1.printStackTrace();
        }
	}
	
	private WindowHandler getWindowHandler()
	{
		//TODO add linux/mac support?
		String os = System.getProperty("os.name");
		WindowHandler wh = null;
		if(os.contains("Windows"))
			wh = new WindowHandlerWindows();
		else
		{
			log("Unsupported OS: " + os);
			log("Exiting");
			System.exit(0);
		}
			
		log(os + " window handler loaded");
		return wh;
	}
	
	public void command(ItemInterface item)
	{
		boolean commandPerformed = outputHandler.command(item.getCommand());

        if(commandPerformed) {
            showMessageBox(item.getMessage());
            //TODO vibrate
        }

	}
	
	public void showMenu(int controller) {
        if(guiManager.isMenuShowing())
            guiManager.hideMenu();
        else {
            if(!activeWindowSettings.getMenuItems().isEmpty())
                guiManager.showMenu(activeWindowSettings.getMenuItems(), controller);
        }
    }

    public boolean isGuiActive(){
        return guiManager.isMenuShowing();
    }
	
	public void pressKey(int key)
	{

		outputHandler.pressKey(key);
	}

	public void showMessageBox(String message)
	{
        if(activeWindowSettings != null && !settings.isDisableMessages()) {
            if (!activeWindowSettings.isDisableMessages()) {
                if (message != null && !message.isEmpty()) {
                    guiManager.showMessageBox(message);
                }
            }
        }
	}

    public void returnFocus(){
        windowHandler.setWindowFocus(activeWindowHandle);
    }

 	private void setActiveWindowSettings()
 	{
        if(isGuiActive())
            return;

 		PointerType hWnd = windowHandler.GetForegroundWindow();
 		
 		if(hWnd == null)
 			return;
 		
 		// Do nothing if the window handle if its the same.
 		// Refresh it every know and then(Some windows change text) every 10 sec by default.
 		if(hWnd.equals(activeWindowHandle) && windowTextRefreshCounter < settings.getWindowPullRefresh())
 		{
 			windowTextRefreshCounter++;
 			return;
 		}
 		windowTextRefreshCounter = 0;
 		
 		String windowName = windowHandler.getWindowText(hWnd);

        if(windowName.equals("Nostalgia message"))
            return;
 		
 		activeWindowSettings = getActiveWindowSettings(windowName);
 		activeWindowHandle = hWnd;

 		// If no bindings are found, a default is set, as long as useDefaultWindowBindings is true
 		if(activeWindowSettings == null)
            //TODO create default window settings handling
            activeWindowSettings = new WindowSetting();

        controllerHandler.setHotkeys(activeWindowSettings.getHotkeys());
 		
 		log("Active binding: " + activeWindowSettings.getName() + "[" + windowName + "]");
 	}

    private WindowSetting getActiveWindowSettings(String windowName)
    {
        for(WindowSetting windowSetting : settings.getWindowSettings())
            if(windowName.contains(windowSetting.getWindowName()))
                return windowSetting;

        return null;
    }

	private void setWindowFocus(PointerType hwnd)
  	{
  		windowHandler.setWindowFocus(hwnd);
  	}
 	
 	private void removeBorders(PointerType hWnd)
 	{
 		if(activeWindowSettings.isRemoveBorders())
 		{
 			windowHandler.removeBorder(hWnd);
 			windowHandler.removeMenu(hWnd);
 		}
 	}
  	
  	private void setTopMost(PointerType hWnd)
  	{
  		if(activeWindowSettings.isTopmost())
  		{
  			windowHandler.setTopMost(hWnd);
  			log("Set window topmost: " + activeWindowSettings.getName());
  		}
  	}
  	
	public void handleFunctionString(String key) 
	{
		String program = settings.getProgramBindings().get(key);
		try 
		{
			if(program.endsWith(".bat"))
				Runtime.getRuntime().exec ("cmd /c " + program);
			else
				Runtime.getRuntime().exec (program);
		} 
		catch (IOException e) 
		{
			log(e.toString());
		}
	}

    public void focusMenu(){
           windowHandler.setWindowFocus(windowHandler.getWindowHandle("Nostalgia menu"));
    }

//	public String getSettings(String key)
//	{
//		return settingsOld.getSettings(key);
//	}

//	public int getSettingInt(String key)
//	{
//		return settingsOld.getSettingsInt(key);
//	}
//
//	public boolean getSettingsBoolean(String key)
//	{
//		return settingsOld.getSettingsBoolean(key);
//	}
	
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
	
	public static void staticLog(String message)
	{
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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

    public Settings getSettings(){
        return settings;
    }

    public void setSettings(Settings settings){
        this.settings = settings;
    }
}