import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.util.ArrayList;
//import java.util.HashMap; //unsynchronized
//import java.util.Hashtable; //synchronized
import java.util.LinkedHashMap;

/*
 * Author: MarcoBackman
 * Email: roni2006@hanmail.net
 * Last modification date: 5-4-2020
 * Java Version: 1.6.0_26
 * Program Version: 0.0.1.1
 * Description: This class is used for the main panel that screens
 *                 out the actual mine sweeping game.
 *              Please report Bug via email.
 */

public class GamePanel<K,V> extends JPanel implements MouseListener, ActionListener {
    
    MineChar instCH = new MineChar();
    boolean G_OVER;
    boolean VICTORY;

    LinkedHashMap<K,V> mapBlock = new LinkedHashMap<K, V>(); //use this for better performance
    ArrayList<Block> arrButton = new ArrayList<Block>();

    //constructor
    GamePanel() {
        setFocusable(false);
        //first take user input
        //then check if user data is valid
        checkValidity();
        //generate grid
        generateButtons(MineChar.H_MATRIX_NUM);
        generateMine(MineChar.H_MINE_NUM, MineChar.H_MATRIX_NUM);        

        //connect mine blocks
        connectBlocks(MineChar.H_MATRIX_NUM);
        
        //show button graphics to the screen
        addToMainPanel(MineChar.H_MATRIX_NUM);

        validate();
    }


    /*
     *******************************************************
     *                                                     *
     *                  initialization                     *
     *                                                     *
     *******************************************************
     */

    //check if the game is available by given length and given number of mines on initial run
    private void checkValidity() {

    }

    private void generateButtons(int len) {
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                Block tempBlock = new Block();
                tempBlock.button.setFocusable(false);
                tempBlock.button.addMouseListener(this);
                arrButton.add(tempBlock);
            }
        }
    }

    private void addToMainPanel(int len) {
        this.setLayout(new GridLayout(len, len, 5, 5));
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                Block tempBlock = arrButton.get(i * len + j);
                this.add(arrButton.get(i * len + j).button);
            }
        }
    }

    private void generateMine (int maxMineNum, int len) {
        int count = 0;
        while (count < maxMineNum) {
            int x = (int) (Math.random() * len);
            int y = (int) (Math.random() * len);
            Block tempBlock = arrButton.get(y * len + x);
            if (!tempBlock.actualScreen.equals(instCH.MINE)) {
                tempBlock.actualScreen = "*";
                tempBlock.isMine = true;
                count++;
            }
        }
    }

    private void connectBlocks (int len) {
        for (int i = 0; i < arrButton.size(); i++) {
            if(i == 0) { //upper left vertex
                Block tempBlock = arrButton.get(0);
                tempBlock.right = arrButton.get(1);
                tempBlock.low = arrButton.get(len);
                tempBlock.lowRDiag = arrButton.get(len + 1);
                arrButton.set(0, tempBlock);
            }

            else if(i == len - 1) { //upper right vertex
                Block tempBlock = arrButton.get(i);
                tempBlock.left = arrButton.get(i - 1);
                tempBlock.low = arrButton.get(i + len + 1);
                tempBlock.lowLDiag = arrButton.get(i + len - 1);
                arrButton.set(i, tempBlock);
            }

            else if(i == len * len - len) { //lower left vertex
                Block tempBlock = arrButton.get(i);
                tempBlock.up = arrButton.get(i - len);
                tempBlock.right = arrButton.get(i + 1);
                tempBlock.upRDiag = arrButton.get(i - len + 1);
                arrButton.set(i, tempBlock);
            }

            else if(i == arrButton.size() - 1) { //lower right vertex
                Block tempBlock = arrButton.get(i);
                tempBlock.left = arrButton.get(i - 1);
                tempBlock.up = arrButton.get(i - len);
                tempBlock.upLDiag = arrButton.get(i - len - 1);
                arrButton.set(i, tempBlock);
            }

            else if(i != 0 && i < len - 1) { //first row
                Block tempBlock = arrButton.get(i);
                tempBlock.left = arrButton.get(i - 1);
                tempBlock.right = arrButton.get(i + 1);
                tempBlock.low = arrButton.get(i + len);
                tempBlock.lowLDiag = arrButton.get(i + len - 1);
                tempBlock.lowRDiag = arrButton.get(i + len + 1);
                arrButton.set(i, tempBlock);
            }

            else if(i % len == 0) { //first column
                Block tempBlock = arrButton.get(i);
                tempBlock.up = arrButton.get(i - len);
                tempBlock.right = arrButton.get(i + 1);
                tempBlock.low = arrButton.get(i + len);
                tempBlock.upRDiag = arrButton.get(i - len + 1);
                tempBlock.lowRDiag = arrButton.get(i + len + 1);
                arrButton.set(i, tempBlock);
            }

            else if(i > arrButton.size() - len) { //last row
                Block tempBlock = arrButton.get(i);
                tempBlock.up = arrButton.get(i - len);
                tempBlock.right = arrButton.get(i + 1);
                tempBlock.left = arrButton.get(i - 1);
                tempBlock.upRDiag = arrButton.get(i - len + 1);
                tempBlock.upLDiag = arrButton.get(i - len - 1);
                arrButton.set(i, tempBlock);
            }

            else if((i + 1) % len == 0) { //last colunm
                Block tempBlock = arrButton.get(i);
                tempBlock.up = arrButton.get(i - len);
                tempBlock.low = arrButton.get(i + len);
                tempBlock.left = arrButton.get(i - 1);
                tempBlock.upLDiag = arrButton.get(i - len - 1);
                tempBlock.lowLDiag = arrButton.get(i + len - 1);
                arrButton.set(i, tempBlock);
            }

            else { //middle
                Block tempBlock = arrButton.get(i);
                tempBlock.up = arrButton.get(i - len);
                tempBlock.right = arrButton.get(i + 1);
                tempBlock.low = arrButton.get(i + len);
                tempBlock.left = arrButton.get(i - 1);
                tempBlock.upLDiag = arrButton.get(i - len - 1);
                tempBlock.upRDiag = arrButton.get(i - len + 1);
                tempBlock.lowLDiag = arrButton.get(i + len - 1);
                tempBlock.lowRDiag = arrButton.get(i + len + 1);
                arrButton.set(i, tempBlock);
            }
        }
    }

    /*
     *******************************************************
     *                                                     *
     *               End of initialization                 *
     *                                                     *
     *******************************************************
     */


    /*
     *  Mine sweep. Checks the vicinity safezone until it reaches the mine-adjacent block
     */
    private void sweepMine (Block targetBlock, boolean rightClicked) {
        if (targetBlock.isMine && !targetBlock.isFlagged) {
            G_OVER = true;
        } else if (!targetBlock.isFlagged && !rightClicked) {
            traverse(targetBlock, null);
            visitReset();
        }
    }

    private Block traverse (Block target, Block previous) {
        if (target.isMine || target.isFlagged) {
            target.visited = true;
            return previous;
        } else {
            target.userScreen = instCH.S_ZONE;
        }
        
        //calls adjacent mine count method
        int mineNum = countMine (target);

        if (mineNum > 0) {
            target.adjMineNum = mineNum;
            if (mineNum == 1) {
               target.setTextColor(37, 37, 245, 1); //blue (cyan)
            } else if (mineNum == 2) {
               target.setTextColor(37, 177, 245, 2); //light-blue
            } else if (mineNum == 3) {
               target.setTextColor(37, 245, 172, 3); //green-light-blue 
            } else if (mineNum == 4) {
               target.setTextColor(37, 245, 37, 4); //light-green 
            } else if (mineNum == 5) {
               target.setTextColor(177, 245, 37, 5); //yellow-green 
            } else if (mineNum == 6) {
               target.setTextColor(245, 245, 37, 6); //yellow
            } else if (mineNum == 7) {
               target.setTextColor(245, 172, 37, 7); //orange
            } else if (mineNum == 8) {
               target.setTextColor(245, 37, 37, 8); //red 
            } else {
               target.setTextColor(148, 21, 21, 9); //dark-red
            }
            return previous;
        } else {
            target.changeUserScreen(MineChar.S_ZONE);
        }
        target.visited = true;
        if (target.up != null)
            if (!target.up.visited)
                traverse(target.up, target);

        if (target.low != null)
            if (!target.low.visited)
                traverse(target.low, target);

        if (target.left != null)
            if (!target.left.visited)
                traverse(target.left, target);

        if (target.right != null)
            if (!target.right.visited)
                traverse(target.right, target);

        if (target.upLDiag != null)
            if (!target.upLDiag.visited)
                traverse(target.upLDiag, target);

        if (target.upRDiag != null)
            if (!target.upRDiag.visited)
                traverse(target.upRDiag, target);

        if (target.lowLDiag != null)
            if (!target.lowLDiag.visited)
                traverse(target.lowLDiag, target);

        if (target.lowRDiag != null)
            if (!target.lowRDiag.visited)
                traverse(target.lowRDiag, target);
        return previous;
    }

    private void visitReset() {
        for (int i = 0; i < arrButton.size(); i++) {
            arrButton.get(i).visited = false;
        }
    }

    //checks the adjacent block's number of mine
    private int countMine (Block target) {
        int count = 0;

        if (target.up != null) {
            if (target.up.isMine)
                ++count;
        }

        if (target.low != null) {
            if (target.low.isMine)
                ++count;
        }

        if (target.left != null) {
            if (target.left.isMine)
                ++count;
        }

        if (target.right != null) {
            if (target.right.isMine)
                ++count;
        }

        if (target.upLDiag != null) {
            if (target.upLDiag.isMine)
                ++count;
        }

        if (target.upRDiag != null) {
            if (target.upRDiag.isMine)
                ++count;
        }

        if (target.lowLDiag != null) {
            if (target.lowLDiag.isMine)
                ++count;
        }

        if (target.lowRDiag != null) {
            if (target.lowRDiag.isMine)
                ++count;
        }
        return count;
    }

    /*
     *  checks the game's victory condition on every move.
     */

    private boolean checkVictory (int difficulty) {
        int mine = difficulty;
        int count = 0;
        for (int i = 0; i < arrButton.size(); i++) {
            Block target = arrButton.get(i);
            if (target.isFlagged && !target.isMine) {
                return false;
            } else if (target.isFlagged && target.isMine) {
                count++;
            }
        }
        if (count == mine) {
            VICTORY = true;
            return true;
        }
        return false;
    }

    private void showActualScreen() {
        for (int i = 0; i < arrButton.size(); i++) {
            Block target = arrButton.get(i);
            if (target.isFlagged && !target.isMine) {
                target.changeUserScreen(MineChar.FLAGED_MINE);
            } else if (!target.isFlagged && target.isMine) {
                target.changeUserScreen(MineChar.MINE);
            }
        }
    }

    private void showVictoryScreen() {
        for (int i = 0; i < arrButton.size(); i++) {
            Block target = arrButton.get(i);
            if (target.isFlagged && target.isMine) {
                target.changeUserScreen("¡Ú");
            }
        }
    }

    private void flagButton (Block targetBlock) {
        if (!targetBlock.isFlagged && targetBlock.userScreen.equals(MineChar.UNKNOWN)) {
            targetBlock.isFlagged = true;
            targetBlock.userScreen = MineChar.FLAG;
            targetBlock.changeUserScreen(MineChar.FLAG);
        } else if (targetBlock.isFlagged) {
            targetBlock.isFlagged = false;
            targetBlock.userScreen = MineChar.UNKNOWN;
            targetBlock.changeUserScreen(MineChar.UNKNOWN);
        }
    }

    private void revalidateButtons() {
        this.updateUI();
        this.validate();
    }

    /*
     *  When user clicks the button
     */
    @Override
    public void actionPerformed(ActionEvent e) {
     
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Object obj = e.getSource();
        if (!VICTORY && !G_OVER && SwingUtilities.isRightMouseButton(e)) {
            for (int i = 0; i < arrButton.size(); i++) { //this degrades the performance. Need new data-structure
                Block tempButton = arrButton.get(i);
                if (tempButton.button.equals(obj)) {
                    //flag the button
                    flagButton(tempButton);
                    revalidateButtons();
                    break;
                }
            }
            VICTORY = checkVictory(MineChar.H_MINE_NUM);
            if (G_OVER && !VICTORY) {
                showActualScreen();
                revalidateButtons();
            } else if (VICTORY) {
                showVictoryScreen();
                revalidateButtons();
            }
        } else if (!VICTORY && !G_OVER && SwingUtilities.isLeftMouseButton(e)) {
            for (int i = 0; i < arrButton.size(); i++) { //this degrades the performance. Need new data-structure
                Block tempButton = arrButton.get(i);
                if (tempButton.button.equals(obj)) {
                    //analyze clicked button
                    sweepMine(tempButton, false);
                    revalidateButtons();
                    break;
                }
            }
            VICTORY = checkVictory(MineChar.H_MINE_NUM);
            if (G_OVER && !VICTORY) {
                showActualScreen();
                revalidateButtons();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}