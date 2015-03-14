package settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jgamepad.enums.Button;
import models.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Settings {

    private boolean firstRun = true;

    private String menuFont = "";
    private int menuFontSize = 25;
    private int menuSelectedFontSize = 30;
    private boolean muteMenu = false;

    private String messageFont = "impact";
    private int messageFontSize = 25;
    private int messageDelay = 2000;
    private boolean disableMessages = false;
    private boolean disableSystemMessages = false;

    private int controllerPullDelay = 100;
    private boolean requireActivate = true;
    private boolean disableVibration = false;
    private boolean disableHotkeys = false;

    private int windowPullDelay = 1000;
    private int windowPullRefresh = 10000;
    private int windowPullRefreshCount = 10;
    private boolean runOnStartup = false;

    private List<WindowSetting> windowSettings = createDefaultWindowSettings();
    private List<Program> programs = new ArrayList<>();
    private List<SubMenu> subMenus = new ArrayList<>();
    private List<Hotkey> systemHotkeys = createDefaultHotkeys();

    private boolean disableController1 = false;
    private boolean disableController2 = false;
    private boolean disableController3 = false;
    private boolean disableController4 = false;

    public boolean isFirstRun() {
        return firstRun;
    }

    private List<Hotkey> createDefaultHotkeys(){
        List<Hotkey> list = new ArrayList<>();
        if(isFirstRun()){
            Hotkey menuHotkey = new Hotkey();
            menuHotkey.addButton(Button.GUIDE);
            menuHotkey.setName("Show menu");
            menuHotkey.setVibrate(false);
            menuHotkey.addCommand(new Command(new Function(Functions.SHOW_MENU, "Show menu"), 200));

            Hotkey freeRoamHotkey = new Hotkey();
            freeRoamHotkey.addButton(Button.BACK);
            freeRoamHotkey.setName("Enable free roam");
            freeRoamHotkey.addCommand(new Command(new Function(Functions.FREE_ROAM, "Free roam"), 200));

            list.add(menuHotkey);
            list.add(freeRoamHotkey);
        }
        return list;
    }

    private List<WindowSetting> createDefaultWindowSettings(){
        List<WindowSetting> list = new ArrayList<>();
        if(isFirstRun()){
            List<MenuItem> items = new ArrayList<>();
            items.add(new MenuItem("Continue", ""));

            List<Command> printCommands = new ArrayList<>();
            printCommands.add(new Command(new Function(Functions.LOG_WINDOW_TEXT, "Print window name"), 200));
            MenuItem printItem = new MenuItem("Print window name", "Window name printed");
            printItem.setCommands(printCommands);

            items.add(printItem);

            List<Command> freeRoamCommands = new ArrayList<>();
            freeRoamCommands.add(new Command(new Function(Functions.FREE_ROAM, "Free roam", ""), 200));
            MenuItem freeRoamItem = new MenuItem("Free roam", "");
            freeRoamItem.setCommands(freeRoamCommands);

            items.add(freeRoamItem);

            List<Command> exitCommands = new ArrayList<>();
            exitCommands.add(new Command(new Function(Functions.KILL, "Exit", ""), 200));
            MenuItem exitItem = new MenuItem("Exit", "Exiting");
            exitItem.setCommands(exitCommands);

            items.add(exitItem);

            Hotkey printWindowHotkey = new Hotkey();
            printWindowHotkey.addButton(Button.L3);
            printWindowHotkey.setName("Print window name");
            printWindowHotkey.addCommand(new Command(new Function(Functions.LOG_WINDOW_TEXT, "Print window name"), 200));

            List<Hotkey> hotkeys = new ArrayList<>();
            hotkeys.add(printWindowHotkey);


            WindowSetting windowSetting = new WindowSetting(true);
            windowSetting.setName("Default");
            windowSetting.setWindowName("~Default");
            windowSetting.setMenuItems(items);
            windowSetting.setHotkeys(hotkeys);

            list.add(windowSetting);
        }
        return list;
    }

    public void setFirstRun(boolean firstRun) {
        this.firstRun = firstRun;
    }

    public String getMenuFont() {
        return menuFont;
    }

    public void setMenuFont(String menuFont) {
        this.menuFont = menuFont;
    }

    public int getMenuFontSize() {
        return menuFontSize;
    }

    public void setMenuFontSize(int menuFontSize) {
        this.menuFontSize = menuFontSize;
    }

    public String getMessageFont() {
        return messageFont;
    }

    public void setMessageFont(String messageFont) {
        this.messageFont = messageFont;
    }

    public int getMessageFontSize() {
        return messageFontSize;
    }

    public void setMessageFontSize(int messageFontSize) {
        this.messageFontSize = messageFontSize;
    }

    public int getMessageDelay() {
        return messageDelay;
    }

    public void setMessageDelay(int messageDelay) {
        this.messageDelay = messageDelay;
    }

    public int getWindowPullDelay() {
        return windowPullDelay;
    }

    public void setWindowPullDelay(int windowPullDelay) {
        this.windowPullDelay = windowPullDelay;
    }

    public int getWindowPullRefresh() {
        return windowPullRefresh;
    }

    public void setWindowPullRefresh(int windowPullRefresh) {
        this.windowPullRefresh = windowPullRefresh;
    }

    public int getControllerPullDelay() {
        return controllerPullDelay;
    }

    public void setControllerPullDelay(int controllerPullDelay) {
        this.controllerPullDelay = controllerPullDelay;
    }

    public boolean isDisableMessages() {
        return disableMessages;
    }

    public void setDisableMessages(boolean disableMessages) {
        this.disableMessages = disableMessages;
    }

    public boolean isDisableSystemMessages() {
        return disableSystemMessages;
    }

    public void setDisableSystemMessages(boolean disableSystemMessages) {
        this.disableSystemMessages = disableSystemMessages;
    }

    public boolean isDisableVibration() {
        return disableVibration;
    }

    public void setDisableVibration(boolean disableVibration) {
        this.disableVibration = disableVibration;
    }

    public List<WindowSetting> getWindowSettings() {
        return windowSettings;
    }

    public void setWindowSettings(List<WindowSetting> windowSettings) {
        this.windowSettings = windowSettings;
    }

    public int getMenuSelectedFontSize() {
        return menuSelectedFontSize;
    }

    public void setMenuSelectedFontSize(int menuSelectedFontSize) {
        this.menuSelectedFontSize = menuSelectedFontSize;
    }

    public boolean menuMuted() {
        return muteMenu;
    }

    public void setMenuMuted(boolean muteMenu) {
        this.muteMenu = muteMenu;
    }

    public boolean isRequireActivate() {
        return requireActivate;
    }

    public void setRequireActivate(boolean requireActivate) {
        this.requireActivate = requireActivate;
    }

    public boolean isDisableHotkeys() {
        return disableHotkeys;
    }

    public void setDisableHotkeys(boolean disableHotkeys) {
        this.disableHotkeys = disableHotkeys;
    }

    public int getWindowPullRefreshCount() {
        return windowPullRefreshCount;
    }

    public void setWindowPullRefreshCount(int windowPullRefreshCount) {
        this.windowPullRefreshCount = windowPullRefreshCount;
    }

    public List<Program> getPrograms() {
        return programs;
    }

    public List<SubMenu> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(List<SubMenu> subMenus) {
        this.subMenus = subMenus;
    }

    public List<Hotkey> getSystemHotkeys() {
        return systemHotkeys;
    }

    public void setSystemHotkeys(List<Hotkey> systemHotkeys) {
        this.systemHotkeys = systemHotkeys;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }

    public List<Boolean> getControllerDisabledStatus() {
        List<Boolean> statuses = new ArrayList<>();
        statuses.add(disableController1);
        statuses.add(disableController2);
        statuses.add(disableController3);
        statuses.add(disableController4);
        return statuses;
    }

    public boolean isDisableController1() {
        return disableController1;
    }

    public void setDisableController1(boolean disableController1) {
        this.disableController1 = disableController1;
    }

    public boolean isDisableController2() {
        return disableController2;
    }

    public void setDisableController2(boolean disableController2) {
        this.disableController2 = disableController2;
    }

    public boolean isDisableController3() {
        return disableController3;
    }

    public void setDisableController3(boolean disableController3) {
        this.disableController3 = disableController3;
    }

    public boolean isDisableController4() {
        return disableController4;
    }

    public void setDisableController4(boolean disableController4) {
        this.disableController4 = disableController4;
    }

    public boolean isRunOnStartup() {
        return runOnStartup;
    }

    public void setRunOnStartup(boolean runOnStartup) {
        this.runOnStartup = runOnStartup;
    }

    public void store(){
        try (Writer writer = new FileWriter("settings.json")) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(this, writer);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Settings load(){
        try{
            FileReader reader = new FileReader(new File("settings.json"));
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(reader, Settings.class);
        }
        catch (Exception e) {
            System.out.println("Unable to load settings");
        }

        return new Settings();
    }
}
