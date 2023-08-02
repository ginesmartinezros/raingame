package raingame;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class Game extends JPanel implements KeyListener {
    // Atributos graficos
    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;
    public static final int ANCHO_PERSONAJE =10;
    public static final int ALTO_PERSONAJE =20;
    public static final int RADIO_GOTA =10;
    public JButton starButton;
    public JButton restartButton;
    public static BufferedImage pngGota;
    public static BufferedImage pngPersonaje;
    public static BufferedImage pngFondo;
    // Atributos sobre la dinamica del juego
    Personaje personaje ;
    Gotas gotas ;
    public static GameState  gameState ;
    public static double gameDifficulty=0.01 ;//inicializamos frecuencia de aparición de burbujas
    public int sliderValue=50; //inicializamos el valor del slider

    public enum GameState {// Estado del juego
        START,
        PLAYING,
        GAME_OVER}

    public Game(){
    
    gameState = GameState.START; // nada mas iniciar un constructor, estamos en START


     //Atributos del frame
    JFrame frame = new JFrame("Rain Game");
    frame.setSize(WIDTH,HEIGHT);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(this);
    frame.addKeyListener(this);
    frame.setVisible(true);
    addKeyListener(this);
    setFocusable(true);



    //Atributos del slider
    JSlider slider = new JSlider(JSlider.HORIZONTAL, 1,100, sliderValue);
    slider.setBounds(WIDTH-200, HEIGHT-80, 150, 20);
    slider.addChangeListener(new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider) e.getSource();
            sliderValue = source.getValue();
            gameDifficulty= 0.5/ sliderValue;// relacion entre el valor del slider y la frecuencia de aparicion de burubjas
        }
    });
    add(slider);

    //Atributos del boton de inicio
    starButton  = new JButton("Comienzo!");
    starButton.setBounds(WIDTH / 2 - 50, HEIGHT / 2 - 20, 100, 40);
    starButton.addActionListener(e ->{ // al apretar el boton pasamos a estado PLAYING, quitamos botones y slider y pasamos foco a los controles del juego
        gameState = GameState.PLAYING;
        remove(starButton);
        remove(slider);
        requestFocusInWindow(); 
    });
    add(starButton);



    // Atributos del boton de reinicio
    restartButton  = new JButton("Game Over. Try again!");
    restartButton.setBounds(WIDTH / 2 - 50, HEIGHT / 2 + 20, 100, 40);
    restartButton.setVisible(false); // por defecto quitado. Al perder partida aparece
    restartButton.addActionListener(e ->{
        gameState = GameState.START; // al apretar volvemos al inicio, inicializamos el juego
        personaje = new Personaje();
        gotas =new Gotas(); 
        restartButton.setVisible(false); // ocultamos el boton y hacemos aparecer slider y boton de inicio
        add(starButton);
        add(slider);
    });
    add(restartButton,BorderLayout.CENTER);

    // Comienzo

    init();


    while(true){
        gotas.updateDificultad();
        update();
        repaint();
        try {
            Thread.sleep(10);
        }
        catch (InterruptedException e){
             e.printStackTrace();
            }
        }

    }

    public void init(){ // inicializamos los objetos del juego y leemos los pngs
        personaje = new Personaje();
        gotas = new Gotas();
        Game.importarPng();
    }

    public void update(){
        if (gameState==GameState.PLAYING){ // si estamos jugando, añade gotas cuando sea posible
            gotas.addGota();
            boolean gameEnded = gotas.ChoqueGotaPersonaje(personaje); // comprueba si hemos perdido
            if (gameEnded){
                System.out.println("Game Over"); // si hemos perdido, vamos a la pantalla GAME_OVER
                gameState=GameState.GAME_OVER;
            }
        }
    }

    public static void importarPng() { // leemos los pngs de fondo, personaje y gotas
        try {
            URL gotaUrl = Game.class.getResource("res/gota_gines.png");
            pngGota = ImageIO.read(gotaUrl);
            URL personajeUrl = Game.class.getResource("res/ajolote_gines.png");
            pngPersonaje = ImageIO.read(personajeUrl);
            URL fondoUrl = Game.class.getResource("res/fondo_gines.png");

            pngFondo = ImageIO.read(fondoUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        @Override
    public void keyPressed(KeyEvent e) { // logica de los controles, con tecla izquierda y derecha
        if (gameState==GameState.PLAYING){
            if (e.getKeyCode() == KeyEvent.VK_RIGHT ) {
                personaje.moveRight();
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT ) {
                personaje.moveLeft();

            }
        }
    }

      @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Game.pngFondo, 0, 0, Game.WIDTH,Game.HEIGHT,null); // dibujamos el fondo

        if (gameState == GameState.START) { // En la pantalla de inicio ponemos unos creditos
            g.setColor(java.awt.Color.WHITE);
            g.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
            String creditText = "Dev: Gines Martinez. Github /ginesmartinezros";
            int textWidth = g.getFontMetrics().stringWidth(creditText);
            int x = (Game.WIDTH - textWidth) / 2;
            int y = Game.HEIGHT  - 100;
            g.drawString(creditText, x, y);

            String creditText2 = "Design: Maria Martinez";
            int textWidth2 = g.getFontMetrics().stringWidth(creditText2);
            int x2 = (Game.WIDTH - textWidth2) / 2;
            int y2 = Game.HEIGHT  - 80;
            g.drawString(creditText2, x2, y2);
        }

        if (gameState==GameState.PLAYING){ // si estamos jugando, dibuja el personaje y las gotas
            if ( personaje != null){

            int posicionXint = (int)personaje.posicionX;
            g.drawImage(Game.pngPersonaje, posicionXint,personaje.posicionY, null);

            }
            if (gotas != null){
            for ( int i=0; i < gotas.gotas.size(); i++){
                Gota gota = gotas.gotas.get(i);
                double ballX = gota.posicionX;
                int ballXint = (int) ballX;
                double ballY = gota.getAltura();
                int ballYint = (int) ballY;
                g.drawImage(Game.pngGota, ballXint, ballYint, null);
                }

            }
        } else if (gameState ==GameState.GAME_OVER){ // si hemos perdido, muestra el botón game over
            restartButton.setVisible(true);
        }
    }


    public static void main (String[ ] args ){
        new Game();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}
