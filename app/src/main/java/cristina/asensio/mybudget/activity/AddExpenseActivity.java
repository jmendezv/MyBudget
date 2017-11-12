package cristina.asensio.mybudget.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.SQLException;

import cristina.asensio.mybudget.R;
import cristina.asensio.mybudget.model.Expense;
import cristina.asensio.mybudget.model.UtilDAOImpl;

public class AddExpenseActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 0;

    private EditText etExpenseQuantity, etExpenseDescription;
    private Button btAddExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        getWidgets();

        this.btAddExpense.setOnClickListener(view -> {
            final float expenseQuantity = Float.parseFloat(String.valueOf(this.etExpenseQuantity.getText()));
            final String expenseDescription = String.valueOf(this.etExpenseDescription.getText());

            try {
                final UtilDAOImpl dao = new UtilDAOImpl(getApplicationContext());
                final Expense newExpense = new Expense(expenseQuantity, expenseDescription);
                dao.addExpense(newExpense);

                finish();

            } catch (SQLException e) {
                Log.e(getApplicationInfo().className, e.toString());
            }

        });


    }

    private void getWidgets() {
        this.etExpenseQuantity = (EditText) findViewById(R.id.etExpenseQuantity);
        this.etExpenseDescription = (EditText) findViewById(R.id.etExpenseDescription);
        this.btAddExpense = (Button) findViewById(R.id.btAddExpense);
    }

}
