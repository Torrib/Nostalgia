package main;


import input.Binding;
import input.KeyEventConverter;
import settings.Hotkey;
import settings.MenuItem;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Set;

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

	public boolean command(String command)
	{	
		if(command.endsWith("()"))
		{
			main.handleFunctionString(command);
			return false;
		}

		return pressButtons(command);
	}

	private boolean pressButtons(String buttons)
	{
		String[] keys = buttons.split(",");
		for(String key : keys)
		{
			String[] holdKey = key.split("\\+");
			if(holdKey.length > 1)
			{
				handleHoldButton(holdKey);
			}
			else
			{
				int keyValue = KeyEventConverter.convert(key);
				if( keyValue >= 0)
					pressKey(keyValue);
				else
				{
					main.log("Unrecognized key: " + key);
                    return false;
				}
			}
		}
        return true;
	}

	private void handleHoldButton(String[] values)
	{
		int key = KeyEventConverter.convert(values[1]);
		if(key < 0)
		{
			main.log("Unkown controll key: " + values[0]);
			System.out.println("Unkown controll key: " + values[0]);
			return;
		}

		if(values[0].equals("alt"))
		{
			pressKey(key, true, false, false);
		}
		else if(values[0].equals("ctrl"))
		{
			pressKey(key, false, true, false);
		}
		else if(values[0].equals("shift"))
		{
			pressKey(key, false, false, true);
		}
		else
		{
			main.log("Unkown controll key: " + values[0]);
			System.out.println("Unkown controll key: " + values[0]);
		}	 			
	}
 	
 	private void pressKey(int key, boolean alt, boolean control, boolean shift)
 	{
 		if(alt)
 			robot.keyPress(KeyEvent.VK_ALT);
 		if(control)
 			robot.keyPress(KeyEvent.VK_CONTROL);
 		if(shift)
 			robot.keyPress(KeyEvent.VK_SHIFT);
 		
 		pressKey(key);
 		
 		if(alt)
 			robot.keyRelease(KeyEvent.VK_ALT);
 		if(control)
 			robot.keyRelease(KeyEvent.VK_CONTROL);
 		if(shift)
 			robot.keyRelease(KeyEvent.VK_SHIFT);
 	}

 	public void pressKey(int key)
 	{
 		robot.keyPress(key);
 		robot.delay(main.getSettings().getButtonPressDelay());
 		robot.keyRelease(key);
 	}
}

