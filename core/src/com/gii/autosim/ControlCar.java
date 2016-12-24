package com.gii.autosim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Timur_hnimdvi on 23-Dec-16.
 */
public class ControlCar {
    private static String TAG = "ControlCar.java";
    public static void changeSpeedAndDirection(Car car, float timeInterval) {
        car.currentState.acceleration = car.maxAcceleration;
        float newRotation = angle(car.currentState.x,car.currentState.y,car.goalStates.get(0).x,car.goalStates.get(0).y);
        float dRotation = newRotation - car.currentState.rotation;
        float distanceToGoal = (distance(car.currentState.x,car.currentState.y,car.goalStates.get(0).x,car.goalStates.get(0).y));

        boolean weNeedToBreak = false;

        float totalDistance = distanceToGoal;
        Vector3 prevPoint = new Vector3(car.currentState.x,car.currentState.y,0);
        for (int i = 0; i < car.goalStates.size() - 1; i++) {
            float currAngle = angle(prevPoint.x, prevPoint.y, car.goalStates.get(i).x,car.goalStates.get(i).y);
            float nextAngle = angle(car.goalStates.get(i).x,car.goalStates.get(i).y, car.goalStates.get(i+1).x,car.goalStates.get(i+1).y);
            float goalSpeed1 = maxSpeedOnAngle(nextAngle - currAngle);
            if (car.currentState.speed > goalSpeed1) {
                float breakDistance = (car.currentState.speed - goalSpeed1) * goalSpeed1 / (car.comfortDeceleration) * 2;
                Gdx.app.log(TAG, "speed: " + car.currentState.speed + ", Break distance = " + breakDistance + ", total distance = " + totalDistance);
                if (totalDistance < breakDistance) {
                    weNeedToBreak = true;
                    Gdx.app.log(TAG, "we need to break! i = " + i);
                }
            }
            totalDistance += distance(car.goalStates.get(i).x,car.goalStates.get(i).y, car.goalStates.get(i+1).x,car.goalStates.get(i+1).y);
            prevPoint.set(car.goalStates.get(i).x,car.goalStates.get(i).y,0);
            if (totalDistance > 500)
                break;
        }

        float rotationSpeed = 60 * timeInterval /1000; //grad / s
        float sign = 1;
        while (dRotation < -180)
            dRotation += 360;
        while (dRotation > 180)
            dRotation -= 360;
        if (dRotation < 0)
            sign = -1;
        float maxSpeedForCurrentTurn = maxSpeedOnAngle(dRotation);

        if (car.currentState.speed > maxSpeedForCurrentTurn)
            //car.currentState.acceleration = Math.min(Math.max (-(car.currentState.speed - maxSpeed)/2, car.minAcceleration),car.comfortDeceleration);
            car.currentState.acceleration = car.minAcceleration;
        else {
            car.currentState.rotation += sign * Math.min(Math.abs(dRotation), rotationSpeed);
            if (weNeedToBreak) {
                car.currentState.acceleration = car.minAcceleration;
            }
        }

        if (distanceToGoal < 2) {
            car.goalStates.remove(0);
        }
    }
    public static float maxSpeedOnAngle(float dRotation) {
        while (dRotation < -180)
            dRotation += 360;
        while (dRotation > 180)
            dRotation -= 360;
        if (Math.abs(dRotation) < 10)
            return 60;
        if (Math.abs(dRotation) < 20)
            return 50;
        if (Math.abs(dRotation) < 80)
            return (50 - Math.abs(dRotation)/2f);
        if (Math.abs(dRotation) < 100)
            return 5;
        return  2;
    }
    public static float angle(float x1, float y1, float x2, float y2) {
        float newRotation = (float)Math.acos((x2 - x1) /
                distance(x1,y1,x2,y2));
        if (y2 < y1)
            newRotation = - newRotation;

        float angle = (float)Math.toDegrees(newRotation);
        while (angle < -180)
            angle += 360;
        while (angle > 180)
            angle -= 360;
        return angle;
    }
    public static float distance(float x1, float y1, float x2, float y2) {
        return (float)Math.sqrt((x2 - x1) * (x2 - x1) +
                (y2 - y1) * (y2 - y1));
    }
}
