package cristina.asensio.mybudget.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import cristina.asensio.mybudget.Constants;
import cristina.asensio.mybudget.R;
import cristina.asensio.mybudget.adapter.ExpenseAdapter;
import cristina.asensio.mybudget.model.Expense;
import cristina.asensio.mybudget.model.UtilDAOImpl;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    public static TextView tvTotalAvailable;
    private ListView lvExpenses;

    private List<Expense> expenses;
    private ExpenseAdapter adapter;


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

        tvTotalAvailable.setText(Constants.TOTAL_AVAILABLE_DEFAULT_VALUE + Constants.EURO);

        this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Add new Expense
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
        this.adapter = new ExpenseAdapter(this, this.expenses);
        this.lvExpenses.setAdapter(this.adapter);
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
            //TODO: Go to Settings
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
            // TODO: Go to Stadistics activity
        }

        this.drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
