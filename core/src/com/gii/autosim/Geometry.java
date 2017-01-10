package com.gii.autosim;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Timur_hnimdvi on 09-Jan-17.
 */
public class Geometry {
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
    public static float normalizeAngle(float angle) {
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
    public static Vector3 tempVector = new Vector3(0,0,0);
    public static Polygon tempPolygon = new Polygon();
    public static void moveToAngle(float inPointX, float inPointY,float degree,float distance) {
        double radians = Math.toRadians((degree));
        tempVector.set(inPointX + (float)Math.cos(radians)*distance,inPointY + (float)Math.sin(radians)*distance,0);
    }
}
