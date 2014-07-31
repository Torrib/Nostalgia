package interfaces;

import com.sun.jna.PointerType;

public interface OsHandler
{
	public void removeBorder(PointerType hWnd);
	
	public void removeMenu(PointerType hWnd);
	
	public void setTopMost(PointerType hWnd);
	
	public void setWindowFocus(PointerType hWnd);
	
	public String getWindowText(PointerType hWnd);
	
	public PointerType GetForegroundWindow();

    public PointerType getWindowHandle(String title);

    public void removeBorder32(PointerType hWnd);

    public void killProcess(PointerType hWnd);

    public void sleep();
    public void shutdown();

    public void increaseVolume();
    public void decreaseVolume();
    public void mute();
    public void handleRunOnStartup(boolean runOnStartup);

}
