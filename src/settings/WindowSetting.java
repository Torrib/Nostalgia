package settings;

import models.Hotkey;
import models.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thb on 06.07.2014.
 */
public class WindowSetting {

    private String name = "";
    private String windowName = "";

    private boolean removeBorders = false;
    private boolean topmost = false;
    private boolean disableMessages = false;
    private boolean disableVibration = false;
    private boolean confirmation = false;
    private boolean disableHotkeys = false;

    private String preMenuComand = "";
    private String postMenuCommand = "";

    private List<MenuItem> menuItems = new ArrayList<>();
    private List<Hotkey> hotkeys = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWindowName() {
        return windowName;
    }

    public void setWindowName(String windowName) {
        this.windowName = windowName;
    }

    public boolean isRemoveBorders() {
        return removeBorders;
    }

    public void setRemoveBorders(boolean removeBorders) {
        this.removeBorders = removeBorders;
    }

    public boolean isTopmost() {
        return topmost;
    }

    public void setTopmost(boolean topmost) {
        this.topmost = topmost;
    }

    public boolean isDisableMessages() {
        return disableMessages;
    }

    public void setDisableMessages(boolean disableMessages) {
        this.disableMessages = disableMessages;
    }

    public boolean isDisableVibration() {
        return disableVibration;
    }

    public void setDisableVibration(boolean disableVibration) {
        this.disableVibration = disableVibration;
    }

    public boolean isConfirmation() {
        return confirmation;
    }

    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    public String getPreMenuComand() {
        return preMenuComand;
    }

    public void setPreMenuComand(String preMenuComand) {
        this.preMenuComand = preMenuComand;
    }

    public String getPostMenuCommand() {
        return postMenuCommand;
    }

    public void setPostMenuCommand(String postMenuCommand) {
        this.postMenuCommand = postMenuCommand;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public List<Hotkey> getHotkeys() {
        return hotkeys;
    }

    public void setHotkeys(List<Hotkey> hotkeys) {
        this.hotkeys = hotkeys;
    }

    public boolean isDisableHotkeys() {
        return disableHotkeys;
    }

    public void setDisableHotkeys(boolean disableHotkeys) {
        this.disableHotkeys = disableHotkeys;
    }

    @Override
    public String toString(){
        return name;
    }

}
