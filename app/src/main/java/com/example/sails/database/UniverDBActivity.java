package com.example.sails.database;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.CancellationSignal;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UniverDBActivity extends AppCompatActivity implements View.OnClickListener{


    static String LOG_TAG = "dbLogs";
    public final String SPINNER_VYKLADACH = "vykladach";
    public final String SPINNER_POSADA = "posada";
    private final int REQUEST_CODE_VYKLADACH = 0;
    private final int REQUEST_CODE_POSADA = 1;
    private final int REQUEST_CODE_CUSTOM_QUERY = 2;

    private final float TEXT_SIZE_TABLNAME = 20;
    private final float TEXT_SIZE_ZAG = 15;
    private final float TEXT_SIZE_COMMON = 14;

    private final int ID_FILL_DEF_VALUES = 0;
    private final int ID_CLEAR_TABLES = 1;
    private final int ID_QUERY_TASK = 2;
    private final int ID_CUSTOM_QUERY = 3;

    Button btnAdd, btnCustom, btnShowAll;
    Spinner spinerTable;
    LinearLayout tableLayout;
    DBHelperUniv dbHelperUniv;
    static SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_univer_db);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnCustom = (Button) findViewById(R.id.btnCustom);
        btnShowAll = (Button) findViewById(R.id.btnShowAll);

        btnAdd.setOnClickListener(this);
        btnCustom.setOnClickListener(this);
        btnShowAll.setOnClickListener(this);

        tableLayout = (LinearLayout) findViewById(R.id.tableLayout);

        spinerTable = (Spinner) findViewById(R.id.spinnerTable);

        dbHelperUniv = new DBHelperUniv(this);
        database = dbHelperUniv.getWritableDatabase();

        Log.d(LOG_TAG, "UnivDBActivity onCreate(). SpinerTable selected item: " + spinerTable.getSelectedItem() + " id: " + spinerTable.getSelectedItemId());


    }

    @Override
    public void onClick(View view) {
        Log.d(LOG_TAG, "UnivDBActivity onClick(). Button " + view.getId() + " clicked.");

        switch (view.getId()){
            case R.id.btnAdd:
                addValues();
                break;

            case R.id.btnShowAll:
                showAll();
                break;

            case R.id.btnCustom:
                callCustom();
                break;
        }

    }

    private void callCustom() {

        Log.d(LOG_TAG, "UnivDBActivity callCustom(). Button btnCustom was clicked. Opening AlertDialog");


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String values[] = {"Fill with default values", "Clear tables", "Query task", "Custom query"};
        builder.setItems(values, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(LOG_TAG, "UnivDBActivity callCustom(). Item " + values[i] + " chosen.");

                switch (i){
                    case ID_FILL_DEF_VALUES:
                        fillTablesDefValues();
                        break;

                    case ID_CLEAR_TABLES:
                        clearTables();
                        break;

                    case ID_QUERY_TASK:
                        String query = "SELECT " + DBHelperUniv.TABLE_VYKLADACH + "." + DBHelperUniv.TABLE_VYKLADACH_PRIZVVYKL + ", " + DBHelperUniv.TABLE_POSADA + "." + DBHelperUniv.TABLE_POSADA_POSTVYKL + "" +
                                " FROM " + DBHelperUniv.TABLE_VYKLADACH + " INNER JOIN " + DBHelperUniv.TABLE_POSADA + " ON " +
                                "" + DBHelperUniv.TABLE_VYKLADACH + "." + DBHelperUniv.TABLE_VYKLADACH_KODPOST + " = " +
                                "" + DBHelperUniv.TABLE_POSADA + "." + DBHelperUniv.TABLE_POSADA_KODPOST + "" +
                                " WHERE " + DBHelperUniv.TABLE_VYKLADACH + "." + DBHelperUniv.TABLE_VYKLADACH_NOMKAF + " = '2' AND " +
                                ""  + DBHelperUniv.TABLE_VYKLADACH + "." +   DBHelperUniv.TABLE_VYKLADACH_KODPOST + "='02';";

                        doQuery(query);
                        break;

                    case ID_CUSTOM_QUERY:
                        Intent intent = new Intent(UniverDBActivity.this, CustomQueryActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_CUSTOM_QUERY);
                        break;
                }

            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d(LOG_TAG, "UnivDBActivity callCustom(). AlertDialog was cancelled.");
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void addValues(){
        Log.d(LOG_TAG, "class UniverDBActivity. method addValues() is working. Selected spinner item: " + spinerTable.getSelectedItem().toString());

        Intent intent = new Intent(this, FieldsActivity.class);
        intent.putExtra("table", spinerTable.getSelectedItem().toString());


        switch ((int)spinerTable.getSelectedItemId()){
            case REQUEST_CODE_VYKLADACH:
                startActivityForResult(intent, REQUEST_CODE_VYKLADACH);
                break;
            case REQUEST_CODE_POSADA:
                startActivityForResult(intent, REQUEST_CODE_POSADA);
                break;
        }

    }


    private void showAll() {
        Log.d(LOG_TAG, "class UniverDBActivity. method showAll() is working. Selected spinner item: \"" + spinerTable.getSelectedItem().toString());

        Cursor cursor;
        cursor = database.rawQuery("SELECT * FROM " + spinerTable.getSelectedItem().toString() + ";", new String[]{});


        tableLayout.removeAllViews();

        addRawTable(new String[]{"Table " + spinerTable.getSelectedItem().toString()}, TEXT_SIZE_TABLNAME);

        if(cursor.moveToFirst()){
            switch (spinerTable.getSelectedItem().toString()) {
                case SPINNER_VYKLADACH:
                    int kodVyklColumn = cursor.getColumnIndex(DBHelperUniv.TABLE_VYKLADACH_KODVYKL);
                    int prizvVyklColumn = cursor.getColumnIndex(DBHelperUniv.TABLE_VYKLADACH_PRIZVVYKL);
                    int kodPostColumn = cursor.getColumnIndex(DBHelperUniv.TABLE_VYKLADACH_KODPOST);
                    int nomKafColumn = cursor.getColumnIndex(DBHelperUniv.TABLE_VYKLADACH_NOMKAF);

                    addRawTable(cursor.getColumnNames(), TEXT_SIZE_ZAG);

                    do {
                        String kodVykl = cursor.getString(kodVyklColumn);
                        String prizvVykl = cursor.getString(prizvVyklColumn);
                        String kodPost = cursor.getString(kodPostColumn);
                        String nomKaf = cursor.getString(nomKafColumn);



                        addRawTable(new String[] {kodVykl,prizvVykl,kodPost,nomKaf}, TEXT_SIZE_COMMON);
                    } while(cursor.moveToNext());
                    break;
                case SPINNER_POSADA:
                    int kodPost1Column = cursor.getColumnIndex(DBHelperUniv.TABLE_POSADA_KODPOST);
                    int postVyklColumn = cursor.getColumnIndex(DBHelperUniv.TABLE_POSADA_POSTVYKL);
                    int normPostColumn = cursor.getColumnIndex(DBHelperUniv.TABLE_POSADA_NORMPOST);

                    addRawTable(cursor.getColumnNames(), TEXT_SIZE_ZAG);

                    do {
                        String kodPost1 = cursor.getString(kodPost1Column);
                        String postVykl = cursor.getString(postVyklColumn);
                        String normPost = cursor.getString(normPostColumn);

                        addRawTable(new String[] { kodPost1,postVykl,normPost}, TEXT_SIZE_COMMON);
                    }while(cursor.moveToNext());
                    break;

            }
        } else Toast.makeText(this, "table is empty", Toast.LENGTH_SHORT).show();
        cursor.close();
    }
    public void addRawTable(String [] mas, float size){
        Log.d("rawTable", "method addRowTable is working. ");

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        layoutParams.setMargins(4, 10, 4, 0);



        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);

        tableLayout.addView(rowLayout);



        for(int i = 0; i < mas.length; i++){
            TextView textView = new TextView(this);
            textView.setText(mas[i]);
            textView.setTextSize(size);
            rowLayout.addView(textView,layoutParams);
        }


        Log.d("rawTable", "method addRowTable stopped working." );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(LOG_TAG, "Class UniverDBActivity, method onActivityResult(). RequestCode = " + requestCode);

        if((null == data))
            return;

        ContentValues contentValues = new ContentValues();

        switch (requestCode) {
            case REQUEST_CODE_VYKLADACH:
                String kodVykl = data.getStringExtra("kodVykl");
                String prizvVykl = data.getStringExtra("prizvVykl");
                String kodPost = data.getStringExtra("kodPost");
                String nomKaf = data.getStringExtra("nomKaf");

                contentValues.put(DBHelperUniv.TABLE_VYKLADACH_KODVYKL, kodVykl);
                contentValues.put(DBHelperUniv.TABLE_VYKLADACH_PRIZVVYKL, prizvVykl);
                contentValues.put(DBHelperUniv.TABLE_VYKLADACH_KODPOST, kodPost);
                contentValues.put(DBHelperUniv.TABLE_VYKLADACH_NOMKAF, nomKaf);

                database.insert(DBHelperUniv.TABLE_VYKLADACH, null, contentValues);

                Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();

                break;
            case REQUEST_CODE_POSADA:
                String kodPost1 = data.getStringExtra("kodPost");
                String postVykl = data.getStringExtra("postVykl");
                String normPost = data.getStringExtra("normPost");

                contentValues.put(DBHelperUniv.TABLE_POSADA_KODPOST, kodPost1);
                contentValues.put(DBHelperUniv.TABLE_POSADA_POSTVYKL, postVykl);
                contentValues.put(DBHelperUniv.TABLE_POSADA_NORMPOST, normPost);

                database.insert(DBHelperUniv.TABLE_POSADA, null, contentValues);

                Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();

                break;

            case REQUEST_CODE_CUSTOM_QUERY:
                String query = data.getStringExtra("query");
                doQuery(query);

        }
    }

    private void doQuery(String query){

        tableLayout.removeAllViews();

        Log.d(LOG_TAG, "Class UniverDBActivity, method doQuery(). \nQuery: " + query);


        switch (query.split(" ")[0]) {
            case "SWITCH":
                Cursor cursor = database.rawQuery(query, new String[]{});
                String values[] = {"", ""};

                if (cursor.moveToFirst()) {
                    addRawTable(new String[]{"Selection"}, TEXT_SIZE_TABLNAME);
                    addRawTable(cursor.getColumnNames(), TEXT_SIZE_ZAG);

                    Toast.makeText(this, "Elements " + cursor.getColumnCount() + "" + cursor.getString(cursor.getColumnIndex(cursor.getColumnNames()[0])), Toast.LENGTH_SHORT).show();
                    do {
                        for (int i = 0; i < cursor.getColumnCount(); i++) {
                            values[i] = cursor.getString(i);
                        }
                        addRawTable(values, TEXT_SIZE_COMMON);
                    } while (cursor.moveToNext());
                } else Toast.makeText(this, "noElements", Toast.LENGTH_SHORT).show();
                cursor.close();
                break;

            default:
                Log.d(LOG_TAG, "default");
               // query = "CREATE TABLE IF NOT EXISTS table (" + TABLE_POSADA_KODPOST + " TEXT, " + TABLE_POSADA_POSTVYKL + " TEXT, " + TABLE_POSADA_NORMPOST + " TEXT);";

                database.execSQL(query);


                break;
        }
    }

    private void clearTables(){

        Log.d(LOG_TAG, "Class UniverDBActivity. method clearTables()");

        database.delete(DBHelperUniv.TABLE_VYKLADACH, null, null);
        database.delete(DBHelperUniv.TABLE_POSADA, null, null);

        Toast.makeText(this, "Cleared", Toast.LENGTH_SHORT).show();
    }

    private void fillTablesDefValues(){

        Log.d(LOG_TAG, "Class UniverDBActivity. method fillTablesDefValues()");

        String masVykladach[][] = {
                {"0101", "Тригуб", "01", "1"},
                {"0102", "Давидова", "11", "1"},
                {"0103", "Морозова", "06", "1"},
                {"0202", "Дихта", "02", "2"},
                {"0203", "Полушкина", "11", "2"},
                {"0204", "Сердюченко", "01", "2"},
                {"0301", "Гришкова", "06", "3"},
                {"0302", "Байдак", "13", "3"},
                {"0303", "Букач", "11", "3"},
                {"0306", "Гасилова", "11", "3"},
                {"0307", "Дидаренко", "13", "3"},
                {"0308", "Диордиева", "11", "3"},
                {"0309", "Кулик", "11", "3"},
                {"0310", "Матвийчук", "13", "3"},
                {"0401", "Дымова", "11", "4"},
                {"0402", "Бондаренко", "11", "4"},
                {"0403", "Гихун", "11", "4"},
                {"0404", "Дзюбан", "14", "4"},
                {"0405", "Вербицкий", "11", "4"},
                {"0501", "Фисун", "01", "5"},
                {"0502", "Бабыч", "06", "5"},
                {"0503", "Нездолий", "11", "5"},
                {"0504", "Галенко", "13", "5"},
                {"0505", "Батюк", "13", "5"},
                {"0506", "Гнездьонова", "13", "5"},
                {"0507", "Дворецкая", "13", "5"},
                {"0509", "Боравльова", "11", "5"},
                {"0601", "Бурлан", "06", "6"},
                {"0701", "Колисниченко", "01", "7"},
                {"0702", "Яжборовская", "11", "7"},
                {"0703", "Ярошенко", "11", "7"},
                {"0801", "Грабак", "01", "8"},
                {"0901", "Каирова", "06", "9"},
                {"1001", "Запорожец", "02", "10"},
                {"1102", "Коновалов", "11", "11"},
                {"1201", "Казарезов", "01", "12"},
                {"1202", "Цыплинская", "06", "12"},
                {"1301", "Букач", "02", "13"},
                {"1302", "Захарова", "11", "13"},
                {"1401", "Дубова", "01", "14"},
                {"1402", "Тулузакова", "13", "14"},
                {"1501", "Горлачук", "01", "15"},
                {"1601", "Сирота", "06", "16"},
                {"1701", "Гожий", "06", "17"},
                {"1702", "Батрак", "06", "17"},
                {"1801", "Трунов", "11", "18"},
                {"1802", "Яремчук", "11", "18"},
                {"1901", "Науменко", "01", "19"},
                {"2001", "Бориславская", "02", "20"},
                {"2101", "Осипов", "06", "21"},
        };

        String masPosada[][] = {
                {"01", "професор, ДН", "410"},
                {"02", "в.о. професора, ДН", "450"},
                {"03", "професор, КН", "450"},
                {"04", "в.о. професора, КН", "515"},
                {"05", "доцент, ДН", "450"},
                {"06", "доцент, КН", "515"},
                {"07", "в.о. доцента, КН", "570"},
                {"08", "доцент БН", "570"},
                {"09", "в.о. доцента БН", "630"},
                {"10", "старший викладач, КН", "570"},
                {"11", "старший викладач, БН", "630"},
                {"12", "викладач-асистент, КН", "570"},
                {"13", "викладач-асистент БН", "690"},
                {"14", "викладач-асистент, БН", "780"},
                {"20", "магистр", "800"}
        };

        clearTables();

        String []vykladachColumnNames = database.rawQuery("SELECT * FROM " + DBHelperUniv.TABLE_VYKLADACH, new String[]{}).getColumnNames();
        String []posadaColumnNames = database.rawQuery("SELECT * FROM " + DBHelperUniv.TABLE_POSADA, new String[]{}).getColumnNames();

        ContentValues contentValues = new ContentValues();

        long timeBegin = System.currentTimeMillis();
        database.beginTransaction();
        try {
            for (int i = 0; i < masVykladach.length; i++) {
                for (int j = 0; j < masVykladach[0].length; j++) {
                    contentValues.put(vykladachColumnNames[j], masVykladach[i][j]);
                }
                database.insert(DBHelperUniv.TABLE_VYKLADACH, null, contentValues);
            }
            contentValues.clear();

            for (int i = 0; i < masPosada.length; i++) {
                for (int j = 0; j < masPosada[0].length; j++) {
                    contentValues.put(posadaColumnNames[j], masPosada[i][j]);
                }
                database.insert(DBHelperUniv.TABLE_POSADA, null, contentValues);
            }

            Toast.makeText(this, "Filled", Toast.LENGTH_SHORT).show();
            database.setTransactionSuccessful();
        }
        catch (SQLiteException e){
            Log.d(LOG_TAG, e.toString());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            database.endTransaction();
        }
        long timeEnd = System.currentTimeMillis();
        Toast.makeText(this,"Time: " + (timeEnd - timeBegin), Toast.LENGTH_SHORT).show();
    }

    public static SQLiteDatabase getDataBase(){
        return database;
    }
}
