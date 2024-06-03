import java.util.ArrayList;
import java.util.Scanner;

/*
 * Author: MarcoBackman
 * Email:  roni2006@hanmail.net
 * Last modification date: 4-29-2020
 * Java Version: 1.6.0_26
 * Program Version: 0.0.1.1
 * Description: Mine Sweeper manual version
 *              Please report Bug via email.
 * UI Language: Korean.
 */


class G1 {
    final char MINE = '*';
    final char S_ZONE = '-';
    final char FLAG = '!';
    final char UNKNOWN = '?';

    final int E_MATRIX_NUM = 10;
    final int M_MATRIX_NUM = 15;
    final int H_MATRIX_NUM = 20;

    final int E_MINE_NUM = 10;
    final int M_MINE_NUM = 15;
    final int H_MINE_NUM = 40;

    final int ZERO_ASCII = 48;
    final int NINE_ASCII = 48;

    boolean G_OVER;
    int safeZoneNum;

    char[][] answerGrid;

    ArrayList<Block> arrList = new ArrayList<Block> ();

    class Block {
        char status;
        char userScreen = '?';
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

        public void setUserBlock(char status) {
            this.userScreen = status;
        }
    }

    public static void main (String[] args) {
        G1 inst = new G1();
        inst.start();
    }

    private void start() {
        System.out.println("������ �ʿ��Ͻø� \"help\" �� �Է��Ͻÿ�.");
        System.out.println("Select level (H/M/E)");
        Scanner sc = new Scanner(System.in);
        int correct = userLevel(sc.nextLine());
        if (correct == -1) {
            start();
        } else if (correct == 0) {
            System.out.println("�Է°��� �߸� �Ǿ����ϴ�. �ٽ� �Է��� �ֽʽÿ�.");
            start();
        } else {
            while (!G_OVER) {
                String[] inputs = userInput(sc, correct);
                sweepMine (Integer.parseInt(inputs[1]),
                   Integer.parseInt(inputs[2]), correct, inputs[0].toUpperCase());
                showPlayerScreen(correct);
                boolean victory = checkVictory(correct);
                if (G_OVER || victory) {
                    showGOverScreen(correct, victory);
                    break;
                }
            }
        }
    }

    private void printHelp() {
        System.out.println("���̵� ���ý� 'H', 'M', 'E'�� �Է��ϰ�\n" +
           " ���͸� ġ�ø� ���۵˴ϴ�. \n ��ǥ�� �Է½�" +
           " [��ɾ�] [x��ǥ] [y��ǥ]�� ���� �־��ݴϴ�.\n" +
           " ��ɾ�� 'P', 'F' �� ������ 'P'�� �����ϱ� 'F'�� ��� ����� �Դϴ�.\n" +
           " ���� ��ɾ�: P 5 5");
    }

    private int userLevel (String level) {
        level = level.toUpperCase();
        if (level.equals("HELP")) {
            printHelp();
            return -1;
        } else if (level.equals("E")) {
            selectEasy();
            return E_MATRIX_NUM;
        }
        return 0;
    }

    private void selectEasy() {
        System.out.println("������ �����ϼ̽��ϴ�.");
        System.out.println("���� ����:" + E_MINE_NUM);
        char[][] grid = makeGrid(E_MATRIX_NUM);
        int gridTotNum = E_MATRIX_NUM * E_MATRIX_NUM;
        safeZoneNum = gridTotNum - E_MINE_NUM;
        printGrid(grid);
        answerGrid = generateMine(E_MINE_NUM, grid);
        //gridToNodes(grid);
        connectBlocks(E_MATRIX_NUM);
    }

    private String[] userInput (Scanner sc, int length) {
        System.out.println("Your guess is? [x y]");
        String str = sc.nextLine();
        String[] strArr = str.split(" ");
        if (strArr.length != 3) {
            System.out.println("Wrong number of input");
            return userInput(sc, length);
        } else if (!isValidCommd(strArr[0])) {
            System.out.println("Wrong command! Please type the command correctly");
            return userInput(sc, length);
        } else if (!isValidCoord(strArr[1], strArr[2], length)) {
            System.out.println("Wrong input! Please type coordinates again");
            return userInput(sc, length);
        }
        return strArr;
    }

    private boolean isValidCommd(String firstInput) {
        firstInput = firstInput.toUpperCase();
        if (firstInput.length() != 1) {
            return false;
        } else if (firstInput.charAt(0) == 'P' || firstInput.charAt(0) == 'F') {
            return true;
        }
        return false;
    }

    //level represents the length of the grid
    private boolean isValidCoord(String x, String y, int level) {
        int xCoord = Integer.parseInt(x);
        int yCoord = Integer.parseInt(y);
        if ((xCoord > NINE_ASCII && yCoord > NINE_ASCII)
           && (xCoord < ZERO_ASCII && yCoord < ZERO_ASCII)) {
            return false;
        } else if (xCoord > level || yCoord > level || xCoord < 1 || yCoord < 1) {
            return false;
        }
        return true;
    }

    private char[][] makeGrid(int length) {
        char[][] propagated = new char[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                propagated[i][j] = '?';
            }
        }
        return propagated;
    }

    private boolean checkVictory (int difficulty) {
        int mine = 0;
        if (difficulty == E_MATRIX_NUM)
            mine = E_MINE_NUM;
        int count = 0;
        for (int i = 0; i < arrList.size(); i++) {
            Block target = arrList.get(i);
            if (target.userScreen == FLAG && target.status != 'M') {
                return false;
            } else if (target.userScreen == FLAG && target.status == 'M') {
                count++;
            }
        }
        if (count == mine)
            return true;
        return false;
    }

    private char[][] generateMine (int maxMineNum, char[][] grid) {
        int count = 0;
        while (count < maxMineNum) {
            int x = (int) (Math.random() * grid[0].length);
            int y = (int) (Math.random() * grid.length);
            if (grid[y][x] != 'M') {
                grid[y][x] = 'M';
                count++;
            }
        }
        return grid;
    }

    private void sweepMine (int x, int y, int len, String command) {
        char letter = command.charAt(0);
        int index = len * (y - 1) + (x - 1);
        Block targetBlock = arrList.get(index);
        if (targetBlock.isMine && letter != 'F') {
            G_OVER = true;
        } else {
            if (targetBlock.userScreen == S_ZONE) {
                System.out.println("Already guessed");
            } else if (letter == 'F' && targetBlock.isFlagged) {
                targetBlock.userScreen = UNKNOWN;
                targetBlock.isFlagged = false;
            } else if (letter == 'F' && !targetBlock.isFlagged) {
                targetBlock.isFlagged = true;
                targetBlock.userScreen = FLAG;
            } else if (letter == 'P') {
                traverse(targetBlock, null);
                visitReset();
            } else {
                System.out.println("Bug occured");
            }
        }
    }

    private void visitReset() {
        for (int i = 0; i < arrList.size(); i++) {
            arrList.get(i).visited = false;
        }
    }

    private Block traverse (Block target, Block previous) {
        if (target.isMine || target.isFlagged) {
            target.visited = true;
            return previous;
        } else {
            target.userScreen = S_ZONE;
        }

        int mineNum = countMine (target);
        if (mineNum > 0) {
            target.userScreen = ((target.adjMineNum = mineNum) + "").charAt(0);
            return previous;
        } else {
            target.userScreen = S_ZONE;
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

    private void printGrid (char[][] grid) {
        System.out.println("Your Current Screen");
        System.out.printf("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.printf("    ");
        for (int i = 0; i < grid.length; i++)
            System.out.printf("%-3d", i + 1);
        System.out.println();
        for (int i = 0; i < grid.length; i++) {
            if (i < grid.length - 1)
                System.out.printf("%-2d", i + 1);
            else
                System.out.printf("%d", i + 1);
            for (int j = 0; j < grid[i].length; j++) {
                System.out.printf("%3c", grid[i][j]);
            }
            System.out.println();
        }
    }

    private void showPlayerScreen(int len) {
        System.out.printf("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.printf("    ");

        for (int i = 0; i < len; i++)
            System.out.printf("%-3d", i + 1);
        int count = 1;
        for (int i = 0; i < arrList.size(); i++) {
            if (i % len == 0) {
                System.out.println("");
                System.out.printf("%-2d", count);
                ++count;
            }
            System.out.printf("%3c", arrList.get(i).userScreen);
        }
        System.out.println("");
    }

    private void showGOverScreen (int len, boolean victory) {
        System.out.printf("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.printf("    ");

        for (int i = 0; i < len; i++)
            System.out.printf("%-3d", i + 1);
        int count = 1;
        for (int i = 0; i < arrList.size(); i++) {
            if (i % len == 0) {
                System.out.println("");
                System.out.printf("%-2d", count);
                ++count;
            }
            char target = arrList.get(i).status;
            if (target == 'M')
                System.out.printf("%3c", target);
            else
                System.out.printf("%3c", arrList.get(i).userScreen);
        }
        if (!victory)
            System.out.println("\n*********************************\n" +
               "\t Boom! Game Over \n\n************************************");
        else
            System.out.println("\n*********************************\n" +
               "\t !! Victiry !! \n\n************************************");
    }

    private void connectBlocks (int len) {
        System.out.println(arrList.size());
        for (int i = 0; i < arrList.size(); i++) {
            if(i == 0) { //upper left vertex
                Block tempBlock = arrList.get(0);
                tempBlock.right = arrList.get(1);
                tempBlock.low = arrList.get(len);
                tempBlock.lowRDiag = arrList.get(len + 1);
                arrList.set(0, tempBlock);
            }

            else if(i == len - 1) { //upper right vertex
                Block tempBlock = arrList.get(i);
                tempBlock.left = arrList.get(i - 1);
                tempBlock.low = arrList.get(i + len + 1);
                tempBlock.lowLDiag = arrList.get(i + len - 1);
                arrList.set(i, tempBlock);
            }

            else if(i == len * len - len) { //lower left vertex
                Block tempBlock = arrList.get(i);
                tempBlock.up = arrList.get(i - len);
                tempBlock.right = arrList.get(i + 1);
                tempBlock.upRDiag = arrList.get(i - len + 1);
                arrList.set(i, tempBlock);
            }

            else if(i == arrList.size() - 1) { //lower right vertex
                Block tempBlock = arrList.get(i);
                tempBlock.left = arrList.get(i - 1);
                tempBlock.up = arrList.get(i - len);
                tempBlock.upLDiag = arrList.get(i - len - 1);
                arrList.set(i, tempBlock);
            }

            else if(i != 0 && i < len - 1) { //first row
                Block tempBlock = arrList.get(i);
                tempBlock.left = arrList.get(i - 1);
                tempBlock.right = arrList.get(i + 1);
                tempBlock.low = arrList.get(i + len);
                tempBlock.lowLDiag = arrList.get(i + len - 1);
                tempBlock.lowRDiag = arrList.get(i + len + 1);
                arrList.set(i, tempBlock);
            }

            else if(i % len == 0) { //first column
                Block tempBlock = arrList.get(i);
                tempBlock.up = arrList.get(i - len);
                tempBlock.right = arrList.get(i + 1);
                tempBlock.low = arrList.get(i + len);
                tempBlock.upRDiag = arrList.get(i - len + 1);
                tempBlock.lowRDiag = arrList.get(i + len + 1);
                arrList.set(i, tempBlock);
            }

            else if(i > arrList.size() - len) { //last row
                Block tempBlock = arrList.get(i);
                tempBlock.up = arrList.get(i - len);
                tempBlock.right = arrList.get(i + 1);
                tempBlock.left = arrList.get(i - 1);
                tempBlock.upRDiag = arrList.get(i - len + 1);
                tempBlock.upLDiag = arrList.get(i - len - 1);
                arrList.set(i, tempBlock);
            }

            else if((i + 1) % len == 0) { //last colunm
                Block tempBlock = arrList.get(i);
                tempBlock.up = arrList.get(i - len);
                tempBlock.low = arrList.get(i + len);
                tempBlock.left = arrList.get(i - 1);
                tempBlock.upLDiag = arrList.get(i - len - 1);
                tempBlock.lowLDiag = arrList.get(i + len - 1);
                arrList.set(i, tempBlock);
            }

            else { //middle
                Block tempBlock = arrList.get(i);
                tempBlock.up = arrList.get(i - len);
                tempBlock.right = arrList.get(i + 1);
                tempBlock.low = arrList.get(i + len);
                tempBlock.left = arrList.get(i - 1);
                tempBlock.upLDiag = arrList.get(i - len - 1);
                tempBlock.upRDiag = arrList.get(i - len + 1);
                tempBlock.lowLDiag = arrList.get(i + len - 1);
                tempBlock.lowRDiag = arrList.get(i + len + 1);
                arrList.set(i, tempBlock);
            }
        }
    }
}