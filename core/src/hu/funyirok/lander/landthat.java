package hu.funyirok.lander;

import com.badlogic.gdx.Game;

import hu.funyirok.lander.screens.Play;
import hu.funyirok.lander.screens.Splash;

public class landthat extends Game {

	public static final String TITLE = "Lander", VERSION = "0.0.0.0.reallyEarly";

	@Override
	public void create() {
		setScreen(new Splash());
		//setScreen(new Play());
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

}
