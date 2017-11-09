package cristina.asensio.mybudget.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Expense Model
 */
@DatabaseTable(tableName = "expenses")
public class Expense {

    private static final String ID_FIELD_NAME = "_id";
    private static final String QUANTITY_FIELD_NAME = "quantity";
    private static final String DESCRIPTION_FIELD_NAME = "description";
    static final String DATE_FIELD_NAME = "date";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;

    @DatabaseField(columnName = QUANTITY_FIELD_NAME, dataType = DataType.FLOAT, canBeNull = false)
    private float quantity;

    @DatabaseField(columnName = DESCRIPTION_FIELD_NAME, dataType = DataType.STRING, canBeNull = false)
    private String description;

    @DatabaseField(columnName = DATE_FIELD_NAME, dataType = DataType.DATE, canBeNull = true)
    private Date date;

    public Expense() {

    }

    public Expense(float quantity, String description) {
        this.quantity = quantity;
        this.description = description;
        this.date = new Date();
    }

    public int getId() {
        return id;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}
