package mediaplayer.fa7.com.br.mediaplayer;

import android.net.Uri;

import java.util.List;

/**
 * Created by antonio on 05/12/2015.
 */
public interface Funcionalidade {

    void proximo();
    void anterior();
    void pausar();
    int getSizeListMusic();
    int getIdentificadorMusica();
    void playMedia();
}
