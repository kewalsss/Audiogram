package pl.edu.wat.audiogram;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class ResultActivity extends ActionBarActivity {

    public int[] left;
    public int[] right;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle("Wyniki badania");

        left = getIntent().getBundleExtra("bundle").getIntArray("left");
        right = getIntent().getBundleExtra("bundle").getIntArray("right");

        calculateHearLoss();


        LineChart chart = (LineChart) findViewById(R.id.chart);
        //tworzenie danych do wykresu
        ArrayList<Entry> leftEar = new ArrayList<Entry>();
        ArrayList<Entry> rightEar = new ArrayList<Entry>();


        Entry li , pi;
        for (int i = 0; i < ExaminationActivity.FREQUENCY.length; i++) {
            li = new Entry(left[i], i);
            leftEar.add(li);
            pi = new Entry(right[i], i);
            rightEar.add(pi);
        }

        LineDataSet setLeft = new LineDataSet(leftEar, "Lewe ucho");
        LineDataSet setRight = new LineDataSet(rightEar, "Prawe ucho");

        //kolory linii na wykresie
        setLeft.setColor(Color.GREEN);
        setRight.setColor(Color.RED);
        setLeft.setCircleColor(Color.GREEN);
        setRight.setCircleColor(Color.RED);


        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(setLeft);
        dataSets.add(setRight);

        ArrayList<String> xVals = new ArrayList<String>();

        for ( int i=0; i<ExaminationActivity.FREQUENCY.length; i++) {xVals.add(String.valueOf(ExaminationActivity.FREQUENCY[i]));}

        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        chart.setTouchEnabled(true);
        chart.setPinchZoom(true);
        chart.getAxisRight().setEnabled(false); // nie wyświetla prawej osi współrzędnych
        chart.setDescription("Ubytek słuchu dla badanych częstotliwości");
        chart.setMaxVisibleValueCount(0);

        //wszystkie wartości zmieszczą się na ekranie
        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelsToSkip(0);
        xAxis.setDrawGridLines(false);


        YAxis leftAxis = chart.getAxisLeft();


        leftAxis.setLabelCount(ExaminationActivity.AMPLITUDE.length);
        leftAxis.setAxisMaxValue(100);
        leftAxis.setAxisMinValue(-10);
        leftAxis.setStartAtZero(false);
        leftAxis.setInverted(true);


        chart.invalidate(); // refresh

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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

    private void calculateHearLoss(){

        //deklarowanie zmiennych z tablicy do lewego i prawego ucha, aby wyliczć ubytki
        int x1=left[1];
        int x2=left[2];
        int x3=left[3];
        int x5=left[5];
        int wynik1;  //ubytek na lewym uchu

        int y1=right[1];
        int y2=right[2];
        int y3=right[3];
        int y5=right[5];
        int wynik2; //ubytek na prawym uchu

        int wynik;
//lewe ucho
        if ((x3-x1)>40 && (x5>x3))
        {wynik1=((x1+x2+x5)/3);}
        else if ((x3-x1)>40)
        {wynik1=((x1+x2+x3+x5)/4);}
        else {wynik1=((x1+x2+x3)/3);}

//prawe ucho
        if ((y3-y1)>40 && (y5>y3))
        {wynik2=((y1+y2+y5)/3);}
        else if ((y3-y1)>40)
        {wynik2=((y1+y2+y3+y5)/4);}
        else {wynik2=((y1+y2+y3)/3);}

//obliczenie finalnego wyniku
        if ((wynik1-wynik2)>25 && (wynik1>wynik2))
        {wynik=wynik1+5;}
        else if ((wynik2-wynik1)>25 && (wynik2>wynik1))
        {wynik=wynik2+5;}
        else if (wynik1>wynik2)
        {wynik=wynik1;}
        else {wynik=wynik2;}



        TextView hearLossValue = (TextView) findViewById(R.id.hearLoss);

        hearLossValue.setText(String.format("Ubytek słuchu wynosi %ddB\nlewe ucho: %ddB, prawe ucho: %ddB"  , wynik, wynik1, wynik2));




    }
}
