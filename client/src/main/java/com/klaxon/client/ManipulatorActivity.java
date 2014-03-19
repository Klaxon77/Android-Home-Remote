package com.klaxon.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.klaxon.remote.client.R;
import com.klaxon.remote.common.io.Client;
import com.klaxon.remote.common.message.MuteToggle;
import com.klaxon.remote.common.message.VolumeChange;

import java.net.SocketAddress;

/**
 * <p>date 3/19/14 </p>
 * @author klaxon
 */
public class ManipulatorActivity extends Activity {

    public static String SOCKET_ADDRESS_KEY = "manipulatorActivity.socketAddress";

    private Client client = null;
    private View rootView = null;
    private SocketAddress socketAddress = null;
    private int toastDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        socketAddress = (SocketAddress) getIntent().getSerializableExtra(SOCKET_ADDRESS_KEY);
        if (socketAddress == null) {
            finish(RESULT_CANCELED);
        }

        setContentView(R.layout.manipulator_layout);

        toastDuration = getResources().getInteger(R.integer.toast_duration_millis);
        rootView = findViewById(android.R.id.content);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!ConnectionHelper.isNetworkConnected(this)) {
            Toast.makeText(this, R.string.no_network_connection, toastDuration).show();
            finish(RESULT_CANCELED);
        }

        client = ConnectionHelper.client(socketAddress);
        if (client == null) {
            setResult(RESULT_CANCELED);
            finish();
        }

        int swipeThreshold = getResources().getInteger(R.integer.swipe_threshold);
        rootView.setOnTouchListener(new ManipulatorTouchListener(client, swipeThreshold));
    }

    private void finish(int result) {
        setResult(result);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        rootView.setOnClickListener(null);
        if (client != null) client.close();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (client == null) return true;

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                client.$bang(new VolumeChange(-0.1f));
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                client.$bang(new VolumeChange(0.1f));
                break;
            case KeyEvent.KEYCODE_MUTE:
                client.$bang(new MuteToggle());
                break;
        }

        return true;
    }

    private static final class ManipulatorTouchListener implements View.OnTouchListener {
        private final GestureDetector manipulatorGestureDetector;

        public ManipulatorTouchListener(Client client, int swipeThreshold) {
            manipulatorGestureDetector = new GestureDetector(new ManipulatorGestureListener(client, swipeThreshold));
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return manipulatorGestureDetector.onTouchEvent(event);
        }
    }

}
