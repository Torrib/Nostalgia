package models;

import java.util.ArrayList;
import java.util.List;

public class SubMenu {

    private String name = "";
    private List<MenuItem> menuItems = new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public String toString(){
        return name;
    }
}
