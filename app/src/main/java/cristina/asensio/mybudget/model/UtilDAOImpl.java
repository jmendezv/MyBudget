package cristina.asensio.mybudget.model;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * CRUD operations for Expenses database
 */

public class UtilDAOImpl {

    private final Context context;
    private DatabaseHelper databaseHelper;
    private Dao<Expense, Integer> expenseDao;

    public UtilDAOImpl(Context context) throws SQLException {
        this.context = context;
        getSetup();
    }

    private void getSetup() throws SQLException {
        databaseHelper = getHelper();
        this.expenseDao = databaseHelper.getExpenseDao();
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void onDestroy() {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public List<Expense> lookupExpenses() throws SQLException {
        final QueryBuilder<Expense, Integer> queryBuilder = expenseDao.queryBuilder();
        return queryBuilder.query();
    }

    public List<Expense> lookupExpensesCurrentMonth() throws SQLException, ParseException {
        final Calendar calendar = Calendar.getInstance();
        final DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        List<Expense> expenses = new ArrayList<>();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);

        final QueryBuilder<Expense, Integer> queryBuilder = expenseDao.queryBuilder();
        queryBuilder.where().between(Expense.DATE_FIELD_NAME, formater.parse(currentYear + "-" + currentMonth + "-01"), calendar.getTime());

        final PreparedQuery<Expense> preparedQuery = queryBuilder.prepare();
        expenses = expenseDao.query(preparedQuery);

        return expenses;
    }

    public void deleteExpense(Expense expense) throws SQLException {
        this.expenseDao.delete(expense);
    }

    public void addExpense(Expense expense) throws SQLException {
        this.expenseDao.create(expense);
    }
}
