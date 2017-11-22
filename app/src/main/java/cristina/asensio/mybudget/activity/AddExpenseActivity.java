package cristina.asensio.mybudget.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.sql.SQLException;

import cristina.asensio.mybudget.Constants;
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
                sendNewExpenseDataToMainActivity(newExpense);

            } catch (SQLException e) {
                Log.e(getApplicationInfo().className, e.toString());
            }

        });
    }

    private void sendNewExpenseDataToMainActivity(Expense expense) {
        final Intent intent = getIntent();
        intent.putExtra(Constants.NEW_EXPENSE_QUANTITY_KEY, expense.getQuantity());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void sendNotification() {
        final Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_description) + " " + Constants.MINIMUM_FOR_SENDING_NOTIFICATION + Constants.EURO)
                .build();
        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void getWidgets() {
        this.etExpenseQuantity = (EditText) findViewById(R.id.etExpenseQuantity);
        this.etExpenseDescription = (EditText) findViewById(R.id.etExpenseDescription);
        this.btAddExpense = (Button) findViewById(R.id.btAddExpense);
    }

    private float getTotalAvailableQuantity() {
        return Float.parseFloat(String.valueOf(MainActivity.tvTotalAvailable.getText()).replace(Constants.EURO, ""));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (getTotalAvailableQuantity() < getMinimumForSendingNotificationfromSettings()) {
            sendNotification();
        }
    }

    private float getMinimumForSendingNotificationfromSettings() {
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        return Float.parseFloat(sharedPrefs.getString(SettingsActivity.PREFERENCES_NOTIFICATION_MIN_QUANTITY_KEY, "100"));

    }
}
