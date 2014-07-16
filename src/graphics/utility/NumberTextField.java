package graphics.utility;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField
{
    public NumberTextField(){
        super();
    }

    public NumberTextField(String text){
        super(text);
    }

    public NumberTextField(int text){
        super(""+text);
    }

    @Override
    public void replaceText(int start, int end, String text)
    {
        if (validate(text))
        {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text)
    {
        if (validate(text))
        {
            super.replaceSelection(text);
        }
    }

    private boolean validate(String text)
    {
        if (text.matches("[0-9]") || text == "")
        {
            return true;
        }
        return false;
    }
}