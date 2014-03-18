package com.klaxon.client;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.klaxon.remote.client.R;
import com.klaxon.remote.common.config.Configuration;
import com.klaxon.remote.common.io.Client;
import com.klaxon.remote.common.io.socket.SocketClient;
import com.klaxon.remote.common.message.VolumeChange;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * <p>date 2/27/14 </p>
 *
 * @author klaxon
 */
public class MainActivity extends Activity {

    private SocketClient client = null;
    private View connectionFormView = null;
    private View rootView = null;
    private EditText addressTextBox = null;
    private BroadcastReceiver mediaReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_manipulator_layout);

        rootView = findViewById(android.R.id.content);
        connectionFormView = findViewById(R.id.connection_form);
        addressTextBox = (EditText) findViewById(R.id.address_text_box);

        View connectButton = findViewById(R.id.button);
        connectButton.setOnClickListener(new ConnectButtonClickListener());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaReceiver != null) {
            unregisterReceiver(mediaReceiver);
            mediaReceiver = null;
        }
        if (client != null) client.close();
    }

    @Override
    public void onBackPressed() {
        if (connectionFormView.getVisibility() != View.VISIBLE) {
            connectionFormView.setVisibility(View.VISIBLE);
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectionFormView.setVisibility(View.VISIBLE);
        rootView.setOnClickListener(null);
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
        }

        return true;
    }

    private final class ConnectButtonClickListener implements View.OnClickListener {
        private final int toastDuration = getResources().getInteger(R.integer.toast_duration_millis);

        @Override
        public void onClick(View v) {
            if (!ConnectionHelper.isNetworkConnected(MainActivity.this)) {
                Toast.makeText(MainActivity.this, R.string.no_network_connection, toastDuration).show();
                return;
            }

            InetAddress host = ConnectionHelper.hostFrom(addressTextBox.getText().toString());
            if (host == null) {
                Toast.makeText(MainActivity.this, R.string.wrong_address, toastDuration).show();
                return;
            }

            SocketAddress address = new InetSocketAddress(host, Configuration.PORT());
            client = ConnectionHelper.client(address);
            if (client == null) {
                Toast.makeText(MainActivity.this, R.string.fail_connect_to_server, toastDuration).show();
                return;
            }

            connectionFormView.setVisibility(View.GONE);
            rootView.setOnTouchListener(new ManipulatorTouchListener(client, getResources().getInteger(R.integer.swipe_threshold)));
        }
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
