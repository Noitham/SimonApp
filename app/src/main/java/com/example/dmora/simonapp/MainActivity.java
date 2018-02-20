package com.example.dmora.simonapp;

import android.annotation.TargetApi;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ArrayList<Integer> tiradesMaquina = new ArrayList<>();
    ArrayList<Integer> tiradesJugador = new ArrayList<>();

    boolean tornJugador, jugando;

    int sonAzul;
    int sonVerde;
    int sonRojo;
    int sonAmarillo;
    int sonError;

    int tirada;

    ImageView ibAzul;
    ImageView ibRojo;
    ImageView ibAmarillo;
    ImageView ibVerde;
    ImageView ibPlay;

    TextView tvPuntos, tvPuntuacio;

    int duracion = 200;

    SoundPool soundPool;

    View.OnClickListener listenerStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jocsimon);

        createSoundPool();

        sonAzul = soundPool.load(this, R.raw.sounds_01, 1);
        sonVerde = soundPool.load(this, R.raw.sounds_02, 1);
        sonRojo = soundPool.load(this, R.raw.sounds_03, 1);
        sonAmarillo = soundPool.load(this, R.raw.sounds_04, 1);
        sonError = soundPool.load(this, R.raw.error, 1);

        tiradesMaquina = new ArrayList<>();
        tiradesJugador = new ArrayList<>();

        ibAzul = (ImageView) findViewById(R.id.ibAzul);
        ibVerde = (ImageView) findViewById(R.id.ibVerde);
        ibRojo = (ImageView) findViewById(R.id.ibRojo);
        ibAmarillo = (ImageView) findViewById(R.id.ibAmarillo);
        ibPlay = (ImageView) findViewById(R.id.ibPlay);

        tvPuntos = (TextView) findViewById(R.id.tvPuntos);
        tvPuntuacio = (TextView) findViewById(R.id.tvPuntuacio);

        listenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        };

        ibAzul.setOnClickListener(listenerColor);
        ibVerde.setOnClickListener(listenerColor);
        ibAmarillo.setOnClickListener(listenerColor);
        ibRojo.setOnClickListener(listenerColor);
        ibPlay.setOnClickListener(listenerStart);

    }

    private void start() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ibPlay.setVisibility(View.INVISIBLE);
                tvPuntuacio.setVisibility(View.VISIBLE);
                tvPuntos.setVisibility(View.VISIBLE);
            }
        }, 1000);
        tvPuntos.setText("" + (tiradesMaquina.size()) + 1);
        reprodueixSonsMaquina();
        tornJugador = true;
        jugando = true;
    }

    /**
     * How to use SoundPool on all API levels
     */
    protected void createSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        } else {
            createOldSoundPool();
        }
    }

    /**
     * Create SoundPool for versions >= LOLLIPOP
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    /**
     * Create SoundPool for deprecated versions < LOLLIPOP
     */
    @SuppressWarnings("deprecation")
    protected void createOldSoundPool() {
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
    }

    /**
     * Afegeix un so a la llista de tiradesMaquina i
     * reproduim els sons de la màquina, quan acaba cedim el torn al jugador
     */
    public void reprodueixSonsMaquina() {
        tiradesMaquina.add(azar());
        // activarem el torn del jugador quan acabi de sonar la màquina.
        // Per fer això creem un Handler amb delay
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // es a dir, li toca al jugador quan hagi acabat de reproduir la màquina
                tornJugador = true;
                // buidem la llista per a que comenci a llençar el jugador
                tiradesJugador.clear();
            }
        }, tiradesMaquina.size() * duracion * 2);
        // fem que llenci la màquina
        for (int i = 0; i < tiradesMaquina.size(); i++) {
            tornJugador = false;
            final int valorTirada = tiradesMaquina.get(i).intValue();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    switch (valorTirada) {
                        case 0:
                            azul();
                            break;
                        case 1:
                            rojo();
                            break;
                        case 2:
                            verde();
                            break;
                        case 3:
                            amarillo();
                            break;
                    }
                }
            }, duracion * i * 2);
        }
    }

    /**
     * Exemple de So pel Blue il·luminant una estona el botó
     */
    public void azul() {
        ibAzul.setImageResource(R.drawable.blueimglight);
        if (jugando)
            //sp.play(soundID, leftVolume, rightVolume, priority, loop, rate);
            soundPool.play(sonAzul, 1, 1, 0, 0, 1);
        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ibAzul.setImageResource(R.drawable.blueimg);
                }
            }, duracion);
        } catch (Exception e) {
            Log.i("Error azul()", e.toString());
        }
    }

    /**
     * Exemple de So pel Blue il·luminant una estona el botó
     */
    public void rojo() {
        ibRojo.setImageResource(R.drawable.redimglight);
        if (jugando)
            //sp.play(soundID, leftVolume, rightVolume, priority, loop, rate);
            soundPool.play(sonRojo, 1, 1, 0, 0, 1);
        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ibRojo.setImageResource(R.drawable.redimg);
                }
            }, duracion);
        } catch (Exception e) {
            Log.i("Error rojo()", e.toString());
        }
    }

    /**
     * Exemple de So pel Blue il·luminant una estona el botó
     */
    public void verde() {
        ibVerde.setImageResource(R.drawable.greenimglight);
        if (jugando)
            //sp.play(soundID, leftVolume, rightVolume, priority, loop, rate);
            soundPool.play(sonVerde, 1, 1, 0, 0, 1);
        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ibVerde.setImageResource(R.drawable.greenimg);
                }
            }, duracion);
        } catch (Exception e) {
            Log.i("Error verde()", e.toString());
        }
    }

    /**
     * Exemple de So pel Blue il·luminant una estona el botó
     */
    public void amarillo() {
        ibAmarillo.setImageResource(R.drawable.yellowimglight);
        if (jugando)
            //sp.play(soundID, leftVolume, rightVolume, priority, loop, rate);
            soundPool.play(sonAmarillo, 1, 1, 0, 0, 1);
        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ibAmarillo.setImageResource(R.drawable.yellowimg);
                }
            }, duracion);
        } catch (Exception e) {
            Log.i("Error amarillo()", e.toString());
        }
    }

    /**
     * Calculate a random value
     *
     * @return random value between 0 and 3
     */
    public int azar() {
        Random rnd = new Random();
        int random = (int) rnd.nextInt(4);
        return random;
    }

    View.OnClickListener listenerColor = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (tornJugador == true && jugando == true) {
                if (v.getId() == R.id.ibAzul) {
                    azul();
                    tiradesJugador.add(0);
                } else if (v.getId() == R.id.ibRojo) {
                    rojo();
                    tiradesJugador.add(1);
                } else if (v.getId() == R.id.ibVerde) {
                    verde();
                    tiradesJugador.add(2);
                } else if (v.getId() == R.id.ibAmarillo) {
                    amarillo();
                    tiradesJugador.add(3);
                }
                comprovatirada();
            }
        }
    };

    public void comprovatirada() {
        Handler handler = new Handler();
        // si falla la tirada
        if (tiradesJugador.get(tirada).intValue() != tiradesMaquina.get(tirada)
                .intValue()) {
            jugando = false; // para controlar el boton de play
            error();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ibPlay.setVisibility(View.VISIBLE);
                    tvPuntuacio.setVisibility(View.INVISIBLE);
                    tvPuntos.setVisibility(View.INVISIBLE);
                }
            }, 1000);
            tiradesMaquina.clear();
            tiradesJugador.clear();
            tirada = 0;
            // que vuelva a tirar la maquina 1 segundo despues si he completado aciertos
        } else if (tiradesJugador.size() == tiradesMaquina.size() && jugando == true) {
            tornJugador = false; // para controlar los onclick
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvPuntos.setText("" + (tiradesMaquina.size() + 1));
                    reprodueixSonsMaquina();
                    tirada = 0;
                }
            }, 1000);
        } else {
            tirada++;
        }
    }

    private void error() {
        soundPool.play(sonError,1,1,0,0,1);
    }


}
