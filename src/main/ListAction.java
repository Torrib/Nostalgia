package main;

import java.awt.event.*;

import javax.swing.*;

public class ListAction implements MouseListener
{
	private static final KeyStroke ENTER = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);

	private JList<String> list;
	private KeyStroke keyStroke;

	public ListAction(JList<String> list, Action action)
	{
		this(list, action, ENTER);
	}

	public ListAction(JList<String> list, Action action, KeyStroke keyStroke)
	{
		this.list = list;
		this.keyStroke = keyStroke;

		InputMap im = list.getInputMap();
		im.put(keyStroke, keyStroke);

		setAction( action );

		list.addMouseListener( this );
	}

	public void setAction(Action action)
	{
		list.getActionMap().put(keyStroke, action);
	}

	public void mouseClicked(MouseEvent e)
	{
		if (e.getClickCount() == 2)
		{
			Action action = list.getActionMap().get(keyStroke);

			if (action != null)
			{
				ActionEvent event = new ActionEvent(
					list,
					ActionEvent.ACTION_PERFORMED,
					"");
				action.actionPerformed(event);
			}
		}
	}

 	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}
}