package fi.tamk.tuplaus.peli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Bilbo on 20.2.2017.
 */

public class CoinActor extends Actor {

    private Texture headsTexture;
    private Texture tailsTexture;
    private Texture texture;

    public void setTexture(boolean b){
        if(b){
            this.texture = headsTexture;
        }else{
            this.texture = tailsTexture;
        }
    }

    public CoinActor(){
        headsTexture = new Texture(Gdx.files.internal("kruuna.png"));
        tailsTexture = new Texture(Gdx.files.internal("klaava.png"));
        texture = tailsTexture;
        this.setWidth(100);
        this.setHeight(100);
        this.setX(480/2 - this.getWidth()/2);
        this.setY(300);
    }

    public void draw(Batch batch){
          batch.draw(texture,
                  this.getX(),this.getY(),
                  this.getOriginX(),
                  this.getOriginY(),
                  this.getWidth(),
                  this.getHeight(),
                  this.getScaleX(),
                  this.getScaleY(),
                  this.getRotation(),0,0,
                  texture.getWidth(), texture.getHeight(), false, false);
    }

}
