package raingame;

public class Personaje {
    public final double XMAX  = Game.WIDTH;
    public final double XMIN = 0.0;
    public double posicionX;
    public final double VELOCIDAD_PERSONAJE =10.0;
    public final int posicionY =  Game.HEIGHT-5* Game.ALTO_PERSONAJE;

    public Personaje(){
        posicionX=Game.WIDTH/2.0;
    }
    public void moveLeft(){
        posicionX= posicionX-this.VELOCIDAD_PERSONAJE;
        if (posicionX<XMIN){
            posicionX= XMIN;
        }
    }
    public void moveRight(){
        posicionX= posicionX+this.VELOCIDAD_PERSONAJE;
        if (posicionX>XMAX){
         posicionX= XMAX;
        }
    }
    
}
