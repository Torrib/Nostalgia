package controllerOld;

import com.sun.jna.Library;

public interface ControllerInterface extends Library {
    boolean initController();
    boolean getControllerConnected(int controller);
    int getGuideStatus(int controller);
    int getControllerState(int controller, ControllerStructure controllerStruct);
    void startVibration(int controller, int leftMotor, int rightMotor);
    void stopVibration(int controller);
    void turnControllerOff(int controller); //Only works for xbox controllers
    void close();
//    void getBatteryState(int controllerOld);
}