package wealthmanagement.com.wealthmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wealthmanagement.com.wealthmanagement.adapter.CustomAdapter;

public class TransactionsActivity extends AppCompatActivity {

    public static final String INCOME_URL = "http://180.149.15.140/wealthmanagement/income.php";
    Spinner categorySpinner,paymentSpinner;
    String[] countryNames={"Entertainment","Food","Medical","Cloathes","Gift","Auto","Travelling","Stationary"};
    int flags[] = {R.drawable.entertainment, R.drawable.food, R.drawable.medical, R.drawable.cloath, R.drawable.gift, R.drawable.auto, R.drawable.travelling, R.drawable.stationar};
    ArrayAdapter<String> adapter;
    List<String> list;

    boolean isIncomeSelected;
    boolean isExpenseSelected;

    EditText priceEdt,dateEdt,timeEdt,descriptionEdt;
    Button incomeBtn,expenseBtn,imageBtn,saveBtn,resetBtn;

    ImageView imageview;
    String formattedDate,currentTime, user_id,date,time,description,category,payment_method,image;

    private Uri filePath;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;

    public static final String KEY_USERID = "user_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_PAYMENTMETHOD = "payment_method";
    public static final String KEY_IMAGE = "image";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        paymentSpinner = (Spinner) findViewById(R.id.paymentSpinner);

        Calendar c = Calendar.getInstance();
        final SimpleDateFormat datevalue = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = datevalue.format(c.getTime());

        DateFormat timevalue = new SimpleDateFormat("hh:mm a");
        currentTime = timevalue.format(c.getTime());


        priceEdt = (EditText) findViewById(R.id.priceEdt);

        dateEdt = (EditText) findViewById(R.id.dateEdt);
        dateEdt.setText(formattedDate);

        timeEdt = (EditText) findViewById(R.id.timeEdt);
        timeEdt.setText(currentTime);

        descriptionEdt = (EditText) findViewById(R.id.descriptionsEdt);

        imageview = (ImageView) findViewById(R.id.image_view);

        incomeBtn = (Button) findViewById(R.id.incomeButton);
        expenseBtn = (Button) findViewById(R.id.expenseButton);
        imageBtn = (Button) findViewById(R.id.next_image);

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        saveBtn = (Button) findViewById(R.id.saveBtn);
        resetBtn = (Button) findViewById(R.id.resetBtn);

        incomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isIncomeSelected) {
                    isIncomeSelected = true;
                    isExpenseSelected = false;
                    incomeBtn.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryDark));
                    expenseBtn.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
                }
            }
        });

        expenseBtn.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryDark));
        expenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isIncomeSelected = false;
                isExpenseSelected = true;
                incomeBtn.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
                expenseBtn.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryDark));
            }
        });


        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),flags,countryNames);
        categorySpinner.setAdapter(customAdapter);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.values_array, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appear
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentSpinner.setAdapter(adapter);



        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Calendar c = Calendar.getInstance();
                final SimpleDateFormat datevalue = new SimpleDateFormat("dd-MMM-yyyy");
                formattedDate = datevalue.format(c.getTime());
                //Toast.makeText(TransactionsActivity.this, ""+formattedDate, Toast.LENGTH_SHORT).show();

                DateFormat timevalue = new SimpleDateFormat("hh:mm a");
                currentTime = timevalue.format(c.getTime());

                user_id = "1";
                date = dateEdt.getText().toString();
                time = timeEdt.getText().toString();
                description = descriptionEdt.getText().toString();
                category="entertainment";
                payment_method="cash";
                image="image/image.png";

                if(isIncomeSelected == true){

                    RequestQueue queue = Volley.newRequestQueue(TransactionsActivity.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, INCOME_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    Log.d("response",response);
                                    Toast.makeText(TransactionsActivity.this,"Successfully inserted..",Toast.LENGTH_LONG).show();

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("response",error.toString());
                                    //Toast.makeText(SignupActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                                }
                            }){
                        @Override
                        protected Map<String,String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();

                            params.put(KEY_USERID,user_id);
                            params.put(KEY_DATE,formattedDate);
                            params.put(KEY_TIME,currentTime);
                            params.put(KEY_DESCRIPTION, description);
                            params.put(KEY_CATEGORY, category);
                            params.put(KEY_PAYMENTMETHOD, payment_method);
                            params.put(KEY_IMAGE, image);

                            return params;

                        }

                    };

                    queue.add(stringRequest);


                }
                if(isExpenseSelected == true){

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
