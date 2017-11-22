package cristina.asensio.mybudget.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.List;

import cristina.asensio.mybudget.Constants;
import cristina.asensio.mybudget.R;
import cristina.asensio.mybudget.adapter.ExpenseAdapter;
import cristina.asensio.mybudget.model.Expense;
import cristina.asensio.mybudget.model.UtilDAOImpl;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int ADD_NEW_EXPENSE_REQUEST_CODE = 100;

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    public static TextView tvTotalAvailable;
    private ListView lvExpenses;

    private List<Expense> expenses;
    private ExpenseAdapter expenseAdapter;
    private float totalAvailable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            getExpensesFromDatabase();

        } catch (SQLException e) {
            Log.e(MainActivity.class.getName(), e.toString());
        }

        getWidgets();
        init();
        getTotalAvailableMoneyRemovingExpenses();

        tvTotalAvailable.setText(String.format("%.2f %s", this.totalAvailable, Constants.EURO));

        this.fab.setOnClickListener(view -> {
            final Intent intent = new Intent(getBaseContext(), AddExpenseActivity.class);
            startActivityForResult(intent, ADD_NEW_EXPENSE_REQUEST_CODE);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NEW_EXPENSE_REQUEST_CODE && resultCode == RESULT_OK) {
            final float newExpenseQuantity = data.getFloatExtra(Constants.NEW_EXPENSE_QUANTITY_KEY, Constants.DEFAULT_QUANTITY);
            addExpenseToTotalAvailable(newExpenseQuantity);
            this.expenseAdapter.notifyDataSetChanged();
        }
    }

    public void addExpenseToTotalAvailable(double newExpense) {
        final float currentAvailableAmount = getTotalAvailableQuantity();
        double newAvailableAmount = currentAvailableAmount - newExpense;
        tvTotalAvailable.setText(String.format("%.2f %s", newAvailableAmount, Constants.EURO));
    }

    private float getTotalAvailableQuantity() {
        return Float.parseFloat(String.valueOf(tvTotalAvailable.getText()).replace(Constants.EURO, ""));
    }

    private void getTotalAvailableMoneyRemovingExpenses() {
        for (final Expense expense : this.expenses) {
            this.totalAvailable = this.totalAvailable - expense.getQuantity();
        }
    }

    private void getExpensesFromDatabase() throws SQLException {
        final UtilDAOImpl utilDAOImpl = new UtilDAOImpl(getApplicationContext());
        this.expenses = utilDAOImpl.lookupExpenses();
    }

    private void setNavigationDrawer(Toolbar toolbar) {
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, this.drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void init() {
        setSupportActionBar(this.toolbar);
        setNavigationDrawer(this.toolbar);
        this.totalAvailable = Constants.TOTAL_AVAILABLE_DEFAULT_VALUE;
        this.expenseAdapter = new ExpenseAdapter(this, this.expenses);
        this.lvExpenses.setAdapter(this.expenseAdapter);
    }

    private void getWidgets() {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.fab = (FloatingActionButton) findViewById(R.id.fab);
        this.drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.lvExpenses = (ListView) findViewById(R.id.lvExpenses);
        this.tvTotalAvailable = (TextView) findViewById(R.id.tvTotalAvailable);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            final Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_nav_stadistics) {
            final Intent stadisticsIntent = new Intent(getApplicationContext(), StadisticsActivity.class);
            startActivity(stadisticsIntent);
        }

        this.drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
