package models;

import java.util.UUID;

public class Program {

    private UUID uuid = UUID.randomUUID();
    private String name = "";
    private String preCommand = "";
    private String postCommand = "";
    private String path = "";

    //TODO icon


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreCommand() {
        return preCommand;
    }

    public void setPreCommand(String preCommand) {
        this.preCommand = preCommand;
    }

    public String getPostCommand() {
        return postCommand;
    }

    public void setPostCommand(String postCommand) {
        this.postCommand = postCommand;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString(){
        return name;
    }

    public UUID getUUID(){
        return uuid;
    }
}
