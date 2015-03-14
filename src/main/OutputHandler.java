package main;

import controller.Controller;
import models.Command;
import models.Functions;
import java.util.List;

public class OutputHandler{
	private Main main;

	public OutputHandler(Main main){
		this.main = main;
	}

    public void doCommand(List<Command> commands, Controller controller){
        for(Command command : commands){
            try {
                Thread.sleep(command.getDelay());

                if(command.getCommandType() == Command.KEY) {
                    Output.pressKey(command.getKeyCommand());
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
                        case Functions.SHOW_MENU:
                            main.showMenu(controller);
                            break;
                        case Functions.LOG_WINDOW_TEXT:
                            main.logActiveWindowText();
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
}

