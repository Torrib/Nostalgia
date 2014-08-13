package main;

import java.util.HashMap;
import java.util.Map;

public class PredefinedProgramCommands {

    public static Map<String, String> preProgram = new HashMap<>();
    public static Map<String, String> postProgram = new HashMap<>();

    static{
        AddPredefinedProgramCommands(".bat", "cmd /c ", "");
        AddPredefinedProgramCommands(".jar", "java -jar", "");
    }

    private static void AddPredefinedProgramCommands(String extension, String preString, String postString){
        preProgram.put(extension, preString);
        postProgram.put(extension, postString);
    }

    public static String getPreString(String extension){
        if(preProgram.containsKey(extension))
            return preProgram.get(extension);

        return "";
    }

    public static String getPostString(String extension){
        if(postProgram.containsKey(extension))
            return postProgram.get(extension);

        return "";
    }


}
