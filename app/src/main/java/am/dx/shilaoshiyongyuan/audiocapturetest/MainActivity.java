package am.dx.shilaoshiyongyuan.audiocapturetest;

import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.support.v4.view.GestureDetectorCompat;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

    private MediaRecorder myRecorder;
    private MediaPlayer myPlayer;
    private String outputFile = null;
    private Button playBtn;
    private Button stopPlayBtn;
    private ToggleButton toggleButton;


    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetectorCompat mDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainactivity);



        mDetector = new GestureDetectorCompat(this,this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);



        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

/*
        startBtn = (Button)findViewById(R.id.start);
        startBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setFile();
                start();
            }
        });

        stopBtn = (Button)findViewById(R.id.stop);
        stopBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                stop(v);
            }
        });

*/

        //set toggle button functions (on button change event...), start thread when ON, kill thread when OFF
        toggleButton = (ToggleButton) findViewById(R.id.startstop);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    start();
                }
                else stop();
            }
        });

/*
        playBtn = (Button)findViewById(R.id.play);
        playBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                play(v);
            }
        });

        stopPlayBtn = (Button)findViewById(R.id.stopPlay);
        stopPlayBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                stopPlay(v);
            }
        });
*/

    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    public void viewSongs(){
        // Create Intent and Start The Activity
        Intent songView=new Intent(getApplicationContext(),SongList.class);
        startActivity(songView);

    }

    public void setFile(){

        final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myRecordings/");

        if (!dir.exists())
        {
            dir.mkdir();
        }

        final String name;
        EditText filename = (EditText) findViewById(R.id.fileName);
        name = filename.getText().toString();
        // store it to sd card
        outputFile = dir.getAbsolutePath() + "/" + name + ".mp4";

        myRecorder.setOutputFile(outputFile);
    }



    public void start(){

        setFile();

        try {
            myRecorder.prepare();
            myRecorder.start();
        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            //e.printStackTrace();
        } catch (IOException e) {
            // prepare() fails
           // e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "Start recording...",
                Toast.LENGTH_SHORT).show();
    }

    public void stop(){
        try {
            myRecorder.stop();
            myRecorder.release();
            myRecorder  = null;

            Toast.makeText(getApplicationContext(), "Stop recording...",
                    Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            //  it is called before start(), thown if folder not exists!
            //e.printStackTrace();

        } catch (RuntimeException e) {
            // no valid audio/video data has been received
            //e.printStackTrace();

        }
    }

    /*
    public void play(View view) {
        try{
            myPlayer = new MediaPlayer();
            myPlayer.setDataSource(outputFile);
            myPlayer.prepare();
            myPlayer.start();

            playBtn.setEnabled(false);
            stopPlayBtn.setEnabled(true);

        } catch (Exception e) {
            // TODO Auto-generated catch block
           // e.printStackTrace();
        }
    }

    public void stopPlay(View view) {
        try {
            if (myPlayer != null) {
                myPlayer.stop();
                myPlayer.release();
                myPlayer = null;
                playBtn.setEnabled(true);
                stopPlayBtn.setEnabled(false);

                Toast.makeText(getApplicationContext(), "Stop playing the recording...",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
    }
*/

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

        Log.d("NICK", "long press...");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            // right to left swipe
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                //Toast.makeText(SelectFilterActivity.this, "Left Swipe", Toast.LENGTH_SHORT).show();
                Log.d("NICK", "swiped left!");
                viewSongs();
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                //Toast.makeText(SelectFilterActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
                Log.d("NICK", "swiped right!");

            }
        } catch (Exception e) {
            // nothing
        }
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
}