package com.gii.autosim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Timur_hnimdvi on 23-Dec-16.
 */
public class ControlCar {
    static Intersector intersector = new Intersector();
    private static String TAG = "ControlCar.java";

    public static void changeSpeedAndDirection(Car car, float timeInterval) {


        //intersector.intersectPolygons()
        //intersectPolygons();

        car.currentState.acceleration = car.maxAcceleration;
        float newRotation = Geometry.angle(car.currentState.x,car.currentState.y,car.goalStates.get(0).x,car.goalStates.get(0).y);
        float dRotation = newRotation - car.currentState.rotation;
        float distanceToGoal = (Geometry.distance(car.currentState.x,car.currentState.y,car.goalStates.get(0).x,car.goalStates.get(0).y));

        boolean weNeedToBreak = false;

        float totalDistance = distanceToGoal;
        Vector3 prevPoint = new Vector3(car.currentState.x,car.currentState.y,0);
        for (int i = 0; i < car.goalStates.size() - 1; i++) {
            float currAngle = Geometry.angle(prevPoint.x, prevPoint.y, car.goalStates.get(i).x,car.goalStates.get(i).y);
            float nextAngle = Geometry.angle(car.goalStates.get(i).x,car.goalStates.get(i).y, car.goalStates.get(i+1).x,car.goalStates.get(i+1).y);
            float goalSpeed1 = Geometry.maxSpeedOnAngle(nextAngle - currAngle);
            if (car.currentState.speed > goalSpeed1) {
                float breakDistance = (car.currentState.speed - goalSpeed1) * goalSpeed1 / (car.comfortDeceleration) * 2;
                //Gdx.app.log(TAG, "speed: " + car.currentState.speed + ", Break distance = " + breakDistance + ", total distance = " + totalDistance);
                if (totalDistance < breakDistance) {
                    weNeedToBreak = true;
                    //Gdx.app.log(TAG, "we need to break! i = " + i);
                }
            }
            totalDistance += Geometry.distance(car.goalStates.get(i).x,car.goalStates.get(i).y, car.goalStates.get(i+1).x,car.goalStates.get(i+1).y);
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
        float maxSpeedForCurrentTurn = Geometry.maxSpeedOnAngle(dRotation);

        boolean conflict = false;


        car.predictTwoSecondsToPredictPolygon();

        for (Car car1 : AutoSim.cars) {
            if (car1 != car) {
                if (intersector.intersectPolygons(car.predictPolygon,car1.predictPolygon,Geometry.tempPolygon)) {
                    float car1angle = Geometry.angle(car.currentState.x, car.currentState.y, car1.currentState.x, car1.currentState.y);
                    car1angle = Geometry.normalizeAngle(car1angle - car.currentState.rotation);
                    if (car1angle < 5 && car1angle > -85)
                        conflict = true;
                    Gdx.app.log(TAG, "car1Angle: " + car1angle + " conflict: " + conflict);
                }
                if (intersector.intersectPolygons(car.rightNowPolygon,car1.rightNowPolygon,Geometry.tempPolygon)) {
                    //float car1angle = Geometry.angle(car.currentState.x, car.currentState.y, car1.currentState.x, car1.currentState.y);
                    //car1angle = Geometry.normalizeAngle(car1angle - car.currentState.rotation);
                    //if (car1angle < 5 && car1angle > -85)
                    //    conflict = true;
                    //Gdx.app.log(TAG, "Force STOP");
                    //car.currentState.speed = 0;
                }
            }
        }

        if (car.currentState.speed > maxSpeedForCurrentTurn || conflict)
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

}
