package io.github.patrickbelanger.game.core.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MouseUtils {
    private final Vector2 initialClick = new Vector2();
    private final Rectangle drawnRectangle = new Rectangle();
    private boolean isDragging = false;

    public void getMouseCoords(Batch batch, BitmapFont font) {
        handleMouseInput(batch, font);
        if (isDragging) {
            drawRectangle(drawnRectangle);
        }
    }

    private void handleMouseInput(Batch batch, BitmapFont font) {
        if (Gdx.input.justTouched()) {
            initialClick.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            drawnRectangle.set(initialClick.x, initialClick.y, 0, 0);
            isDragging = true;
        }

        if (isDragging && Gdx.input.isTouched()) {
            float currentX = Gdx.input.getX();
            float currentY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Invert Y for game coordinates

            drawnRectangle.set(
                    Math.min(initialClick.x, currentX),
                    Math.min(initialClick.y, currentY),
                    Math.abs(currentX - initialClick.x),
                    Math.abs(currentY - initialClick.y)
            );
        }

        font.setColor(Color.BLACK);
        font.draw(batch, String.format("Mouse coord (x: %s, y: %s)", Gdx.input.getX(), Gdx.input.getY()), 5, 600);

        if (isDragging && !Gdx.input.isTouched()) {
            isDragging = false;
            System.out.printf("Rectangle drawn at (%.2f, %.2f) with width %.2f and height %.2f%n",
                    drawnRectangle.x, drawnRectangle.y, drawnRectangle.width, drawnRectangle.height);
        }
    }

    private void drawRectangle(Rectangle rect) {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        shapeRenderer.end();
    }

}
