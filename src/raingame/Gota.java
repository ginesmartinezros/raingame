package raingame;

import java.time.LocalDateTime;
import java.time.Duration;

public class Gota {

    public LocalDateTime tiempoCreacion;
    public final double ALTURA_INICIAL=0.0;
    public final double VELOCIDAD =100.0;
    public double posicionX;

    public Gota(int posicionXinicial){

        this.posicionX=posicionXinicial;
        this.tiempoCreacion=LocalDateTime.now();

    }
    public double getAltura(){ //La altura dependerá del tiempo que lleve cayendo la gota
        LocalDateTime tiempoActual = LocalDateTime.now();
        Duration tiempoDeVida = Duration.between(this.tiempoCreacion, tiempoActual);
        double tiempoDeVidaSegundos = tiempoDeVida.toMillis()/1000.0;
        double altura = this.ALTURA_INICIAL + this.VELOCIDAD*tiempoDeVidaSegundos;
        return altura;

    }
    public double getPosicionX(){
        return posicionX;
    }
}
