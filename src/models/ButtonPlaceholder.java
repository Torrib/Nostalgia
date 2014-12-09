package models;


import jgamepad.enums.Button;

public class ButtonPlaceholder {

    private Button button;

    public ButtonPlaceholder(){
        button = Button.A;
    }

    public ButtonPlaceholder(Button button){
        this.button = button;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }
}


