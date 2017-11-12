package cristina.asensio.mybudget.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.sql.SQLException;

import cristina.asensio.mybudget.Constants;
import cristina.asensio.mybudget.R;
import cristina.asensio.mybudget.model.Expense;
import cristina.asensio.mybudget.model.UtilDAOImpl;

public class AddExpenseActivity extends AppCompatActivity {

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
                sendNewExpenseDataToMainActivity(newExpense);

            } catch (SQLException e) {
                Log.e(getApplicationInfo().className, e.toString());
            }

        });
    }

    private void sendNewExpenseDataToMainActivity(Expense expense) {
        final Intent intent = getIntent();
        intent.putExtra(Constants.NEW_EXPENSE_QUANTITY_KEY, expense.getQuantity());
        intent.putExtra(Constants.NEW_EXPENSE_DESCRIPTION_KEY, expense.getDescription());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void getWidgets() {
        this.etExpenseQuantity = (EditText) findViewById(R.id.etExpenseQuantity);
        this.etExpenseDescription = (EditText) findViewById(R.id.etExpenseDescription);
        this.btAddExpense = (Button) findViewById(R.id.btAddExpense);
    }

}
