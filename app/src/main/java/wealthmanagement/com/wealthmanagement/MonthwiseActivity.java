package wealthmanagement.com.wealthmanagement;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import wealthmanagement.com.wealthmanagement.adapter.DatewiseAdapter;
import wealthmanagement.com.wealthmanagement.adapter.MonthdisplayAdapter;
import wealthmanagement.com.wealthmanagment.Transaction;

public class MonthwiseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MonthdisplayAdapter mAdapter;
    private StringRequest stringRequest;
    private String category,date,price,description;
    private List<Transaction> transactionList = new ArrayList<Transaction>();
    private Button incomeButton,expenseButton;
    boolean isIncomeSelected;
    boolean isExpenseSelected;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datewisesearch);

        MonthwiseActivity.this.setTitle("iAccounts");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        incomeButton = (Button) findViewById(R.id.incomeButton);
        expenseButton = (Button) findViewById(R.id.expenseButton);

        mAdapter = new MonthdisplayAdapter(transactionList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        incomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AllTransactionsActivity.this, "income button selected", Toast.LENGTH_SHORT).show();
                transactionList.clear();
                mAdapter = new MonthdisplayAdapter(transactionList);
                recyclerView.setAdapter(mAdapter);
                if (!isIncomeSelected) {
                    prepareDataforincome();
                    isIncomeSelected = true;
                    isExpenseSelected = false;
                    incomeButton.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryDark));
                    expenseButton.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
                }
            }
        });

        expenseButton.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryDark));
        expenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transactionList.clear();
                mAdapter = new MonthdisplayAdapter(transactionList);
                recyclerView.setAdapter(mAdapter);
                prepareDataforexpense();
                //Toast.makeText(AllTransactionsActivity.this, "expense button selected", Toast.LENGTH_SHORT).show();
                isIncomeSelected = false;
                isExpenseSelected = true;
                incomeButton.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
                expenseButton.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryDark));
            }
        });
    }


    private void prepareDataforincome() {
        stringRequest = new StringRequest("http://ingtechbd.com/demo/wealthmanagement/getransactionbyid.php?user_id="+6,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray j = jsonObject.getJSONArray("result");
                            for(int i=0;i<j.length();i++){
                                try {
                                    //Getting json object
                                    JSONObject json = j.getJSONObject(i);
                                    category = json.getString("category");
                                    date = json.getString("date");

                                    price = json.getString("price");
                                    Log.d("priceincome",price);
                                    description = json.getString("description");

                                    transactionList.add(new Transaction(category,date,price,description));
                                    mAdapter.notifyDataSetChanged();
                                    progressDialog.dismiss();
                                    //Toast.makeText(AllTransactionsActivity.this, "category"+category, Toast.LENGTH_SHORT).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("response",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MonthwiseActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(MonthwiseActivity.this);
        requestQueue.add(stringRequest);
        progressDialog = new ProgressDialog(MonthwiseActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
    }

    private void prepareDataforexpense() {
        stringRequest = new StringRequest("http://ingtechbd.com/demo/wealthmanagement/getransactionbyidexpense.php?user_id="+6,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray j = jsonObject.getJSONArray("result");
                            for(int i=0;i<j.length();i++){
                                try {
                                    //Getting json object
                                    JSONObject json = j.getJSONObject(i);
                                    category = json.getString("category");
                                    date = json.getString("date");

                                   // SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                                   //String month = monthFormat.format(date);
                                   // Log.d("month",month);

                                    price = json.getString("price");
                                    Log.d("priceexpense",price);
                                    description = json.getString("description");

                                    transactionList.add(new Transaction(category,date,price,description));
                                    mAdapter.notifyDataSetChanged();
                                    progressDialog.dismiss();
                                    //Toast.makeText(AllTransactionsActivity.this, "category"+category, Toast.LENGTH_SHORT).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("response",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MonthwiseActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(MonthwiseActivity.this);
        requestQueue.add(stringRequest);
        progressDialog = new ProgressDialog(MonthwiseActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
    }
}
