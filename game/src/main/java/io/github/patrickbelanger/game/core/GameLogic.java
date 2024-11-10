package io.github.patrickbelanger.game.core;

import com.badlogic.gdx.Game;
import io.github.patrickbelanger.game.core.screens.menus.MainMenu;

public class GameLogic extends Game {
    @Override
    public void create() {
        setScreen(new MainMenu(this));
    }
}
