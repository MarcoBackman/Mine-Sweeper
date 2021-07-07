import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;

/*
 * Author: MarcoBackman
 * Email: roni2006@hanmail.net
 * Last modification date: 4-29-2020
 * Java Version: 1.6.0_26
 * Program Version: 0.0.1.1
 * Description: This class is used for the main frame of the mine sweeping game.
 *              Please report Bug via email.
 */

public class MainFrame extends JFrame implements ActionListener, MenuKeyListener, KeyListener {
    final int WIDTH = 600;
    final int HEIGHT = 600;

    JMenuBar menuBar = new JMenuBar();
    JMenu firstMenu, secondMenu, thirdMenu, fourthMenu;

    JPanel mainPanel;

    MainFrame () {
        super("Mine Sweeper");
        addKeyListener(this);
    }

    // Exterior frame of menu bar
    private void MenuBarSetting () {
        setJMenuBar(menuBar);

        firstMenu = new JMenu("File");
        setFileMenu();
        menuBar.add(firstMenu);

        secondMenu = new JMenu("Edit");
        setEditMenu();
        menuBar.add(secondMenu);

        fourthMenu = new JMenu("Help");
        setHelpMenu();
        menuBar.add(fourthMenu);   
    }

    // Internal components of "file" menu from menu bar
    private void setFileMenu () {
        JMenuItem newGame = new JMenuItem("New Game (F5)");
        newGame.addActionListener(this);
        newGame.addMenuKeyListener(this);
        firstMenu.add(newGame);

        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(this);
        open.addMenuKeyListener(this);
        firstMenu.add(open);

        JMenuItem exitProg = new JMenuItem("Exit");
        exitProg.addActionListener(this);
        exitProg.addMenuKeyListener(this);
        firstMenu.add(exitProg);
    }

    private void setEditMenu () {

    }

    private void setHelpMenu () {

    }

    private void frameSetting() {
        setResizable(false);
        setSize(WIDTH, HEIGHT);

        setFocusable(true);
        requestFocusInWindow();

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();

        setLocation(centerPoint.x - getSize().width / 2,
           centerPoint.y - getSize().height / 2);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        MenuBarSetting();
        setVisible(true);
    }

    private void setFrame() {
        mainPanel = new GamePanel();
        this.add(mainPanel);
        this.validate();
        this.frameSetting();
    }

    public static void main(String[] args) {
        MainFrame instMF = new MainFrame();
        instMF.setFrame();
    }

    @Override
    public void menuKeyTyped(MenuKeyEvent e) {

    }

    @Override
    public void menuKeyPressed(MenuKeyEvent e) {

    }

    @Override
    public void menuKeyReleased(MenuKeyEvent e) {

    }

    //menubar item features
    @Override
    public void actionPerformed (ActionEvent e) {
        Object obj = e.getSource();
        if (obj.equals(firstMenu.getItem(0))) { // file - new game
            //generate new game board
            this.remove(mainPanel);
            mainPanel = new GamePanel();
            this.add(mainPanel);
            this.validate();
        } else if (obj.equals(firstMenu.getItem(2))) { //exit program
            System.exit(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Object key = e.getKeyCode();
        if (key.equals(KeyEvent.VK_F5)) {
            this.remove(mainPanel);
            mainPanel = new GamePanel();
            this.add(mainPanel);
            this.validate();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}