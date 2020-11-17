package com.davidalexanderkelly.game.Entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.davidalexanderkelly.game.SpaceGamePrototype;
import com.davidalexanderkelly.game.Entities.Behaviors.Node;
import com.davidalexanderkelly.game.Entities.Behaviors.Pathfinding;
import com.davidalexanderkelly.game.Screens.PlayScreen;
import com.davidalexanderkelly.game.Tools.InteractableWorldCreator;
import com.davidalexanderkelly.game.Tools.PathfindingWorldCreator;

public class Enemy extends Sprite{
	public enum State{IDLE,RUNNING};
	public State currentState;
	public State previousState;


	private InteractableWorldCreator interactables;
	private Animation<TextureRegion> playerIdle;
	private Animation<TextureRegion> playerRun;
	private float stateTimer;
	private boolean facingRight;
	private PlayScreen screen;

	
	public World world;
	public Body box2dBody;
	
	private Pathfinding pathfinding;
	private ArrayList<Node> path;
	
	private boolean moving;
	

	
	public Enemy(World world,PlayScreen screen) {
		
		super(screen.getAtlas().findRegion("enemyIdle"));
		this.screen = screen;
		this.world = world;
		currentState = State.IDLE;
		previousState = State.IDLE;
		stateTimer = 0;
		facingRight = true;
		
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for(int i = 12; i < 16; i ++) {
			
			frames.add(new TextureRegion(getTexture(), (i * 19) + 7,0,19,23));
		}		
		playerIdle = new Animation<TextureRegion>(0.125f,frames);
		frames.clear();
		
		for(int i = 0; i < 6; i++) {
			frames.add(new TextureRegion(getTexture(), (i * 19)+3, 0,19,23));
		}			
		playerRun = new Animation<TextureRegion>(0.125f,frames);
				
		defineEnemy();
		setBounds(0,0,19 / SpaceGamePrototype.PixelsPerMetre, 23 / SpaceGamePrototype.PixelsPerMetre);
		setPath(0,1);
		
	}
	
	public void update(float deltaTime) {
		
		setPosition((box2dBody.getPosition().x - getWidth()/2),(box2dBody.getPosition().y - getHeight()/4) );
		setRegion(getFrame(deltaTime));
		moving = true;
		move(path.get(0));
		
        
	}
	
	public static float round(float d, int decimalPlace) {
	    BigDecimal bd = new BigDecimal(Float.toString(d));
	    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
	    return bd.floatValue();
	}
	
	public void move(Node node) {

			if(round(node.getWorldPosition().x,2) == round(box2dBody.getPosition().x,2) && round(node.getWorldPosition().y,2) == round(box2dBody.getPosition().y,2)) {
				path.remove(0);
				if(path.size() == 0) {
					setPath(1,2);
				}
			}
			else {
				if(round(node.getWorldPosition().y,2) - round(box2dBody.getPosition().y,2) == 0) {
					if(round(node.getWorldPosition().x,2) - round(box2dBody.getPosition().x,2) > 0) {
						box2dBody.setTransform(round(box2dBody.getPosition().x + 0.01f,2),round(box2dBody.getPosition().y,2),0);
						facingRight = true;
					}
					else {
						box2dBody.setTransform(round(box2dBody.getPosition().x - 0.01f,2),round(box2dBody.getPosition().y,2),0);
						facingRight = false;
					}	
				}									
				else {
					if(round(node.getWorldPosition().y,2) - round(box2dBody.getPosition().y,2) > 0) {
						box2dBody.setTransform(round(box2dBody.getPosition().x,2),round(box2dBody.getPosition().y + 0.01f,2),0);
					}
					else {
						box2dBody.setTransform(round(box2dBody.getPosition().x,2),round(box2dBody.getPosition().y - 0.01f,2),0);
					}
				}
					
			}
			
				
			
		
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
		if((moving && !facingRight) && !region.isFlipX()) {
			region.flip(true,false);
			facingRight = false;
		}
		else if((moving && facingRight) && region.isFlipX()) {
			region.flip(true,false);
			facingRight = true;
		}
		
		stateTimer = currentState == previousState ? stateTimer + deltaTime : 0;
		previousState = currentState;
		return region;
		
		
	}
	
	public State getState() {
		if(moving == true)
			return State.RUNNING;
		else
			return State.IDLE;
	}
	
	public void setPath(int start, int end) {
		moving = false;
		pathfinding = new Pathfinding();
		List<Node> interactables = screen.interactables.getLocations();
		path = pathfinding.findPath(interactables.get(start),interactables.get(end),screen.pathfinder);
	}
	
	
	public void defineEnemy() {
		BodyDef bodyDefinition = new BodyDef();
		bodyDefinition.position.set(screen.interactables.getLocations().get(0).getWorldPosition().x,screen.interactables.getLocations().get(0).getWorldPosition().y);
		bodyDefinition.type = BodyDef.BodyType.DynamicBody;
		box2dBody = world.createBody(bodyDefinition);
		FixtureDef fixtureDefinition = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(0 / SpaceGamePrototype.PixelsPerMetre);
		
		fixtureDefinition.shape = shape;

		box2dBody.createFixture(fixtureDefinition);
	}
}
