package windows;

import com.sun.jna.platform.win32.Advapi32Util;
import static com.sun.jna.platform.win32.WinReg.HKEY_LOCAL_MACHINE;

public class Registry {

    public void setOnBoot(){
        Advapi32Util.registryCreateKey(HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run", "Nostalgia");
        Advapi32Util.registrySetStringValue(HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run", "Nostalgia",
                "C:\\Users\\thb\\Documents\\GitHub\\Nostalgia\\out\\artifacts\\Nostalgia\\bundles\\Nostalgia\\Nostalgia.exe");
    }

    //Doesn't work properly
    public void removeBoot(){
        System.out.println(Advapi32Util.registryKeyExists(HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run\\Nostalgia"));
        Advapi32Util.registryDeleteKey(HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run", "Nostalgia");
    }

}