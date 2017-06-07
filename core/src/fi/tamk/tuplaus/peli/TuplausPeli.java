package fi.tamk.tuplaus.peli;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;


public class TuplausPeli extends Game {
	private SpriteBatch batch;
    Preferences prefs;
    Locale locale;
    Locale defaultLocale;
    I18NBundle myBundle;
    boolean musicPlays = false;

    private MainMenuScreen mainmenu;

    public SpriteBatch getBatch(){
        return batch;
    }

	@Override
	public void create () {
		batch = new SpriteBatch();
        prefs = Gdx.app.getPreferences("MyPreferences");

        locale = Locale.getDefault();
        defaultLocale = Locale.getDefault();
        myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale);

        mainmenu = new MainMenuScreen(this);

        setScreen(mainmenu);
	}

	@Override
	public void render () {
        super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
