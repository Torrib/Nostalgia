package main;

import controller.Controller;
import models.Command;
import models.Functions;
import models.KeyCommand;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;

public class OutputHandler{
	private Main main;
	private Robot robot;

	public OutputHandler(Main main){
		this.main = main;
		try 
		{
			robot = new Robot();
            robot.setAutoDelay(100);
		} 
		catch (Exception e) 
		{
			Logger.log(e.getMessage());
			System.exit(0);
		}
	}

    public void doCommand(List<Command> commands, Controller controller){
        for(Command command : commands){
            try {
                Thread.sleep(command.getDelay());

                if(command.getCommandType() == Command.KEY) {
                    KeyCommand keyCommand = command.getKeyCommand();
                    if(keyCommand == null || keyCommand.getKeyCode() == null)
                        return;

                    if (keyCommand.isCtrl()) robot.keyPress(KeyEvent.VK_CONTROL);
                    if (keyCommand.isAlt()) robot.keyPress(KeyEvent.VK_ALT);
                    if (keyCommand.isShift()) robot.keyPress(KeyEvent.VK_SHIFT);
                    if (keyCommand.isWindows()) robot.keyPress(KeyEvent.VK_WINDOWS);

                    pressKey(keyCommand.getKeyCode().impl_getCode());

                    if (keyCommand.isCtrl()) robot.keyRelease(KeyEvent.VK_CONTROL);
                    if (keyCommand.isAlt()) robot.keyRelease(KeyEvent.VK_ALT);
                    if (keyCommand.isShift()) robot.keyRelease(KeyEvent.VK_SHIFT);
                    if (keyCommand.isWindows()) robot.keyRelease(KeyEvent.VK_WINDOWS);
                }
                else if(command.getCommandType() == Command.FUNCTION){
                    switch (command.getFunction().getFunctionType()){
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
                            main.toggleHotkeys();
                            break;
                        case Functions.TURN_OFF_CONTROLLERS:
                            main.turnOffControllers();
                            break;
                        case Functions.INCREASE_VOLUME:
                            main.increaseVolume();
                            break;
                        case Functions.DECREASE_VOLUME:
                            main.decreaseVolume();
                            break;
                        case Functions.MUTE:
                            main.mute();
                            break;
                        case Functions.FREE_ROAM:
                            main.startFreeRoam(controller);
                            break;
                    }
                }
                else if(command.getCommandType() == Command.PROGRAM){
                    main.runProgram(command.getProgram());
                }
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

 	public void pressKey(int key){
 		robot.keyPress(key);
 		robot.keyRelease(key);
 	}
}

