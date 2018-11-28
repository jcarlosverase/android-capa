package pe.startup.myapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import pe.startup.myapp.R;
import pe.startup.myapp.adapter.ModelAdapter;
import pe.startup.myapp.entity.Model;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModelFragment extends Fragment {

    private DatabaseReference mDatabase;
    private DatabaseReference mModelReference;
    private ChildEventListener mModelListener;

    protected RecyclerView rvModel;
    protected ModelAdapter modelAdapter;
    protected List<Model> listModel;

    private String TAG = "Firebase";

    public ModelFragment() {
        // Required empty public constructor
    }

    public static ModelFragment newInstance() {
        ModelFragment fragment = new ModelFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_model, container, false);

        setUpView(view);
        //initData();
        //loadData();
        listenRealtimeDatabase();

        return view;
    }

    private void setUpView(View view) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mModelReference = FirebaseDatabase.getInstance().getReference("models");
        listModel = new ArrayList<Model>();

        rvModel = (RecyclerView)view.findViewById(R.id.rvModel);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvModel.setLayoutManager(linearLayoutManager);
        rvModel.addItemDecoration(new DividerItemDecoration(rvModel.getContext(), DividerItemDecoration.VERTICAL));
    }

    private void initData() {

        listModel = new ArrayList<Model>();

        Model model;
        model = new Model();
        model.setId(0);
        model.setName("A7");
        model.setDescription("El mejor auto del mundo");
        model.setPhoto("https://www.audi.com/content/dam/gbp2/experience-audi/models-and-technology/serial-models/q7/my2019/1920x1080-gal-prop-tx/1920x1080_desktop_AQ7_151003_3.jpg?downsize=600px:*");
        listModel.add(model);

        model = new Model();
        model.setId(1);
        model.setName("A8");
        model.setDescription("El mejor auto del mundo");
        model.setPhoto("https://www.audi.com/content/dam/gbp2/experience-audi/models-and-technology/serial-models/q7/my2019/1920x1080-gal-prop-tx/1920x1080_desktop_AQ7_151003_3.jpg?downsize=600px:*");
        listModel.add(model);
    }

    private void loadData() {

        modelAdapter = new ModelAdapter(getContext(), listModel);

        rvModel.setAdapter(modelAdapter);
    }

    private void listenRealtimeDatabase() {

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                // A new data item has been added, add it to the list
                Model model = dataSnapshot.getValue(Model.class);
                listModel.add(model);
                loadData();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A data item has changed
                Model model = dataSnapshot.getValue(Model.class);

                listModel.set(model.getId(), model);
                loadData();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A data item has been removed
                Model model = dataSnapshot.getValue(Model.class);
                loadData();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A data item has changed position
                Model model = dataSnapshot.getValue(Model.class);
                loadData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
                Toast.makeText(getContext(), "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        };

        mModelReference.addChildEventListener(childEventListener);

    }

}
