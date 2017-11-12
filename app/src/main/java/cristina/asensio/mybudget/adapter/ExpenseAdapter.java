package cristina.asensio.mybudget.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.List;

import cristina.asensio.mybudget.Constants;
import cristina.asensio.mybudget.R;
import cristina.asensio.mybudget.model.Expense;
import cristina.asensio.mybudget.model.UtilDAOImpl;

/**
 * Custom Adapter for the Expenses list displayed in Main Activity
 */

public class ExpenseAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Expense> expenses;

    private TextView tvExpenseQuantity, tvExpenseDescription;
    private Button btDeleteExpense;

    public ExpenseAdapter(Context context, List<Expense> expenses) {
        this.context = context;
        this.expenses = expenses;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(this.expenses != null) {
            return this.expenses.size();
        }
        return 0;
    }

    @Override
    public Expense getItem(int position) {
        return this.expenses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.expenses.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = this.inflater.inflate(R.layout.expense_row_for_adapter, null);
        }

        initWidgets(view);
        displayExpenseInList(position);

        this.btDeleteExpense.setOnClickListener(btView -> {
            final Expense expenseToDelete = getItem(position);

            try {
                deleteExpenseFromDatabase(expenseToDelete);
                deleteExpenseFromExpensesList(position);
                refreshMainActivityAdapter();
            } catch (SQLException e) {
                Log.e(this.context.getApplicationInfo().className, e.toString());
            }
        });

        return view;
    }

    private void refreshMainActivityAdapter() {
        notifyDataSetChanged();
    }

    private void deleteExpenseFromExpensesList(int position) {
        this.expenses.remove(position);
    }

    private void deleteExpenseFromDatabase(Expense expenseToDelete) throws SQLException {
        final UtilDAOImpl utilDAOImpl = new UtilDAOImpl(this.context);
        utilDAOImpl.deleteExpense(expenseToDelete);
    }

    private void displayExpenseInList(int position) {
        final Expense expense = this.expenses.get(position);
        this.tvExpenseQuantity.setText(String.format("%.2f %s", expense.getQuantity(), Constants.EURO));
        this.tvExpenseDescription.setText(expense.getDescription());
    }

    private void initWidgets(View view) {
        this.tvExpenseQuantity = (TextView) view.findViewById(R.id.tvExpenseQuantity);
        this.tvExpenseDescription = (TextView) view.findViewById(R.id.tvExpenseDescription);
        this.btDeleteExpense = (Button) view.findViewById(R.id.btDeleteExpense);
    }
}
