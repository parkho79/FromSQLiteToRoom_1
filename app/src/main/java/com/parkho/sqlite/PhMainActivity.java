package com.parkho.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import com.parkho.sqlite.PhDatabaseContract.StudentEntry;

import java.util.ArrayList;
import java.util.List;

public class PhMainActivity extends AppCompatActivity {

    private PhDatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private List mItemList = new ArrayList<PhStudentEntity>();
    private PhRecyclerAdapter mRecyclerAdapter;
    private int currentCursorId = -1;

    private EditText mEtGrade;
    private EditText mEtNumber;
    private EditText mEtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // List 설정
        bindList();

        // Input 설정
        bindInput();

        // 삽입 설정
        bindInsert();

        // 갱신 설정
        bindUpdate();

        // 삭제 설정
        bindDelete();

        // DB 초기화
        mDbHelper = new PhDatabaseHelper(this);
        mDb = mDbHelper.getWritableDatabase();

        // List 반영
        updateList();
    }

    /**
     * List 설정
     */
    private void bindList() {
        mRecyclerAdapter = new PhRecyclerAdapter(mItemList);
        mRecyclerAdapter.setOnItemClickListener((a_view, a_position) -> {
            PhStudentEntity studentEntity = (PhStudentEntity) mItemList.get(a_position);
            currentCursorId = studentEntity.getId();

            mEtGrade.setText(Integer.toString(studentEntity.getGrade()));
            mEtNumber.setText(Integer.toString(studentEntity.getNumber()));
            mEtName.setText(studentEntity.getName());
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mRecyclerAdapter);
    }

    /**
     * Input 설정
     */
    private void bindInput() {
        mEtGrade = findViewById(R.id.et_grade);
        mEtNumber = findViewById(R.id.et_number);
        mEtName = findViewById(R.id.et_name);
    }

    /**
     * 삽입 설정
     */
    private void bindInsert() {
        findViewById(R.id.btn_insert).setOnClickListener(view -> {
            // DB 삽입
            final int grade = Integer.parseInt(mEtGrade.getText().toString());
            final int number = Integer.parseInt(mEtNumber.getText().toString());
            final String strName = mEtName.getText().toString();
            insertData(grade, number, strName);

            // 입력값 초기화
            clearInputs();

            // List 반영
            updateList();
        });
    }

    /**
     * 갱신 설정
     */
    private void bindUpdate() {
        findViewById(R.id.btn_update).setOnClickListener(view -> {
            if (currentCursorId == -1) {
                Toast.makeText(this, R.string.err_no_selected_item, Toast.LENGTH_SHORT).show();
                return;
            }

            // DB 갱신
            final int grade = Integer.parseInt(mEtGrade.getText().toString());
            final int number = Integer.parseInt(mEtNumber.getText().toString());
            final String strName = mEtName.getText().toString();
            updateData(currentCursorId, grade, number, strName);

            // 입력값 초기화
            clearInputs();

            // List 반영
            updateList();
        });
    }

    /**
     * 삭제 설정
     */
    private void bindDelete() {
        findViewById(R.id.btn_delete).setOnClickListener(view -> {
            if (currentCursorId == -1) {
                Toast.makeText(this, R.string.err_no_selected_item, Toast.LENGTH_SHORT).show();
                return;
            }

            // DB 삭제
            deleteData(currentCursorId);

            // 입력값 초기화
            clearInputs();

            // List 반영
            updateList();
        });
    }

    /**
     * DB 에서 data 를 읽어 list 에 담기
     */
    private void updateList() {
        mItemList.clear();
        mItemList.addAll(getAllData());
        mRecyclerAdapter.notifyDataSetChanged();
    }

    /**
     * 입력값 초기화
     */
    private void clearInputs() {
        mEtGrade.getText().clear();
        mEtNumber.getText().clear();
        mEtName.getText().clear();

        currentCursorId = -1;
    }

    /**
     * DB 삽입
     */
    private void insertData(int a_grade, int a_number, String a_strName) {
        ContentValues values = new ContentValues();
        values.put(StudentEntry.GRADE, a_grade);
        values.put(StudentEntry.NUMBER, a_number);
        values.put(StudentEntry.NAME, a_strName);

        // DB 삽입
        currentCursorId = (int) mDb.insert(StudentEntry.TABLE_NAME, null, values);
    }

    /**
     * DB 갱신
     */
    private void updateData(int a_id, int a_grade, int a_number, String a_strName) {
        ContentValues values = new ContentValues();
        values.put(StudentEntry.GRADE, a_grade);
        values.put(StudentEntry.NUMBER, a_number);
        values.put(StudentEntry.NAME, a_strName);

        // DB 갱신
        mDb.update(StudentEntry.TABLE_NAME, values, StudentEntry._ID + "=" + a_id, null);
    }

    /**
     * DB query
     */
    private List getAllData() {
        List entityList = new ArrayList<>();

        String[] projection = {
                StudentEntry._ID,
                StudentEntry.GRADE,
                StudentEntry.NUMBER,
                StudentEntry.NAME
        };

        // 전체 query
        Cursor cursor = mDb.query(StudentEntry.TABLE_NAME, projection,
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(StudentEntry._ID));
            int grade = cursor.getInt(cursor.getColumnIndexOrThrow(StudentEntry.GRADE));
            int number = cursor.getInt(cursor.getColumnIndexOrThrow(StudentEntry.NUMBER));
            String strName = cursor.getString(cursor.getColumnIndexOrThrow(StudentEntry.NAME));
            entityList.add(new PhStudentEntity(id, grade, number, strName));
        }
        cursor.close();

        return entityList;
    }

    /**
     * DB 삭제
     */
    private boolean deleteData(int a_id) {
        return mDb.delete(StudentEntry.TABLE_NAME, StudentEntry._ID + "=" + a_id, null) > 0;
    }
}