package com.example.a41709606.juegololazo;

/**
 * Created by 41709606 on 20/9/2016.
 */

        import android.util.Log;
        import android.view.MotionEvent;

        import org.cocos2d.actions.interval.MoveTo;
        import org.cocos2d.actions.interval.RotateTo;
        import org.cocos2d.actions.interval.ScaleBy;
        import org.cocos2d.layers.Layer;
        import org.cocos2d.nodes.Director;
        import org.cocos2d.nodes.Label;
        import org.cocos2d.nodes.Scene;
        import org.cocos2d.nodes.Sprite;
        import org.cocos2d.opengl.CCGLSurfaceView;
        import org.cocos2d.types.CCPoint;
        import org.cocos2d.types.CCSize;

        import java.util.ArrayList;
        import java.util.Random;
        import java.util.Timer;
        import java.util.TimerTask;


public class clsJuego {
    CCGLSurfaceView _VistaDelJuego;
    CCSize PantallaDelDispositivo;
    Sprite constructorjugador, ImagenFondo, ladrillo;
    Label lblTituloJuego;
    ArrayList<Sprite> arrLadrillos;

    public clsJuego(CCGLSurfaceView VistaDelJuego) {
        Log.d("Bob", "Comienza el ladrillo de la clase");
        _VistaDelJuego = VistaDelJuego;
        arrLadrillos = new ArrayList<>();
    }

    public void ComenzarJuego() {
        Log.d("Comenzar", "Comienza el juego");
        Director.sharedDirector().attachInView(_VistaDelJuego);


        PantallaDelDispositivo = Director.sharedDirector().displaySize();
        Log.d("Comenzar", "Pantalla del dispositivo - ancho:" + PantallaDelDispositivo.width + " - alto: " + PantallaDelDispositivo.height);
        Director.sharedDirector().runWithScene(EscenaDelJuego());


    }

    private Scene EscenaDelJuego() {
        Log.d("EscenaDelJuego", "Comienza el armado de escena");


        Log.d("EscenaDelJuego", "Declaro e instancio la escena del juego en si");
        Scene EscenaADevolver;
        EscenaADevolver = Scene.node();

        Log.d("EscenaDelJuego", "Declaro e instancio la capa que va a contener la imagen de fondo");
        CapaDeFondo MiCapaFondo = new CapaDeFondo();
        CapaFrente MiCapaFrente = new CapaFrente();


        Log.d("EscenaDelJuego", "Declaro e instancio la capa que va a contener el jugador y a los enemigos");
        EscenaADevolver.addChild(MiCapaFondo, -10);
        EscenaADevolver.addChild(MiCapaFrente, 10);


        Log.d("EscenaDelJuego", "Devuelvo la escena ya armada");
        return EscenaADevolver;
    }


    class CapaDeFondo extends Layer {
        public CapaDeFondo() {
            Log.d("CapaDelFondo", "Comienza el ladrillo de la capa del fondo");

            Log.d("CapaDelFondo", "Pongo la imagen del fondo del juego");
            PonerImagenFondo();
        }

        private void PonerImagenFondo() {
            Log.d("PonerImagenFondo", "Comienzo a poner la imagen del fondo");

            Log.d("PonerImagenFondo", "Instancio el sprite");
            ImagenFondo = Sprite.sprite("fondo1.jpg");

            Log.d("PonerImagenFondo", "La ubico en el centro de la pantalla");
            ImagenFondo.setPosition(PantallaDelDispositivo.width / 2, PantallaDelDispositivo.height / 2);

            Log.d("PonerImagenFondo", "Agrando la imagen al doble de su tamaño actual");
            ImagenFondo.runAction(ScaleBy.action(0.01f, 5.0f, 5.0f));

            Log.d("PonerImagenFondo", "Lo agrego a la capa");
            super.addChild(ImagenFondo);
        }
    }


    private class CapaFrente extends Layer {
        public CapaFrente() {
            Log.d("CapaDelFrente", "Comienza el ladrillo de la capa del frente");

            Log.d("CapaDelFrente", "Pongo el jugador en su posicion inicial");
            PonerLadrilloPosicionInicial();
            PonerTitulo();

            TimerTask TareaPonerEnemigos;
            TareaPonerEnemigos = new TimerTask() {
                @Override
                public void run() {
                    PonerUnLadrillo();
                }
            };
            Timer RelojEnemigos;
            RelojEnemigos = new Timer();
            RelojEnemigos.schedule(TareaPonerEnemigos, 0, 1000);

            TimerTask TareaVerificarImpactos;
            TareaVerificarImpactos = new TimerTask() {
                @Override
                public void run() {
                    DetectarColisiones();
                }
            };
            Timer RelojVerificarImpactos;
            RelojVerificarImpactos = new Timer();
            RelojVerificarImpactos.schedule(TareaVerificarImpactos, 0, 100);

            Log.d("ConstructorCapaDelJuego", "Habilito el control del Touch");
            this.setIsTouchEnabled(true);
        }


        public boolean ccTouchesBegan(MotionEvent event) {
            Log.d("Toque comienza", "X: "+event.getX()+" - Y: "+event.getY());

            return true;
        }



        public boolean ccTouchesEnded(MotionEvent event) {
            Log.d("Toque termina", "X: "+event.getX()+" - Y: "+event.getY());

            return true;
        }


        public boolean ccTouchesMoved(MotionEvent event) {
            Log.d("Toque mueve", "X: "+event.getX()+" - Y: "+event.getY());

            MoverNaveJugador(event.getX(), event.getY());

            return true;
        }

        void MoverNaveJugador(float DestinoX,float DestinoY) {
            constructorjugador.setPosition(DestinoX,DestinoY);
        }



        void MoverNaveJugador(float DestinoX, Float DestinoY) {

            float MovimientoHorizontal, MovimientoVertical, SuavizadorDeMovimiento;
            MovimientoHorizontal = DestinoX - PantallaDelDispositivo.getWidth()/2;
            MovimientoVertical = DestinoY - PantallaDelDispositivo.getHeight()/2;

            SuavizadorDeMovimiento=20;
            MovimientoHorizontal = MovimientoHorizontal/SuavizadorDeMovimiento;
            MovimientoVertical = MovimientoVertical/SuavizadorDeMovimiento;
            Log.d("MoverJugador", "Obtengo la posicion final a la que deberia ir el jugador");
            float PosicionFinalX, PosicionFinalY;
            PosicionFinalX=constructorjugador.getPositionX() + MovimientoHorizontal;
            PosicionFinalY=constructorjugador.getPositionY() + MovimientoVertical;

            constructorjugador.setPosition(constructorjugador.getPositionX() + MovimientoHorizontal, constructorjugador.getPositionY()+MovimientoVertical);



        Log.d("MoverJugador", "Me fijo si no se fue de los limites de la pantalla");
        if(PosicionFinalX<constructorjugador.getWidth()/2) {
            Log.d("MoverJugador", "Se fue por la izquierda");
            PosicionFinalX=PantallaDelDispositivo.getWidth()-constructorjugador.getWidth()/2;
        }

        if (PosicionFinalX>PantallaDelDispositivo.getWidth()-constructorjugador.getWidth()/2) {
            Log.d("MoverJugador", "Se fue por la derecha");
            PosicionFinalX=PantallaDelDispositivo.getWidth()-constructorjugador.getWidth()/2;
            {
                if (PosicionFinalY<constructorjugador.getHeight()/2) {
                    Log.d("MoverJugador", "Se fue por abajo");
                    PosicionFinalY=constructorjugador.getHeight()/2;
                }
                if (PosicionFinalY>PantallaDelDispositivo.getHeight()-constructorjugador.getHeight()/2){
                    Log.d("MoverJugador", "Se fue por arriba");
                    PosicionFinalY=PantallaDelDispositivo.getHeight()-constructorjugador.getHeight()/2;
                }

                Log.d("MoverJugador", "Lo ubico en X: "+PosicionFinalX+" - Y: "+PosicionFinalY);
                constructorjugador.setPosition(PosicionFinalX, PosicionFinalY);

            }
        }
    }
        private void PonerLadrilloPosicionInicial() {
            Log.d("PonerNaveJugador", "Comienzo a poner la nave del jugador en su posicion inicial");
            Log.d("PonerNaveJugador", "Instancio el sprite");
            constructorjugador = Sprite.sprite("constructor.png");

            float PosicionInicialX, PosicionInicialY;
            Log.d("PonerNaveJugador", "Obtengo la mitad del ancho de la pantalla");
            PosicionInicialX = PantallaDelDispositivo.width / 2;

            Log.d("PonerNaveJugador", "Obtengo la mitad de la alatura de la nave");
            PosicionInicialY = constructorjugador.getHeight() / 2;

            Log.d("PonerNaveJugador", "Lo ubico en X: " + PosicionInicialX + " -Y:" + PosicionInicialY);
            constructorjugador.setPosition(PosicionInicialX, PosicionInicialY);

            Log.d("PonerNaveJugador", "Lo agrego a la capa");
            super.addChild(constructorjugador);
        }

        private void PonerTitulo() {

            Log.d("PonerTitulo", "Comienzo a poner el titulo");
            lblTituloJuego = Label.label("Super mega juegazo", "Verdana", 30);

            float AltoDelTitulo;
            AltoDelTitulo = lblTituloJuego.getHeight();

            lblTituloJuego.setPosition(PantallaDelDispositivo.width / 2, PantallaDelDispositivo.height - AltoDelTitulo / 2);
            super.addChild(lblTituloJuego);
        }

        void PonerUnLadrillo() {
            Log.d("PonerEnemigo", "Instancio el sprite del enemigo");
            ladrillo = Sprite.sprite("ladrillo.png");

            Log.d("PonerEnemigo", "Determino la posicion inicial");
            CCPoint PosicionInicial;
            PosicionInicial = new CCPoint();

            Log.d("PonerEnemigo", "La posicion Y es arriba de todo de la pantalla, totalmente afuera");
            float AlturaLadrillo, AnchoLadrillo;
            AlturaLadrillo = ladrillo.getHeight();
            AnchoLadrillo = ladrillo.getWidth();
            PosicionInicial.y = PantallaDelDispositivo.height + AlturaLadrillo / 2;

            Log.d("PonerEnemigo", "Declaro e incializo el generador de azar");
            Random GeneradorDeAzar;
            GeneradorDeAzar = new Random();

            Log.d("PonerEnemigo", "La posicion Y es arriba de todo de la pantalla");
            PosicionInicial.y = PantallaDelDispositivo.height;

            Log.d("PonerEnemigo", "La posicion X es en el centro");
            PosicionInicial.x = GeneradorDeAzar.nextInt((int) PantallaDelDispositivo.width - (int) AnchoLadrillo) + AnchoLadrillo / 2;

            Log.d("PonerUnLadrillo","Agrego el enemigo al array");
            arrLadrillos.add(ladrillo);
            Log.d("PonerUnLadrillo","Hay "+ arrLadrillos.size()+" enemigos en vuelo");

            Log.d("PonerEnemigo", "La ubico en las posiciones que determiné");
            ladrillo.setPosition(PosicionInicial.x, PosicionInicial.y);

            Log.d("PonerEnemigo", "Lo roto para que mire hacia abajo");
            ladrillo.runAction(RotateTo.action(0.01f, 180f));

            Log.d("PonerEnemigo", "Determino la posicion final");
            CCPoint PosicionFinal;
            PosicionFinal = new CCPoint();

            Log.d("PonerEnemigo", "La posicion final X va a ser igual que la inicial");
            PosicionFinal.x = PosicionInicial.x;

            Log.d("PonerEnemigo", "La posicion final Y va a ser abajo de todo");
            PosicionFinal.y = 0;

            Log.d("PonerEnemigo", "La posicion final Y va a ser abajo de todo");
            PosicionFinal.y = -AlturaLadrillo / 2;

            Log.d("PonerEnemigo", "Le digo que se desplace hacia la posicion final, y demore 3 segundos en hacerlo");
            ladrillo.runAction(MoveTo.action(3, PosicionFinal.x, PosicionFinal.y));

            Log.d("PonerEnemigo", "Agrego el sprite a la capa");
            super.addChild(ladrillo);
        }

        boolean InterseccionEntreSprites(Sprite Sprite1, Sprite Sprite2) {

            boolean Devolver;

            Devolver = false;

            int Sprite1Izquierda, Sprite1Derecha, Sprite1Abajo, Sprite1Arriba;

            int Sprite2Izquierda, Sprite2Derecha, Sprite2Abajo, Sprite2Arriba;

            Sprite1Izquierda = (int) (Sprite1.getPositionX() - Sprite1.getWidth() / 2);

            Sprite1Derecha = (int) (Sprite1.getPositionX() + Sprite1.getWidth() / 2);

            Sprite1Abajo = (int) (Sprite1.getPositionY() - Sprite1.getHeight() / 2);

            Sprite1Arriba = (int) (Sprite1.getPositionY() + Sprite1.getHeight() / 2);

            Sprite2Izquierda = (int) (Sprite2.getPositionX() - Sprite2.getWidth() / 2);

            Sprite2Derecha = (int) (Sprite2.getPositionX() + Sprite2.getWidth() / 2);

            Sprite2Abajo = (int) (Sprite2.getPositionY() - Sprite2.getHeight() / 2);

            Sprite2Arriba = (int) (Sprite2.getPositionY() + Sprite2.getHeight() / 2);

            Log.d("Interseccion", "Sp1 -Izq: " + Sprite1Izquierda + " -Der:" + Sprite1Derecha + "-Aba:" + Sprite1Abajo + "-Arr" + Sprite1Arriba);
            Log.d("Interseccion", "Sp2-Izq:" + Sprite2Izquierda + "-Der:" + Sprite2Derecha + "-Aba:" + Sprite2Abajo + "-Arr:" + Sprite2Arriba);


//Borde izq y borde inf de Sprite 1 está dentro de Sprite 2

            if (EstaEntre(Sprite1Izquierda, Sprite2Izquierda, Sprite2Derecha) && EstaEntre(Sprite1Abajo, Sprite2Abajo, Sprite2Arriba)) {
                Log.d("Interseccion", "1");
                Devolver = true;
            }


//Borde izq y borde sup de Sprite 1 está dentro de Sprite 2

            if (EstaEntre(Sprite1Izquierda, Sprite2Izquierda, Sprite2Derecha) && EstaEntre(Sprite1Arriba, Sprite2Abajo, Sprite2Arriba)) {
                Log.d("Interseccion", "2");
                Devolver = true;

            }

//Borde der y borde sup de Sprite 1 está dentro de Sprite 2

            if (EstaEntre(Sprite1Derecha, Sprite2Izquierda, Sprite2Derecha) && EstaEntre(Sprite1Arriba, Sprite2Abajo, Sprite2Arriba)) {
                Log.d("Interseccion", "3");
                Devolver = true;
            }

//Borde der y borde inf de Sprite 1 está dentro de Sprite 2

            if (EstaEntre(Sprite1Derecha, Sprite2Izquierda, Sprite2Derecha) && EstaEntre(Sprite1Abajo, Sprite2Abajo, Sprite2Arriba)) {
                Log.d("Interseccion", "4");
                Devolver = true;

            }

//Borde izq y borde inf de Sprite 2 está dentro de Sprite 1

            if (EstaEntre(Sprite2Izquierda, Sprite1Izquierda, Sprite1Derecha) && EstaEntre(Sprite2Abajo, Sprite1Abajo, Sprite1Arriba)) {
                Log.d("Interseccion", "5");
                Devolver = true;

            }

//Borde izq y borde sup de Sprite 1 está dentro de Sprite 1

            if (EstaEntre(Sprite2Izquierda, Sprite1Izquierda, Sprite1Derecha) && EstaEntre(Sprite2Arriba, Sprite1Abajo, Sprite1Arriba)) {
                Log.d("Interseccion", "6");
                Devolver = true;

            }

//Borde der y borde sup de Sprite 2 está dentro de Sprite 1

            if (EstaEntre(Sprite2Derecha, Sprite1Izquierda, Sprite1Derecha) && EstaEntre(Sprite2Arriba, Sprite1Abajo, Sprite1Arriba)) {
                Log.d("Interseccion", "7");
                Devolver = true;

            }

//Borde der y borde inf de Sprite 2 está dentro de Sprite 1

            if (EstaEntre(Sprite2Derecha, Sprite1Izquierda, Sprite1Derecha) && EstaEntre(Sprite2Abajo, Sprite1Abajo, Sprite1Arriba)) {
                Log.d("Interseccion", "8");
                Devolver = true;
            }
            return Devolver;
        }

        boolean EstaEntre(int NumeroAComparar, int NumeroMenor, int NumeroMayor) {
            boolean Devolver;
            Log.d("EstaEntre", "Me los mandaron invertidos, los ordeno");
            int Auxiliar;
            Auxiliar = NumeroMayor;
            NumeroMayor = NumeroMenor;
            NumeroMenor = Auxiliar;


            if (NumeroAComparar >= NumeroMenor && NumeroAComparar >= NumeroMayor) {
                Log.d("EstaEntre", "Esta entre");
                Devolver = true;
            } else {
                Log.d("EstaEntre", "No esta entre");
                Devolver = false;
            }
            return Devolver;
        }
        void DetectarColisiones() {
            Log.d("DetectarColision", "Voy a verificar los"+ arrLadrillos.size()+"ladrillos");
            boolean HuboAlgunaColision;
            HuboAlgunaColision=false;
            for(Sprite UnEnemigoAVerificar: arrLadrillos) {
                if(InterseccionEntreSprites(constructorjugador, UnEnemigoAVerificar)) {
                    Log.d("DetectarColision", "BOOOOOOOM!!!!");
                    HuboAlgunaColision=true;
                }
            }
            if(HuboAlgunaColision==true){
                Log.d("DetectarColision","Hubo colision");
            } else {
                Log.d("DetectarColision","No Hubo COLISION");
            }
        }
    }
}


