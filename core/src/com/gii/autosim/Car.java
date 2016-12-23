package com.gii.autosim;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Timur_hnimdvi on 22-Dec-16.
 */
public class Car {
    public static String TAG = "Car.java";
    float width = 3.8f;
    float height = 1.8f;
    float maxSpeed = 60;
    CarState currentState = new CarState();
    CarState goalState = new CarState();

    public Car(float x, float y, float rotation) {
        this.currentState.x = x;
        this.currentState.y = y;
        this.currentState.rotation = rotation;
    }
    public void draw(SpriteBatch batch, Texture texture) {
        Rectangle position = new Rectangle();
        position.set(currentState.x - width/2f, currentState.y- height/2f, width, height);
        batch.draw(texture, position.x, position.y, width/2f, height/2f,width,height,1,1,currentState.rotation,0,0,100,52,false,false);
    }
    public void update(float timeInterval) {
        //x = x + 3/timeInterval; //10 = 36kmh
        move(timeInterval);
    }

    private void move(float timeInterval) {
        float speedMS = currentState.speed * 1000f / 3600f;
        float accMS = currentState.acceleration * 1000f / 3600f;
        float t = timeInterval / 1000f;
        float s = speedMS * t + accMS * t * t / 2f;
        Gdx.app.log(TAG, "move: time:" + timeInterval + ", speedMS: " +speedMS + ", accMS: "+ accMS +
                    ", t:" + t + ", s:" + s);
        float radians = (float)(currentState.rotation / Math.PI);
        float newX = currentState.x + (float)(Math.cos(radians) * s);
        float newY = currentState.y + (float)(Math.sin(radians) * s);
        currentState.x = newX;
        currentState.y = newY;
        currentState.speed = Math.min(currentState.speed + currentState.acceleration * t,maxSpeed);
        Gdx.app.log(TAG, "move speed: " + currentState.speed + " (added " + currentState.acceleration * t + ")");
    }
}
