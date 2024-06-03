package component;

import config.BlockConstant;
import ui.GamePanel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MineGame implements KeyHandler {
    //Key: x-y coordinate, Value: Block
    private final LinkedHashMap<String, Block> mapBlock = new LinkedHashMap<>(); //use this for better performance

    //Todo: consider refactoring into concurrentHashMap
    private final ConcurrentHashMap<String, Block> fastMapBlock = new ConcurrentHashMap<>();
    GamePanel parentPanel;

    public MineGame(GamePanel parentPanel,
                    int numOfMine,
                    int lengthOfX,
                    int lengthOfY) {
        this.parentPanel = parentPanel;

        generateBlocksWithButtons(lengthOfX, lengthOfY);
        generateMine(numOfMine, lengthOfX, lengthOfY);

        //connect mine blocks
        connectBlocks(lengthOfX, lengthOfY);
    }

    public LinkedHashMap<String, Block> getMapBlock() {
        return mapBlock;
    }

    private void generateBlocksWithButtons(int lengthOfX, int lengthOfY) {
        for (int y = 0; y < lengthOfY; y++) {
            for (int x = 0; x < lengthOfX; x++) {
                Block tempBlock = new Block();
                tempBlock.getBlockButton().setFocusable(false);
                tempBlock.getBlockButton().addMouseListener(parentPanel);
                addBlock(x, y, tempBlock);
            }
        }
    }

    private void generateMine(int maxMineNum, int lengthOfX, int lengthOfY) {
        int count = 0;
        while (count < maxMineNum) {
            int x = (int) (Math.random() * lengthOfX);
            int y = (int) (Math.random() * lengthOfY);
            Block tempBlock = mapBlock.get(getKey(x, y));
            if (!tempBlock.getActualScreen().equals(BlockConstant.MINE)) {
                tempBlock.setActualScreen("*");
                tempBlock.setMine();
                count++;
            }
        }
    }

    public void addBlock(int x, int y, Block block) {
        String key = getKey(x, y);
        if (!mapBlock.containsKey(key)) {
            mapBlock.put(key, block);
        } else {
            System.out.println("Something went wrong. Please debug");
        }
    }

    private Block getBlock(int x, int y) {
        String key = getKey(x, y);
        return mapBlock.get(key);
    }

    private void connectSingleBlock(int x, int y, Block block) {
        block.setUp(mapBlock.get(getKey(x, y - 1)));
        block.setDown(mapBlock.get(getKey(x, y + 1)));
        block.setLeft(mapBlock.get(getKey(x - 1, y)));
        block.setRight(mapBlock.get(getKey(x + 1, y)));
        block.setUpLDiag(mapBlock.get(getKey(x - 1, y - 1)));
        block.setUpRDiag(mapBlock.get(getKey(x + 1, y - 1)));
        block.setDownLDiag(mapBlock.get(getKey(x - 1, y + 1)));
        block.setDownRDiag(mapBlock.get(getKey(x + 1, y + 1)));
    }

    private void connectBlocks (int xLength, int yLength) {
        for (int y = 0; y < yLength; y++) {
            for (int x = 0; x < xLength; x++) {
                Block targetBlock = mapBlock.get(getKey(x, y));
                connectSingleBlock(x, y, targetBlock);
            }
        }
    }

    /*
     *  Mine sweep. Checks the vicinity safezone until it reaches the mine-adjacent block
     */
    public void sweepMine (Block targetBlock, boolean rightClicked) {
        if (targetBlock.isMine() && !targetBlock.isFlagged()) {
            parentPanel.setGameOver(true);
        } else if (!targetBlock.isFlagged() && !rightClicked) {
            traverse(targetBlock, null);
            visitReset();
        }
    }

    private void traverse (Block target, Block previous) {
        if (target.isMine() || target.isFlagged()) {
            target.setVisited();
            return;
        }

        target.setUserBlock(BlockConstant.S_ZONE);

        //calls adjacent mine count method
        int mineNum = countMine (target);

        if (mineNum > 0) {
            target.setAdjacentNumOfMine(mineNum);
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
            return;
        } else {
            target.changeUserScreen(BlockConstant.S_ZONE);
        }

        target.setVisited();
        if (target.getUp() != null)
            if (!target.getUp().isVisited())
                traverse(target.getUp(), target);

        if (target.getDown() != null)
            if (!target.getDown().isVisited())
                traverse(target.getDown(), target);

        if (target.getLeft() != null)
            if (!target.getLeft().isVisited())
                traverse(target.getLeft(), target);

        if (target.getRight() != null)
            if (!target.getRight().isVisited())
                traverse(target.getRight(), target);

        if (target.getUpLDiag() != null)
            if (!target.getUpLDiag().isVisited())
                traverse(target.getUpLDiag(), target);

        if (target.getUpRDiag() != null)
            if (!target.getUpRDiag().isVisited())
                traverse(target.getUpRDiag(), target);

        if (target.getDownLDiag() != null)
            if (!target.getDownLDiag().isVisited())
                traverse(target.getDownLDiag(), target);

        if (target.getDownRDiag() != null)
            if (!target.getDownRDiag().isVisited())
                traverse(target.getDownRDiag(), target);
    }

    private void visitReset() {
        mapBlock.forEach((s, block) -> {
            block.resetVisited();
        });
    }

    //checks the adjacent block's number of mine
    private int countMine (Block target) {
        AtomicInteger count = new AtomicInteger();

        List<Block> adjacentBlocks = target.getAdjacentBlocks();

        adjacentBlocks.parallelStream().forEach(adjacentBlock -> {
            if (adjacentBlock.isMine()) {
                count.incrementAndGet();
            }
        });

        adjacentBlocks.clear();
        return count.get();
    }


    /*
     *  checks the game's victory condition on every move.
     */

    public boolean checkVictory(int totalMine) {
        AtomicInteger count = new AtomicInteger();
        mapBlock.forEach((s, block) -> {
            if (block.isFlagged() && block.isMine()) {
                count.incrementAndGet();
            }
        });

        if (count.get() == totalMine) {
            parentPanel.setVictory(true);
            return true;
        }
        return false;
    }

    public void changeToVictoryScreen() {
        mapBlock.forEach((s, block) -> {
            if (block.isFlagged() && block.isMine()) {
                block.changeUserScreen(BlockConstant.VICTORY);
            }
        });
    }

}
