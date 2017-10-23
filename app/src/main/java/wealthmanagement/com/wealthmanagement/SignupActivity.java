package wealthmanagement.com.wealthmanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wealthmanagement.com.wealthmanagement.adapter.JSONParser;

public class SignupActivity extends AppCompatActivity {

    public static final String REGISTER_URL = "http://180.149.15.140/wealthmanagement/insert.php";
    EditText firstnameEdt,lastnameEdt,emailEdt,passwordEdt;
    Button submitBtn;
    public static final String KEY_FIRSTNAME = "first_name";
    public static final String KEY_LASTNAME = "last_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    String first_name,last_name,email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstnameEdt = (EditText) findViewById(R.id.firstname);
        lastnameEdt = (EditText) findViewById(R.id.lastname);
        emailEdt = (EditText) findViewById(R.id.emailEdt);
        passwordEdt = (EditText) findViewById(R.id.password);

        submitBtn = (Button)findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                first_name = firstnameEdt.getText().toString();
                last_name = lastnameEdt.getText().toString();
                email = emailEdt.getText().toString();
                password = passwordEdt.getText().toString();

                //new CreateNewUser().execute();

                RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.d("response",response);
                                Toast.makeText(SignupActivity.this,"Registered successfully..",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                                startActivity(intent);
                                finish();
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
                        params.put(KEY_FIRSTNAME,first_name);
                        params.put(KEY_LASTNAME,last_name);
                        params.put(KEY_EMAIL,email);
                        params.put(KEY_PASSWORD, password);
                        return params;
                    }

                };

                queue.add(stringRequest);


            }
        });
    }

    /*
    class CreateNewUser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(KEY_FIRSTNAME, first_name));
            params.add(new BasicNameValuePair(KEY_LASTNAME, last_name));
            params.add(new BasicNameValuePair(KEY_EMAIL, email));
            params.add(new BasicNameValuePair(KEY_PASSWORD, password));

            JSONObject json = jsonParser.makeHttpRequest(REGISTER_URL, "POST", params);

            try {

                int success = json.getInt(TAG_SUCCESS);

                // Toast.makeText(RegistrationActivity.this, "" + success, Toast.LENGTH_SHORT).show();
                if (success == 1) {
                    // successfully created product

                    SignupActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(SignupActivity.this.getBaseContext(), "Registration completed..", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
        }
    }
    */

}
