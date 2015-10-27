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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;

public class Car extends InputAdapter {

	private Body chassis, leftWheel, rightWheel;
	private WheelJoint leftAxis, rightAxis;
	private float motorSpeed = 75;
	private SpriteBatch batch;
	private Sprite boxSprite;
	private Sprite wheelSprite;
	public Car(World world, FixtureDef chassisFixtureDef, FixtureDef wheelFixtureDef, float x, float y, float width, float height) {



		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		batch = new SpriteBatch();
		// chassis
		PolygonShape chassisShape = new PolygonShape();
		//chassisShape.set(new float[] {-width / 2, -height / 2, width / 2, -height / 2, width / 2 * .4f, height / 2, -width / 2 * .8f, height / 2 * .8f}); // counterclockwise order
        chassisShape.setAsBox(2f,.60f);
		chassisFixtureDef.shape = chassisShape;


        //chassis texture betoltese
		boxSprite = new Sprite(new Texture("img/chassis.png"));
		boxSprite.setSize(5, 3);
		boxSprite.setOrigin(boxSprite.getWidth() / 2, boxSprite.getHeight() / 2);

        //Wheel texture betoltese
        wheelSprite = new Sprite(new Texture("img/wheel.png"));
        wheelSprite.setSize(.5f * 2, .5f * 2);
        wheelSprite.setOrigin(wheelSprite.getWidth() / 2, wheelSprite.getHeight() / 2);

		chassis = world.createBody(bodyDef);
		chassis.createFixture(chassisFixtureDef);

		chassis.setUserData(boxSprite);

		// left wheel
		CircleShape wheelShape = new CircleShape();
		wheelShape.setRadius(height / 3.5f);

		wheelFixtureDef.shape = wheelShape;

		leftWheel = world.createBody(bodyDef);
		leftWheel.createFixture(wheelFixtureDef);


		leftWheel.setUserData(wheelSprite);

		// right wheel
		rightWheel = world.createBody(bodyDef);
		rightWheel.createFixture(wheelFixtureDef);

		rightWheel.setUserData(wheelSprite); //add texture

		// left axis
		WheelJointDef axisDef = new WheelJointDef();
		axisDef.bodyA = chassis;
		axisDef.bodyB = leftWheel;
		axisDef.localAnchorA.set(-width / 2 * .75f + wheelShape.getRadius(), -height / 2 * 1.25f);
		axisDef.frequencyHz = chassisFixtureDef.density;
		axisDef.localAxisA.set(Vector2.Y);
		axisDef.maxMotorTorque = chassisFixtureDef.density * 10;
        axisDef.localAnchorA.x *= 2;
		leftAxis = (WheelJoint) world.createJoint(axisDef);

		// right axis
		axisDef.bodyB = rightWheel;
		axisDef.localAnchorA.x *= -1;

		rightAxis = (WheelJoint) world.createJoint(axisDef);
	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
		case Keys.W:
			leftAxis.enableMotor(true);
			leftAxis.setMotorSpeed(-motorSpeed);
			break;
		case Keys.S:
			leftAxis.enableMotor(true);
			leftAxis.setMotorSpeed(motorSpeed);
		}
		return true;
	}

	public Vector2 vissza(){return	leftWheel.getLinearVelocity();}
	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
		case Keys.W:
		case Keys.S:
			leftAxis.enableMotor(false);
		}
		return true;
	}

	public Body getChassis() {
		return chassis;
	}

}
