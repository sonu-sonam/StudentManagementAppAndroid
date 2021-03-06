package com.example.sonamserchan.studentmanagementapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sonamserchan.studentmanagementapp.adapter.ListAdapterStudent;
import com.example.sonamserchan.studentmanagementapp.model.Student;
import com.example.sonamserchan.studentmanagementapp.repo.StudentRepo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StudentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public StudentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentFragment newInstance(String param1, String param2) {
        StudentFragment fragment = new StudentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //initializing variables
    ListView listViewStudent;
    StudentRepo studentRepo;
    View rootView;
    CheckBox cbStudent;
    ListAdapterStudent adapter;
    List<Student> arrayofStudents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_student, container, false);
        //Construct the data source
        studentRepo = new StudentRepo();
        listViewStudent = rootView.findViewById(R.id.listViewStudent);

        setAdapter();

        listViewStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), StudentRecordActivity.class);
                Student student = (Student) adapterView.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("student", student);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return rootView;
    }

    //adapter for listview
    public void setAdapter() {
        arrayofStudents = studentRepo.getStudents();
        //Create the adapter to convert the array to views
        adapter = new ListAdapterStudent(getActivity(), arrayofStudents);
        //Attach the adapter to a Listview
        listViewStudent.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                //do something
                Intent intent = new Intent(getActivity(), StudentAddActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_delete:
                //do something
                deleteData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteData() {
        boolean[] checked = adapter.getCheckBoxState();
        ArrayList<Student> selectedItems = new ArrayList<Student>();
        //String[] studentId = {};
        List<String> studentId = new ArrayList<>();
        for (int i = 0; i < checked.length; i++) {
            if (checked[i]) {
                selectedItems.add(adapter.getItem(i));
                studentId.add(String.valueOf(adapter.getItem(i).getStudentId()));
            }
        }

        //String[] studentId = new String[selectedItems.size()];
        //trying to get checked student IDs in array of strings
        /*for (int i = 0; i < selectedItems.size(); i++) {
            studentId[i] = String.valueOf(adapter.getItem(i).getStudentId());
        }*/
        studentRepo.delete(studentId);

        setAdapter();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
