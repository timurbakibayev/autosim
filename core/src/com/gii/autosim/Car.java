package com.gii.autosim;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Created by Timur_hnimdvi on 22-Dec-16.
 * Changed by Kuanysh!
 */
public class Car {
    public static String TAG = "Car.java";
    float length = 3.8f;
    float width = 1.8f;
    float angleInsideCar = 10;
    float halfDiagonalInsideCar = 0;
    float maxSpeed = 60;
    float maxAcceleration = 10;
    float minAcceleration = -35;
    float comfortDeceleration = 10;
    Polygon predictPolygon = new Polygon();
    Polygon rightNowPolygon = new Polygon();

    CarState currentState = new CarState();
    ArrayList<CarState> goalStates = new ArrayList<CarState>();

    public Car(float x, float y, float rotation) {
        this.currentState.x = x;
        this.currentState.y = y;
        this.currentState.rotation = rotation;
        calculateAngleInsideCar();
    }

    private void calculateAngleInsideCar() {
        angleInsideCar = (float)Math.toDegrees(Math.acos(length/Math.sqrt(length*length + width*width)));
        halfDiagonalInsideCar = (float)Math.sqrt(length*length + width*width)/2;
        Gdx.app.log(TAG, "angleInsideCar: " + angleInsideCar);
        Gdx.app.log(TAG, "halfDiagInsideCar: " + halfDiagonalInsideCar);
    }

    public void draw(SpriteBatch batch, Texture texture) {
        Rectangle position = new Rectangle();
        position.set(currentState.x - length /2f, currentState.y- width /2f, length, width);
        batch.draw(texture, position.x, position.y, length /2f, width /2f, length, width,1,1,currentState.rotation,0,0,100,52,false,false);
    }
    public void update(float timeInterval) {
        //x = x + 3/timeInterval; //10 = 36kmh
        control(timeInterval);
        move(timeInterval);
    }

    private void control(float timeInterval) {
        if (goalStates.size() == 0) {
            currentState.acceleration = minAcceleration;
            return;
        }
        ControlCar.changeSpeedAndDirection(this, timeInterval);
    }

    public static CarState carState1 = new CarState();
    public static CarState carState2 = new CarState();
    public static CarState carState3 = new CarState();

    private void move(float timeInterval) {
        movePredict(timeInterval,carState1);
        currentState.x = carState1.x;
        currentState.y = carState1.y;
        currentState.speed = carState1.speed;
    }
    public void movePredict(float timeInterval,CarState carState) {
        float speedMS = currentState.speed * 1000f / 3600f;
        float accMS = currentState.acceleration * 1000f / 3600f;
        float t = timeInterval / 1000f;
        float s = speedMS * t + accMS * t * t / 2f;
        float radians = (float)(currentState.rotation * Math.PI / 180);
        float newX = currentState.x + (float)(Math.cos(radians) * s);
        float newY = currentState.y + (float)(Math.sin(radians) * s);
        carState.x = newX;
        carState.y = newY;
        carState.speed = Math.max(Math.min(currentState.speed + currentState.acceleration * t,maxSpeed),0);
    }
    public void predictTwoSecondsToPredictPolygon() {
        movePredict(1000,carState2);
        movePredict(200,carState3);
        float way1Angle = Geometry.angle(currentState.x,currentState.y,carState2.x,carState2.y);
        //float way2Angle = Geometry.angle(carState2.x,carState2.y,carState3.x,carState3.y);
        {
            float[] vertices = new float[4 * 2];
            Geometry.moveToAngle(currentState.x, currentState.y, way1Angle + 180 + angleInsideCar, halfDiagonalInsideCar);
            vertices[0] = Geometry.tempVector.x;
            vertices[1] = Geometry.tempVector.y;
            Geometry.moveToAngle(currentState.x, currentState.y, way1Angle + 180 - angleInsideCar, halfDiagonalInsideCar);
            vertices[2] = Geometry.tempVector.x;
            vertices[3] = Geometry.tempVector.y;
            Geometry.moveToAngle(carState2.x, carState2.y, way1Angle + angleInsideCar, halfDiagonalInsideCar);
            vertices[4] = Geometry.tempVector.x;
            vertices[5] = Geometry.tempVector.y;
            Geometry.moveToAngle(carState2.x, carState2.y, way1Angle - angleInsideCar, halfDiagonalInsideCar);
            vertices[6] = Geometry.tempVector.x;
            vertices[7] = Geometry.tempVector.y;
            predictPolygon.setVertices(vertices);
        }
        {
            float[] vertices = new float[4 * 2];
            Geometry.moveToAngle(currentState.x, currentState.y, way1Angle + 180 + angleInsideCar, halfDiagonalInsideCar);
            vertices[0] = Geometry.tempVector.x;
            vertices[1] = Geometry.tempVector.y;
            Geometry.moveToAngle(currentState.x, currentState.y, way1Angle + 180 - angleInsideCar, halfDiagonalInsideCar);
            vertices[2] = Geometry.tempVector.x;
            vertices[3] = Geometry.tempVector.y;
            Geometry.moveToAngle(carState2.x, carState3.y, way1Angle + angleInsideCar, halfDiagonalInsideCar);
            vertices[4] = Geometry.tempVector.x;
            vertices[5] = Geometry.tempVector.y;
            Geometry.moveToAngle(carState2.x, carState3.y, way1Angle - angleInsideCar, halfDiagonalInsideCar);
            vertices[6] = Geometry.tempVector.x;
            vertices[7] = Geometry.tempVector.y;
            rightNowPolygon.setVertices(vertices);
        }
    }
}
