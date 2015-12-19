package mediaplayer.fa7.com.br.mediaplayer.player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
import mediaplayer.fa7.com.br.mediaplayer.MyServicePlay;
import mediaplayer.fa7.com.br.mediaplayer.R;

public class MusicPlayerActivity extends Activity implements View.OnClickListener   {

    // For use within demo app code.
    public static final String CONTENT_ID_EXTRA = "content_id";
    public static final String CONTENT_TYPE_EXTRA = "content_type";
    public static final String PROVIDER_EXTRA = "provider";
    public static final int TYPE_DASH = 0;
    public static final int TYPE_SS = 1;
    public static final int TYPE_HLS = 2;
    public static final int TYPE_OTHER = 3;

    // For use when launching the demo app using adb.
    private static final String CONTENT_EXT_EXTRA = "type";
    private static final String EXT_DASH = ".mpd";
    private static final String EXT_SS = ".ism";
    private static final String EXT_HLS = ".m3u8";

    private static final String TAG = "PlayerActivity";
    private static final int MENU_GROUP_TRACKS = 1;
    private static final int ID_OFFSET = 2;

    //States
    public static final int STATE_IDLE = 1;
    public static final int STATE_PREPARING = 2;
    public static final int STATE_BUFFERING = 3;
    public static final int STATE_READY = 4;
    public static final int STATE_ENDED = 5;
    public static final int TRACK_DISABLED = -1;
    public static final int TRACK_DEFAULT = 0;

    private MediaController mediaController;
    private View debugRootView;
    private View shutterView;
    private FrameLayout videoFrame;
    private SurfaceView surfaceView;

    private MyServicePlay controller;
    private boolean playerNeedsPrepare;

    private long playerPosition;
    private boolean enableBackgroundAudio;

    private Uri contentUri;
    private int contentType;
    private String contentId;
    private String provider;

    // Activity lifecycle

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_music_player);
        View root = findViewById(R.id.root);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    toggleControlsVisibility();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.performClick();
                }
                return true;
            }
        });
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE
                        || keyCode == KeyEvent.KEYCODE_MENU) {
                    return false;
                }
                return mediaController.dispatchKeyEvent(event);
            }
        });

        shutterView = findViewById(R.id.shutter);
        debugRootView = findViewById(R.id.controls_root);

        videoFrame = (FrameLayout) findViewById(R.id.video_frame);
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        //surfaceView.getHolder().addCallback(this);

        mediaController = new KeyCompatibleMediaController(this);
        mediaController.setAnchorView(root);

    }

    @Override
    public void onNewIntent(Intent intent) {
        releasePlayer();
        playerPosition = 0;
        setIntent(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        contentUri = intent.getData();
        contentType = intent.getIntExtra(CONTENT_TYPE_EXTRA,
                inferContentType(contentUri, intent.getStringExtra(CONTENT_EXT_EXTRA)));
        contentId = intent.getStringExtra(CONTENT_ID_EXTRA);
        provider = intent.getStringExtra(PROVIDER_EXTRA);

        if (controller == null) {

            // controller.setBackgrounded(false);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (!enableBackgroundAudio) {
            releasePlayer();
        } else {
            controller.setBackgrounded(true);
        }
        shutterView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //audioCapabilitiesReceiver.unregister();
        releasePlayer();
    }

    // OnClickListener methods

    @Override
    public void onClick(View view) {
//        if (view == retryButton) {
//            preparePlayer(true);
//        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            preparePlayer(true);
        } else {
//            Toast.makeText(getApplicationContext(), R.string.storage_permission_denied,
//                    Toast.LENGTH_LONG).show();
//            finish();
        }
    }

    // Permission management methods



    private void preparePlayer(boolean playWhenReady) {
        if (controller == null) {
            //controller = new MyServicePlay(getRendererBuilder());
//            controller.addListener(this);
//            controller.setCaptionListener(this);
//            controller.setMetadataListener(this);
//            controller.seekTo(playerPosition);
            playerNeedsPrepare = true;
            //mediaController.setMediaPlayer(controller.getPlayerControl());
            mediaController.setEnabled(true);
        }
        if (playerNeedsPrepare) {
            //controller.prepare();
            playerNeedsPrepare = false;

        }
        //controller.setPlayWhenReady(playWhenReady);
    }

    private void releasePlayer() {
        if (controller != null) {
            //debugViewHelper.stop();
            //debugViewHelper = null;
            //playerPosition = player.getCurrentPosition();
            //controller.release();
            controller = null;
        }
    }

    // DemoPlayer.Listener implementation

    public void onStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == STATE_ENDED) {
            showControls();
        }
    }


    // User controls

//    private boolean haveTracks(int type) {
//        return controller != null && controller.getTrackCount(type) > 0;
//    }



    private boolean onTrackItemClick(MenuItem item, int type) {
        if (controller == null || item.getGroupId() != MENU_GROUP_TRACKS) {
            return false;
        }
        //controller.setSelectedTrack(type, item.getItemId() - ID_OFFSET);
        return true;
    }

    private void toggleControlsVisibility()  {
        if (mediaController.isShowing()) {
            mediaController.hide();
            debugRootView.setVisibility(View.GONE);
        } else {
            showControls();
        }
    }

    private void showControls() {
        mediaController.show(0);
        debugRootView.setVisibility(View.VISIBLE);
    }

    private static int inferContentType(Uri uri, String fileExtension) {
        String lastPathSegment = !TextUtils.isEmpty(fileExtension) ? "." + fileExtension
                : uri.getLastPathSegment();
        if (lastPathSegment == null) {
            return TYPE_OTHER;
        } else if (lastPathSegment.endsWith(EXT_DASH)) {
            return TYPE_DASH;
        } else if (lastPathSegment.endsWith(EXT_SS)) {
            return TYPE_SS;
        } else if (lastPathSegment.endsWith(EXT_HLS)) {
            return TYPE_HLS;
        } else {
            return TYPE_OTHER;
        }
    }

    private static final class KeyCompatibleMediaController extends MediaController {

        private MediaController.MediaPlayerControl playerControl;

        public KeyCompatibleMediaController(Context context) {
            super(context);
        }

        @Override
        public void setMediaPlayer(MediaController.MediaPlayerControl playerControl) {
            super.setMediaPlayer(playerControl);
            this.playerControl = playerControl;
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            int keyCode = event.getKeyCode();
            if (playerControl.canSeekForward() && keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    playerControl.seekTo(playerControl.getCurrentPosition() + 15000); // milliseconds
                    show();
                }
                return true;
            } else if (playerControl.canSeekBackward() && keyCode == KeyEvent.KEYCODE_MEDIA_REWIND) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    playerControl.seekTo(playerControl.getCurrentPosition() - 5000); // milliseconds
                    show();
                }
                return true;
            }
            return super.dispatchKeyEvent(event);
        }
    }

}
