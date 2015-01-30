package es.caballero.frog;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MainActivity extends Activity implements View.OnClickListener {
    private int points;
    private int round;
    private int countdown;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void newGame() {
        points = 0;
        round = 1;
        initRound();
    }

    private void initRound() {
        countdown = 10;
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
        }
    }

    private void startGame() {
        newGame();
    }
}
