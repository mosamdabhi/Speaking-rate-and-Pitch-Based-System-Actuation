// Author: Mosam Dabhi
// Final Application used: fused_test_2


package com.mycompany.mosam.fused_test_2;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import com.mathworks.toolbox.javabuilder.*;
//import magicsquare.*;


import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.GraphicalView;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.filters.BandPass;
import be.tarsos.dsp.filters.HighPass;
import be.tarsos.dsp.filters.IIRFilter;
import be.tarsos.dsp.filters.LowPassSP;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class MainActivity extends Activity {

    Button startRec, stopRec, playBack;
    TextView text;
    Boolean recording;
    long startTime = 0;
    AudioDispatcher dispatcher;
    boolean bool = false;
    Button btnStart, btnSend,btnSend2;
    TextView textStatus;
    NetworkTask networktask;


    //smoothArray smoothArrayObject;
    //peakDetection peakDetectionObject;
    //MinMaxFinder MinMaxFinderObject;


//    public MainActivity() {
//        smoothArrayObject = new smoothArray(EnergySecond, 1);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startRec = (Button)findViewById(R.id.startrec);
        stopRec = (Button)findViewById(R.id.stoprec);
        playBack = (Button)findViewById(R.id.playback);
        text = (TextView) findViewById(R.id.text_test);
        text = (TextView) findViewById(R.id.textView1);
        stopRec.setEnabled(false);
        btnStart = (Button)findViewById(R.id.btnStart);
        btnSend = (Button)findViewById(R.id.btnSend);
        btnSend2 = (Button)findViewById(R.id.btnSend2);
        textStatus = (TextView)findViewById(R.id.textStatus);
        btnStart.setOnClickListener(btnStartListener);
        btnSend.setOnClickListener(btnSendListener);
        btnSend2.setOnClickListener(btnSend2Listener);
        networktask = new NetworkTask();


        startRec.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                StartRecord();
                count=0;
                count1=0;
                num=0;
                l=0;
                bool = false;
                stopRec.setEnabled(true);
                textStatus.setText("Status");
                bytePlay = Arrays.copyOf(bytePlay, 20480);
//                recordingBuffer = Arrays.copyOf(recordingBuffer, 16000);
                startTime = System.currentTimeMillis();
            }
        });
        stopRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StopRecording();
            }
        });
        playBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRecord();
            }
        });

    }




    int count,counter=0,num,time,l,  count1, bufLength = 1024, n=0, looplength=50, frame_count=0,smoothing = 6,index,index2,max2counter,spcounter=0,flagcounter,SendButton1=0;

    float[] recordingBuffer = new float[16000];
    float[] storageBuffer = new float[16000];
    float[] RequiredBuffer = new float[16000];
    float[] Energy = new float[looplength*2];
    float[] Energy2 = new float[320];
    float[] EnergySecond = new float[100];
    float[] EnergySecondFiltered = new float[100];
    float[] normalizedEnergy = new float[100];
    float[] EnergyPeaks = new float[100];
    float[] fiveSecondStorage = new float[16000];
    float[] EnergyPeaksthresold = new float[100];
    float[] flag = new float[100];
    float[] flagtemporary = new float[100];
    float[] newEnergyPeaks = new float[100];
    float[] peakCounter = new float[100];
    float[] maxes = new float[100];
    float[] maxes2 = new float[100];
    float[] filter1Out = new float[100];
    float[] filter2Out = new float[100];
    float[] EnergyFrame = new float[recordingBuffer.length];
    float[] Energy100 = new float[100];
    double[] output = new double[100];
    double[] EnergyFiltered = new double[100];
    double[] EnergyFilteredthreshold = new double[100];
    float  EnergyFrame2, EnergyFrame3, EnergyFrame4, EnergyFrame5, EnergyFrame6, unitValue, currentValue,alpha,T,tau,tempStorage;
    double thresholding,firstDiff;
    float largest = EnergySecondFiltered[0];
    boolean nextflag = false;


    float[] EnergyArray = new float[recordingBuffer.length];
    float[] BufferStorage = new float[20480];
    float[] alphaBuffer = new float[16000];
//    ArrayList<Integer> mins = new ArrayList<Integer>();
//    ArrayList<Integer> maxs = new ArrayList<Integer>();
//    List list = new ArrayList();



    byte [] bytePlay = new byte[20480];
    double[] powerVal = new double[10];
    double F = 0;
    public void StartRecord(){

        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(16000,bufLength,0);
        dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.YIN, 16000, 1024, new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {

                final float[] buffer = audioEvent.getFloatBuffer();
                final float pitch = pitchDetectionResult.getPitch();
                final byte[] playByte = audioEvent.getByteBuffer();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        long timer = System.currentTimeMillis() - startTime;
                        time = (int) timer / 1000;
                        num = 0;


                        if (recordingBuffer.length < (count + 2) * bufLength) {
                            recordingBuffer = Arrays.copyOf(recordingBuffer, recordingBuffer.length + bufLength * 10);

                        }
                        if (storageBuffer.length < (count + 2) * bufLength) {
                            storageBuffer = Arrays.copyOf(storageBuffer, storageBuffer.length + bufLength * 10);


                        }
                        if (bytePlay.length < count1 * 2048 + 4096) {

                            bytePlay = Arrays.copyOf(bytePlay, count1 * 20480);
                        }
                        System.arraycopy(playByte, 0, bytePlay, count1 * 2048, 2048);
                        count1++;
                        if (powerVal.length < count + 2) {
                            powerVal = Arrays.copyOf(powerVal, powerVal.length + 1);
                        }

                        System.arraycopy(buffer, 0, recordingBuffer, count*bufLength, bufLength);

                        count++;
                        if (count == 16){
                            System.arraycopy(recordingBuffer,0,alphaBuffer,0,16000);
                            recordingBuffer = new float[16000];
                            count = 0;
                            counter = counter + 1;

                            int i =0,k=0;
                            float[] EnergyFrame = new float[alphaBuffer.length];
                            while(i<alphaBuffer.length) {

                                Energy = Arrays.copyOfRange(alphaBuffer, i, i + 320);
                                if (k<=100) {
                                    EnergyFrame[k] = EnergyStorage(Energy);
                                }
                                k++;
                                i += 160;

                                System.arraycopy(EnergyFrame,0,EnergySecond,0,100);


                                for (int debugCount=0;debugCount<EnergySecond.length-2;debugCount++){
                                    output[0] = EnergySecond[0];
                                    output[1] = EnergySecond[1];

                                    output[debugCount+2] = 0.0366 * EnergySecond[debugCount + 2] + 0.0731 * EnergySecond[debugCount + 1] + 0.0366 * EnergySecond[debugCount] + 1.3909 * output[debugCount + 1] - 0.5372 * output[debugCount];

                                    EnergyFiltered[debugCount] = output[debugCount];
                                }


                                thresholding=0.6;
                                for (int numcounter=0;numcounter<EnergyFiltered.length;numcounter++){
                                    if (EnergyFiltered[numcounter] > thresholding){
                                        EnergyFilteredthreshold[numcounter] = EnergyFiltered[numcounter];
                                    }
                                    else{
                                        EnergyFilteredthreshold[numcounter] = 0;
                                    }
                                }

                                for (int index = 0;index<EnergyFilteredthreshold.length-1;index++){

                                    firstDiff = EnergyFilteredthreshold[index+1] - EnergyFilteredthreshold[index];

                                    if (firstDiff>0){
                                        flag[index] = +1;

                                    }
                                    else {
                                        flag[index] = -1;
                                    }
                                    System.arraycopy(flag,0,flagtemporary,0,100);
                                }
                                int l=0;
                                flagcounter=0;
                                while (l<99){

                                    if (flagtemporary[l] != flagtemporary[l+1] && flagtemporary[l+1] < 0){
                                        flagcounter = flagcounter+1;
                                        l=l+18;
                                    }
                                    else {
                                        l=l+1;
                                    }
                                }

                            }
                        }


                        if (SendButton1 == 1){
                            //networktask.SendDataToNetwork(flagcounter*2+"");
                            if (flagcounter*2 == 10 ){
                                networktask.SendDataToNetwork("10");
                            }


//
//                           if (flagcounter*2 == 12){
//                               networktask.SendDataToNetwork("A");
//                           }
                            else {
                                networktask.SendDataToNetwork(flagcounter*2+"");
                            }
                        }

                        if (SendButton1 == 2 && count == 8){
                            networktask.SendDataToNetwork((int) pitch+"");
                        }


                        text.setText("Time: " + time + "" + " "+ " " + " "+"Speaking Rate: " + flagcounter*2+ " " + " "+ " " +"Pitch: " + pitch) ;
                        Energy100 = Arrays.copyOfRange(EnergyArray,0,100);
                    }


                });

            }

        }));
        new Thread(dispatcher, "Audio Dispatcher").start();

    }

    private float EnergyStorage(final float[] buffer) {
        float power = 0;
        for (float element : buffer) {
            power += element * element;
        }
        return power;
    }


    private void StopRecording(){
        dispatcher.stop();
        stopRec.setEnabled(false);
        SendButton1 = 0;
        textStatus.setText("Stopped ... Press Start Recording ");
    }

    public void playRecord() {
        ShortBuffer sBuf = ByteBuffer.wrap(bytePlay).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        final short[] shorts = new short[sBuf.capacity()];
        sBuf.get(shorts);

        try {
            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    16000, //hard coded! not ideal
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    shorts.length,
                    AudioTrack.MODE_STREAM);
            audioTrack.play();
            // Write the music buffer to the AudioTrack object
            audioTrack.write(shorts, 0, shorts.length);
        }

        catch (Exception e) {
            e.printStackTrace();
        }


    }

    private OnClickListener btnStartListener = new OnClickListener() {
        public void onClick(View v){
            btnStart.setVisibility(View.INVISIBLE);
            networktask = new NetworkTask();
            networktask.execute();

        }
    };

    private OnClickListener btnSendListener = new OnClickListener() {
        public void onClick(View v) {
            textStatus.setText("Speaking rate sending to Server !!");
            SendButton1 = 1;
            //networktask.SendDataToNetwork(flagcounter*2+"");


        }

    };

    private OnClickListener btnSend2Listener = new OnClickListener() {
        public void onClick(View v) {
            textStatus.setText("Pitch sending to Server !!");
            SendButton1 = 2;
            //networktask.SendDataToNetwork(flagcounter*2+"");


        }

    };



//    public int R(final float[] buffer) {
//        float power = 0;
//        for (float element : buffer) {
//            power += element * element;
//        }
//        return power;
//    }


    public class NetworkTask extends AsyncTask<Void, byte[], Boolean> {
        Socket nsocket;
        InputStream nis;
        OutputStream nos;

        @Override
        protected void onPreExecute() {
            Log.i("AsyncTask", "onPreExecute");
        }

        @Override
        protected Boolean doInBackground(Void... params) { //This runs on a different thread
            boolean result = false;
            try {
                Log.i("AsyncTask", "doInBackground: Creating socket");
                SocketAddress sockaddr = new InetSocketAddress("169.254.1.1", 2000);
                nsocket = new Socket();
                nsocket.connect(sockaddr, 5000); //10 second connection timeout
                if (nsocket.isConnected()) {
                    nis = nsocket.getInputStream();
                    nos = nsocket.getOutputStream();
                    Log.i("AsyncTask", "doInBackground: Socket created, streams assigned");
                    Log.i("AsyncTask", "doInBackground: Waiting for inital data...");
                    byte[] buffer = new byte[4096];
                    int read = nis.read(buffer, 0, 4096); //This is blocking
                    while(read != -1){
                        byte[] tempdata = new byte[read];
                        System.arraycopy(buffer, 0, tempdata, 0, read);
                        publishProgress(tempdata);
                        Log.i("AsyncTask", "doInBackground: Got some data");
                        read = nis.read(buffer, 0, 4096); //This is blocking
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("AsyncTask", "doInBackground: IOException");
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("AsyncTask", "doInBackground: Exception");
                result = true;
            } finally {
                try {
                    nis.close();
                    nos.close();
                    nsocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("AsyncTask", "doInBackground: Finished");
            }
            return result;
        }

        public void SendDataToNetwork(String cmd) { //You run this from the main thread.
            try {
                if (nsocket.isConnected()) {
                    Log.i("AsyncTask", "SendDataToNetwork: Writing received message to socket");
                    nos.write(cmd.getBytes());
                } else {
                    Log.i("AsyncTask", "SendDataToNetwork: Cannot send message. Socket is closed");
                }
            } catch (Exception e) {
                Log.i("AsyncTask", "SendDataToNetwork: Message send failed. Caught an exception");
            }
        }

        @Override
        protected void onProgressUpdate(byte[]... values) {
            if (values.length > 0) {
                Log.i("AsyncTask", "onProgressUpdate: " + values[0].length + " bytes received.");
                textStatus.setText(new String(values[0]));
            }
        }
        @Override
        protected void onCancelled() {
            Log.i("AsyncTask", "Cancelled.");
            btnStart.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Log.i("AsyncTask", "onPostExecute: Completed with an Error.");
                textStatus.setText("There was a connection error.");
            } else {
                Log.i("AsyncTask", "onPostExecute: Completed.");
            }
            btnStart.setVisibility(View.VISIBLE);
        }
    }




}

