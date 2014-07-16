package interfaces;

import settings.Command;

import java.util.List;

/**
 * Created by thb on 10.07.2014.
 */
public interface Item {

    public String getMessage();
    public List<Command> getCommands();
    public boolean vibrate();
}
