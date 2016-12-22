package com.gii.autosim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

public class MyGdxGame extends ApplicationAdapter {
	Texture car1;
	public OrthographicCamera camera;
	public SpriteBatch batch;
	private Rectangle carRectangle;

	float x = 0, y = 100;
	
	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800/10, 480/10);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
		car1 = new Texture(Gdx.files.internal("car1.png"));
		AutoSim.generateCars();
	}

	long lastTime = 0;

	Vector3 lastMouse = new Vector3(-1,-1,0);
	int lastFingerDistance = -1;

	@Override
	public void render () {
		long newTime =  TimeUtils.millis();
		if (lastTime == 0 || (newTime - lastTime) > AutoSim.timeInterval) {
			if (lastTime == 0)
				lastTime = newTime;
			else
				lastTime += AutoSim.timeInterval;
			AutoSim.update();
		} else {
			Gdx.app.log("MyGdxGame time:",lastTime + "," + newTime);
		}

		if(Gdx.input.isTouched() && !Gdx.input.isTouched(1)) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			Vector3 touch1 = new Vector3(touchPos.x,touchPos.y,touchPos.z);
			camera.unproject(touch1);
			if (lastMouse.x != -1) {
				camera.position.set(camera.position.x - (touch1.x - lastMouse.x),
						camera.position.y - (touch1.y - lastMouse.y), 0);
				camera.update();
			}
			camera.unproject(touchPos);
			lastMouse.set(touchPos);
		} else
			lastMouse.set(-1,-1,0);

		if (Gdx.input.isTouched(1)) {
			Vector3 finger1 = new Vector3();
			Vector3 finger2 = new Vector3();
			finger1.set(Gdx.input.getX(0), Gdx.input.getY(0), 0);
			finger2.set(Gdx.input.getX(1), Gdx.input.getY(1), 0);
			int fingerDistance = (int)(Math.sqrt((finger1.x - finger2.x)*(finger1.x - finger2.x) +
					(finger1.y - finger2.y)*(finger1.y - finger2.y)));
			if (lastFingerDistance != -1) {
				float zoom = fingerDistance/(float)lastFingerDistance;
				camera.zoom *= 1/zoom;
			}
			lastFingerDistance = fingerDistance;
			camera.update();
		} else
			lastFingerDistance = -1;

		//camera.position.set(camera.position.x, camera.position.y - 0.1f, 0);
		//camera.update();

		x++;
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (Car car : AutoSim.cars) {
			car.draw(batch,car1);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		car1.dispose();
	}
}
