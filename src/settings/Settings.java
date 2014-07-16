package settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Program;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thb on 05.07.2014.
 */
public class Settings {

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
    private boolean disableControllers = false;
    private boolean disableHotkeys = false;
    private int buttonPressDelay = 50;

    private int windowPullDelay = 1000;
    private int windowPullRefresh = 10000;
    private int windowPullRefreshCount = 10;

    private List<WindowSetting> windowSettings = new ArrayList<>();
    private List<Program> programs = new ArrayList<>();
    private Map<String, String> programBindings = new HashMap<>();

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

    public Map<String, String> getProgramBindings() {
        return programBindings;
    }

    public void setProgramBindings(Map<String, String> programBindings) {
        this.programBindings = programBindings;
    }

    public int getButtonPressDelay() {
        return buttonPressDelay;
    }

    public void setButtonPressDelay(int buttonPressDelay) {
        this.buttonPressDelay = buttonPressDelay;
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

    public boolean isDisableControllers() {
        return disableControllers;
    }

    public void setDisableControllers(boolean disableControllers) {
        this.disableControllers = disableControllers;
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

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
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
            e.printStackTrace();
        }
        return new Settings();
    }
}
