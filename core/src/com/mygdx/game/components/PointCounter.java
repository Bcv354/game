package com.mygdx.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class PointCounter {

    int x, y;
    BitmapFont font;

    public PointCounter(int x, int y) {
        this.x = x;
        this.y = y;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/saira.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter params =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 48;
        params.color = Color.WHITE;
        params.borderWidth = 2f;
        params.borderColor = Color.BLACK;

        font = generator.generateFont(params);
        generator.dispose();
    }

    public void draw(Batch batch, int countOfPoints) {
        font.draw(batch, "Score: " + countOfPoints, x, y);
    }

    public void draw(Batch batch, String label, int value) {
        font.draw(batch, label + value, x, y);
    }

    public void dispose() {
        font.dispose();
    }
}