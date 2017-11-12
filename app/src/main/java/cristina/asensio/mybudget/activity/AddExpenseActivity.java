package cristina.asensio.mybudget.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cristina.asensio.mybudget.R;

public class AddExpenseActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 0;

    private EditText etExpenseQuantity, etExpenseDescription;
    private Button btAddExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        getWidgets();


    }

    private void getWidgets() {
        this.etExpenseQuantity = (EditText) findViewById(R.id.etExpenseQuantity);
        this.etExpenseDescription = (EditText) findViewById(R.id.etExpenseDescription);
        this.btAddExpense = (Button) findViewById(R.id.btAddExpense);
    }

}
