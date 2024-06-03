package ui;

import component.Block;
import component.MineGame;
import config.BlockConstant;
import config.ConfigReader;
import config.MineConfig;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements MouseListener, ActionListener {

    private boolean gameOver;
    private boolean victory;
    private final int numOfMine;

    MineGame mineGame;
    MineConfig mineConfig;

    //constructor
    GamePanel() {
        setFocusable(false);
        mineConfig = ConfigReader.readGameData();
        int xSize = mineConfig.getMatrixXSize();
        int ySize = mineConfig.getMatrixYSize();
        numOfMine = mineConfig.getNumOfMines();

        //Initialize game data
        mineGame = new MineGame(this,numOfMine, xSize, ySize);
        
        //show button graphics to the screen
        addToMainPanel(xSize, ySize);

        validate();
    }

    private void addToMainPanel(int xLength, int yLength) {
        this.setLayout(new GridLayout(xLength, yLength, 5, 5));
        mineGame.getMapBlock().forEach((s, block) -> {
            this.add(block.getBlockButton());
        });
    }

    private void showActualScreen() {
        mineGame.getMapBlock().forEach((s, block) -> {
            if (block.isFlagged() && !block.isMine()) {
                block.changeUserScreen(BlockConstant.FLAGGED_MINE);
            } else if (!block.isFlagged() && block.isMine()) {
                block.changeUserScreen(BlockConstant.MINE);
            }
        });
    }

    private void flagButton (Block targetBlock) {
        if (!targetBlock.isFlagged() && targetBlock.getUserScreen().equals(BlockConstant.UNKNOWN)) {
            targetBlock.setFlagged();
            targetBlock.setUserBlock(BlockConstant.FLAG);
            targetBlock.changeUserScreen(BlockConstant.FLAG);
        } else if (targetBlock.isFlagged()) {
            targetBlock.resetFlagged();
            targetBlock.setUserBlock(BlockConstant.UNKNOWN);
            targetBlock.changeUserScreen(BlockConstant.UNKNOWN);
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
        if (!victory && !isGameOver() && SwingUtilities.isRightMouseButton(e)) {
            mineGame.getMapBlock().forEach((s, block) -> {
                if (block.getBlockButton().equals(obj)) {
                    flagButton(block);
                    revalidateButtons();
                }
            });

            setVictory(mineGame.checkVictory(numOfMine));

            if (isGameOver() && !isVictory()) {
                showActualScreen();
                revalidateButtons();
            } else if (isVictory()) {
                mineGame.changeToVictoryScreen();
                revalidateButtons();
            }
        } else if (!isVictory() && !isGameOver() && SwingUtilities.isLeftMouseButton(e)) {
            mineGame.getMapBlock().forEach((s, block) -> {
                if (block.getBlockButton().equals(obj)) {
                    mineGame.sweepMine(block, false);
                    revalidateButtons();
                }
            });

            setVictory(mineGame.checkVictory(numOfMine));

            if (isGameOver() && !isVictory()) {
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

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isVictory() {
        return victory;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setVictory(boolean victory) {
        this.victory = victory;
    }
}