package com.gii.autosim;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Timur_hnimdvi on 22-Dec-16.
 */
public class AutoSim {
    public static String TAG = "AutoSim.java";
    public static ArrayList<Car> cars = new ArrayList<Car>();
    public static float timeInterval = 1000/40;
    public static Vector3 target = new Vector3(-1,-1,0);

    public static void generateCars() {
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
           cars.add(new Car(rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(180)));
        }
    }

    public static void update() {
//        Gdx.app.log(TAG, "update");
        for (Car car : cars) {
            car.update(timeInterval);
        }
    }

    public static void goTo(Vector3 target) {
        for (Car car : cars) {
            CarState goal = new CarState();
            goal.x = target.x;
            goal.y = target.y;
            goal.speed = 0;
            goal.rotation = 0;
            goal.acceleration = 0;
            car.goalStates.add(goal);
        }
    }
}
