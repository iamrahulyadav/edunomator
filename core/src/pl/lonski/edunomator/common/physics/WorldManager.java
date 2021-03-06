package pl.lonski.edunomator.common.physics;

import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class WorldManager {

	public static final float PIXELS_TO_METERS = 100f;

	private final float screenWidth;
	private final float screenHeight;

	private World world;
	private Box2DDebug debugRender;

	private Body floor;
	private Body ceil;
	private Body leftWall;
	private Body rightWall;
	private boolean freezed;

	public WorldManager(float screenWidth, float screenHeight) {
		this(screenWidth, screenHeight, new Vector2(0, -10));
	}

	public WorldManager(float screenWidth, float screenHeight, Vector2 gravity) {
		this(screenWidth, screenHeight, gravity, true);
	}

	public WorldManager(float screenWidth, float screenHeight, Vector2 gravity, boolean walls) {

		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.world = new World(gravity, true);

		if (walls) {
			float w = screenWidth / PIXELS_TO_METERS;
			float h = screenHeight / PIXELS_TO_METERS;

			float thickness = 10;
			floor = createWall(w / 2, -thickness / 2f, w + 2 * thickness, thickness);
			ceil = createWall(w / 2, h + thickness / 2f, w + 2 * thickness, thickness);
			leftWall = createWall(-thickness / 2f, h / 2, thickness, h + 2 * thickness);
			rightWall = createWall(w + thickness / 2f, h / 2, thickness, h + 2 * thickness);
		}
		debugRender = new Box2DDebug(screenWidth, screenHeight);
	}

	public World getWorld() {
		return world;
	}

	public void update(float delta) {
		if (freezed) {
			return;
		}

		world.step(delta, 6, 2);

		Array<Body> bodies = new Array<>();
		world.getBodies(bodies);
		for (Body b : bodies) {
			pl.lonski.edunomator.common.physics.BodyUserData data =
					(pl.lonski.edunomator.common.physics.BodyUserData) b.getUserData();
			if (data != null) {
				if (data.isDeleteFlag()) {
					world.destroyBody(b);
				}
			}
		}
	}

	public void debugDraw() {
		debugRender.render(world);
	}

	private Body createWall(float x, float y, float hx, float hy) {
		BodyDef wallDef = new BodyDef();
		wallDef.type = StaticBody;
		wallDef.position.set(x, y);

		FixtureDef fixtureDef3 = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(hx / 2, hy / 2, new Vector2(0, 0), 0);
		fixtureDef3.shape = shape;

		Body body = world.createBody(wallDef);
		body.createFixture(fixtureDef3);
		shape.dispose();
		return body;
	}

	public void setFreezed(boolean freezed) {
		this.freezed = freezed;
	}

	private static class Box2DDebug {

		private OrthographicCamera camera;
		private Viewport viewport;
		private Box2DDebugRenderer debugRenderer;

		Box2DDebug(float screenWidth, float screenHeight) {
			camera = new OrthographicCamera(screenWidth, screenHeight);
			camera.setToOrtho(false, screenWidth, screenHeight);
			viewport = new FillViewport(screenWidth / 40, screenHeight / 40, camera);
			viewport.apply();
			camera.position.set(camera.viewportWidth / 4, camera.viewportHeight / 4, 0);

			debugRenderer = new Box2DDebugRenderer();
		}

		void render(World world) {
			camera.update();
			debugRenderer.render(world, camera.combined);
		}
	}
}

