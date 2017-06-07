package fi.tamk.tuplaus.peli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Locale;

/**
 * Created by Bilbo on 18.2.2017.
 */
public class MainMenuScreen implements Screen {

    private TuplausPeli game;
    private SpriteBatch batch;

    private Stage startStage;
    private float width = 480f;
    private float height = 800f;



    private OrthographicCamera camera;

    private Texture backGroundTexture;

    final TextButton scoreButton;
    final TextButton playButton;
    final TextButton langButton;

    BitmapFont font;
    BitmapFont font2;
    GlyphLayout layout;

    String play;
    String scores;
    String lang;
    String title;

    Music music;

    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    public MainMenuScreen(TuplausPeli g){
        this.game = g;
        batch = game.getBatch();

        startStage = new Stage(new FitViewport(width, height), batch);

        if(!game.musicPlays) {
            music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
            music.setLooping(true);
            music.play();
            game.musicPlays = true;
        }

        play = game.myBundle.get("play");
        scores = game.myBundle.get("scores");
        lang = game.myBundle.get("lang");
        title = game.myBundle.get("title");

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        playButton = new TextButton(play, skin);
        scoreButton = new TextButton(scores, skin);
        langButton = new TextButton(lang, skin);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("Pacifico.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;
        parameter.borderColor = Color.ORANGE;
        parameter.borderWidth = 0;

        font = generator.generateFont(parameter);

        parameter.size = 20;
        font2 = generator.generateFont(parameter);

        TextButton.TextButtonStyle buttonStyle = playButton.getStyle();
        buttonStyle.font = font;
        playButton.setColor(Color.GOLD);
        playButton.setWidth(350f);
        playButton.setHeight(100f);
        playButton.setPosition(480/2 - playButton.getWidth()/2, 400);
        playButton.setStyle(buttonStyle);

        startStage.addActor(playButton);
        Gdx.input.setInputProcessor(startStage);

        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new GameScreen(game));
                Gdx.input.setInputProcessor(startStage);
            }
        });

        buttonStyle = playButton.getStyle();
        buttonStyle.font = font;
        scoreButton.setColor(Color.GOLD);
        scoreButton.setWidth(350f);
        scoreButton.setHeight(100f);
        scoreButton.setPosition(480/2 - scoreButton.getWidth()/2, 200);
        scoreButton.setStyle(buttonStyle);

        startStage.addActor(scoreButton);
        Gdx.input.setInputProcessor(startStage);

        scoreButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new ScoreScreen(game));
                Gdx.input.setInputProcessor(startStage);
            }
        });

        buttonStyle = langButton.getStyle();
        buttonStyle.font = font2;
        langButton.setColor(Color.GOLD);
        langButton.setWidth(80f);
        langButton.setHeight(80f);
        langButton.setPosition(480-85, 715);
        langButton.setStyle(buttonStyle);

        startStage.addActor(langButton);
        Gdx.input.setInputProcessor(startStage);

        langButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(game.locale != Locale.getDefault()) {
                    game.locale = Locale.getDefault();
                    game.myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), game.locale);
                    Gdx.input.setInputProcessor(startStage);
                }else{
                    game.locale = new Locale("en", "UK");
                    game.myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), game.locale);
                    Gdx.input.setInputProcessor(startStage);
                }
            }
        });

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);

        font = new BitmapFont(Gdx.files.internal("font.txt"));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta){
        Gdx.input.setInputProcessor(startStage);
        batch.setProjectionMatrix(camera.combined);

        play = game.myBundle.get("play");
        scores = game.myBundle.get("scores");
        lang = game.myBundle.get("lang");
        title = game.myBundle.get("title");

        playButton.setText(play);
        scoreButton.setText(scores);
        langButton.setText(lang);

        Gdx.app.log("MenuScreen", "render");
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        startStage.draw();
        batch.begin();



        parameter.size = 80;
        layout = new GlyphLayout();
        layout.setText(font, title);
        float width = layout.width;
        float height = layout.height;
        font.draw(batch, title,
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

    public void dispose(){
        batch.dispose();
    }

}
