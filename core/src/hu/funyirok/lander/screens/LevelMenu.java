package hu.funyirok.lander.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.dermetfan.gdx.scenes.scene2d.Scene2DUtils;

import javafx.scene.Scene;

public class LevelMenu implements Screen {

    private Stage stage;
    private Table table;
    private Skin skin;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        //stage.setViewport(width, height, false);
        stage.getViewport().update(width, height, true);
        table.invalidateHierarchy();
    }

    public boolean giro = false, onscreen = true;

    @Override
    public void show() {


        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), new TextureAtlas("ui/atlas.pack"));

        table = new Table(skin);
        table.setFillParent(true);

        //List list = new List(/*new String[] {"one", "two", "thrasdfasfsadfadsfasdfasdfadfasdfasdfasdfasdfasdfee", "and", "so", "on", "two", "threadfasdfae", "and", "so", "on", "tafdasdfasdfwo", "three", "and", "so", "on", "two", "three", "and", "so", "on", "two", "three", "and", "so", "on"}, skin*/);
        List list = new List(skin);
        list.setItems(new String[]{"moon", "mars","sun","yoMamma"});
        ScrollPane scrollPane = new ScrollPane(list, skin);


        final CheckBox gyroCheckBox = new CheckBox(" vSync", skin);
        giro = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
        gyroCheckBox.setChecked(giro);

        final CheckBox onScreen = new CheckBox(" onScreen" + "\n" + "  button", skin);
        onScreen.setChecked(onscreen);


        TextButton play = new TextButton("PLAY", skin, "big");
        play.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new Play());
            }

        });
        play.pad(15);

        TextButton back = new TextButton("BACK", skin);
        back.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(sequence(moveTo(0, stage.getHeight(), .5f), run(new Runnable() {

                    @Override
                    public void run() {
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
                    }

                })));
            }
        });
        back.pad(10);
        ClickListener buttonHandler = new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getListenerActor() == gyroCheckBox) {
                    if (giro) {
                        giro = false;
                    } else {
                        giro = true;
                    }
                    gyroCheckBox.setChecked(giro);
                }
                if (event.getListenerActor() == onScreen) {
                    if (onscreen) {
                        onscreen = false;
                    } else {
                        onscreen = true;
                    }
                    onScreen.setChecked(onscreen);
                }
            }
        };


        gyroCheckBox.addListener(buttonHandler);
        onScreen.addListener(buttonHandler);

        table.add(new Label("SELECT LEVEL", skin, "big")).colspan(3).expandX().space(50).row();
        table.add(scrollPane).expandY().top().colspan(3).row();

        if (giro) {
            table.add(play).uniformX().center().colspan(3).row();
            table.add(onScreen).spaceBottom(280);
            table.add(gyroCheckBox).spaceBottom(280).row();
        } else {
            table.add(play).uniformX().center().colspan(3).row();
            table.add(onScreen).center().colspan(3).spaceBottom(280);
        }
        table.add(back).uniformX().bottom().colspan(3).right();

        stage.addActor(table);

        stage.addAction(sequence(moveTo(0, stage.getHeight()), moveTo(0, 0, .5f))); // coming in from top animation
        table.setDebug(true);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

}
