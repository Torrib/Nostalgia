package input;

/**
 * Created by thb on 29.06.2014.
 */
public class HotkeyConverter {

    public static int getHotkeyValue(String hotkey){
        switch (hotkey.toLowerCase()){
            case "cross":
            case "a":
                return 0;
            case "circle":
            case "b":
                return 1;
            case "triangle":
            case "y":
                return 2;
            case "square":
            case "x":
                return 3;
            case "l1":
                return 4;
            case "r1":
                return 5;
            case "select":
            case "back":
                return 6;
            case "start":
                return 7;
            case "l3":
                return 8;
            case "r3":
                return 9;
            default:
                return -1;
        }
    }
}
