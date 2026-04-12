package com.mygdx.game.screens;

import static com.mygdx.game.MyGdxGame.SCR_HEIGHT;
import static com.mygdx.game.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.charasters.Bird;
import com.mygdx.game.charasters.Tube;
import com.mygdx.game.components.MovingBackground;
import com.mygdx.game.components.PointCounter;

import java.util.Random;

public class ScreenGame implements Screen {

    final int pointCounterMarginTop = 60;
    final int pointCounterMarginRight = 400;

    MyGdxGame myGdxGame;

    Bird bird;
    PointCounter pointCounter;
    PointCounter speedCounter;
    MovingBackground background;

    int tubeCount = 3;
    Tube[] tubes;

    int gamePoints;

    int bestScore = 0; // добавить рядом с gamePoints
    boolean isGameOver;


    float speedChangeTimer = 0f;
    final float speedChangeInterval = 3f; // каждые 3 секунды меняем скорость
    Random random = new Random();

    public ScreenGame(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        initTubes();
        background = new MovingBackground("background/game_bg.png");
        bird = new Bird(20, SCR_HEIGHT / 2, 10, 200, 105);
        pointCounter = new PointCounter(SCR_WIDTH - pointCounterMarginRight, SCR_HEIGHT - pointCounterMarginTop);
        speedCounter = new PointCounter(SCR_WIDTH - pointCounterMarginRight, SCR_HEIGHT - pointCounterMarginTop - 80); // добавить, чуть ниже
    }

    @Override
    public void show() {
        gamePoints = 0;
        isGameOver = false;
        speedChangeTimer = 0f;
        bird.setY(SCR_HEIGHT / 2);
        initTubes();
        for (Tube t : tubes) t.setSpeed(10); // сброс скорости
    }

    @Override
    public void render(float delta) {
// добавить рядом с isGameOver

        if (isGameOver) {

            if (gamePoints > bestScore) bestScore = gamePoints;
            myGdxGame.screenRestart.gamePoints = gamePoints;
            myGdxGame.screenRestart.bestScore = bestScore;
            myGdxGame.setScreen(myGdxGame.screenRestart);

        }

        if (Gdx.input.justTouched()) {
            bird.onClick();
        }

        // таймер смены скорости
        speedChangeTimer += delta;
        if (speedChangeTimer >= speedChangeInterval) {
            speedChangeTimer = 0f;
            int newSpeed = 8 + random.nextInt(8); // рандом от 8 до 15
            for (Tube t : tubes) t.setSpeed(newSpeed);
        }

        background.move(delta);
        bird.fly(delta);
        if (!bird.isInField()) {
            isGameOver = true;
        }
        for (Tube tube : tubes) {
            tube.move(delta);
            if (tube.isHit(bird)) {
                isGameOver = true;
            } else if (tube.needAddPoint(bird)) {
                gamePoints += 1;
                tube.setPointReceived();
            }
        }

        ScreenUtils.clear(1, 0, 0, 1);
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        myGdxGame.batch.begin();

        background.draw(myGdxGame.batch);
        bird.draw(myGdxGame.batch);
        for (Tube tube : tubes) tube.draw(myGdxGame.batch);
        pointCounter.draw(myGdxGame.batch, "Score: ", gamePoints);
        speedCounter.draw(myGdxGame.batch, "Speed: ", tubes[0].speed);

        myGdxGame.batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        bird.dispose();
        background.dispose();
        pointCounter.dispose();
        for (int i = 0; i < tubeCount; i++) {
            tubes[i].dispose();
        }
    }

    void initTubes() {
        tubes = new Tube[tubeCount];
        for (int i = 0; i < tubeCount; i++) {
            tubes[i] = new Tube(tubeCount, i);
        }
    }
}