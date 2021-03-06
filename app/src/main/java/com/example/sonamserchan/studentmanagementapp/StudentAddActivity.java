package com.example.sonamserchan.studentmanagementapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sonamserchan.studentmanagementapp.app.App;
import com.example.sonamserchan.studentmanagementapp.model.Student;
import com.example.sonamserchan.studentmanagementapp.repo.StudentRepo;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StudentAddActivity extends AppCompatActivity {

    Spinner spinnerCourse, spinnerGender;
    EditText etStudentId, etFirstName, etLastName, etAge, etAddress;
    String spCourse, spGender;
    ArrayAdapter<CharSequence> adapter;

    public static final String TAG = StudentAddActivity.class.getSimpleName();
    Student editStudent;
    boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_add);

        //Edittext
        etStudentId = findViewById(R.id.editTextStudentId);
        etFirstName = findViewById(R.id.editTextFirstName);
        etLastName = findViewById(R.id.editTextLastName);
        etAge = findViewById(R.id.editTextAge);
        etAddress = findViewById(R.id.editTextAddress);

        //enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinnerGender = findViewById(R.id.spinnerGender);
        //Spinner for course study
        spinnerCourse = findViewById(R.id.spinnerCourse);
        //create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this, R.array.entries_course, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        spinnerCourse.setAdapter(adapter);
        //implement setOnItemSelectedListener for spinner
        spinnerCourse.setOnItemSelectedListener(spinnerCourseListener);
        spinnerGender.setOnItemSelectedListener(spinnerGenderListener);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            editStudent = (Student) bundle.getSerializable("student");
            setEditValues();
        }
    }

    public void setEditValues(){
        etStudentId.setText(String.valueOf(editStudent.getStudentId()));
        etFirstName.setText(editStudent.getFirstName());
        etLastName.setText(editStudent.getLastName());
        int coursePosition = adapter.getPosition(editStudent.getCourseStudy());
        spinnerCourse.setSelection(coursePosition);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.entries_gender, android.R.layout.simple_spinner_item);
        int genderPosition = genderAdapter.getPosition(editStudent.getGender());
        spinnerGender.setSelection(genderPosition);
        etAge.setText(String.valueOf(editStudent.getAge()));
        etAddress.setText(editStudent.getAddress());
        edit = true;
    }

    AdapterView.OnItemSelectedListener spinnerCourseListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            spCourse = String.valueOf(spinnerCourse.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    AdapterView.OnItemSelectedListener spinnerGenderListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            spGender = String.valueOf(spinnerGender.getSelectedItem());
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save_show, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();;
                break;
            case R.id.menu_save:
                saveData();
                break;
            case R.id.menu_showData:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("ItemPosition", 0);
        startActivity(intent);
    }

    private void saveData(){
        Student student = new Student();
        student.setStudentId(Integer.parseInt(String.valueOf(etStudentId.getText())));
        student.setFirstName(String.valueOf(etFirstName.getText()));
        student.setLastName(String.valueOf(etLastName.getText()));
        student.setGender(spGender);
        student.setCourseStudy(spCourse);
        student.setAge(Integer.parseInt(String.valueOf(etAge.getText())));
        student.setAddress(String.valueOf(etAddress.getText()));
        StudentRepo studentRepo = new StudentRepo();
        if(edit == false){
            //insert new record
            studentRepo.insert(student);
        } else {
            //update data
            studentRepo.update(student);
        }
        Toast.makeText(StudentAddActivity.this, String.valueOf("A new student has been added to the database"), Toast.LENGTH_SHORT).show();
        clearForm();
    }

    private void clearForm(){
        etStudentId.setText("");
        etFirstName.setText("");
        etLastName.setText("");
        etAge.setText("");
        etAddress.setText("");
        spinnerGender.setSelection(0);
        spinnerCourse.setSelection(0);
    }

}
