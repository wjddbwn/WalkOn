package com.walkon.firebase;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private InputStream inputStream;

    private static final int REQUEST_CODE_BLUETOOTH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Bluetooth 권한 확인 및 요청
        checkPermissions();
    }

    // Bluetooth 권한 확인 및 요청
    private boolean checkBluetoothPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void checkPermissions() {
        if (!checkBluetoothPermissions()) {
            // 권한이 없으면 권한 요청
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    REQUEST_CODE_BLUETOOTH);
        } else {
            // 권한이 있으면 Bluetooth 초기화
            initializeBluetooth();
        }
    }

    // 권한 요청 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_BLUETOOTH) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용되면 Bluetooth 초기화
                initializeBluetooth();
            } else {
                // 권한이 거부되면 알림
                Toast.makeText(this, "Bluetooth 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Bluetooth 초기화
    private void initializeBluetooth() {
        try {
            if (bluetoothAdapter == null) {
                Toast.makeText(this, "이 장치는 Bluetooth을 지원하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();  // 이 작업에는 BLUETOOTH_ADMIN 권한이 필요합니다.
            }

            // Bluetooth 장치 검색 시작
            startDeviceDiscovery();
        } catch (SecurityException e) {
            Toast.makeText(this, "Bluetooth 권한이 없거나 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // 장치 검색 시작
    private void startDeviceDiscovery() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothReceiver, filter);

        bluetoothAdapter.startDiscovery();
    }

    // Bluetooth 장치 수신
    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceAddress = device.getAddress(); // MAC 주소

                Log.d("Bluetooth", "Device found: " + deviceName + " Address: " + deviceAddress);

                // 여기서 사용자가 장치를 선택할 수 있도록 할 수 있음
                connectBluetooth(deviceAddress);
            }
        }
    };

    // Bluetooth 장치 연결
    private void connectBluetooth(String deviceAddress) {
        try {
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);

            // RFCOMM 소켓 생성 및 연결
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            bluetoothSocket.connect();

            // 입출력 스트림 생성
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();

            // 데이터 전송 및 수신
            sendData("Hello Arduino");
            readData();

        } catch (SecurityException e) {
            Toast.makeText(this, "Bluetooth 연결 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, "Bluetooth 연결 실패", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void sendData(String data) throws IOException {
        if (outputStream != null) {
            outputStream.write(data.getBytes());
        }
    }

    private void readData() throws IOException {
        byte[] buffer = new byte[1024];
        int bytes;

        while ((bytes = inputStream.read(buffer)) > 0) {
            String receivedData = new String(buffer, 0, bytes);
            Log.d("Bluetooth Data", receivedData);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (bluetoothSocket != null) bluetoothSocket.close();
            unregisterReceiver(bluetoothReceiver);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
