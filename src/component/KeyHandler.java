package component;

public interface KeyHandler {
    default String getKey(int x, int y) {
        return x + "," + y;
    }
}
