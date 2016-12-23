package com.gii.autosim;

/**
 * Created by Timur_hnimdvi on 23-Dec-16.
 */
public class ControlCar {
    public static void changeSpeedAndDirection(Car car, float timeInterval) {
        car.currentState.acceleration = car.maxAcceleration;
        float newRotation = (float)Math.acos((car.goalState.x - car.currentState.x) /
                Math.sqrt((car.goalState.x - car.currentState.x) * (car.goalState.x - car.currentState.x) +
                        (car.goalState.y - car.currentState.y) * (car.goalState.y - car.currentState.y)));
        if (car.goalState.y < car.currentState.y)
            newRotation = - newRotation;
        newRotation = (float)Math.toDegrees(newRotation);
        float dRotation = newRotation - car.currentState.rotation;
        float rotationSpeed = 45 *timeInterval /1000; //grad / s
        if (dRotation > 180)
            dRotation -= 360;
        if (dRotation < -180)
            dRotation += 360;
        float sign = 1;
        if (dRotation < 0)
            sign = -1;

        float maxSpeed;
        if (Math.abs(dRotation) < 20) {
            maxSpeed = 60;
        } else if (Math.abs(dRotation) < 80) {
            maxSpeed = 60 - Math.abs(dRotation)/2;
        } else if (Math.abs(dRotation) < 100) {
            maxSpeed = 20;
        } else
            maxSpeed = 5;

        if (car.currentState.speed > maxSpeed)
            car.currentState.acceleration = Math.min(Math.max (-(car.currentState.speed - maxSpeed)/2, car.minAcceleration),-10);
        else
            car.currentState.rotation += sign * Math.min(Math.abs(dRotation),rotationSpeed);
//        car.goalState = null;
    }
}
