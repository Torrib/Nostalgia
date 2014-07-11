package main;

import com.sun.jna.PointerType;

public interface WindowHandler 
{
	public void removeBorder(PointerType hWnd);
	
	public void removeMenu(PointerType hWnd);
	
	public void setTopMost(PointerType hWnd);
	
	public void setWindowFocus(PointerType hWnd);
	
	public String getWindowText(PointerType hWnd);
	
	public PointerType GetForegroundWindow();

    public PointerType getWindowHandle(String title);

}
