package com.example.a41709606.juegololazo;

         import android.support.v7.app.AppCompatActivity;
         import android.os.Bundle;
         import android.view.WindowManager;
         import org.cocos2d.opengl.CCGLSurfaceView;


        public class MainActivity extends AppCompatActivity {


            CCGLSurfaceView VistaPrincipal;


                @Override
       protected void onCreate(Bundle savedInstanceState) {
               super.onCreate(savedInstanceState);
                 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                VistaPrincipal = new CCGLSurfaceView(this);
               setContentView(VistaPrincipal);
           }
      @Override
         protected void onStart() {
             super.onStart();
               clsJuego mijuego;
              mijuego= new clsJuego(VistaPrincipal);
               mijuego.ComenzarJuego();
      }
        }
