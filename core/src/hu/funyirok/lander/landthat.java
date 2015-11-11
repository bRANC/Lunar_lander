package hu.funyirok.lander;

import com.badlogic.gdx.Game;
import com.sun.org.apache.bcel.internal.generic.NEW;

import hu.funyirok.lander.screens.LevelMenu;
import hu.funyirok.lander.screens.MainMenu;
import hu.funyirok.lander.screens.Play;
import hu.funyirok.lander.screens.Settings;

public class landthat extends Game {

    public static final String TITLE = "Lander", VERSION = "0.0.0.0.reallyEarly";
    public LevelMenu levelmenu;
    public MainMenu mainmenu;
    public Play play;
    public Settings setting;

    @Override
    public void create() {
        levelmenu = new LevelMenu();
        levelmenu.osobj=this;
        mainmenu=new MainMenu();
        mainmenu.osobj=this;
        play=new Play();
        play.osobj=this;
        setting= new Settings();
        setting.osobj=this;
        setScreen(levelmenu);
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
