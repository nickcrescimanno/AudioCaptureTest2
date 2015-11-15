package am.dx.shilaoshiyongyuan.audiocapturetest;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import am.dx.shilaoshiyongyuan.audiocapturetest.name.HttpFileUpload;


public class SongList extends Activity {

    private MediaPlayer myPlayer;


    public void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_song_list);

        // Get the reference of ListViewAnimals
        ListView songs=(ListView)findViewById(R.id.songList);

        //make list of songs
        final File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/myRecordings/");
        File[] filelist = dir.listFiles();
        final String[] theNamesOfFiles = new String[filelist.length];
        for (int i = 0; i < theNamesOfFiles.length; i++) {
            theNamesOfFiles[i] = filelist[i].getName();
        }

        //creates adapter, pass names of songs
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, theNamesOfFiles);

        // Set The Adapter
        songs.setAdapter(arrayAdapter);

        // register onClickListener to handle click events on each item
        songs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

                try {

                    myPlayer = new MediaPlayer();
                    myPlayer.setDataSource(dir + "/" + theNamesOfFiles[position]);
                    myPlayer.prepare();
                    myPlayer.start();

                    //playBtn.setEnabled(false);
                    //stopPlayBtn.setEnabled(true);

                    Log.d("NICK", dir + "/" + theNamesOfFiles[position]);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                }

            }
        });

    }

    //Here’s the function to call the HttpFileUpload and Start sending the file and the data :
    public void UploadFile(){
        try {
            // Set your file path here
            FileInputStream fstrm = new FileInputStream(Environment.getExternalStorageDirectory().toString()+"/DCIM/file.mp4");

            // Set your server page url (and the file title/description)
            HttpFileUpload hfu = new HttpFileUpload("http://www.myurl.com/fileup.aspx", "my file title","my file description");

            hfu.Send_Now(fstrm);

        } catch (FileNotFoundException e) {
            // Error: File not found
        }
    }
}
