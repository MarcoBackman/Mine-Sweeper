package component;

import config.BlockConstant;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;

/**
 * A Block that holds a single space
 */
public class Block {

    private final Font font;
    //Block button
    private final JButton button;
    private int hashNum;
    private char status;
    private String userScreen = "?";

    private String actualScreen = " ";
    private boolean isMine;
    private boolean visited;
    private boolean isFlagged;
    private int adjacentNumOfMine;

    //Adjacent blocks
    private Block up = null;
    private Block upLDiag = null;
    private Block upRDiag = null;
    private Block left = null;
    private Block right = null;
    private Block down = null;
    private Block downLDiag = null;
    private Block downRDiag = null;

    public Block() {
        Insets inset = new Insets(0, 0, 0, 0);
        font = new Font("?", Font.BOLD, 10);
        button = new JButton(); //initial icon
        button.setFont(font);
        button.setMargin(inset);
        changeUserScreen(BlockConstant.UNKNOWN);
        button.setVisible(true);
    }

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
        if (userScreen.equals(BlockConstant.S_ZONE)) {
            setButtonColor(255, 255, 255, userScreen); //White
        }
    }

    public void changeUserScreen(String text) {
        if (text.equals(BlockConstant.S_ZONE)) {
            setButtonColor(255, 255, 255, text); //White
        } else if (text.equals(BlockConstant.FLAG)) {
            setButtonColor(0, 255, 0, text); //green
        } else if (text.equals(BlockConstant.MINE)) {
            setTextColor(148, 21, 21, text); //Dark-Red
            setButtonColor(255, 0, 0, text); //Red
        } else if (text.equals(BlockConstant.VICTORY)) {
            setTextColor(0, 0, 0, "NICE"); //BLACK
            setButtonColor(255, 0, 255, " ");//PINK
        } else if (text.equals(BlockConstant.UNKNOWN)) {
            setTextColor(0, 0, 0, BlockConstant.UNKNOWN); //BLACK
            setButtonColor(235, 234, 234, BlockConstant.UNKNOWN); //light-blue
        } else if (text.equals(BlockConstant.FLAGGED_MINE)) {
            setTextColor(148, 21, 21, text); //Dark-Red
            setButtonColor(255, 255, 0, BlockConstant.FLAGGED_MINE); //light-blue
        }
    }

    public Font getFont() {
        return font;
    }

    public JButton getBlockButton() {
        return button;
    }

    public int getHashNum() {
        return hashNum;
    }

    public char getStatus() {
        return status;
    }

    public String getUserScreen() {
        return userScreen;
    }

    public String getActualScreen() {
        return actualScreen;
    }

    public void setActualScreen(String actualScreen) {
        this.actualScreen = actualScreen;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine() {
        isMine = true;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited() {
        visited = true;
    }

    public void resetVisited() {
        visited = false;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged() {
        isFlagged = true;
    }

    public void resetFlagged() {
        isFlagged = false;
    }

    public int getAdjacentNumOfMine() {
        return adjacentNumOfMine;
    }

    public void setAdjacentNumOfMine(int mineNum) {
        adjacentNumOfMine = mineNum;
    }

    public Block getUp() {
        return up;
    }

    public Block getUpLDiag() {
        return upLDiag;
    }

    public Block getUpRDiag() {
        return upRDiag;
    }

    public Block getLeft() {
        return left;
    }

    public Block getRight() {
        return right;
    }

    public Block getDown() {
        return down;
    }

    public Block getDownLDiag() {
        return downLDiag;
    }

    public Block getDownRDiag() {
        return downRDiag;
    }

    public void setDownRDiag(Block downRDiag) {
        this.downRDiag = downRDiag;
    }

    public void setDownLDiag(Block downLDiag) {
        this.downLDiag = downLDiag;
    }

    public void setDown(Block down) {
        this.down = down;
    }

    public void setRight(Block right) {
        this.right = right;
    }

    public void setLeft(Block left) {
        this.left = left;
    }

    public void setUpRDiag(Block upRDiag) {
        this.upRDiag = upRDiag;
    }

    public void setUpLDiag(Block upLDiag) {
        this.upLDiag = upLDiag;
    }

    public void setUp(Block up) {
        this.up = up;
    }

    public List<Block> getAdjacentBlocks() {
        List<Block> blockList = Collections.synchronizedList(new ArrayList<>());
        if (getUp() != null) {
            blockList.add(getUp());
        }

        if (getDown() != null) {
            blockList.add(getDown());
        }

        if (getLeft() != null) {
            blockList.add(getLeft());
        }

        if (getRight() != null) {
            blockList.add(getRight());
        }

        if (getUpLDiag() != null) {
            blockList.add(getUpLDiag());
        }

        if (getUpRDiag() != null) {
            blockList.add(getUpRDiag());
        }

        if (getDownLDiag() != null) {
            blockList.add(getDownLDiag());
        }

        if (getDownRDiag() != null) {
            blockList.add(getDownRDiag());
        }
        return blockList;
    }


}