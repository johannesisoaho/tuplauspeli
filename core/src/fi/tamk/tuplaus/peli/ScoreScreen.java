package fi.tamk.tuplaus.peli;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Bilbo on 20.2.2017.
 */

public class ScoreScreen implements Screen {

    private TuplausPeli game;
    private SpriteBatch batch;

    private Stage scoreStage;
    private float width = 480f;
    private float height = 800f;

    private OrthographicCamera camera;

    private Texture backGroundTexture;

    TextButton backButton;

    BitmapFont font;
    BitmapFont font2;
    GlyphLayout layout;

    String best;
    String back;

    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    public ScoreScreen(TuplausPeli g){
        this.game = g;
        batch = game.getBatch();

        best = game.myBundle.get("best");
        back = game.myBundle.get("back");

        scoreStage = new Stage(new FitViewport(width, height), batch);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        backButton = new TextButton(back, skin);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("Pacifico.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 60;
        parameter.borderColor = Color.ORANGE;
        parameter.borderWidth = 0;

        font = generator.generateFont(parameter);

        parameter.size = 40;
        font2 = generator.generateFont(parameter);

        TextButton.TextButtonStyle buttonStyle = backButton.getStyle();
        buttonStyle.font = font;
        backButton.setColor(Color.GOLD);
        backButton.setWidth(350f);
        backButton.setHeight(100f);
        backButton.setPosition(480/2 - backButton.getWidth()/2, 20);
        backButton.setStyle(buttonStyle);

        scoreStage.addActor(backButton);
        Gdx.input.setInputProcessor(scoreStage);

        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new MainMenuScreen(game));
                Gdx.input.setInputProcessor(scoreStage);
            }
        });

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        float pos = 600;

        Gdx.input.setInputProcessor(scoreStage);
        batch.setProjectionMatrix(camera.combined);
        Gdx.app.log("ScoreScreen", "render");
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        scoreStage.draw();
        batch.begin();
        ArrayList<Object> scores = new ArrayList<Object>();


        for (Map.Entry<String, ?> entry : game.prefs.get().entrySet()){
            Object i = entry.getValue();
            scores.add(i);
            Collections.sort(scores, Collections.reverseOrder());
        }

        for(int j = 0; j < scores.size(); j++){
            if(j < 5){
            for (Map.Entry<String, ?> entry : game.prefs.get().entrySet()){

                if(entry.getValue() == scores.get(j)){
                font2.draw(batch,
                        (j+1) + ".  " +entry.getKey()+ ":  " + (scores.get(j)) + "€",
                        60,
                        pos);
                pos -= 100;
        }}}}

        layout = new GlyphLayout();
        layout.setText(font, best);
        float width = layout.width;
        font.draw(batch, best,
                (480 - width)/2,
                700);

        batch.end();
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
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
