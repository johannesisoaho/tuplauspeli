package fi.tamk.tuplaus.peli.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fi.tamk.tuplaus.peli.TuplausPeli;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Tuplauspeli";
        config.width = 480;
        config.height = 800;
		new LwjglApplication(new TuplausPeli(), config);
	}
}
