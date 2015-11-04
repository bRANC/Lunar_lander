package hu.funyirok.lander.entities;

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

/**
 * Created by bRANC on 11/4/2015.
 */


public class Debris {
    public Body debryy;
    public Sprite debryySprite;
    public Debris(World world, FixtureDef FixtureDef, float x, float y,String helyszin) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.KinematicBody;
        bodyDef.position.set(x, y);

        PolygonShape debrisShape = new PolygonShape();
        debrisShape.setAsBox(.70f, .40f);
        FixtureDef.shape = debrisShape;

        debryy = world.createBody(bodyDef);
        debryy.createFixture(FixtureDef);

        debryySprite = new Sprite(new Texture("img/"+helyszin+"/lik.png"));
        debryySprite.setSize(.70f*2, .40f * 2);
        debryySprite.setOrigin(debryySprite.getWidth() / 2, debryySprite.getHeight() / 2);
        debryy.setUserData(debryySprite);

        YourCustomUserData data = new YourCustomUserData();
        data.forcollison = "d";
        debryy.getFixtureList().get(0).setUserData(data);
    }
}