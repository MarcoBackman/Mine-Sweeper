package config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ConfigReader {

    private static final String CONFIG_FILE_PATH = "game-config.json";

    public static MineConfig readGameData() {
        MineConfig mineConfig;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File(CONFIG_FILE_PATH);
            mineConfig = objectMapper.readValue(file, MineConfig.class);
        } catch (IOException exception) {
            throw new RuntimeException("Failed reading config file: " + exception);
        }
        return mineConfig;
    }
}
