package wealthmanagement.com.wealthmanagement;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public static final String GETINFO_URL = "http://ingtechbd.com/demo/wealthmanagement/getvalueinfo.php";
    public static final String INCOME_URL = "http://ingtechbd.com/demo/wealthmanagement/income.php";
    public static final String EXPENSE_URL = "http://ingtechbd.com/demo/wealthmanagement/expense.php";
    public static final String INSERTBALANCE_URL = "http://ingtechbd.com/demo/wealthmanagement/insertintobalance.php";
    public static final String CHECK_URL = "http://ingtechbd.com/demo/wealthmanagement/checkifnull.php";
    public static final String POST_URL = "http://ingtechbd.com/demo/wealthmanagement/getvalue.php";
    public static final String UPDATEBALANCE_URL = "http://ingtechbd.com/demo/wealthmanagement/updatebalance.php";

    Spinner categorySpinner,paymentSpinner;
    String[] countryNames={"Entertainment","Food","Medical","Cloathes","Gift","Auto","Travelling","Stationary"};
    int flags[] = {R.drawable.entertainment, R.drawable.food, R.drawable.medical, R.drawable.cloath, R.drawable.gift, R.drawable.auto, R.drawable.travelling, R.drawable.stationar};
    ArrayAdapter<String> adapter;
    List<String> list;

    boolean isIncomeSelected;
    boolean isExpenseSelected;

    EditText priceEdt,dateEdt,timeEdt,descriptionEdt;
    Button incomeBtn,expenseBtn,imageBtn,saveBtn,resetBtn;
    TextView balanceresult;
    ScrollView scrollview;
    ImageView imageview;
    String user_id,formattedDate,currentTime,date,time,description,category,payment_method,image,resultcheck,balance,email;
    double price;
    private Uri filePath;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;

    public static final String KEY_USERID = "user_id";
    public static final String KEY_PRICE = "price";
    public static final String KEY_BALANCE = "balance";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_PAYMENTMETHOD = "payment_method";
    public static final String KEY_IMAGE = "image";

    private JSONArray jsonarray;
    private Context context;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        //Getting the instance of Spinner and applying OnItemSelectedListener on it

        incomeBtn = (Button) findViewById(R.id.incomeButton);
        expenseBtn = (Button) findViewById(R.id.expenseButton);
        imageBtn = (Button) findViewById(R.id.next_image);

        scrollview = (ScrollView) findViewById(R.id.scrollview);
        scrollview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard(view);
                return false;
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("incomestate", "");
        if(!name.equalsIgnoreCase(""))
        {
            if(name.equals("1")){
                isIncomeSelected = true;
                isExpenseSelected = false;
                incomeBtn.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));

                expenseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isIncomeSelected = false;
                        isExpenseSelected = true;

                        incomeBtn.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
                        expenseBtn.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryDark));
                    }
                });

            }
        }



        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        paymentSpinner = (Spinner) findViewById(R.id.paymentSpinner);


        balanceresult = (TextView) findViewById(R.id.balance);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        getuserinfo(email);
        getvalueifnull();
        //getcurrentbalancewithid(user_id);


        Calendar c = Calendar.getInstance();
        final SimpleDateFormat datevalue = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = datevalue.format(c.getTime());

        DateFormat timevalue = new SimpleDateFormat("hh:mm a");
        currentTime = timevalue.format(c.getTime());


        priceEdt = (EditText) findViewById(R.id.priceEdt);
        priceEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        dateEdt = (EditText) findViewById(R.id.dateEdt);
        dateEdt.setText(formattedDate);
        dateEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        timeEdt = (EditText) findViewById(R.id.timeEdt);
        timeEdt.setText(currentTime);
        timeEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        descriptionEdt = (EditText) findViewById(R.id.descriptionsEdt);
        descriptionEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        imageview = (ImageView) findViewById(R.id.image_view);

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
                isIncomeSelected = true;
                isExpenseSelected = false;

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(TransactionsActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("incomestate","1");
                editor.apply();

                Log.d("bol", String.valueOf(isExpenseSelected)+""+ String.valueOf(isIncomeSelected));
                incomeBtn.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryDark));
                expenseBtn.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));



            }
        });

        expenseBtn.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryDark));
        expenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isIncomeSelected = false;
                isExpenseSelected = true;

                Log.d("boole", String.valueOf(isExpenseSelected)+""+ String.valueOf(isIncomeSelected));
                incomeBtn.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
                expenseBtn.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryDark));
            }
        });


        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),flags,countryNames);
        categorySpinner.setAdapter(customAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {
                category = ((TextView) v.findViewById(R.id.textView)).getText().toString();
                //Toast.makeText(TransactionsActivity.this, ""+text1, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.values_array, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appear
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentSpinner.setAdapter(adapter);

        paymentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                payment_method = paymentSpinner.getSelectedItem().toString();
                //Toast.makeText(TransactionsActivity.this, ""+text2, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Calendar c = Calendar.getInstance();
                final SimpleDateFormat datevalue = new SimpleDateFormat("dd-MMM-yyyy");
                formattedDate = datevalue.format(c.getTime());
                //Toast.makeText(TransactionsActivity.this, ""+formattedDate, Toast.LENGTH_SHORT).show();

                DateFormat timevalue = new SimpleDateFormat("hh:mm a");
                currentTime = timevalue.format(c.getTime());

                //user_id = "1";
                price = Double.parseDouble(priceEdt.getText().toString());
                date = dateEdt.getText().toString();
                time = timeEdt.getText().toString();
                description = descriptionEdt.getText().toString();
                image="image/image.png";

                if(isIncomeSelected == true && isExpenseSelected == false){

                    if(resultcheck.equals("[]")) {
                        setvalueIntoIncome();
                        setvalueIntoBalance();
                    }
                    else{
                       setvalueIntoBalancewithPlus();
                    }
                }

                if(isIncomeSelected == false && isExpenseSelected == true){
                    if(resultcheck.equals("[]")) {
                        setvalueIntoExpense();
                        setvalueIntoBalance();
                    }
                    else{
                        setvalueIntoBalancewithMinus();
                    }
                }
            }
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void getuserinfo(String email) {
        Uri.Builder builder = Uri.parse(GETINFO_URL).buildUpon();
        builder.appendQueryParameter("email", email);
        String loginUrl=builder.build().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, loginUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonarray = jsonObject.getJSONArray("result");
                            getInfo(jsonarray);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("response",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }){
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TransactionsActivity.this);
        requestQueue.add(stringRequest);

    }


    private void getvalueifnull() {
        StringRequest stringRequest = new StringRequest(CHECK_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            resultcheck = jsonObject.getString("result");
                            Log.d("object", resultcheck);
                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TransactionsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(TransactionsActivity.this);
        requestQueue.add(stringRequest);
    }


    private void getInfo(JSONArray j) {
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);
                user_id = json.getString("id");
                getcurrentbalancewithid(user_id);
                //Toast.makeText(TransactionsActivity.this, "user_id"+user_id, Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getcurrentbalancewithid(String user_id) {
        Uri.Builder builder = Uri.parse(POST_URL).buildUpon();
        builder.appendQueryParameter("user_id", user_id);
        String loginUrl=builder.build().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, loginUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonarray = jsonObject.getJSONArray("result");
                            getValue(jsonarray);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("response",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }){
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TransactionsActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getValue(JSONArray j) {
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);
                balance = json.getString("balance");
                balanceresult.setText(balance);
                Log.d("balance",balance);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void setvalueIntoBalancewithPlus() {

        setvalueIntoIncomeagain();
        Double balancevalue = Double.parseDouble(balance);
        final double totbalance = price + balancevalue;

        RequestQueue queues = Volley.newRequestQueue(TransactionsActivity.this);

        StringRequest stringRequests = new StringRequest(Request.Method.POST, UPDATEBALANCE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        Toast.makeText(TransactionsActivity.this, "inserted successfully..", Toast.LENGTH_SHORT).show();
                        balanceresult.setText(""+totbalance);
                        // Display the response string.
                        //_response.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //_response.setText("That didn't work!");
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("balance", String.valueOf(totbalance));
                params.put("user_id", user_id);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queues.add(stringRequests);
    }

    private void setvalueIntoIncomeagain() {
        RequestQueue queue = Volley.newRequestQueue(TransactionsActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, INCOME_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                params.put(KEY_PRICE, String.valueOf(price));
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

    private void setvalueIntoBalancewithMinus() {
        setvalueIntoExpenseagain();

        //Toast.makeText(TransactionsActivity.this, ""+balance, Toast.LENGTH_SHORT).show();
        Double balancevalue = Double.parseDouble(balanceresult.getText().toString());

        Log.d("balancevalue", String.valueOf(balancevalue));
        Log.d("price", String.valueOf(price));

        final double totbalance = balancevalue - price;
        //Toast.makeText(TransactionsActivity.this, ""+totbalance, Toast.LENGTH_SHORT).show();

        RequestQueue queues = Volley.newRequestQueue(TransactionsActivity.this);

        StringRequest stringRequests = new StringRequest(Request.Method.POST, UPDATEBALANCE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        Toast.makeText(TransactionsActivity.this, "updated successfully..", Toast.LENGTH_SHORT).show();
                        balanceresult.setText(""+totbalance);
                        // Display the response string.
                        //_response.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //_response.setText("That didn't work!");
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("balance", String.valueOf(totbalance));
                params.put("user_id", user_id);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queues.add(stringRequests);
    }

    private void setvalueIntoExpenseagain() {
        RequestQueue queue = Volley.newRequestQueue(TransactionsActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EXPENSE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(TransactionsActivity.this,"Successfully inserted..",Toast.LENGTH_LONG).show();
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
                params.put(KEY_PRICE, String.valueOf(price));
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

    private void setvalueIntoIncome() {

        RequestQueue queue = Volley.newRequestQueue(TransactionsActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, INCOME_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("response",response);
                        balanceresult.setText(""+price);
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
                params.put(KEY_PRICE, String.valueOf(price));
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

    private void setvalueIntoExpense() {
        RequestQueue queue = Volley.newRequestQueue(TransactionsActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EXPENSE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        double res = price - 2*price;
                        Log.d("response",response);
                        balanceresult.setText(""+res);
                        //Toast.makeText(TransactionsActivity.this,"Successfully inserted..",Toast.LENGTH_LONG).show();

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
                params.put(KEY_PRICE, String.valueOf(price));
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



    private void setvalueIntoBalance() {

        RequestQueue queue = Volley.newRequestQueue(TransactionsActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSERTBALANCE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        balanceresult.setText(""+price);
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
                params.put(KEY_BALANCE, String.valueOf(price));

                return params;
            }

        };

        queue.add(stringRequest);
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
