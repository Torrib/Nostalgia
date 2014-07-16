package main;


import input.KeyEventConverter;
import javafx.scene.input.KeyCode;
import settings.Command;
import settings.Functions;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;

public class OutputHandler
{
	private Main main;
	private Robot robot;

	public OutputHandler(Main main)
	{
		this.main = main;
		try 
		{
			robot = new Robot();
		} 
		catch (Exception e) 
		{
			main.log(e.toString());
			e.printStackTrace();
			System.exit(0);
		}
	}

    public void handleKeyCommands(List<Command> commands){
        for(Command command : commands){
            try {
                Thread.sleep(command.getDelay());

                if(command.getCommandType() == Command.KEY) {
                    if(command.getKeyCode() == null)
                        return;

                    if (command.isCtrl()) robot.keyPress(KeyEvent.VK_CONTROL);
                    if (command.isAlt()) robot.keyPress(KeyEvent.VK_ALT);
                    if (command.isShift()) robot.keyPress(KeyEvent.VK_SHIFT);

                    pressKey(command.getKeyCode().impl_getCode());

                    if (command.isCtrl()) robot.keyRelease(KeyEvent.VK_CONTROL);
                    if (command.isAlt()) robot.keyRelease(KeyEvent.VK_ALT);
                    if (command.isShift()) robot.keyRelease(KeyEvent.VK_SHIFT);
                }
                else if(command.getCommandType() == Command.FUNCTION){
                    switch (command.getFunction()){
                        case Functions.KILL:
                            main.killProcess();
                            break;
                        case Functions.EXIT_NOSTALGIA:
                            main.exit();
                            break;
                        case Functions.SHUTDOWN:
                            main.shutdown();
                            break;
                        case Functions.SLEEP:
                            main.sleep();
                            break;
                        case Functions.TOGGLE_HOTKEYS:
                            main.getActiveWindowSettings().setDisableHotkeys(
                                    main.getActiveWindowSettings().isDisableHotkeys() ? false : true);
                            break;
                    }
                }
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

 	public void pressKey(int key)
 	{
 		robot.keyPress(key);
 		robot.delay(main.getSettings().getButtonPressDelay());
 		robot.keyRelease(key);
 	}
}

