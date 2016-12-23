package com.gii.autosim;

/**
 * Created by Timur_hnimdvi on 23-Dec-16.
 */
public class ControlCar {
    public static void changeSpeedAndDirection(Car car) {
        double phiRad = Math.acos((car.currentState.x * car.goalState.x + car.currentState.y * car.goalState.y)/
                (Math.sqrt(car.currentState.x * car.currentState.x + car.currentState.y * car.currentState.y) *
                        Math.sqrt(car.goalState.x * car.goalState.x + car.goalState.y * car.goalState.y)
                ));
        float phiGrad = (float)(phiRad * 180 / Math.PI);
        car.currentState.rotation += phiGrad;
        car.goalState = null;
    }
}
