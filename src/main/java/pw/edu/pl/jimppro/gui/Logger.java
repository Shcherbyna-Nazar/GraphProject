package pw.edu.pl.jimppro.gui;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

public class Logger extends TextArea {

    private static Logger instance = null;
    private int flag;

    public Logger() {
        setFont(Font.font("Serif", 16));
        flag = 1;
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void setMessage(String s, int color) {
        setText(s);
        if (color == 1) {
            setStyle("-fx-text-inner-color: green;");
        }
        else if (color == 0) {
            setStyle("-fx-text-inner-color: red;");
        }
        else {
            setStyle("-fx-text-inner-color: orange;");
        }
        flag = color;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int value) {
        flag = value;
    }
}
