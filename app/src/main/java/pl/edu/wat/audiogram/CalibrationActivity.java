package pl.edu.wat.audiogram;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;


public class CalibrationActivity extends Activity {

    final private int MAX = 1000, DIV = 200000;
    private ToneGenerator toneGenerator;
    private SeekBar seekBar;
    private float p ;

    TextView seekBarValue ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        toneGenerator = new ToneGenerator();
        toneGenerator.playCalibration();
        toneGenerator.setCalibrationVolume(0);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(MAX);
        seekBar.setProgress(0);
        seekBarValue = (TextView) findViewById(R.id.seekbarvalue);

        //zablokowanie wygaszania ekranu
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                toneGenerator.setCalibrationVolume((float) progress / DIV);
                seekBarValue.setText(String.valueOf(progress));
                p = (float) progress / DIV;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calibration, menu);
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

    public void onSaveClick(View view) {
        ToneGenerator.calibration = p;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toneGenerator.stop();
    }
}
