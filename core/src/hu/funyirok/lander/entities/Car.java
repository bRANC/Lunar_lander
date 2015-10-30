package hu.funyirok.lander.entities;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;

public class Car extends InputAdapter {

    private Body chassis, leftStick, rightWheel;
    private WheelJoint leftAxis, rightAxis;
    private float motorSpeed = 30;
    private SpriteBatch batch;
    private Sprite boxSprite;
    private Sprite wheelSprite;
    private boolean fel = false, le = false, bal = false, jobb = false;
    public double angel;
    public float irany_x,irany_y;
    private Vector2 direction;
    //http://gamedev.stackexchange.com/questions/84429/box2d-and-libgdx-attach-particleeffect-to-body
    public Car(World world, FixtureDef chassisFixtureDef, FixtureDef wheelFixtureDef, float x, float y, float width, float height) {


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        batch = new SpriteBatch();
        // chassis
        PolygonShape chassisShape = new PolygonShape();
        //chassisShape.set(new float[] {-width / 2, -height / 2, width / 2, -height / 2, width / 2 * .4f, height / 2, -width / 2 * .8f, height / 2 * .8f}); // counterclockwise order
        chassisShape.setAsBox(.60f, 1);
        chassisFixtureDef.shape = chassisShape;


        //chassis texture betoltese
        boxSprite = new Sprite(new Texture("img/ship/body.png"));
        boxSprite.setSize(3, 5);
        boxSprite.setOrigin(boxSprite.getWidth() / 2, boxSprite.getHeight() / 2);


        chassis = world.createBody(bodyDef);
        chassis.createFixture(chassisFixtureDef);

        chassis.setUserData(boxSprite);

        // left wheel
        PolygonShape lander_stick = new PolygonShape();
        lander_stick.setAsBox(.2f, .2f);

        wheelFixtureDef.shape = lander_stick;


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
        rightWheel = world.createBody(bodyDef);
        rightWheel.createFixture(wheelFixtureDef);

        rightWheel.setUserData(wheelSprite); //add texture

        // left axis
        WheelJointDef axisDef = new WheelJointDef();
        //axisDef.bodyA = chassis;
        //axisDef.bodyB = leftStick;
        axisDef.localAnchorA.set(-width / 2 * .35f + lander_stick.getRadius(), -height / 2 * 3f);
        axisDef.frequencyHz = chassisFixtureDef.density;
        axisDef.localAxisA.set(Vector2.Y);
        axisDef.maxMotorTorque = chassisFixtureDef.density * 10;
        axisDef.localAnchorA.x *= 2;
//        leftAxis = (WheelJoint) world.createJoint(axisDef);

        // right axis
        //axisDef.bodyB = rightWheel;
        axisDef.localAnchorA.x *= -1;

        //       rightAxis = (WheelJoint) world.createJoint(axisDef);
    }


    //minden eggyes render kockában meg hívva
    public void mozgas() {
        //angel = (angel * 180) / Math.PI;//fokba alakítás
        /*try {

            direction.x =((float)Math.cos(chassis.getAngle()));
            direction.y =((float)Math.sin(chassis.getAngle()));
        }catch (Exception e){
            System.out.println("valami van "+e);
        }*/
        irany_y = (float) Math.cos(chassis.getAngle())*motorSpeed;
        irany_x = (float) Math.sin(chassis.getAngle())*motorSpeed;

       /* try {
            direction.add((float) chassis.getAngle(), (float) chassis.getAngle());
        } catch (Exception e) {
            System.out.println("valami van " + e);
        }
*/

        if (fel) {
            chassis.applyLinearImpulse(-irany_x, irany_y, chassis.getWorldCenter().x, chassis.getWorldCenter().y, true);
        }
        if (le) {
            chassis.applyLinearImpulse(irany_x, -irany_y, chassis.getWorldCenter().x, chassis.getWorldCenter().y, true);
        }
        if (bal) {
            chassis.applyAngularImpulse(-5f, true);
        }/*else{
            chassis.setFixedRotation(true);
            chassis.setFixedRotation(false);
        }*/

        if (jobb) {
            chassis.applyAngularImpulse(5f, true);
        }
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
        return leftStick.getLinearVelocity();
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
