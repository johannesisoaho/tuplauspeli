package fi.tamk.tuplaus.peli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by Bilbo on 18.2.2017.
 */
public class GameScreen implements Screen {
    private TuplausPeli game;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private int width = 480;
    private int height = 800;

    private Stage gameStage;

    static String name = "Anonymous";
    String dialog;
    String initial = "";
    String hint;

    BitmapFont font;
    BitmapFont font2;
    GlyphLayout layout;

    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    Input.TextInputListener listener;

    float money;
    private boolean guessedTails;

    final CoinActor coin;

    ParallelAction parallel;
    SequenceAction sequence;
    ParallelAction parallel2;

    final TextButton menuButton;
    final TextButton headsButton;
    final TextButton tailsButton;

    String collect;
    String heads;
    String tails;
    String winnings;

    public GameScreen(TuplausPeli g) {
        Gdx.app.log("GameScreen", "created");
        this.game = g;
        batch = game.getBatch();

        money = 0.40f;
        String collect = game.myBundle.get("collect");
        String heads = game.myBundle.get("heads");;
        String tails = game.myBundle.get("tails");;
        String winnings = game.myBundle.get("winnings");
        String dialog = game.myBundle.get("dialog");
        String hint = game.myBundle.get("hint");

        gameStage = new Stage(new FitViewport(width, height), batch);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        menuButton = new TextButton(winnings, skin);
        headsButton = new TextButton(heads, skin);
        tailsButton = new TextButton(tails, skin);

        coin = new CoinActor();

        gameStage.addActor(coin);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 45;
        parameter.borderColor = Color.WHITE;
        parameter.borderWidth = 0;

        font = generator.generateFont(parameter);

        parameter.size = 60;
        parameter.borderColor = Color.GOLD;
        parameter.borderWidth = 2;
        parameter.color = Color.WHITE;

        font2 = generator.generateFont(parameter);

        TextButton.TextButtonStyle buttonStyle = menuButton.getStyle();
        buttonStyle.font = font;
        menuButton.setStyle(buttonStyle);
        menuButton.setColor(Color.GOLD);
        menuButton.setWidth(350f);
        menuButton.setHeight(100f);
        menuButton.setPosition(480/2 - menuButton.getWidth()/2, 20);

        gameStage.addActor(menuButton);
        Gdx.input.setInputProcessor(gameStage);

        menuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){

                game.prefs.putString(name, name);
                game.prefs.putFloat(name, money);
                game.prefs.flush();

                game.setScreen(new MainMenuScreen(game));
                Gdx.input.setInputProcessor(gameStage);
            }

        });

        headsButton.setStyle(buttonStyle);
        headsButton.setColor(Color.GOLD);
        headsButton.setWidth(200f);
        headsButton.setHeight(75f);
        headsButton.setPosition(120 - headsButton.getWidth()/2, 150);

        tailsButton.setStyle(buttonStyle);
        tailsButton.setColor(Color.GOLD);
        tailsButton.setWidth(200f);
        tailsButton.setHeight(75f);
        tailsButton.setPosition(360 - tailsButton.getWidth()/2, 150);

        Group group = new Group();

        group.addActor(headsButton);
        group.addActor(tailsButton);

        gameStage.addActor(group);

        Gdx.input.setInputProcessor(gameStage);

        tailsButton.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor){
                guessedTails = true;
                createActions();
                coin.addAction(sequence);
                Gdx.input.setInputProcessor(gameStage);
                Gdx.app.log("Klaava", "clicked");
            }
        });

        headsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                guessedTails = false;
                createActions();
                coin.addAction(sequence);
                Gdx.input.setInputProcessor(gameStage);
                Gdx.app.log("Kruuna", "clicked");
            }
        });

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);

        listener = new Input.TextInputListener() {
            @Override
            public void input(String text) {
                name = text;
            }

            @Override
            public void canceled() {

            }
        };
        Gdx.input.getTextInput(listener, dialog, initial, hint);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(gameStage);
    }

    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(gameStage);
        batch.setProjectionMatrix(camera.combined);

        String collect = game.myBundle.get("collect");
        String heads = game.myBundle.get("heads");;
        String tails = game.myBundle.get("tails");;
        String winnings = game.myBundle.get("winnings");

        menuButton.setText(collect);
        headsButton.setText(heads);
        tailsButton.setText(tails);

        //Gdx.app.log("GameScreen", "render");

        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        coin.setOrigin(coin.getWidth()/2, coin.getHeight()/2);
        coin.act(Gdx.graphics.getDeltaTime());
        gameStage.draw();

        batch.begin();

        layout = new GlyphLayout();
        layout.setText(font2, winnings);
        float width = layout.width;
        float height = layout.height;
        font2.draw(batch, winnings+": ",
                (480 - width)/2,
                750);

        layout.setText(font2, Float.toString(money));
        width = layout.width;
        height = layout.height;
        font2.draw(batch,Float.toString(money) + "€",
                (480 - width)/2,
                700);

        coin.draw(batch);

        batch.end();
        }

    public void createActions(){
        coin.setOrigin(coin.getWidth()/2, coin.getHeight()/2);

        parallel = new ParallelAction();
        sequence = new SequenceAction();

        final Action action = flipIn(coin.getX(), coin.getWidth(), 1);

        final MoveToAction moveAction = new MoveToAction();
        final RotateToAction rotateAction = new RotateToAction();
        RunnableAction runnableAction = new RunnableAction();

        moveAction.setPosition(coin.getX(), 500f);
        moveAction.setDuration(1f);

        rotateAction.setRotation(-180);
        rotateAction.setDuration(1f);

        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                Gdx.app.log("parallel", "done");
            }
        });

        parallel.addAction(moveAction);
        parallel.addAction(rotateAction);
        parallel.addAction(action);

        parallel2 = new ParallelAction();

        final MoveToAction moveAction2 = new MoveToAction();
        final RotateToAction rotateAction2 = new RotateToAction();

        moveAction2.setPosition(coin.getX(), 300f);
        moveAction2.setDuration(1f);
        moveAction2.setInterpolation(Interpolation.bounceOut);

        rotateAction2.setRotation(-360f);
        rotateAction2.setDuration(1f);

        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                Gdx.app.log("parallel2", "done");
                boolean isTails = MathUtils.randomBoolean();
                if(isTails && guessedTails){
                    money = money*2;
                }else if(isTails && !guessedTails){
                    money = 0;
                }else if(!isTails && !guessedTails){
                    money = money*2;
                }else if(!isTails && guessedTails){
                    money = 0;
                }
                Gdx.input.setInputProcessor(gameStage);

                if(isTails){
                    coin.setTexture(false);
                }else{
                    coin.setTexture(true);
                }
                rotateAction.reset();
                moveAction.reset();
                rotateAction2.reset();
                moveAction2.reset();
                action.reset();
                parallel.reset();
                parallel2.reset();
                sequence.reset();
            }
        });

        coin.setOrigin(coin.getWidth()/2, coin.getHeight()/2);
        Action action2 = flipOut(coin.getX(), coin.getWidth(), 0.5f);
        Action action3 = flipIn(coin.getX(), coin.getWidth(), 0.5f);

        SequenceAction sequenceAction2 = new SequenceAction();
        sequenceAction2.addAction(action2);
        coin.setOrigin(coin.getWidth()/2, coin.getHeight()/2);
        sequenceAction2.addAction(action3);

        parallel2.addAction(moveAction2);
        parallel2.addAction(rotateAction2);
        parallel2.addAction(sequenceAction2);

        sequence.addAction(parallel);
        sequence.addAction(parallel2);
        sequence.addAction(runnableAction);
    }

    public static Action flipIn(final float x, final float width, final float duration) {
        return new Action() {
            float done = 0;

            @Override
            public boolean act(float delta) {
                done += delta;
                if (done >= duration) {
                    actor.setX(x);
                    actor.setWidth(width);
                    return true;
                }
                float tmpWidth = width * (done / duration);
                actor.setX(x + ((width / 2) - (tmpWidth / 2)));
                actor.setWidth(tmpWidth);
                return false;
            }
        };
    }

    public static Action flipOut(final float x, final float width, final float duration) {
        return new Action() {
            float left = duration;

            @Override
            public boolean act(float delta) {
                left -= delta;
                if (left <= 0) {
                    actor.setX(x + (width / 2));
                    actor.setWidth(0);
                    return true;
                }
                float tmpWidth = width * (left / duration);
                actor.setX(x + ((width / 2) - (tmpWidth / 2)));
                actor.setWidth(tmpWidth);
                return false;
            }
        };
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
        batch.dispose();
    }
}
