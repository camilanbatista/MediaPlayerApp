package mediaplayer.fa7.com.br.mediaplayer;

import android.net.Uri;

import java.util.Locale;

/**
 * Created by Caio on 18/12/2015.
 */
public class Media {

    public final String name;
    public final String contentId;
    public final String provider;
    public final Uri uri;
    public final int type;

    public Media(String name, Uri uri, int type) {
        this(name, name.toLowerCase(Locale.US).replaceAll("\\s", ""), "", uri, type);
    }

    public Media(String name, String contentId, String provider, Uri uri, int type) {
        this.name = name;
        this.contentId = contentId;
        this.provider = provider;
        this.uri = uri;
        this.type = type;
    }
}
