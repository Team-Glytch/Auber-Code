package com.auber.game.Entities;

import com.auber.game.SpaceGamePrototype;
import com.auber.game.Screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Player extends Sprite{
	public enum State{IDLE,RUNNING};
	public State currentState;
	public State previousState;



	private Animation<TextureRegion> playerIdle;
	private Animation<TextureRegion> playerRun;
	private float stateTimer;
	private boolean facingRight;
	
	public World world;
	public Body box2dBody;

	
	public Player(World world,PlayScreen screen) {
		super(screen.getAtlas().findRegion("playerIdle"));
		this.world = world;
		currentState = State.IDLE;
		previousState = State.IDLE;
		stateTimer = 0;
		facingRight = true;
		
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for(int i = 16; i < 20; i ++) {
			
			frames.add(new TextureRegion(getTexture(), (i * 19) + 7,0,19,23));
		}		
		playerIdle = new Animation<TextureRegion>(0.125f,frames);
		frames.clear();
		
		for(int i = 6; i < 12; i++) {
			
			frames.add(new TextureRegion(getTexture(), (i * 19)+3, 0,19,23));
		}			
		playerRun = new Animation<TextureRegion>(0.125f,frames);
				
		definePlayer();
		setBounds(0,0,19 / SpaceGamePrototype.PixelsPerMetre, 23 / SpaceGamePrototype.PixelsPerMetre);
		
	}
	
	public void update(float deltaTime) {
		
		setPosition((box2dBody.getPosition().x - getWidth()/2),(box2dBody.getPosition().y - getHeight()/4) );
		setRegion(getFrame(deltaTime));
        
	}
	
	public TextureRegion getFrame(float deltaTime) {
		
		currentState = getState();
		TextureRegion region;
		switch(currentState) {
			case RUNNING:
				region = playerRun.getKeyFrame(stateTimer,true);
				break;
			case IDLE:
			default:
				region = playerIdle.getKeyFrame(stateTimer,true);
				break;
				
		}
		
		if((box2dBody.getLinearVelocity().x < 0 || !facingRight) && !region.isFlipX()) {
			region.flip(true,false);
			facingRight = false;
		}
		else if((box2dBody.getLinearVelocity().x > 0 || facingRight) && region.isFlipX()) {
			region.flip(true,false);
			facingRight = true;
		}
		
		stateTimer = currentState == previousState ? stateTimer + deltaTime : 0;
		previousState = currentState;
		return region;
		
		
	}
	
	public State getState() {
		if(box2dBody.getLinearVelocity().x != 0 || box2dBody.getLinearVelocity().y != 0)
			return State.RUNNING;
		else
			return State.IDLE;
	}
	
	
	public void definePlayer() {
		BodyDef bodyDefinition = new BodyDef();
		bodyDefinition.position.set(780 / SpaceGamePrototype.PixelsPerMetre,1250 / SpaceGamePrototype.PixelsPerMetre);
		bodyDefinition.type = BodyDef.BodyType.DynamicBody;
		box2dBody = world.createBody(bodyDefinition);	
		FixtureDef fixtureDefinition = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(5 / SpaceGamePrototype.PixelsPerMetre);	
		
		fixtureDefinition.shape = shape;

		box2dBody.createFixture(fixtureDefinition);
	}
	

			
			
		
	
	
	 
}
