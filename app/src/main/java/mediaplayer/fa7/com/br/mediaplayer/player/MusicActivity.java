package mediaplayer.fa7.com.br.mediaplayer.player;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import mediaplayer.fa7.com.br.mediaplayer.Funcionalidade;
import mediaplayer.fa7.com.br.mediaplayer.MyServicePlay;
import mediaplayer.fa7.com.br.mediaplayer.R;

public class MusicActivity extends Activity implements ServiceConnection {

    private Intent serviceIntent;
    private Button buttonPlayStop;
    private boolean boolMusicPlaying = false;
    private Button btnProximo;
    private Button btnAnterior;
    private Button btnParar;
    private MyServicePlay.Controller controle;
    private Funcionalidade funcionalidades;
    private ServiceConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        try {
            serviceIntent = new Intent(this, MyServicePlay.class);
            initViews();
            setListeners();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getClass().getName() + " " +
                    e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void initViews() {
        buttonPlayStop = (Button) findViewById(R.id.ButtonPlayStop);
        buttonPlayStop.setBackgroundResource(R.drawable.playbuttonsm);
        btnAnterior = (Button) findViewById(R.id.btnAnterior);
        btnProximo = (Button) findViewById(R.id.btnProximo);
        btnParar = (Button) findViewById(R.id.btnParar);

    }

    private void setListeners() {
        buttonPlayStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonPlayStopClick();
            }
        });

        btnAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anterior();

            }
        });

        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proximo();
            }
        });

        btnParar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopMyPlayService();
            }
        });
    }

    private void buttonPlayStopClick() {
        if (!boolMusicPlaying) {
            buttonPlayStop.setBackgroundResource(R.drawable.playbuttonsm);
            playAudio();
            boolMusicPlaying = true;
        } else {
            if (boolMusicPlaying) {
                buttonPlayStop.setBackgroundResource(R.drawable.pausebuttonsm);
                funcionalidades.pausar();
                boolMusicPlaying = false;
            }
        }
    }

    private void playAudio() {
        try {
            if (connection == null) {
                connection = this;
                bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
                startService(serviceIntent);
                return ;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getClass().getName() + " " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        funcionalidades.playMedia();
    }

    private void proximo() {
        try {
            if ((funcionalidades.getIdentificadorMusica() < (funcionalidades.getSizeListMusic() - 1))) {
                funcionalidades.proximo();
            } else {
                Toast.makeText(this, "Musica Final do Player List", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getClass().getName() + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void anterior() {
        try {
            if (funcionalidades.getIdentificadorMusica() > 0) {
                funcionalidades.anterior();
            } else {
                Toast.makeText(this, "Musica Inicial do Player List", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getClass().getName() + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void stopMyPlayService() {
        try {
            if (connection != null) {
                unbindService(connection);
                connection = null;
                stopService(serviceIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getClass().getName() + " " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        boolMusicPlaying = false;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        controle = (MyServicePlay.Controller) iBinder;
        funcionalidades = controle.getMediaPlayer();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }


}
