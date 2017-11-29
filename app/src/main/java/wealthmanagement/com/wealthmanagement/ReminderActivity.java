package wealthmanagement.com.wealthmanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;

import wealthmanagement.com.wealthmanagement.adapter.CustomAdapter;

public class ReminderActivity extends AppCompatActivity {

    String[] countryNames={"Entertainment","Food","Medical","Cloathes","Gift","Auto","Travelling","Stationary"};
    int flags[] = {R.drawable.entertainment, R.drawable.food, R.drawable.medical, R.drawable.cloath, R.drawable.gift, R.drawable.auto, R.drawable.travelling, R.drawable.stationar};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        ReminderActivity.this.setTitle("iAccounts");
        Spinner spin = (Spinner) findViewById(R.id.categorySpinner);

        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),flags,countryNames);
        spin.setAdapter(customAdapter);
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
}
