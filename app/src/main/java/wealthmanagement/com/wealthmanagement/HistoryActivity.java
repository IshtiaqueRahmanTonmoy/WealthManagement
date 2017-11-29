package wealthmanagement.com.wealthmanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import wealthmanagement.com.wealthmanagement.adapter.CategorywiseAdapter;

public class HistoryActivity extends AppCompatActivity {

    Button allTransactions,allDate,allMonth,allCategory,allMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        HistoryActivity.this.setTitle("iAccounts");
        allTransactions = (Button) findViewById(R.id.allTransactions);
        allDate = (Button) findViewById(R.id.allDate);
        allMonth = (Button) findViewById(R.id.allMonth);
        allCategory = (Button) findViewById(R.id.allCategory);
        allMode = (Button) findViewById(R.id.allMode);

        allTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryActivity.this,AllTransactionsActivity.class);
                startActivity(intent);
            }
        });

        allDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryActivity.this,DatewisesearchActivity.class);
                startActivity(intent);
            }
        });

        allMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryActivity.this,MonthwiseActivity.class);
                startActivity(intent);
            }
        });

        allCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryActivity.this,CategorywiseActivity.class);
                startActivity(intent);
            }
        });

        allMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryActivity.this,ModewiseActivity.class);
                startActivity(intent);
            }
        });

    }
}
