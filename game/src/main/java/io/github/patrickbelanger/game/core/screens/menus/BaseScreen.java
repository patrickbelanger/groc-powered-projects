package io.github.patrickbelanger.game.core.screens.menus;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.patrickbelanger.game.core.utils.MouseUtils;
import io.github.patrickbelanger.groq.client.GroqClient;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseScreen extends ScreenAdapter {
    protected static final float INPUT_DELAY = 0.15f;
    protected Game game;
    protected GroqClient groqClient;
    protected MouseUtils mouseUtils; // Will be supported later

    protected float timeSinceLastInput = 0f;

    public BaseScreen(Game game) {
        this.game = game;
        this.mouseUtils = new MouseUtils();
        this.groqClient = new GroqClient();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    public void drawDialogue(String dialogue, BitmapFont font, SpriteBatch batch, float startX, float startY) {
        int maxCharsPerLine = 80;
        float lineHeight = 20f;

        StringBuilder lineBuilder = new StringBuilder();
        List<String> lines = new ArrayList<>();
        int lineLength = 0;

        String[] words = dialogue.split(" ");

        for (String word : words) {
            if (lineLength + word.length() > maxCharsPerLine) {
                lines.add(lineBuilder.toString().trim());
                lineBuilder.setLength(0);
                lineLength = 0;
            }

            lineBuilder.append(word).append(" ");
            lineLength += word.length() + 1;
        }

        if (!lineBuilder.isEmpty()) {
            lines.add(lineBuilder.toString().trim());
        }

        float currentY = startY;
        for (String line : lines) {
            font.draw(batch, line, startX, currentY);
            currentY -= lineHeight;
        }
    }

    public void drawTextPanel(float delta, SpriteBatch batch, TextureRegion panel, BitmapFont font, String dialogue,
                              Runnable onEnterPressedLogic) {
        drawTextPanel(delta, batch, panel, font, dialogue, onEnterPressedLogic, true);
    }

    public void drawTextPanel(float delta, SpriteBatch batch, TextureRegion panel, BitmapFont font, String dialogue,
                              Runnable onEnterPressedLogic, boolean showPressEnter) {
        timeSinceLastInput += delta;

        batch.draw(panel, 50, 50);
        font.setColor(Color.BLACK);
        drawDialogue(dialogue, font, batch, 66, 158);

        if (showPressEnter) {
            font.setColor(Color.YELLOW);
            drawDialogue("Press Enter to continue...", font, batch, 543, 38);
        }

        if (timeSinceLastInput >= INPUT_DELAY) {
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                timeSinceLastInput = 0;
                onEnterPressedLogic.run();
            }
        }
    }

}
