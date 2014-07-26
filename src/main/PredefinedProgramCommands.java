package main;

import java.util.HashMap;
import java.util.Map;

public class PredefinedProgramCommands {

    Map<String, String> preProgram = new HashMap<>();
    Map<String, String> postProgram = new HashMap<>();

    public PredefinedProgramCommands(){
        AddPredefinedProgramCommands(".bat", "cmd /c ", "");
        AddPredefinedProgramCommands(".jar", "java -jar", "");
    }

    public void AddPredefinedProgramCommands(String extension, String preString, String postString){
        preProgram.put(extension, preString);
        postProgram.put(extension, preString);
    }

    public String getPreString(String extension){
        if(preProgram.containsKey(extension))
            return preProgram.get(extension);

        return "";
    }

    public String getPostString(String extension){
        if(postProgram.containsKey(extension))
            return postProgram.get(extension);

        return "";
    }


}
