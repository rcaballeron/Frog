package es.caballero.frog;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import es.caballero.tools.BaseGameActivity;
import es.caballero.tools.SimpleAnimationListener;


public class MainActivity extends BaseGameActivity implements View.OnClickListener {
    public static final String TYPEFACE_TITLE = "JandaManateeSolid";
    private static final int FROG_HEIGHT = 72;
    private static final int FROG_WIDTH = 64;
    private int points;
    private int round;
    private int countdown;
    private int highscore;
    private ImageView frog;
    private Random rnd = new Random();
    private Handler handler = new Handler();
    private Typeface ttf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Manage and assign font to textviews
        addTypeface(TYPEFACE_TITLE);
        setTypefaceToViews();

        findViewById(R.id.help).setOnClickListener(this);
        showStartFragment();
    }

    private void setTypefaceToViews() {
        setTypeface((TextView) findViewById(R.id.countdown), TYPEFACE_TITLE);
        setTypeface((TextView) findViewById(R.id.round), TYPEFACE_TITLE);
        setTypeface((TextView) findViewById(R.id.points), TYPEFACE_TITLE);
        setTypeface((TextView) findViewById(R.id.help), TYPEFACE_TITLE);
        setTypeface((TextView) findViewById(R.id.highscore), TYPEFACE_TITLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void newGame() {
        points = 0;
        round = 1;
        initRound();
    }

    private void initRound() {
        countdown = 10;
        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        container.removeAllViews();

        WimmelView wv = new WimmelView(this);
        container.addView(wv, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        wv.setImageCount(WimmelView.NUMBER_DISTRACT_IMAGES * (10 + round));

        frog = new ImageView(this);
        frog.setId(R.id.A121212);
        frog.setImageResource(R.drawable.frog);
        frog.setScaleType(ImageView.ScaleType.CENTER);
        float scale = getResources().getDisplayMetrics().density;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                Math.round(FROG_WIDTH * scale),
                Math.round(FROG_HEIGHT * scale));
        lp.leftMargin = rnd.nextInt(container.getWidth() - Math.round(FROG_WIDTH * scale));
        lp.topMargin = rnd.nextInt(container.getHeight() - Math.round(FROG_HEIGHT * scale));
        lp.gravity = Gravity.TOP + Gravity.LEFT;
        frog.setOnClickListener(this);
        container.addView(frog, lp);

        handler.postDelayed(runnable, 1000 - round * 50);

        update();
    }

    private void fillTextView(int id, String text) {
        TextView tv = (TextView)findViewById(id);
        tv.setText(text);
    }

    private void update() {
        fillTextView(R.id.points, Integer.toString(points));
        fillTextView(R.id.round, Integer.toString(round));
        fillTextView(R.id.countdown, Integer.toString(countdown * 1000));
        loadHighScore();
        fillTextView(R.id.highscore, Integer.toString(highscore));
    }

    private void showStartFragment() {
        ViewGroup container = (ViewGroup)findViewById(R.id.container);
        container.removeAllViews();
        View start = getLayoutInflater().inflate(R.layout.fragment_start, null);
        container.addView(start);
        ((TextView)findViewById(R.id.title)).setTypeface(ttf);
        ((TextView)findViewById(R.id.btn_start)).setTypeface(ttf);

        Animation a = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        a.setAnimationListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                registerStarButtonListener();
            }
        });
        start.startAnimation(a);
    }

    private void registerStarButtonListener() {
        ViewGroup container = (ViewGroup)findViewById(R.id.container);
        container.findViewById(R.id.btn_start).setOnClickListener(this);
    }

    private void showGameOverFragment() {
        ViewGroup container = (ViewGroup)findViewById(R.id.container);
        container.removeAllViews();
        container.addView(getLayoutInflater().inflate(R.layout.fragment_gameover, null));
        ((TextView)findViewById(R.id.btn_play_again)).setTypeface(ttf);
        ((TextView)findViewById(R.id.title)).setTypeface(ttf);

        container.findViewById(R.id.btn_play_again).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_start) {
            Animation a = AnimationUtils.loadAnimation(this, R.anim.pulse);
            a.setAnimationListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    startGame();
                }
            });
            v.startAnimation(a);
        } else if (v.getId() == R.id.btn_play_again) {
            showStartFragment();
        } else if (v.getId() == R.id.A121212) {
            kissFrog();
        } else if (v.getId() == R.id.help) {
            showTutorial();
        }
    }

    private void showTutorial() {
        final Dialog dialog = new Dialog(
                this,
                android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_tutorial);
        ((TextView)dialog.findViewById(R.id.text)).setTypeface(ttf);
        ((TextView)dialog.findViewById(R.id.start)).setTypeface(ttf);
        dialog.findViewById(R.id.start).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        startGame();
                    }
                }
        );
        dialog.show();
    }

    private void kissFrog() {
        handler.removeCallbacks(runnable);
        showToast(R.string.kissed);
        points += countdown * 1000;
        round++;
        initRound();

    }

    private void showToast(int stringId) {
        Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER, 0 , 0);
        toast.setDuration(Toast.LENGTH_SHORT);

        TextView textView = new TextView(this);
        textView.setText(stringId);
        textView.setTextColor(getResources().getColor(R.color.points));
        textView.setTextSize(48f);
        textView.setTypeface(ttf);

        toast.setView(textView);
        toast.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    private void startGame() {
        newGame();
    }

    private void countdown() {
        countdown--;
        update();
        if (countdown <= 0) {
            frog.setOnClickListener(null);
            if (points > highscore) {
                saveHighScore(points);
            }
            showGameOverFragment();
        } else {
            handler.postDelayed(runnable, 1000 - round * 50);
        }
    }

    private void loadHighScore() {
        SharedPreferences sp =getPreferences(MODE_PRIVATE);
        highscore = sp.getInt("highscore", 0);
    }



    private void saveHighScore(int points) {
        highscore = points;
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putInt("highscore", highscore);
        e.commit();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            countdown();
        }
    };
}
