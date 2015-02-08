package es.caballero.frog;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends Activity implements View.OnClickListener {
    private static final int FROG_ID = 212121;
    private static final int FROG_HEIGHT = 72;
    private static final int FROG_WIDTH = 64;
    private int points;
    private int round;
    private int countdown;
    private ImageView frog;
    private Random rnd = new Random();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showStartFragment();
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
        frog.setId(FROG_ID);
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
    }

    private void showStartFragment() {
        ViewGroup container = (ViewGroup)findViewById(R.id.container);
        container.removeAllViews();
        container.addView(getLayoutInflater().inflate(R.layout.fragment_start, null));

        container.findViewById(R.id.btn_start).setOnClickListener(this);
    }

    private void showGameOverFragment() {
        ViewGroup container = (ViewGroup)findViewById(R.id.container);
        container.removeAllViews();
        container.addView(getLayoutInflater().inflate(R.layout.fragment_gameover, null));

        container.findViewById(R.id.btn_play_again).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_start) {
            startGame();
        } else if (v.getId() == R.id.btn_play_again) {
            showStartFragment();
        } else if (v.getId() == FROG_ID) {
            handler.removeCallbacks(runnable);
            Toast.makeText(this, R.string.kissed, Toast.LENGTH_SHORT).show();
            points += countdown * 1000;
            round++;
            initRound();
        }
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
            showGameOverFragment();
        } else {
            handler.postDelayed(runnable, 1000 - round * 50);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            countdown();
        }
    };
}
