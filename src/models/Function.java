package models;

/**
 * Created by thb on 17.07.2014.
 */
public class Function {
    private int functionType = 0;
    private String name = "";
    private String description = "";

    private boolean toggle = false;

    public Function(int functionType, String name, String description){
        this.functionType = functionType;
        this.name = name;
        this.description = description;
    }

    public Function(int functionType, String name, String description, boolean toggle){
        this.functionType = functionType;
        this.name = name;
        this.description = description;
        this.toggle = toggle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isToggle() {
        return toggle;
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }

    public int getFunctionType() {
        return functionType;
    }

    public void setFunctionType(int functionType) {
        this.functionType = functionType;
    }

    @Override
    public String toString(){
        return name;
    }
}
