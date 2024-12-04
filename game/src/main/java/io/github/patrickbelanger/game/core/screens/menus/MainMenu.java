package io.github.patrickbelanger.game.core.screens.menus;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.GL20;
import io.github.patrickbelanger.game.core.GameAssetManager;
import io.github.patrickbelanger.game.core.types.MainMenuOptions;

public class MainMenu extends BaseScreen {

    private GameAssetManager assetManager;
    private BitmapFont font;
    private TextureRegion mainMenuBackground;
    private TextureRegion mainMenuTitle;
    private TextureRegion mainMenuPanel;
    private SpriteBatch batch;
    private MainMenuOptions mainMenuOptions = MainMenuOptions.NEW_GAME;

    private float timeSinceLastInput = 0f;
    private boolean showMainMenuOptions = false;

    public MainMenu(Game game) {
        super(game);
    }

    @Override
    public void show() {
        assetManager = new GameAssetManager();
        batch = new SpriteBatch();

        assetManager.loadFont("minecraft", "Minecraft.ttf", 16);
        assetManager.loadTexture("main-menu.png");
        assetManager.loadTexture("main-menu-title.png");
        assetManager.loadTexture("main-menu-panel.png");
        assetManager.finishLoading();

        mainMenuBackground = new TextureRegion(assetManager.getTexture("main-menu.png"), 0, 0, 800, 600);
        mainMenuTitle = new TextureRegion(assetManager.getTexture("main-menu-title.png"), 0, 0, 800, 600);
        mainMenuPanel = new TextureRegion(assetManager.getTexture("main-menu-panel.png"), 0, 0, 346, 124);

        font = assetManager.getFont("minecraft");

        batch.getProjectionMatrix().setToOrtho2D(0, 0, 800, 600);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(mainMenuBackground, 0, 0);
        batch.draw(mainMenuTitle, 0, 0);
        font.setColor(Color.WHITE);

        font.draw(batch, "Copyright (c) Patrick Belanger 2024. All rights reserved.", 180, 50);
        font.setColor(Color.CHARTREUSE);
        font.draw(batch, "V 1.0.0-SNAPSHOT", 650, 20);

        if (!showMainMenuOptions) {
            font.draw(batch, "Press any key", 350, 190);
        } else {
            batch.draw(mainMenuPanel, 227, 160);
            drawMenuOption("Start a new game", mainMenuOptions.equals(MainMenuOptions.NEW_GAME), 330, 260);
            drawMenuOption("Load a game", mainMenuOptions.equals(MainMenuOptions.LOAD_GAME), 350, 230);
            drawMenuOption("Exit game", mainMenuOptions.equals(MainMenuOptions.QUIT_GAME), 360, 200);
            font.setColor(Color.WHITE);
            font.draw(batch, "Use W/S or UP/DOWN to select, Enter to confirm", 210, 140);
        }

        updateScreen(v);
        batch.end();
    }

    public void updateScreen(float delta) {
        timeSinceLastInput += delta;

        if (timeSinceLastInput >= INPUT_DELAY) {
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER) && showMainMenuOptions) {
                performMainMenuAction();
                timeSinceLastInput = 0;
            }
            if (showMainMenuOptions) {
                updateMainMenuSelection();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || Gdx.input.justTouched()) {
                showMainMenuOptions = true;
                timeSinceLastInput = 0;
            }
        }
    }

    private void performMainMenuAction() {
        switch (mainMenuOptions) {
            case NEW_GAME:
                game.setScreen(new CharacterSelectionMenu(game));
                break;
            case LOAD_GAME:
                break;
            case QUIT_GAME:
                Gdx.app.exit();
                break;
        }
    }

    private void updateMainMenuSelection() {
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            mainMenuOptions = getNextOption(mainMenuOptions);
            timeSinceLastInput = 0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            mainMenuOptions = getPreviousOption(mainMenuOptions);
            timeSinceLastInput = 0;
        }
    }

    private MainMenuOptions getNextOption(MainMenuOptions current) {
        return switch (current) {
            case NEW_GAME -> MainMenuOptions.LOAD_GAME;
            case LOAD_GAME -> MainMenuOptions.QUIT_GAME;
            default -> MainMenuOptions.NEW_GAME;
        };
    }

    private MainMenuOptions getPreviousOption(MainMenuOptions current) {
        return switch (current) {
            case NEW_GAME -> MainMenuOptions.QUIT_GAME;
            case LOAD_GAME -> MainMenuOptions.NEW_GAME;
            default -> MainMenuOptions.LOAD_GAME;
        };
    }

    private void drawMenuOption(String text, boolean isSelected, float x, float y) {
        font.setColor(isSelected ? Color.YELLOW : Color.BLACK);
        font.draw(batch, text, x, y);
    }

    @Override
    public void hide() {
        Gdx.app.debug("EOTAK", "dispose main menu");
        batch.dispose();
        mainMenuBackground.getTexture().dispose();
        mainMenuPanel.getTexture().dispose();
        mainMenuTitle.getTexture().dispose();
        assetManager.dispose();
    }
}
