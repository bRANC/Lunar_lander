package hu.funyirok.lander.entities;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;

import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

public class Rocket extends InputAdapter {

    public Body chassis, leftStick, rightStick;
    public WheelJoint leftAxis, rightAxis;
    private float motorSpeed = 30;
    private SpriteBatch batch;
    private Sprite boxSprite;
    private Sprite wheelSprite;
    public AnimatedSprite animsprite;
    private boolean fel = false, le = false, bal = false, jobb = false;
    public double angel;
    public float irany_x, irany_y;
    private Vector2 direction;

    //http://gamedev.stackexchange.com/questions/84429/box2d-and-libgdx-attach-particleeffect-to-body
    public Rocket(World world, FixtureDef chassisFixtureDef, FixtureDef wheelFixtureDef, float x, float y, float width, float height) {


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        batch = new SpriteBatch();
        // chassis
        PolygonShape chassisShape = new PolygonShape();
        //chassisShape.set(new float[] {-width / 2, -height / 2, width / 2, -height / 2, width / 2 * .4f, height / 2, -width / 2 * .8f, height / 2 * .8f}); // counterclockwise order
        chassisShape.setAsBox(.60f, 1);
        chassisFixtureDef.shape = chassisShape;
        chassisFixtureDef.density = 5;


        chassis = world.createBody(bodyDef);
        chassis.createFixture(chassisFixtureDef);

        //chassis animated texture betoltese
        Animation anim = new Animation(1 / 3f, new TextureRegion(new Texture("img/ship/body.png")), new TextureRegion(new Texture("img/ship/body.png")), new TextureRegion(new Texture("img/ship/body.png")));
        anim.setPlayMode(Animation.PlayMode.LOOP);
        animsprite = new AnimatedSprite(anim);
        animsprite.setSize(3, 5);
        animsprite.setOrigin(animsprite.getWidth() / 2, animsprite.getHeight() / 2);

        chassis.setUserData(anim);

        // left wheel
        PolygonShape lander_stick = new PolygonShape();
        lander_stick.setAsBox(.2f, .2f);


        wheelFixtureDef.shape = lander_stick;
        wheelFixtureDef.density = 0;

//Wheel texture betoltese
        wheelSprite = new Sprite(new Texture("img/ship/left_stick.png"));
        wheelSprite.setSize(.5f * 2, .5f * 2);
        wheelSprite.setOrigin(wheelSprite.getWidth() / 2, wheelSprite.getHeight() / 2);

        leftStick = world.createBody(bodyDef);
        leftStick.createFixture(wheelFixtureDef);

        leftStick.setUserData(wheelSprite);

        //Wheel texture betoltese
        wheelSprite = new Sprite(new Texture("img/ship/right_stick.png"));
        wheelSprite.setSize(.5f * 2, .5f * 2);
        wheelSprite.setOrigin(wheelSprite.getWidth() / 2, wheelSprite.getHeight() / 2);

        // right wheel
        rightStick = world.createBody(bodyDef);
        rightStick.createFixture(wheelFixtureDef);

        rightStick.setUserData(wheelSprite); //add texture

        // left axis
        WheelJointDef axisDef = new WheelJointDef();
        axisDef.bodyA = chassis;
        axisDef.bodyB = leftStick;
        axisDef.localAnchorA.set(-width / 2 * .40f + lander_stick.getRadius(), -height / 2 * 3f);
        axisDef.frequencyHz = chassisFixtureDef.density;
        axisDef.localAxisA.set(Vector2.Y);
        //axisDef.maxMotorTorque = chassisFixtureDef.density * 10;
        axisDef.localAnchorA.x *= 2;
        leftAxis = (WheelJoint) world.createJoint(axisDef);

        // right axis
        axisDef.bodyB = rightStick;
        axisDef.localAnchorA.x *= -1;

        rightAxis = (WheelJoint) world.createJoint(axisDef);
        YourCustomUserData dataa = new YourCustomUserData();
        YourCustomUserData datab = new YourCustomUserData();
        YourCustomUserData datac = new YourCustomUserData();
        dataa.forcollison = "l";
        leftStick.getFixtureList().get(0).setUserData(dataa);
        datab.forcollison = "r";
        rightStick.getFixtureList().get(0).setUserData(datab);
        datac.forcollison = "c";
        chassis.getFixtureList().get(0).setUserData(datac);


    }


    //minden eggyes render kockában meg hívva
    public void mozgas(boolean foldon_l, boolean foldon_r) {
        //angel = (angel * 180) / Math.PI;//fokba alakítás
        if (0.08 > Math.sqrt(Math.pow((chassis.getAngularVelocity()), 2))) {//extra stabilitás hogy ne prögöjön mint a pörgő warrior
            chassis.setAngularVelocity(0);
        }
        // System.out.println(chassis.getAngularVelocity());
        irany_y = (float) Math.cos(chassis.getAngle()) * motorSpeed * 16;
        irany_x = (float) Math.sin(chassis.getAngle()) * motorSpeed * 16;

        if (fel) {
            chassis.applyForce(-irany_x, irany_y, chassis.getWorldCenter().x, chassis.getWorldCenter().y, true);
        }
        if (le) {
            chassis.applyForce(irany_x, -irany_y, chassis.getWorldCenter().x, chassis.getWorldCenter().y, true);
        }
        if (bal) {
            chassis.applyAngularImpulse(-2.5f, true);
        }

        if (jobb) {
            chassis.applyAngularImpulse(2.5f, true);
        }
        if (!foldon_l)
            leftStick.setTransform(leftStick.getPosition().x, leftStick.getPosition().y, chassis.getAngle());
        if (!foldon_r)
            rightStick.setTransform(rightStick.getPosition().x, rightStick.getPosition().y, chassis.getAngle());

    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.W:
                leftAxis.enableMotor(true);
                leftAxis.setMotorSpeed(-motorSpeed);
                break;
            case Keys.S:
                leftAxis.enableMotor(true);
                leftAxis.setMotorSpeed(motorSpeed);
                break;
            case Keys.UP:
                fel = true;
                break;
            case Keys.LEFT:
                bal = true;
                break;
            case Keys.RIGHT:
                jobb = true;
                break;
            case Keys.DOWN:
                le = true;
                break;
        }
        return true;
    }


    public Vector2 vissza() {
        return chassis.getLinearVelocity();
    }

    public Vector2 vissza_left() {
        return leftStick.getLinearVelocity();
    }

    public Vector2 vissza_right() {
        return rightStick.getLinearVelocity();
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.W:
            case Keys.S:
                leftAxis.enableMotor(false);
                break;
            case Keys.UP:
                fel = false;
                break;
            case Keys.LEFT:
                bal = false;
                break;
            case Keys.RIGHT:
                jobb = false;
                break;
            case Keys.DOWN:
                le = false;
                break;
        }
        return true;
    }

    public Body getChassis() {
        return chassis;
    }

}
