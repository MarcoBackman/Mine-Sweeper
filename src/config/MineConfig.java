package config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MineConfig {

    private final int MAX_X_SIZE = 20;
    private final int MAX_Y_SIZE = 20;

    @JsonProperty("MATRIX_X_SIZE")
    private int matrixXSize;

    @JsonProperty("MATRIX_Y_SIZE")
    private int matrixYSize;

    @JsonProperty("NUMBER_OF_MINE")
    private int numOfMines;

    public int getMatrixXSize() {
        if (matrixXSize > MAX_X_SIZE) {
            return MAX_X_SIZE;
        }
        return matrixXSize;
    }

    public int getMatrixYSize() {
        if (matrixXSize > MAX_Y_SIZE) {
            return MAX_Y_SIZE;
        }
        return matrixYSize;
    }

    public int getNumOfMines() {
        int availableSpace = getMatrixXSize() * getMatrixYSize();
        if (availableSpace <= numOfMines) {
            return availableSpace - 1;
        }
        return numOfMines;
    }
}
