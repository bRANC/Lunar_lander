package hu.funyirok.lander.screens;

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
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import hu.funyirok.lander.entities.Debris;
import hu.funyirok.lander.entities.Rocket;
import hu.funyirok.lander.entities.YourCustomUserData;
import hu.funyirok.lander.landthat;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class Play implements Screen, ContactListener {

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
    private Rocket rocket;
    public ArrayList<Debris> debrisList;
    private Label sebbseg_ki, x_ki, y_ki;
    private boolean ready = false;
    private Array<Body> tmpBodies = new Array<Body>();
    public boolean foldon_l = false, foldon_r = false;
    public Body test_joints_destroy;
    public landthat osobj;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!world.isLocked() && test_joints_destroy != null) {
            removeJointSafely();
        }
        world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);

        if (ready) {
            rocket.mozgas(foldon_l, foldon_r);
        }
        camera.position.set(rocket.getChassis().getPosition().x, rocket.getChassis().getPosition().y /*- Gdx.graphics.getHeight()/4*/, 0);
        camera.update();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        world.getBodies(tmpBodies);
        for (Body body : tmpBodies) {
            if (body.getUserData() instanceof Sprite) {

                Sprite sprite = (Sprite) body.getUserData();

                sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
                sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
                sprite.draw(batch);
            }
            if (body.getUserData() instanceof AnimatedSprite) {

                AnimatedSprite sprite = (AnimatedSprite) body.getUserData();

                sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
                sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
                sprite.draw(batch);
            }
            //animated instanceof sprite dolog
        }
        batch.end();
        ready = true;

        stage.act(delta);
        sebbseg_ki.setText("x+y M/s: " + (int) (Math.sqrt(Math.pow((rocket.vissza().x), 2)) + Math.sqrt(Math.pow((rocket.vissza().y), 2))));
        x_ki.setText("x M/s: " + (int) rocket.vissza().x);
        y_ki.setText("y M/s: " + (int) rocket.vissza().y);

        stage.draw();

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
        //world = new World(new Vector2(-3.25f, -6.81f), true); //meg dönti a gravitációt
        world = new World(new Vector2(0, -9.81f), true);
        debugRenderer = new Box2DDebugRenderer();
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.zoom = 2;
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef(), wheelFixtureDef = new FixtureDef();

        // rocket
        fixtureDef.density = 5;
        fixtureDef.friction = .4f;
        fixtureDef.restitution = .3f;

        wheelFixtureDef.density = 0;
        wheelFixtureDef.restitution = .4f;

        rocket = new Rocket(osobj, world, fixtureDef, wheelFixtureDef, 0, 1000);
        Random r = new Random();
        float a;
        debrisList = new ArrayList<Debris>();
        for (int i = 0; i < r.nextInt(20) * 4; i++) {
            a = r.nextInt(100);
            if (r.nextBoolean()) {
                a *= (-1);
            }
            debrisList.add(new Debris(world, fixtureDef, a, .4f, "moon"));
        }

        final TextButton buttonLeft = new TextButton(" left ", skin);
        buttonLeft.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(sequence(moveTo(0, -stage.getHeight(), .5f), run(new Runnable() {

                    @Override
                    public void run() {
                        System.out.println("valami");
                    }
                })));
            }
        });
        /*final ClickListener buttonHandler = new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getListenerActor() == buttonLeft) {
                    System.out.println("left");
                }

            }
        };
        buttonLeft.addListener(buttonHandler);*/
        buttonLeft.pad(15).row();
        table.add(buttonLeft);

        System.out.println(debrisList.size());
        Gdx.input.setInputProcessor(new InputMultiplexer(new InputAdapter() {

            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Keys.ESCAPE:
                        ((Game) Gdx.app.getApplicationListener()).setScreen(osobj.levelmenu);
                        break;
                }
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                camera.zoom += amount / 25f;
                return true;
            }

        }, rocket));

        // GROUND
        // body definition
        bodyDef.type = BodyType.KinematicBody;
        bodyDef.position.set(0, -5f);
        //bodyDef.angle = 320;//megdöntött


        // ground shape
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(100f, 5f);

        //groundShape.setRadius(50);

        // fixture definition
        fixtureDef.shape = groundShape;
        fixtureDef.friction = .5f;
        fixtureDef.restitution = 0;


        //föld textura fel "húzása"
        boxSprite = new Sprite(new Texture("img/moon/lik.png"));
        boxSprite.setSize(200f, 9.8f);
        boxSprite.setOrigin(boxSprite.getWidth() / 2, boxSprite.getHeight() / 2);


        ground = world.createBody(bodyDef);
        ground.createFixture(fixtureDef);
        ground.setUserData(boxSprite);

        groundShape.setAsBox(5f, 25f);
        YourCustomUserData dataa = new YourCustomUserData();
        dataa.forcollison = "g";
        bodyDef.position.set(105f, 15);
        world.createBody(bodyDef).createFixture(fixtureDef).setUserData(dataa);

        bodyDef.position.set(-105f, 15);
        world.createBody(bodyDef).createFixture(fixtureDef).setUserData(dataa);

        groundShape.setAsBox(25f, 2f);
        bodyDef.position.set(0, 40);
        //world.createBody(bodyDef).createFixture(fixtureDef);


        groundShape.dispose();

        //szöveg ki írás
        table.add().row();
        x_ki = new Label("M/s: " + rocket.vissza().x, skin, "big");
        table.add(x_ki).padLeft(25).row();
        y_ki = new Label("M/s: " + rocket.vissza().y, skin, "big");
        table.add(y_ki).padLeft(25).row();
        sebbseg_ki = new Label("M/s: " + (rocket.vissza().x + rocket.vissza().y), skin, "big");
        table.add(sebbseg_ki).padLeft(25);
        stage.addActor(table);
        ground.getFixtureList().get(0).setUserData(dataa);
        world.setContactListener(this);
        //ready=true;
    }

    @Override
    public void beginContact(Contact contact) {
        YourCustomUserData dataA = (YourCustomUserData) contact.getFixtureA().getUserData();
        YourCustomUserData dataB = (YourCustomUserData) contact.getFixtureB().getUserData();
        if (dataA.forcollison.equals("l") && dataB.forcollison.equals("g") || dataA.forcollison.equals("g") && dataB.forcollison.equals("l")) {
            foldon_l = true;
            if ((Math.sqrt(Math.pow((rocket.vissza_left().x), 2)) + Math.sqrt(Math.pow((rocket.vissza_left().y), 2))) > 10) {
                System.out.println("túlgyors");
            } else {
                System.out.println("jólesz");
            }
        }
        if (dataA.forcollison.equals("g") && dataB.forcollison.equals("r") || dataA.forcollison.equals("r") && dataB.forcollison.equals("g")) {
            foldon_r = true;
            if ((Math.sqrt(Math.pow((rocket.vissza_right().x), 2)) + Math.sqrt(Math.pow((rocket.vissza_right().y), 2))) > 10) {
                System.out.println("túlgyors");
            } else {
                System.out.println("jólesz");
            }
        }
        if (dataA.forcollison.equals("c") && dataB.forcollison.equals("g") || dataA.forcollison.equals("g") && dataB.forcollison.equals("c")) {
            System.out.println("főborult");
        }


    }

    @Override
    public void endContact(Contact contact) {
        YourCustomUserData dataA = (YourCustomUserData) contact.getFixtureA().getUserData();
        YourCustomUserData dataB = (YourCustomUserData) contact.getFixtureB().getUserData();
        if (dataA.forcollison.equals("l") && dataB.forcollison.equals("g") || dataA.forcollison.equals("g") && dataB.forcollison.equals("l")) {
            foldon_l = false;
        }
        if (dataA.forcollison.equals("r") && dataB.forcollison.equals("g") || dataA.forcollison.equals("r") && dataB.forcollison.equals("g")) {
            foldon_r = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {


    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        YourCustomUserData dataA = (YourCustomUserData) contact.getFixtureA().getUserData();
        YourCustomUserData dataB = (YourCustomUserData) contact.getFixtureB().getUserData();
        if (dataA.forcollison.contains("l") && dataB.forcollison.contains("g") || dataA.forcollison.equals("g") && dataB.forcollison.equals("l")) {
            if ((Math.sqrt(Math.pow((rocket.vissza_left().x), 2)) + Math.sqrt(Math.pow((rocket.vissza_left().y), 2))) > 10) {
                test_joints_destroy = rocket.leftStick;
            }
        }
        if (dataA.forcollison.contains("r") && dataB.forcollison.contains("g") || dataA.forcollison.equals("g") && dataB.forcollison.equals("r")) {
            if ((Math.sqrt(Math.pow((rocket.vissza_left().x), 2)) + Math.sqrt(Math.pow((rocket.vissza_left().y), 2))) > 10) {
                test_joints_destroy = rocket.rightStick;
            }
        }
    }

    public void removeJointSafely() {
        final Array<JointEdge> list = test_joints_destroy.getJointList();
        while (list.size > 0) {
            world.destroyJoint(list.get(0).joint);
            test_joints_destroy = null;
        }
    }

    @Override
    public void hide() {

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