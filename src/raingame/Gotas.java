package raingame;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Gotas {

    public List<Gota> gotas;
    public LocalDateTime creacionPrimeraGota;
    public double frecuenciaGotas= 0.01; //inicializamos la frecuencia con la que caen las gotas: segundos entre la caida de cada gota. cuanto menor, mas gotas

    public Gotas(){
        Random random = new Random();
        int posicionInicial = random.nextInt(Game.WIDTH);//constructor que incluye una sola gota
        this.gotas = new ArrayList<>();
        Gota gota = new Gota(  posicionInicial);
        gotas.add(gota);

    }

    public boolean isPossibleToAddGota(){ // es posible añadir una gota si ha pasado más tiempo que frecuenciaGotas desde que se creo la ultima

        Gota ultimaGota = gotas.get(this.gotas.size()-1);
        LocalDateTime tiempoUltimaGota = ultimaGota.tiempoCreacion;
        LocalDateTime tiempoActual = LocalDateTime.now();
        Duration tiempoDesdeUltimaGota =  Duration.between(tiempoUltimaGota,tiempoActual);
        double segundosDesdeUltimaGota = tiempoDesdeUltimaGota.toMillis()/1000.0;
        if (segundosDesdeUltimaGota > this.frecuenciaGotas){
            return true;
        } else{
            return false;
        }
    }

    public void addGota(){ // si es posible, sea añade
        if (isPossibleToAddGota()){
            Random random = new Random();
            int posicionInicial = random.nextInt(Game.WIDTH);
            Gota gota = new Gota(  posicionInicial);
            gotas.add(gota);

        }
    }

    public boolean ChoqueGotaPersonaje( Personaje personaje){ //comprobamos si la gota choca contra el personaje. Devolvemos true si ha chocado, false si no

        double posicionPersonaje = personaje.posicionX   ;
        boolean sigueVivo = true;

        for (int i=0; i < gotas.size(); i++){

            Gota gota = gotas.get(i);
            double posicionGota = gota.getPosicionX();
            double alturaGota = gota.getAltura();
            double distanciaX = Math.abs(posicionGota-posicionPersonaje);
            double distanciaY = Math.abs(personaje.posicionY-alturaGota);
            if (( distanciaX < Game.ANCHO_PERSONAJE) && (distanciaY < Game.ALTO_PERSONAJE) ){
                sigueVivo= false;
            } 
        }

        return !sigueVivo;
    }

    public void updateDificultad(){ // para traer el valor del slider
        frecuenciaGotas= Game.gameDifficulty;
    }
}
