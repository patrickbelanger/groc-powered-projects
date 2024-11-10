package io.github.patrickbelanger.game.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import java.util.HashMap;
import java.util.Map;

public class GameAssetManager extends AssetManager implements Disposable {

    private final Map<String, BitmapFont> fontCache = new HashMap<>();

    public void loadTexture(String filename) {
        String basePathTexture = "game/assets/data/%s";
        this.load(basePathTexture.formatted(filename), Texture.class);
    }

    public Texture getTexture(String filename) {
        String basePathTexture = "game/assets/data/%s";
        return this.get(basePathTexture.formatted(filename), Texture.class);
    }

    public void loadFont(String fontName, String filename, int size) {
        if (!fontCache.containsKey(fontName)) {
            String basePathFont = "game/assets/fonts/%s";
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(basePathFont.formatted(filename)));
            FreeTypeFontParameter parameter = new FreeTypeFontParameter();
            parameter.size = size;

            BitmapFont font = generator.generateFont(parameter);
            fontCache.put(fontName, font);

            generator.dispose();
        }
    }

    public BitmapFont getFont(String fontName) {
        return fontCache.get(fontName);
    }

    @Override
    public void dispose() {
        super.dispose();

        for (BitmapFont font : fontCache.values()) {
            font.dispose();
        }
        fontCache.clear();
    }
}
