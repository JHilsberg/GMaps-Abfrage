package de.hszg.julian.gm_abfrage;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;


public class GMQuery extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmquery);

        //StrictMode.ThreadPolicy tp = StrictMode.ThreadPolicy.LAX;
        //StrictMode.setThreadPolicy(tp);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gmquery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true; }
        return super.onOptionsItemSelected(item);
    }

    public void goButtonIsClicked(View v) {
        new GetGMData() {
            @Override
            public void onPostExecute(String result) {
                try { showCoordinates(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.execute(getInputAddress());
    }

    public String getInputAddress() {
        EditText address_input = (EditText) findViewById(R.id.address_input);
        return address_input.getText().toString();
    }

    public void showCoordinates(String coordinatesData) throws IOException {
        TextView responseText = (TextView) findViewById(R.id.coordinates_text);
        responseText.setText(coordinatesData);
    }
}
