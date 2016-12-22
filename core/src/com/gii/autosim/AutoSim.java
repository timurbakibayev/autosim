package com.gii.autosim;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

/**
 * Created by Timur_hnimdvi on 22-Dec-16.
 */
public class AutoSim {
    public static String TAG = "AutoSim.java";
    public static ArrayList<Car> cars = new ArrayList<Car>();
    public static float timeInterval = 1000/40;

    public static void generateCars() {
        for (int i = 0; i < 5; i++) {
            cars.add(new Car(i*5, i*5));
        }
    }

    public static void update() {
        Gdx.app.log(TAG, "update");
        for (Car car : cars) {
            car.update(timeInterval);
        }
    }
}
