package io.github.patrickbelanger.game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.patrickbelanger.game.core.GameLogic;

public class DesktopRuntime {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Echoes of the Ancient Kingdom - © Patrick Bélanger 2024");
        config.setWindowedMode(800, 600);
        new Lwjgl3Application(new GameLogic(), config);
    }
}
