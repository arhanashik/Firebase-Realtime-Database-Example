package com.blackspider.fcmrealtimedbexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DeveloperAdapter.OnDeveloperSelectedListener{
    private TextView tvNoData;
    private RecyclerView recyclerView;

    private List<Developer> developers;
    private DeveloperAdapter adapter;

    private DatabaseReference developerReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();
    }

    private void initView() {
        developerReference = FirebaseDatabase.getInstance().getReference("developers");

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDataDialog();
            }
        });

        tvNoData = findViewById(R.id.txt_no_item);
        recyclerView = findViewById(R.id.rv_developers);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        developers = new ArrayList<>();
        adapter = new DeveloperAdapter(developers);
        adapter.setDeveloperSelectedListener(this);
        recyclerView.setAdapter(adapter);

        developerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                developers.clear();
                for(DataSnapshot developerSnapshot : dataSnapshot.getChildren()){
                    developers.add(developerSnapshot.getValue(Developer.class));
                }

                if(developers.size()>0) {
                    tvNoData.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeveloperClicked(Developer developer) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("developer", developer);

        startActivity(intent);
    }

    @Override
    public void onDeveloperLongClicked(Developer developer) {
        customizeDeveloper(developer);
    }

    private void showAddDataDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.prompt_developer, null);
        final EditText etDeveloperName = view.findViewById(R.id.et_name);
        final Spinner spExpertField = view.findViewById(R.id.sp_expert);
        Button btnAddDeveloper = view.findViewById(R.id.btn_add_developer);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add developer");
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        btnAddDeveloper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etDeveloperName.getText().toString();

                if(TextUtils.isEmpty(name)) {
                    Toast.makeText(MainActivity.this, "Name required!", Toast.LENGTH_SHORT).show();
                }
                else if(spExpertField.getSelectedItemPosition() == 0){
                    Toast.makeText(MainActivity.this, "Development field required!", Toast.LENGTH_SHORT).show();
                }
                else {
                    String expertIn = spExpertField.getSelectedItem().toString();
                    addDeveloper(name, expertIn);
                }
            }
        });
    }

    private void addDeveloper(String name, String expertIn) {
        String id = developerReference.push().getKey();

        Developer developer = new Developer(id, name, expertIn);

        developerReference.child(id).setValue(developer);

        Toast.makeText(this, "Developer added", Toast.LENGTH_SHORT).show();
    }

    private void customizeDeveloper(final Developer developer){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.prompt_developer, null);
        final EditText etDeveloperName = view.findViewById(R.id.et_name);
        final Spinner spExpertField = view.findViewById(R.id.sp_expert);
        Button btnUpdateDeveloper = view.findViewById(R.id.btn_add_developer);
        Button btnDeleteDeveloper = view.findViewById(R.id.btn_delete_developer);

        etDeveloperName.setText(developer.getDeveloperName());
        btnUpdateDeveloper.setText("Update Developer");
        btnDeleteDeveloper.setVisibility(View.VISIBLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Developer");
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

        btnUpdateDeveloper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etDeveloperName.getText().toString();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(MainActivity.this, "Name is required!", Toast.LENGTH_SHORT).show();
                }
                else if(spExpertField.getSelectedItemPosition() == 0){
                    Toast.makeText(MainActivity.this, "Development field required!", Toast.LENGTH_SHORT).show();
                }
                else {
                    String expertIn = spExpertField.getSelectedItem().toString();

                    developer.setDeveloperName(name);
                    developer.setDeveloperExpert(expertIn);

                    developerReference
                            .child(developer.getDeveloperId())
                            .setValue(developer);
                    Toast.makeText(MainActivity.this, "Developer Updated!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDeleteDeveloper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                developerReference.child(developer.getDeveloperId()).removeValue();
                DatabaseReference projectsReference = FirebaseDatabase.getInstance()
                        .getReference("projects").child(developer.getDeveloperId());
                projectsReference.removeValue();
                Toast.makeText(MainActivity.this, "Developer deleted!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
