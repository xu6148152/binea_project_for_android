/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.classical;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.Constants;
import com.example.android.bluetoothchat.R;
import com.example.android.common.logger.Log;
import com.example.android.model.GlobalVar;
import com.example.android.utils.Byte2Hex;
import com.example.android.utils.FileUtil;
import com.example.android.utils.RegexUtil;
import java.nio.ByteOrder;

/**
 * This fragment controls Bluetooth to communicate with other devices.
 */
public class BluetoothChatFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "BluetoothChatFragment";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Layout Views
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mStartDribblingButton;
    private Button mStartShootingButton;
    private Button mStartRawStream;
    private Chronometer time;
    private EditText et_ip;

    private boolean isStartDribbling = false;
    private boolean isStartShooting = false;
    private boolean isStartRawStream = false;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<String> mConversationArrayAdapter;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothChatService mChatService = null;

    private TextView tv_shoot_count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //StringBuilder sb = new StringBuilder();
        //for(int i = 0;i<RawData.RAWDATA.length();i+=4){
        //    final String stringData = RawData.RAWDATA.substring(i, i + 4);
        //    final int str = Integer.parseInt(stringData, 16);
        //    sb.append(str + " ");
        //}
        //sb.toString();
        Log.d("PATH ", FileUtil.filePath);
        setHasOptionsMenu(true);
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //ShootingRecord record = new ShootingRecord();
        //PackageData data = new PackageData(MessageType.BALL_EVENT, EventType.SHOOTING_RESULT, record);
        Log.d(TAG, " " + ByteOrder.nativeOrder());
        return inflater.inflate(R.layout.fragment_bluetooth_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mConversationView = (ListView) view.findViewById(R.id.in);
        mOutEditText = (EditText) view.findViewById(R.id.edit_text_out);
        mStartDribblingButton = (Button) view.findViewById(R.id.btn_start_dribbling);
        tv_shoot_count = (TextView) view.findViewById(R.id.tv_shoot_count);
        mStartShootingButton = (Button) view.findViewById(R.id.btn_start_shooting);
        mStartRawStream = (Button) view.findViewById(R.id.btn_start_raw_stream);
        time = (Chronometer) view.findViewById(R.id.time);
        et_ip = (EditText) view.findViewById(R.id.et_ip);
        et_ip.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    GlobalVar.SERVER_ADDRESS = et_ip.getText().toString();
                }
            }
        });
        et_ip.setText(GlobalVar.SERVER_ADDRESS);
        time.setText("00:00:00");
        time.setFormat("计时：%s");
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);

        mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        mStartDribblingButton.setOnClickListener(this);
        mStartShootingButton.setOnClickListener(this);
        mStartRawStream.setOnClickListener(this);

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(getActivity(), mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    /**
     * Makes this device discoverable.
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            //byte[] send = message.getBytes();
            final byte[] send = Byte2Hex.hexToBytes(message);
            for(int i = 0;i<send.length / 2; i++){
                byte tmp = send[i];
                send[i] = send[send.length - 1 - i];
                send[send.length - 1 - i] = tmp;
            }
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            mOutEditText.setText(mOutStringBuffer);
        }
    }

    /**
     * The action listener for the EditText widget, to listen for the return key
     */
    private TextView.OnEditorActionListener mWriteListener
            = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            return true;
        }
    };

    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            mConversationArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    GlobalVar.currentBallName = mConnectedDeviceName;
                    GlobalVar.currentBallMacAddress = msg.getData().getString(Constants.DEVICE_ADDRESS);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;

                case Constants.MESSAGE_DRIBBLING_ACTIVITY_RECORD_NOTIFICATION:
                    final long totalDribblingCount =
                            msg.getData().getLong(Constants.DRIBBLING_ACTIVITY_RECORD_NOTIFICATION);
                    tv_shoot_count.setText(String.valueOf(totalDribblingCount / 10));
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(!RegexUtil.isValidIp(et_ip.getText().toString())){
            Toast.makeText(getActivity(), "please use right ip format", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    }

    /**
     * Establish connection with other divice
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bluetooth_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.insecure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }
            case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }
        }
        return false;
    }

    @Override public void onClick(View v) {
        if(mChatService.delegate == null){
            Toast.makeText(getActivity(), "basketball do not connect", Toast.LENGTH_SHORT).show();
            return;
        }
        GlobalVar.SERVER_ADDRESS = et_ip.getText().toString();
        if(!RegexUtil.isValidIp(GlobalVar.SERVER_ADDRESS)){
            Toast.makeText(getActivity(), "please use right ip format", Toast.LENGTH_SHORT).show();
            return;
        }
        String msg = null;
        switch (v.getId()){
            case R.id.btn_start_dribbling:
                if(isStartShooting || isStartRawStream){
                    showToastForHintOtherRequestIsStart();
                    return;
                }
                if (!isStartDribbling) {
                    mChatService.delegate.startDribblingActivity();
                    isStartDribbling = true;
                } else {
                    //mChatService.delegate.setEndDribblingListener(new EndDribblingListener() {
                    //    @Override public void onResponse(boolean isOk) {
                    //        if (isOk) {
                    //            isStartDribbling = !isStartDribbling;
                    //            getActivity().runOnUiThread(new Runnable() {
                    //                @Override public void run() {
                    //                    if (isStartDribbling) {
                    //                        mStartDribblingButton.setText(getString(R.string.stop));
                    //                    } else {
                    //                        mStartDribblingButton.setText(
                    //                                getString(R.string.start_dribbling));
                    //                    }
                    //                }
                    //            });
                    //
                    //        }
                    //    }
                    //});

                    //mChatService.delegate.endDribblingActivity();
                    mChatService.delegate.endDribblingActivity();
                    //isStartDribbling = !isStartDribbling;
                    //getActivity().runOnUiThread(new Runnable() {
                    //    @Override public void run() {
                    //        if (isStartDribbling) {
                    //            mStartDribblingButton.setText(getString(R.string.stop));
                    //        } else {
                    //            mStartDribblingButton.setText(
                    //                    getString(R.string.start_dribbling));
                    //        }
                    //    }
                    //});
                    isStartDribbling = !isStartDribbling;
                }

                if (isStartDribbling) {
                    mStartDribblingButton.setText(getString(R.string.stop));
                } else {
                    mStartDribblingButton.setText(
                            getString(R.string.start_dribbling));
                }

                break;

            case R.id.btn_start_shooting:
                if(isStartDribbling || isStartRawStream){
                    showToastForHintOtherRequestIsStart();
                    return;
                }
                if (!isStartShooting) {
                    mChatService.delegate.startShootingActivity();
                    isStartShooting = true;
                } else {
                    //mChatService.delegate.setEndShootingListener(new EndShootingListener() {
                    //    @Override public void onResponse(boolean isOk) {
                    //        if (isOk) {
                    //            isStartShooting = !isStartShooting;
                    //            getActivity().runOnUiThread(new Runnable() {
                    //                @Override public void run() {
                    //                    if (isStartShooting) {
                    //                        mStartShootingButton.setText(getString(R.string.stop));
                    //                    } else {
                    //                        mStartShootingButton.setText(
                    //                                getString(R.string.start_shooting));
                    //                    }
                    //                }
                    //            });
                    //
                    //        }
                    //    }
                    //});
                    //mChatService.delegate.endShootingActivity();
                    mChatService.delegate.endShootingActivity();
                    isStartShooting = !isStartShooting;
                }
                if(isStartShooting){
                    mStartShootingButton.setText(getString(R.string.stop));
                }else{
                    mStartShootingButton.setText(getString(R.string.start_shooting));
                }
                break;

            case R.id.btn_start_raw_stream:
                if(isStartDribbling || isStartShooting){
                    return;
                }
                if (!isStartRawStream) {
                    time.setBase(SystemClock.elapsedRealtime());
                    time.start();
                    FileUtil.createFile();
                    mChatService.delegate.startRawStream();
                    isStartRawStream = true;
                } else {
                    //mChatService.delegate.setEndRawStreamListener(new EndRawStreamListener() {
                    //    @Override public void onResponse(boolean isOk) {
                    //        if (isOk) {
                    //            isStartRawStream = !isStartRawStream;
                    //            getActivity().runOnUiThread(new Runnable() {
                    //                @Override public void run() {
                    //                    time.stop();
                    //                    DialogUtil.showDialog(getActivity());
                    //
                    //                    if (isStartRawStream) {
                    //                        mStartRawStream.setText(getString(R.string.stop));
                    //                    } else {
                    //                        mStartRawStream.setText(
                    //                                getString(R.string.start_raw_stream));
                    //                    }
                    //                }
                    //            });
                    //        }
                    //    }
                    //});
                    mChatService.delegate.endRawStream();
                    mChatService.delegate.endRawStream();
                    isStartRawStream = !isStartRawStream;
                }

                if(isStartRawStream){
                    mStartRawStream.setText(getString(R.string.stop));
                }else{
                    mStartRawStream.setText(getString(R.string.start_raw_stream));
                }

                break;
        }
    }

    private void showToastForHintOtherRequestIsStart(){
        Toast.makeText(getActivity(), "other request is started, please stop it before you can start a new request", Toast.LENGTH_SHORT).show();
    }
}
