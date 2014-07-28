package windows;


import com.sun.jna.Library;

public interface WindowsUtility  extends Library{
    void increaseVolume();
    void decreaseVolume();
    void mute();
}
