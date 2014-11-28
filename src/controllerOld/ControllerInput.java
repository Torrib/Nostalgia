package controllerOld;


import com.sun.jna.Native;
import com.sun.jna.Platform;
import main.Logger;

public class ControllerInput {
    public static ControllerInterface ci;

    static {
        try {
            if (Platform.is64Bit()) {
                ci = (ControllerInterface) Native.loadLibrary("windows/Controller64.dll", ControllerInterface.class);
                Logger.log("Controller64.dll loaded");
            } else {
                ci = (ControllerInterface) Native.loadLibrary("windows/controller.dll", ControllerInterface.class);
                Logger.log("controller.dll loaded");
            }
            Logger.log("Loading controllerOld interface");
            boolean loaded = ci.initController();
            if(!loaded){
                Logger.log("Unable to load controllerOld.dll");
                System.exit(1);
            }
        } catch (Exception e) {
            Logger.log(e.toString());
            Logger.log(e.getMessage());
        }
    }
}
