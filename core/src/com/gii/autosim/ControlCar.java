package com.gii.autosim;

/**
 * Created by Timur_hnimdvi on 23-Dec-16.
 */
public class ControlCar {
    public static void changeSpeedAndDirection(Car car, float timeInterval) {
        car.currentState.acceleration = car.maxAcceleration;
        float newRotation = angle(car.currentState.x,car.currentState.y,car.goalStates.get(0).x,car.goalStates.get(0).y);
        float dRotation = newRotation - car.currentState.rotation;

        float goalSpeed = 0;
        if (car.goalStates.size() > 1) {
            float turnAngle = angle(car.goalStates.get(0).x,car.goalStates.get(0).y,car.goalStates.get(1).x,car.goalStates.get(1).y);
            goalSpeed = maxSpeed(turnAngle - newRotation);
        }

        float rotationSpeed = 45 * timeInterval /1000; //grad / s
        if (dRotation > 180)
            dRotation -= 360;
        if (dRotation < -180)
            dRotation += 360;
        float sign = 1;
        if (dRotation < 0)
            sign = -1;

        float maxSpeed = maxSpeed(dRotation);
        float distanceToGoal = (distance(car.currentState.x,car.currentState.y,car.goalStates.get(0).x,car.goalStates.get(0).y));

        if (car.currentState.speed > maxSpeed)
            //car.currentState.acceleration = Math.min(Math.max (-(car.currentState.speed - maxSpeed)/2, car.minAcceleration),car.comfortDeceleration);
            car.currentState.acceleration = car.minAcceleration;
        else {
            car.currentState.rotation += sign * Math.min(Math.abs(dRotation), rotationSpeed);
            if (car.currentState.speed > goalSpeed) {
                float breakDistance = (car.currentState.speed - goalSpeed) * goalSpeed / (car.comfortDeceleration);
                if (distanceToGoal < breakDistance) {
                    car.currentState.acceleration = car.minAcceleration;
                }
            }
        }

        if (distanceToGoal < 2) {
            car.goalStates.remove(0);
        }
    }
    public static float maxSpeed(float dRotation) {
        if (Math.abs(dRotation) < 20)
            return 60;
        if (Math.abs(dRotation) < 80)
            return (60 - Math.abs(dRotation)/2);
        if (Math.abs(dRotation) < 100)
            return 20;
        return  5;
    }
    public static float angle(float x1, float y1, float x2, float y2) {
        float newRotation = (float)Math.acos((x2 - x1) /
                distance(x1,y1,x2,y2));
        if (y2 < y1)
            newRotation = - newRotation;
        return  (float)Math.toDegrees(newRotation);
    }
    public static float distance(float x1, float y1, float x2, float y2) {
        return (float)Math.sqrt((x2 - x1) * (x2 - x1) +
                (y2 - y1) * (y2 - y1));
    }
}
