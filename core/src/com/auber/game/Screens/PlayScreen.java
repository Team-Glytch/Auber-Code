package com.auber.game.Screens;



import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.auber.game.SpaceGamePrototype;
import com.auber.game.Entities.Enemy;
import com.auber.game.Entities.Player;
import com.auber.game.Entities.Behaviors.Node;
import com.auber.game.Entities.Behaviors.Pathfinding;
import com.auber.game.Scenes.Hud;
import com.auber.game.Tools.Assets;
import com.auber.game.Tools.Box2DWorldCreator;
import com.auber.game.Tools.EnemyUpdater;
import com.auber.game.Tools.InteractableWorldCreator;
import com.auber.game.Tools.PathfindingWorldCreator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PlayScreen implements Screen {
	//Reference to Game, used to set screen
	private SpaceGamePrototype game;
	
	public SpriteBatch batch;
	
	private TextureAtlas atlas;
	public Texture img;
	private Skin skin;
	
	private static final int IDLE = 0;
	private static final int RUNNING = 1;
	
	
	//Camera and Camera manipulation variables
	private OrthographicCamera gamecam;
	private Viewport gamePort;  
	private Hud hud;
	
	//Tiled map variables
	private TmxMapLoader maploader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	
	//Box2d variables
	private World world;
	private Box2DDebugRenderer collisionRenderer;
	
	public PathfindingWorldCreator pathfinder;
	public InteractableWorldCreator interactables;

	private Assets assets;
	private Player player;
	private Enemy enemy;

	

	public PlayScreen(SpaceGamePrototype game) {
		
		atlas = new TextureAtlas("assets/Sprites/Spritepack.atlas");
		this.game = game;
		//create camera used to follow player
		gamecam = new OrthographicCamera();	
		//create a FitViewport to maintain virtual aspect ratio despite screen size
		gamePort = new ExtendViewport(SpaceGamePrototype.V_WIDTH / SpaceGamePrototype.PixelsPerMetre, SpaceGamePrototype.V_HEIGHT / SpaceGamePrototype.PixelsPerMetre, gamecam);
		
		hud = new Hud(game.batch);
		
		//Load the map and setup its renderer
		maploader = new TmxMapLoader();
		map = maploader.load("assets/Maps/SpaceStation.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1 / SpaceGamePrototype.PixelsPerMetre);
		
		//Game camera position and zoom level
		gamecam.position.set(gamePort.getWorldWidth() /2 , gamePort.getWorldHeight() /2,0);
		gamecam.zoom = 0.2f;
		
		//Creates the collision world
		world = new World(new Vector2(0,0), true);
		
		interactables = new InteractableWorldCreator(map);
		interactables.setLocations();
		
		pathfinder = new PathfindingWorldCreator(map);
		pathfinder.setLocations();
		
		
		
		//allows the rendering of collision boxes
		collisionRenderer = new Box2DDebugRenderer();
		
		new Box2DWorldCreator(this);
		
		batch = new SpriteBatch();

		assets = new Assets();
		assets.load();
		assets.manager.finishLoading();

		skin = new Skin();
		skin.addRegions(assets.manager.get("assets/Sprites/Spritepack.atlas", TextureAtlas.class));
		
		ArrayList<Node> path = new ArrayList<Node>();
		
		//creates Player in the world
		player = new Player(world,this );
		enemy = new Enemy(world,this);


				
	}
		
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
	
	public void handleInput(float deltaTime) {
		
		//Player Movement
		boolean moveUp = (Gdx.input.isKeyPressed(Input.Keys.UP));
		boolean moveDown = (Gdx.input.isKeyPressed(Input.Keys.DOWN));
		boolean moveRight = (Gdx.input.isKeyPressed(Input.Keys.RIGHT));
		boolean moveLeft = (Gdx.input.isKeyPressed(Input.Keys.LEFT));
		
		if (moveUp && player.box2dBody.getLinearVelocity().y < 0.7f && !moveDown) {
			player.box2dBody.applyLinearImpulse(new Vector2(0f,0.15f), player.box2dBody.getWorldCenter(), true);
		} else if (moveDown && player.box2dBody.getLinearVelocity().y > -0.7f && !moveUp) {
			player.box2dBody.applyLinearImpulse(new Vector2(0f,-0.15f), player.box2dBody.getWorldCenter(), true);
		} else if (moveUp == moveDown) {
			player.box2dBody.setLinearVelocity(player.box2dBody.getLinearVelocity().x,0f);
		}
		
		if (moveRight && player.box2dBody.getLinearVelocity().x < 0.7f && !moveLeft) {
			player.box2dBody.applyLinearImpulse(new Vector2(0.15f, 0f), player.box2dBody.getWorldCenter(), true);
		} else if (moveLeft && player.box2dBody.getLinearVelocity().x > -0.7f && !moveRight) {
			player.box2dBody.applyLinearImpulse(new Vector2(-0.15f, 0f), player.box2dBody.getWorldCenter(), true);
		} else if (moveRight == moveLeft) {
			player.box2dBody.setLinearVelocity(0f, player.box2dBody.getLinearVelocity().y);
		}

		
	}
	
	
	
	public void update(float deltaTime) {		
		//looks for player input
		handleInput(deltaTime);
		
		//updates physics 60 times per second
		world.step(1/60f,6,2);
		player.update(deltaTime);
		EnemyUpdater enemyUpdate = new EnemyUpdater(enemy,deltaTime);
		enemyUpdate.start();		
		//Game camera follows the player
		gamecam.position.x = player.box2dBody.getPosition().x;
		gamecam.position.y = player.box2dBody.getPosition().y;
		//System.out.println(player.box2dBody.getPosition());
		gamecam.update();
		
		
		
		//Tells the renderer to only draw what the player can see
		renderer.setView(gamecam);
	}

	@Override
	public void render(float delta) {
		//separate update logic from renderer
		update(delta);
		//Clear game screen with Black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//render the game map
		renderer.render();
		
		//renderer for the collision boxes
		collisionRenderer.render(world, gamecam.combined);
		
		game.batch.setProjectionMatrix(gamecam.combined);
		game.batch.begin();
		player.draw(game.batch);
		enemy.draw(game.batch);
		game.batch.end();
	}
	
	public TiledMap getMap() {
		return this.map;
	}
	
	public World getWorld() {
		return world;
	}
	

	@Override
	public void resize(int width, int height) {
		gamePort.update(width,height);
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		//dispose of all opened resources
		map.dispose();
		renderer.dispose();
		world.dispose();
		collisionRenderer.dispose();
		hud.dispose();
		skin.dispose();
		batch.dispose();
	
		
	}

	public TextureAtlas getAtlas() {
		return atlas;
	}

}
