package cristina.asensio.mybudget.model;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
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
        if(databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void onDestroy() {
        if(databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public List<Expense> lookupExpenses() throws SQLException {
        final QueryBuilder<Expense, Integer> queryBuilder = expenseDao.queryBuilder();
        return queryBuilder.query();
    }
}
