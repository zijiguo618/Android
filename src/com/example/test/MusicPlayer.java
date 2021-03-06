package com.example.test;

import android.support.v7.app.ActionBarActivity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.Visualizer;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import com.example.test.MusicService.MusicBinder;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.TextView;
import android.os.IBinder;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.MediaColumns;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.widget.MediaController.MediaPlayerControl;
import android.media.audiofx.Equalizer;

@SuppressLint("NewApi")
public class MusicPlayer extends Activity implements
MediaController.MediaPlayerControl {
	private ArrayList<Song> songList;
	private ListView songView;
	private static final String SD_PATH = "/sbcard/";
	private static final float VISUALIZER_HEIGHT_DIP = 50f;
	private MusicService musicSrv;
	private Intent playIntent;
	private boolean musicBound = false;
	private MediaController controller;
	private boolean paused = false, playbackPaused = false;
	private Button Settingb;
	private Button EQ;
	private VisualizerView mVisualizerView;
	private int sid = 0;
	private Equalizer mEqualizer;
	private LinearLayout mLinearLayout;
	// private MediaPlayerControl mMediaPlayer;
	private Visualizer mVisualizer;
	private MediaPlayer mMediaPlayer;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_player);
		mLinearLayout = (LinearLayout) findViewById(R.id.lineareq);
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		songView = (ListView) findViewById(R.id.song_list);
		songList = new ArrayList<Song>();
		Log.v("-------", "----------");
		
        
		getSongList();
        
		Collections.sort(songList, new Comparator<Song>() {
			@Override
			public int compare(Song a, Song b) {
				return a.getTitle().compareTo(b.getTitle());
			}
            
		});
		Log.v("---++----", "------++----");
		Song temp = new Song(1, "yiyongjun", "jinxingqu");
		ArrayList<Song> temarr = new ArrayList<Song>();
		temarr.add(temp);
		SongAdapter songAdt = new SongAdapter(this, songList);
		songView.setAdapter(songAdt);
		Log.v("controller", "begin" + this.getAudioSessionId());
		controller = new MediaController(this);
        //		controller = new MediaController();
		controller.setPrevNextListeners(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playNext();
			}
		}, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playPrev();
			}
		});
		controller.setMediaPlayer(this);
		sid = this.getAudioSessionId();
		controller.setAnchorView(findViewById(R.id.song_list));
		controller.setEnabled(true);
		Log.v("---+****+----", "------+*****+----" + this.getAudioSessionId());
        
		setupVisualizerFxAndUI();
		setupEqualizerFxAndUI();
		mVisualizer.setEnabled(true);
		mMediaPlayer
        .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                mVisualizer.setEnabled(false);
            }
        });
        
		// mMediaPlayer.release();
	}
    
	@Override
	protected void onDestroy() {
		stopService(playIntent);
		musicSrv = null;
		super.onDestroy();
	}
    
	private ServiceConnection musicConnection = new ServiceConnection() {
    
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
    MusicBinder binder = (MusicBinder) service;
    // get service
    musicSrv = binder.getService();
    // pass list
    musicSrv.setList(songList);
    musicBound = true;
}

@Override
public void onServiceDisconnected(ComponentName name) {
musicBound = false;
}
};

@Override
protected void onStart() {
super.onStart();
if (playIntent == null) {
playIntent = new Intent(this, MusicService.class);
bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
startService(playIntent);
}
}

@Override
protected void onPause() {
super.onPause();
paused = true;
}

@Override
protected void onResume() {
super.onResume();
if (paused) {
setController();
paused = false;
}
}

@Override
protected void onStop() {
controller.hide();
super.onStop();
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
getMenuInflater().inflate(R.menu.song, menu);
return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
int id = item.getItemId();
if (id == R.id.action_settings) {
return true;
}
switch (item.getItemId()) {
case R.id.action_end:
stopService(playIntent);
musicSrv = null;
System.exit(0);
break;
case R.id.action_shuffle:
musicSrv.setShuffle();
break;
}
return super.onOptionsItemSelected(item);
}

public void getSongList() {
Log.v("ba qi", "---------------------------------------------");
ContentResolver musicResolver = getContentResolver();
Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
// Uri temppath =
// Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.sebastian);
Cursor musicCursor = musicResolver.query(musicUri, null, null, null,
null);

try {
mMediaPlayer.setDataSource(getApplicationContext(), musicUri);
mMediaPlayer.prepare();
} catch (IllegalArgumentException e) {
// TODO Auto-generated catch block
e.printStackTrace();
} catch (SecurityException e) {
// TODO Auto-generated catch block
e.printStackTrace();
} catch (IllegalStateException e) {
// TODO Auto-generated catch block
e.printStackTrace();
} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
// mMediaPlayer = MediaPlayer.create(this,musicUri);

if (musicCursor != null && musicCursor.moveToFirst()) {
// get columns
Log.v("ba qi", "-------------------233--------------------------"
+ musicCursor.getCount());
int titleColumn = musicCursor.getColumnIndex(MediaColumns.TITLE);
int idColumn = musicCursor.getColumnIndex(BaseColumns._ID);
int artistColumn = musicCursor.getColumnIndex(AudioColumns.ARTIST);
Log.v("gao duan", "---------------------------------------------"
+ titleColumn);
// add songs to list
do {

long thisId = musicCursor.getLong(idColumn);
String thisTitle = musicCursor.getString(titleColumn);
String thisArtist = musicCursor.getString(artistColumn);
songList.add(new Song(thisId, thisTitle, thisArtist));
Log.v("gao duan", "ID:" + thisId + " tittle:" + thisTitle
+ " artist:" + thisArtist + " sessionID:"
+ mMediaPlayer.getAudioSessionId()+"   "+this.getAudioSessionId());

} while (musicCursor.moveToNext());
}
}

public void songPicked(View view) {
musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
musicSrv.playSong();
if (playbackPaused) {
setController();
playbackPaused = false;
}
controller.show(0);
}

private void setController() {

Log.v("controller", "begin" + this.getAudioSessionId());
controller = new MediaController(this);
controller.setPrevNextListeners(new View.OnClickListener() {
@Override
public void onClick(View v) {
playNext();
}
}, new View.OnClickListener() {
@Override
public void onClick(View v) {
playPrev();
}
});
controller.setMediaPlayer(this);
sid = this.getAudioSessionId();
controller.setAnchorView(findViewById(R.id.song_list));
controller.setEnabled(true);
}

private void playNext() {
musicSrv.playNext();
if (playbackPaused) {
setController();
playbackPaused = false;
}
controller.show(0);
}

// play previous
private void playPrev() {
musicSrv.playPrev();
if (playbackPaused) {
setController();
playbackPaused = false;
}
controller.show(0);
}

@Override
public void pause() {
playbackPaused = true;
musicSrv.pausePlayer();
}

@Override
public void seekTo(int pos) {
musicSrv.seek(pos);
}

@Override
public void start() {
musicSrv.go();
}

@Override
// TODO Auto-generated method stub
public int getDuration() {
if (musicSrv != null && musicBound && musicSrv.isPng())
return musicSrv.getDur();
else
return 0;
}

@Override
public int getCurrentPosition() {
// TODO Auto-generated method stub
if (musicSrv != null && musicBound && musicSrv.isPng())
return musicSrv.getPosn();
else
return 0;
}

@Override
public boolean isPlaying() {
// TODO Auto-generated method stub
if (musicSrv != null && musicBound)
return musicSrv.isPng();
return false;
}

@Override
public int getBufferPercentage() {
// TODO Auto-generated method stub
return 0;
}

@Override
public boolean canPause() {
// TODO Auto-generated method stub
return true;
}

@Override
public boolean canSeekBackward() {
// TODO Auto-generated method stub
return true;
}

@Override
public boolean canSeekForward() {
// TODO Auto-generated method stub
return true;
}

private void setupVisualizerFxAndUI() {
// Create a VisualizerView (defined below), which will render the
// simplified audio
// wave form to a Canvas.
mVisualizerView = new VisualizerView(this);
mVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(
ViewGroup.LayoutParams.FILL_PARENT,
(int) (VISUALIZER_HEIGHT_DIP * getResources()
.getDisplayMetrics().density)));
mLinearLayout.addView(mVisualizerView);

// Create the Visualizer object and attach it to our media player.
mVisualizer = new Visualizer(0);
mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
mVisualizer.setDataCaptureListener(
new Visualizer.OnDataCaptureListener() {
public void onWaveFormDataCapture(Visualizer visualizer,
byte[] bytes, int samplingRate) {
mVisualizerView.updateVisualizer(bytes);
}

public void onFftDataCapture(Visualizer visualizer,
byte[] bytes, int samplingRate) {
}
}, Visualizer.getMaxCaptureRate() / 2, true, false);
}

private void setupEqualizerFxAndUI() {
// Create the Equalizer object (an AudioEffect subclass) and attach it
// to our media player,
// with a default priority (0).
mEqualizer = new Equalizer(0, 0);
mEqualizer.setEnabled(true);

TextView eqTextView = new TextView(this);
eqTextView.setText("Equalizer:");
mLinearLayout.addView(eqTextView);

short bands = mEqualizer.getNumberOfBands();

final short minEQLevel = mEqualizer.getBandLevelRange()[0];
final short maxEQLevel = mEqualizer.getBandLevelRange()[1];

for (short i = 0; i < bands; i++) {
final short band = i;

TextView freqTextView = new TextView(this);
freqTextView.setLayoutParams(new ViewGroup.LayoutParams(
ViewGroup.LayoutParams.FILL_PARENT,
ViewGroup.LayoutParams.WRAP_CONTENT));
freqTextView.setGravity(Gravity.CENTER_HORIZONTAL);
freqTextView.setText((mEqualizer.getCenterFreq(band) / 1000)
+ " Hz");
freqTextView.setTextColor(Color.WHITE);
mLinearLayout.addView(freqTextView);

LinearLayout row = new LinearLayout(this);
row.setOrientation(LinearLayout.HORIZONTAL);

TextView minDbTextView = new TextView(this);
minDbTextView.setLayoutParams(new ViewGroup.LayoutParams(
ViewGroup.LayoutParams.WRAP_CONTENT,
ViewGroup.LayoutParams.WRAP_CONTENT));
minDbTextView.setText((minEQLevel / 100) + " dB");
minDbTextView.setTextColor(Color.WHITE);
TextView maxDbTextView = new TextView(this);
maxDbTextView.setLayoutParams(new ViewGroup.LayoutParams(
ViewGroup.LayoutParams.WRAP_CONTENT,
ViewGroup.LayoutParams.WRAP_CONTENT));
maxDbTextView.setText((maxEQLevel / 100) + " dB");
maxDbTextView.setTextColor(Color.WHITE);
LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
ViewGroup.LayoutParams.FILL_PARENT,
ViewGroup.LayoutParams.WRAP_CONTENT);
layoutParams.weight = 1;
SeekBar bar = new SeekBar(this);
bar.setLayoutParams(layoutParams);
bar.setMax(maxEQLevel - minEQLevel);
bar.setProgress(mEqualizer.getBandLevel(band));

bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
public void onProgressChanged(SeekBar seekBar, int progress,
boolean fromUser) {
mEqualizer.setBandLevel(band,
(short) (progress + minEQLevel));
}

public void onStartTrackingTouch(SeekBar seekBar) {
}

public void onStopTrackingTouch(SeekBar seekBar) {
}
});

row.addView(minDbTextView);
row.addView(bar);
row.addView(maxDbTextView);

mLinearLayout.addView(row);
}
}

@Override
public int getAudioSessionId() {
// TODO Auto-generated method stub
return 0;
}

}
