package hu.funyirok.lander.screens;

import hu.funyirok.lander.entities.Car;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class Play implements Screen {

	private Stage stage;
	private Table table;
	private Skin skin;

	private World world;
    private Body ground;
	private Box2DDebugRenderer debugRenderer;
	private SpriteBatch batch;
	private OrthographicCamera camera;

	private final float TIMESTEP = 1 / 60f;
	private final int VELOCITYITERATIONS = 8, POSITIONITERATIONS = 3;
    private Sprite boxSprite;
	private Car car,car1;
	private Label sebbseg_ki;

	private Array<Body> tmpBodies = new Array<Body>();

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);

		camera.position.set(car.getChassis().getPosition().x, car.getChassis().getPosition().y /*- Gdx.graphics.getHeight()/4*/, 0);
		camera.update();

		batch.setProjectionMatrix(camera.combined);

        batch.begin();

		world.getBodies(tmpBodies);
		for(Body body : tmpBodies){
        if(body.getUserData() instanceof Sprite) {

            Sprite sprite = (Sprite) body.getUserData();

				sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
				sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
				sprite.draw(batch);
			}}
		batch.end();


		stage.act(delta);
        sebbseg_ki.setText("KM/H: " +(int)Math.sqrt(Math.pow((car.vissza().x+car.vissza().y),2)));

        stage.draw();
		System.out.println(car.vissza().x+" "+car.vissza().y);
		//debugRenderer.render(world, camera.combined); // hogy lássuk a mesh-t
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width / 25;
		camera.viewportHeight = height / 25;

	}

	@Override
	public void show() {
		stage = new Stage();

		Gdx.input.setInputProcessor(stage);

		skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), new TextureAtlas("ui/atlas.pack"));

		table = new Table(skin);
		table.setFillParent(true);
        table.left().top();
		world = new World(new Vector2(0, -9.81f), true);
		debugRenderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();

		camera = new OrthographicCamera();

		BodyDef bodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef(), wheelFixtureDef = new FixtureDef();

		// car
		fixtureDef.density = 5;
		fixtureDef.friction = .4f;
		fixtureDef.restitution = .3f;

		wheelFixtureDef.density = fixtureDef.density * 1.5f;
		wheelFixtureDef.friction = 50;
		wheelFixtureDef.restitution = .4f;

		car = new Car(world, fixtureDef, wheelFixtureDef, 0, 3, 3, 1.25f);
        car1 = new Car(world, fixtureDef, wheelFixtureDef, 5, 3, 3, 1.25f);


		Gdx.input.setInputProcessor(new InputMultiplexer(new InputAdapter() {

			@Override
			public boolean keyDown(int keycode) {
				switch (keycode) {
					case Keys.ESCAPE:
						((Game) Gdx.app.getApplicationListener()).setScreen(new LevelMenu());
						break;
				}
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				camera.zoom += amount / 25f;
				return true;
			}

		}, car));

		// GROUND
		// body definition
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(0, -5f);

		// ground shape
        PolygonShape groundShape = new PolygonShape();
		groundShape.setAsBox(25f, 5f);


        // fixture definition
		fixtureDef.shape = groundShape;
		fixtureDef.friction = .5f;
		fixtureDef.restitution = 0;


        //föld textura fel "húzása"
        boxSprite = new Sprite(new Texture("img/splash.png"));
        boxSprite.setSize(50f, 9.8f);
        boxSprite.setOrigin(boxSprite.getWidth() / 2, boxSprite.getHeight() / 2);

		ground = world.createBody(bodyDef);
		ground.createFixture(fixtureDef);
		ground.setUserData(boxSprite);

		groundShape.dispose();

		//szöveg ki írás

        sebbseg_ki=new Label("KM/H: "+car.vissza().x, skin, "big");
		table.add(sebbseg_ki).padLeft(25);
		stage.addActor(table);
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
		world.dispose();
		debugRenderer.dispose();
	}

}