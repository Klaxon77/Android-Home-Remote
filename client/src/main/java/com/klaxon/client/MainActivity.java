package com.klaxon.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.klaxon.remote.client.R;
import com.klaxon.remote.common.config.Configuration;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * <p>date 2/27/14 </p>
 *
 * @author klaxon
 */
public class MainActivity extends Activity {

    private EditText addressTextBox = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_form_layout);

        addressTextBox = (EditText) findViewById(R.id.address_text_box);

        View connectButton = findViewById(R.id.button);
        connectButton.setOnClickListener(new ConnectButtonClickListener());
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

            Intent intent = new Intent(MainActivity.this, ManipulatorActivity.class);
            intent.putExtra(ManipulatorActivity.SOCKET_ADDRESS_KEY, address);
            MainActivity.this.startActivity(intent);
        }
    }

}
