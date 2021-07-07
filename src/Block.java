import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;

class Block {

    Font font;
    JButton button;
    int hashNum;
    char status;
    String userScreen = "?";
    String actualScreen = " ";
    boolean isMine;
    boolean visited;
    boolean isFlagged;
    int adjMineNum;

    Block up = null;
    Block upLDiag = null;
    Block upRDiag = null;
    Block left = null;
    Block right = null;
    Block low = null;
    Block lowLDiag = null;
    Block lowRDiag = null;

    public void setBlock(char status) {
        this.status = status;
        if (status == 'M')
            this.isMine = true;
    }

    public void setUserBlock(String status) {
        this.userScreen = status;
    }

    public void setHashNum (int hashNum) {
        this.hashNum = button.hashCode();
    }

    public void setTextColor (int r, int g, int b, int num) {
        button.setText(num + "");
        button.setForeground(new Color(r, g, b));
    }

    public void setTextColor (int r, int g, int b, String str) {
        button.setText(str);
        button.setForeground(new Color(r, g, b));
    }

    public void setButtonColor (int r, int g, int b, String str) {
        button.setText(str);
        button.setBackground(new Color(r, g, b));
    }

    protected void goldenButton (String text) {
        userScreen = text;
        if (userScreen.equals(MineChar.S_ZONE)) {
            setButtonColor(255, 255, 255, userScreen); //White
        }
    }

    protected void changeUserScreen(String text) {
        if (text.equals(MineChar.S_ZONE)) {
            setButtonColor(255, 255, 255, text); //White
        } else if (text.equals(MineChar.FLAG)) {
            setButtonColor(0, 255, 0, text); //green
        } else if (text.equals(MineChar.MINE)) {
            setTextColor(148, 21, 21, text); //Dark-Red
            setButtonColor(255, 0, 0, text); //Red
        } else if (text.equals("¡Ú")) {
            setTextColor(0, 0, 0, "NICE"); //BLACK
            setButtonColor(255, 0, 255, " ");//PINK
        } else if (text.equals(MineChar.UNKNOWN)) {
            setTextColor(0, 0, 0, MineChar.UNKNOWN); //BLACK
            setButtonColor(235, 234, 234, MineChar.UNKNOWN); //light-blue
        } else if (text.equals(MineChar.FLAGED_MINE)) {
            setTextColor(148, 21, 21, text); //Dark-Red
            setButtonColor(255, 255, 0, MineChar.FLAGED_MINE); //light-blue
        }
    }

    Block () {
        Insets inset = new Insets(0, 0, 0, 0);
        font = new Font("?", 1, 10);
        button = new JButton(); //initial icon
        button.setFont(font);
        button.setMargin(inset);
        changeUserScreen(MineChar.UNKNOWN);
        button.setVisible(true);
    }

}