package main;

import com.sun.jna.Native;
import com.sun.jna.PointerType;
import com.sun.jna.platform.win32.BaseTSD.LONG_PTR;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public class WindowHandlerWindows implements WindowHandler
{
	
	private interface User32 extends StdCallLibrary 
 	{
 		User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);

 	    int GetWindowTextA(PointerType hWnd, byte[] lpString, int nMaxCount);
 	    HWND GetForegroundWindow();
 	    int SetForegroundWindow(PointerType hWnd);
 	    LONG_PTR GetWindowLongPtr(PointerType hWnd, int nIndex);
        HWND FindWindow(String winClass, String title);
 	    LONG_PTR SetWindowLongPtr(PointerType hWnd, int nIndex, LONG_PTR dwNewLong);
 	    boolean SetWindowPos(PointerType hWnd, int hWndInsertAfter, int X, int Y, int cx, int cy, int uFlags);
 	    IntByReference GetMenu(PointerType hWnd);
 	    int GetMenuItemCount(PointerType hMenu);
 	    boolean DrawMenuBar(PointerType hWnd);
 	    boolean RemoveMenu(PointerType hMenu, int uPosition, int uFlags);
 	}

	@Override
	public void removeBorder(PointerType hWnd) 
	{
		LONG_PTR style = User32.INSTANCE.GetWindowLongPtr(hWnd, -16);
  		int WS_CAPTION = 0x00800000 | 0x00400000;
  		long windowStyle = Long.parseLong(style.toString());
		windowStyle = windowStyle & ~WS_CAPTION;
		windowStyle = windowStyle & ~524288; //sysmenu
		windowStyle = windowStyle & ~262144; //thickframe
		windowStyle = windowStyle & ~536870912; //minimize
		windowStyle = windowStyle & ~65536; //maximize
//		windowStyle = windowStyle & ~12582913;
		style = new LONG_PTR(windowStyle | 0x1);
		User32.INSTANCE.SetWindowLongPtr(hWnd, -16, style);
		User32.INSTANCE.SetWindowPos(hWnd, 0, 0, 0, 0, 0, 0x2 | 0x1 | 0x20);
	}

	@Override
	public void removeMenu(PointerType hWnd) 
	{
		IntByReference HMENU = User32.INSTANCE.GetMenu(hWnd);
        int count = User32.INSTANCE.GetMenuItemCount(HMENU);
        for (int i = 0; i < count; i++)
        	User32.INSTANCE.RemoveMenu(HMENU, 0, (0x400 | 0x1000));

        User32.INSTANCE.DrawMenuBar(hWnd);
	}

	@Override
	public void setTopMost(PointerType hWnd) 
	{
		User32.INSTANCE.SetWindowPos(hWnd, -1, 0, 0, 0, 0, 0x0001 | 0x0002);
	}

	@Override
	public void setWindowFocus(PointerType hWnd) 
	{
		User32.INSTANCE.SetForegroundWindow(hWnd);
		
	}

	@Override
	public String getWindowText(PointerType hWnd) 
	{
		byte[] windowText = new byte[512];
		User32.INSTANCE.GetWindowTextA(hWnd, windowText, 512);
		return Native.toString(windowText).trim();
	}

	@Override
	public PointerType GetForegroundWindow() 
	{
		return User32.INSTANCE.GetForegroundWindow();	
	}


    public PointerType getWindowHandle(String title){
        return User32.INSTANCE.FindWindow(null, title);
    }
}
