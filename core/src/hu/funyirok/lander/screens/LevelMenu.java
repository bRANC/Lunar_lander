package hu.funyirok.lander.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

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

	@Override
	public void show() {
		stage = new Stage();

		Gdx.input.setInputProcessor(stage);

		skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), new TextureAtlas("ui/atlas.pack"));

		table = new Table(skin);
		table.setFillParent(true);

		//List list = new List(/*new String[] {"one", "two", "thrasdfasfsadfadsfasdfasdfadfasdfasdfasdfasdfasdfee", "and", "so", "on", "two", "threadfasdfae", "and", "so", "on", "tafdasdfasdfwo", "three", "and", "so", "on", "two", "three", "and", "so", "on", "two", "three", "and", "so", "on"}, skin*/);
		List list =new List(skin);
		list.setItems(new String[] {"one", "two", "thrasdfasfsadfadsfasdfasdfadfasdfasdfasdfasdfasdfee", "and", "so", "on", "two", "threadfasdfae", "and", "so", "on", "tafdasdfasdfwo", "three", "and", "so", "on", "two", "three", "and", "so", "on", "two", "three", "and", "so", "on"});
		ScrollPane scrollPane = new ScrollPane(list, skin);

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

		table.add(new Label("SELECT LEVEL", skin, "big")).colspan(3).expandX().spaceBottom(50).row();
		table.add(scrollPane).uniformX().expandY().top().left();
		table.add(play).uniformX();
		table.add(back).uniformX().bottom().right();

		stage.addActor(table);

		stage.addAction(sequence(moveTo(0, stage.getHeight()), moveTo(0, 0, .5f))); // coming in from top animation
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
