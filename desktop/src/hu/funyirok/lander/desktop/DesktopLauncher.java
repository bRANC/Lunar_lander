package hu.funyirok.lander.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import hu.funyirok.lander.landthat;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = landthat.TITLE + " v" + landthat.VERSION;
		config.vSyncEnabled = true;
		//config.useGL20 = true;
		config.width = 720;
		config.height = 720;
		config.addIcon("img/icon.png", Files.FileType.Internal);
		new LwjglApplication(new landthat(), config);
	}
}
