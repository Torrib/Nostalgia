package models;

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

    public String getDescription() {
        return description;
    }

    public boolean isToggle() {
        return toggle;
    }

    public int getFunctionType() {
        return functionType;
    }

    @Override
    public String toString(){
        return name;
    }
}
