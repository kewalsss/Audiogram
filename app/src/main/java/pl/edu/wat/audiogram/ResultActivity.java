package pl.edu.wat.audiogram;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends ActionBarActivity {

    public int[] left;
    public int[] right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        left = getIntent().getBundleExtra("bundle").getIntArray("left");
        right = getIntent().getBundleExtra("bundle").getIntArray("right");

        calculateHearLoss();
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

        hearLossValue.setText(String.format("Ubytek słuchu wynosi:\nlewe ucho - %ddB \nprawe ucho - %ddB \nłącznie - %ddB"  , wynik1, wynik2, wynik));




    }
}
