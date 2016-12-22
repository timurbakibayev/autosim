package com.gii.autosim;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Timur_hnimdvi on 22-Dec-16.
 */
public class Car {
    Rectangle position = new Rectangle();
    float x = 0; //of center
    float y = 0;
    float width = 4.3f;
    float height = 1.8f;
    float rotation = 0;
    public Car(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public void draw(SpriteBatch batch, Texture texture) {
        position.set(x - width/2, y- height/2, width, height);
        batch.draw(texture, position.x, position.y, width/2, height/2,width,height,1,1,rotation,0,0,100,52,false,false);
    }
    public void update(float timeInterval) {
        x = x + 3/timeInterval; //10 = 36kmh
    }
}
