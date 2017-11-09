package cristina.asensio.mybudget.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Database helper class used to manage the creation and upgrading of the database. This class also provides
 * the DAOs used by the other classes.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "db_my_budget.sql";
    private static final int DATABASE_VERSION = 1;

    private Dao<Expense, Integer> expenseDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        Log.i(DatabaseHelper.class.getName(), "in onCreate");

        try {
            TableUtils.createTable(connectionSource, Expense.class);
            populateExpenses();
        } catch (Exception e) {
            Log.e(DatabaseHelper.class.getName(), e.toString());
        }

    }

    private void populateExpenses() throws SQLException, ParseException {
        final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        expenseDao = getExpenseDao();

        Expense expense = new Expense(200, "casa");
        expense.setDate(formatter.parse("2017-10-01"));
        expenseDao.create(expense);

        expense = new Expense(34.40f, "gym");
        expense.setDate(formatter.parse("2017-10-05"));
        expenseDao.create(expense);

        expense = new Expense(99.60f, "tren");
        expense.setDate(formatter.parse("2017-10-10"));
        expenseDao.create(expense);

        expense = new Expense(200, "casa");
        expense.setDate(formatter.parse("2017-11-01"));
        expenseDao.create(expense);

        expense = new Expense(34.40f, "gym");
        expense.setDate(formatter.parse("2017-11-05"));
        expenseDao.create(expense);

        expense = new Expense(99.60f, "tren");
        expense.setDate(formatter.parse("2017-11-10"));
        expenseDao.create(expense);
    }

    Dao<Expense, Integer> getExpenseDao() throws SQLException{
        if(expenseDao == null) {
            return getDao(Expense.class);
        }
        return expenseDao;
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");

            // Drop older db if existed
            database.execSQL("DROP DATABASE " + DATABASE_NAME);

            // Create tables again
            onCreate(database);

        } catch (Exception e) {
            Log.i(DatabaseHelper.class.getName(), e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
