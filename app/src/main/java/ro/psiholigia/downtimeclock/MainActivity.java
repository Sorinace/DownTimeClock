package ro.psiholigia.downtimeclock;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button normalMeeting;
    Button doubleMeeting;
    TextView timeTextView;
    FloatingActionButton fab;
    FloatingActionButton fab_mute;
    FloatingActionButton fab_exit;
    ImageView backImageView;

    final int normalHour = 50 * 60;
    final int doubleHour = 90 * 60;

    CountDownTimer countDownTimer;
    int buttonPress = 1;
    boolean start = false;
    // check if is on the last 10 minutes (in delay)
    boolean minus = false;

    // counters variables
    int hours;
    int minute;
    int seconds;

    // sounds
    MediaPlayer mediaPlayer;

    public void normalMeeting (View view){
        normalMeeting.setEnabled(false);
        doubleMeeting.setEnabled(true);
        timeTextView.setText("0:50:00");
        buttonPress = 1;
        setCount(normalHour);
        mediaPlayer.stop();
        timeTextView.setTextColor(Color.rgb(255,255,255));
        fab_mute.hide();
        minus = false;
        start = false;
    }

    public void doubleMeeting (View view){
        normalMeeting.setEnabled(true);
        doubleMeeting.setEnabled(false);
        timeTextView.setText("1:30:00");
        buttonPress = 2;
        setCount(doubleHour);
        mediaPlayer.stop();
        timeTextView.setTextColor(Color.rgb(255,255,255));
        fab_mute.hide();
        minus = false;
        start = false;
    }


    private void unlockScreen() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        normalMeeting = findViewById(R.id.normalMeeting);
        doubleMeeting = findViewById(R.id.dobleMeeting);
        timeTextView = findViewById(R.id.timeTextView);

        backImageView = findViewById(R.id.backImageView);

        timeTextView.setTextColor(Color.rgb(255,255,255));
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.end);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To start the timer...
                if (!start) {
                    countDownTimer.start();
                    normalMeeting.setEnabled(false);
                    doubleMeeting.setEnabled(false);
                    fab.setImageResource(android.R.drawable.ic_media_pause);
                    start = true;
                    randomImages();
                    fab_exit.hide();
                } else {
                    countDownTimer.cancel();
                    normalMeeting.setEnabled(true);
                    doubleMeeting.setEnabled(true);
                    if (minus){
                        setCount(normalHour);
                        timeTextView.setTextColor(Color.rgb(255,255,255));
                        fab_mute.hide();
                        minus = false;
                    } else {
                        setCount(hours * 3600 + minute * 60 + seconds);
                    }
                    fab.setImageResource(android.R.drawable.ic_media_play);
                    start = false;
                    fab_exit.show();
                }
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        fab_mute = findViewById(R.id.fab_mute);
        fab_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To mute ...
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    normalMeeting(null);
                }
            }
        });

        fab_exit = findViewById(R.id.fab_exit);
        fab_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To exit ...
                System.exit(0);
            }
        });

        if (buttonPress ==1) {
            normalMeeting(null);

            //setCount(5);

        } else {
            doubleMeeting(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Snackbar.make(backImageView, "Is not yet implemented! \n I will come soon.", Snackbar.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setCount(int secondSet) {
        countDownTimer = new CountDownTimer((secondSet + 600) * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //this will be called every second.
                int secRemain = (int) (millisUntilFinished / 1000) - 600;
                hours = secRemain / 3600;
                minute = (secRemain - 3600 * hours) / 60;
                seconds = secRemain  - (3600 * hours + 60 * minute);

                switch ((int)(millisUntilFinished/1000)) {
                    case 600:
                    case 595:
                    case 590:
                                Snackbar.make(fab, "Timpul s-a terminat !!!", Snackbar.LENGTH_LONG).show();
                                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.alert5);
                                mediaPlayer.start();
                                timeTextView.setTextColor(Color.rgb(255,255,0));
                                minus = true;
                                fab_mute.show();
                                unlockScreen();
                        break;
                    case 450:   mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.alert2);
                                mediaPlayer.start();
                                timeTextView.setTextColor(Color.rgb(255,200,0));
                                unlockScreen();
                        break;
                    case 300:   mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.alert3);
                                mediaPlayer.start();
                                timeTextView.setTextColor(Color.rgb(255,150,0));
                                unlockScreen();
                        break;
                    case 150:   mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.alert4);
                                mediaPlayer.start();
                                timeTextView.setTextColor(Color.rgb(255,0,0));
                                unlockScreen();
                        break;
                }

                String time = "";
                if ((millisUntilFinished / 1000) >= 600) {
                    time = String.valueOf(hours);
                    time += ":";
                } else {
                    time += "- ";
                }
                if (minute < 0) minute = -minute;
                if (seconds < 0) seconds = -seconds;
                if (minute > 9){
                    time += minute;
                } else {
                    time = time + "0" + minute;
                }
                time += ":";
                if (seconds > 9){
                    time += seconds;
                } else {
                    time = time + "0" + seconds;
                }

                timeTextView.setText(time);
            }

            @Override
            public void onFinish() {
                // final sound
                timeTextView.setText("Stop!");
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.end);
                mediaPlayer.start();
                fab.setImageResource(android.R.drawable.ic_media_play);
                Snackbar.make(fab, "Timpul s-a terminat de acum 10 minute!!!", Snackbar.LENGTH_LONG).show();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        normalMeeting(null);
                    }
                });
            }
        };
    }


    public void randomImages() {
        int min = 1;
        int max = 14;

        Random r = new Random();
        int picture = r.nextInt(max - min + 1) + min;

        switch (picture){
            case 1: backImageView.setImageResource(R.drawable.bak1);
                break;
            case 2: backImageView.setImageResource(R.drawable.bak2);
                break;
            case 3: backImageView.setImageResource(R.drawable.bak3);
                break;
            case 4: backImageView.setImageResource(R.drawable.bak4);
                break;
            case 5: backImageView.setImageResource(R.drawable.bak5);
                break;
            case 6: backImageView.setImageResource(R.drawable.bak6);
                break;
            case 7: backImageView.setImageResource(R.drawable.bak7);
                break;
            case 8: backImageView.setImageResource(R.drawable.bak8);
                break;
            case 9: backImageView.setImageResource(R.drawable.bak9);
                break;
            case 10: backImageView.setImageResource(R.drawable.bak10);
                break;
            case 11: backImageView.setImageResource(R.drawable.bak11);
                break;
            case 12: backImageView.setImageResource(R.drawable.bak12);
                break;
            case 13: backImageView.setImageResource(R.drawable.bak13);
                break;
            case 14: backImageView.setImageResource(R.drawable.bak14);
                break;

        }
    }

}
