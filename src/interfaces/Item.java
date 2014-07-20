package interfaces;

import models.Command;

import java.util.List;

public interface Item {

    public String getMessage();
    public List<Command> getCommands();
    public boolean vibrate();
}
