package com.walkon.firebase;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private InputStream inputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 화면의 시스템 바를 고려한 패딩 설정
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 네비게이션 바 초기화
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 블루투스 설정
        initializeBluetooth();

        // 초기 화면 띄우기
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_frame, new Fragment1())
                .commit();

        // 네비게이션 아이템 선택 시 화면 전환
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_frame, new Fragment1())
                                .commit();
                        break;

                    case R.id.nav_tutorial:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_frame, new Fragment2())
                                .commit();
                        break;

                    case R.id.nav_analysis:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_frame, new Fragment3())
                                .commit();
                        break;

                    case R.id.nav_my:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_frame, new Fragment4())
                                .commit();
                        break;
                }
                return true;
            }
        });
    }

    // 블루투스 초기화 메서드
    private void initializeBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.e("Bluetooth", "Device doesn't support Bluetooth");
            return;
        }

        // 연결할 장치의 MAC 주소 (아두이노)
        bluetoothDevice = bluetoothAdapter.getRemoteDevice("00:11:22:33:AA:BB");

        try {
            // 소켓 생성 및 연결
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(
                    UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
            );
            bluetoothSocket.connect();

            // 스트림 초기화
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();

            // 초기 데이터 전송 및 수신
            sendData("Hello Arduino");
            readData();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Bluetooth", "Error during Bluetooth connection");
        }
    }

    // 데이터 전송 메서드
    private void sendData(String data) throws IOException {
        if (outputStream != null) {
            outputStream.write(data.getBytes());
        }
    }

    // 데이터 수신 메서드
    private void readData() throws IOException {
        byte[] buffer = new byte[1024];
        int bytes;

        while ((bytes = inputStream.read(buffer)) > 0) {
            String receivedData = new String(buffer, 0, bytes);
            Log.d("Bluetooth Data", receivedData);
        }
    }

    // 자원 정리 메서드
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
