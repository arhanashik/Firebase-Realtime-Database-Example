package com.blackspider.fcmrealtimedbexample;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity implements ProjectsAdapter.OnProjectSelectedListener{
    private TextView tvNoData;
    private RecyclerView recyclerView;

    private DatabaseReference projectsReference;
    private Developer developer;

    private List<Project> projects;
    private ProjectsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        developer = (Developer) getIntent().getSerializableExtra("developer");

        if(developer == null) {
            Toast.makeText(this, "Invalid developer", Toast.LENGTH_SHORT).show();
            finish();
        }

        setTitle(developer.getDeveloperName());

        initView();
    }

    @Override
    public void onProjectSelected(Project project) {
        customizeProject(project);
    }

    private void initView() {
        projectsReference = FirebaseDatabase.getInstance()
                .getReference("projects") //get projects database
                .child(developer.getDeveloperId()); //get the child for this id(foreign key)

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddProjectDataDialog();
            }
        });

        tvNoData = findViewById(R.id.tv_no_project);
        recyclerView = findViewById(R.id.rv_projects);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        projects = new ArrayList<>();
        adapter = new ProjectsAdapter(projects);
        adapter.setProjectSelectedListener(this);
        recyclerView.setAdapter(adapter);

        projectsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                projects.clear();
                for(DataSnapshot projectSnapshot : dataSnapshot.getChildren()){
                    projects.add(projectSnapshot.getValue(Project.class));
                }

                if(projects.size()>0) {
                    tvNoData.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showAddProjectDataDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.prompt_project, null);
        final EditText etProjectName = view.findViewById(R.id.et_project_name);
        final EditText etPlatform = view.findViewById(R.id.et_platform);
        Button btnAddProject = view.findViewById(R.id.btn_add_project);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Project");
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        btnAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etProjectName.getText().toString();
                String platform = etPlatform.getText().toString();

                addProject(name, platform);
            }
        });
    }

    private void addProject(String name, String platform) {
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(platform)) {
            Toast.makeText(this, "All fields required!", Toast.LENGTH_SHORT).show();
        }
        else {
            String id = projectsReference.push().getKey();

            Project project = new Project(id, name, platform);

            projectsReference.child(id).setValue(project);

            Toast.makeText(this, "Project added", Toast.LENGTH_SHORT).show();
        }
    }

    private void customizeProject(final Project project){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.prompt_project, null);
        final EditText etProjectName = view.findViewById(R.id.et_project_name);
        final EditText etPlatform = view.findViewById(R.id.et_platform);
        Button btnUpdateProject = view.findViewById(R.id.btn_add_project);
        Button btnDeleteProject = view.findViewById(R.id.btn_delete_project);

        etProjectName.setText(project.getProjectName());
        etPlatform.setText(project.getPlatform());
        btnUpdateProject.setText("Update Project");
        btnDeleteProject.setVisibility(View.VISIBLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Project");
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

        btnUpdateProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etProjectName.getText().toString();
                String platform = etPlatform.getText().toString();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(platform)){
                    Toast.makeText(DetailsActivity.this, "All fields required!", Toast.LENGTH_SHORT).show();
                }
                else {
                    project.setProjectName(name);
                    project.setPlatform(platform);

                    projectsReference
                            .child(project.getProjectId())
                            .setValue(project);
                    Toast.makeText(DetailsActivity.this, "Project Updated!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDeleteProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectsReference.child(project.getProjectId()).removeValue();
                Toast.makeText(DetailsActivity.this, "Project deleted!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
