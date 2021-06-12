package com.SakshmBhat.sit_hub_administrator.faculty_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.SakshmBhat.sit_hub_administrator.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateAndAddFacultyActivity extends AppCompatActivity {

    FloatingActionButton fAB;

    private RecyclerView ISE,AD,CSE,ECE,ME,TE,EIE,CE,IE,IEM,Maths,Physics,Chemistry,BT,MBA,Architecture,AandNT;

    private LinearLayout ISEnoData,CSEnoData,ADnoData,ECEnoData,MEnoData,TEnoData,EIEnoData,CEnoData,IEnoData,IEMnoData,MathsNoData,PhysicsNoData,ChemistryNoData,BTnoData,MBAnoData,ArchitectureNoData,AandNTnoData;

    private List<FacultyAttributes> ISE_list,AD_list,CSE_list,ECE_list,ME_list,TE_list,EIE_list,CE_list,IE_list,IEM_list,Maths_list,Physics_list,Chemistry_list,BT_list,MBA_list,Architecture_list,AandNT_list;

    private DatabaseReference databaseReference,dbRef;

    private FacultyInfoAdapter recyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_and_add_faculty);

        fAB=findViewById(R.id.floatingAB);

        AD=findViewById(R.id.ADFacultyRecycler);
        ISE=findViewById(R.id.ISEFacultyRecycler);
        CSE=findViewById(R.id.CSEFacultyRecycler);
        ECE=findViewById(R.id.ECEFacultyRecycler);
        ME=findViewById(R.id.MEFacultyRecycler);
        TE=findViewById(R.id.TEFacultyRecycler);
        EIE=findViewById(R.id.EIEFacultyRecycler);
        CE=findViewById(R.id.CEFacultyRecycler);
        IE=findViewById(R.id.IEFacultyRecycler);
        IEM=findViewById(R.id.IEMFacultyRecycler);
        Maths=findViewById(R.id.MathsFacultyRecycler);
        Physics=findViewById(R.id.PhyFacultyRecycler);
        Chemistry=findViewById(R.id.ChemFacultyRecycler);
        BT=findViewById(R.id.BioFacultyRecycler);
        MBA=findViewById(R.id.MBAFacultyRecycler);
        Architecture=findViewById(R.id.ArchFacultyRecycler);
        AandNT=findViewById(R.id.NTFacultyRecycler);


        ADnoData=findViewById(R.id.ADfacultyDataNotFound);
        ISEnoData=findViewById(R.id.ISEfacultyDataNotFound);
        CSEnoData=findViewById(R.id.CSEfacultyDataNotFound);
        ECEnoData=findViewById(R.id.ECEfacultyDataNotFound);
        MEnoData=findViewById(R.id.MEfacultyDataNotFound);
        TEnoData=findViewById(R.id.TEfacultyDataNotFound);
        EIEnoData=findViewById(R.id.EIEfacultyDataNotFound);
        CEnoData=findViewById(R.id.CEfacultyDataNotFound);
        IEnoData=findViewById(R.id.IEfacultyDataNotFound);
        IEMnoData=findViewById(R.id.IEMfacultyDataNotFound);
        MathsNoData=findViewById(R.id.MathsfacultyDataNotFound);
        PhysicsNoData=findViewById(R.id.PhyfacultyDataNotFound);
        ChemistryNoData=findViewById(R.id.ChemfacultyDataNotFound);
        BTnoData=findViewById(R.id.BiofacultyDataNotFound);
        MBAnoData=findViewById(R.id.MBAfacultyDataNotFound);
        ArchitectureNoData=findViewById(R.id.ArchfacultyDataNotFound);
        AandNTnoData=findViewById(R.id.NTfacultyDataNotFound);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Faculty");


        iseDepartment();
        cseDepartment();
        eceDepartment();
        meDepartment();
        teDepartment();
        eieDepartment();
        ceDepartment();
        teDepartment();
        iemDepartment();
        mathsDepartment();
        physicsDepartment();
        chemistryDepartment();
        btDepartment();
        mbaDepartment();
        architectureDepartment();
        adDepartment();
        ieDepartment();
        ntDepartment();//NON teaching and administration





        fAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateAndAddFacultyActivity.this, AddFacultyActivity.class));
            }
        });
    }

    private void ieDepartment() {

        dbRef=databaseReference.child("Administration and Non-teaching");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                IE_list = new ArrayList<>();

                if(!snapshot.exists()){

                    IEnoData.setVisibility(View.VISIBLE);
                    IE.setVisibility(View.GONE);
                }else{

                    IEnoData.setVisibility(View.GONE);
                    IE.setVisibility(View.VISIBLE);

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                        FacultyAttributes attributes= dataSnapshot.getValue(FacultyAttributes.class);
                        IE_list.add(attributes);

                    }
                    IE.setHasFixedSize(true);
                    IE.setLayoutManager(new LinearLayoutManager(UpdateAndAddFacultyActivity.this));
                    recyclerAdapter = new FacultyInfoAdapter(IE_list, UpdateAndAddFacultyActivity.this,"IE");
                    IE.setAdapter(recyclerAdapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateAndAddFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void adDepartment() {

        dbRef=databaseReference.child("AD");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                AD_list = new ArrayList<>();

                if(!snapshot.exists()){

                    ADnoData.setVisibility(View.VISIBLE);
                    AD.setVisibility(View.GONE);
                }else{

                    ADnoData.setVisibility(View.GONE);
                    AD.setVisibility(View.VISIBLE);

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                        FacultyAttributes attributes= dataSnapshot.getValue(FacultyAttributes.class);
                        AD_list.add(attributes);

                    }
                    AD.setHasFixedSize(true);
                    AD.setLayoutManager(new LinearLayoutManager(UpdateAndAddFacultyActivity.this));
                    recyclerAdapter = new FacultyInfoAdapter(AD_list, UpdateAndAddFacultyActivity.this,"AD");
                    AD.setAdapter(recyclerAdapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateAndAddFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void ntDepartment() {

        dbRef=databaseReference.child("Administration and Non-teaching");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                AandNT_list = new ArrayList<>();

                if(!snapshot.exists()){

                    AandNTnoData.setVisibility(View.VISIBLE);
                    AandNT.setVisibility(View.GONE);
                }else{

                    AandNTnoData.setVisibility(View.GONE);
                    AandNT.setVisibility(View.VISIBLE);

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                        FacultyAttributes attributes= dataSnapshot.getValue(FacultyAttributes.class);
                        AandNT_list.add(attributes);

                    }
                    AandNT.setHasFixedSize(true);
                    AandNT.setLayoutManager(new LinearLayoutManager(UpdateAndAddFacultyActivity.this));
                    recyclerAdapter = new FacultyInfoAdapter(AandNT_list, UpdateAndAddFacultyActivity.this,"Administration and Non-teaching");
                    AandNT.setAdapter(recyclerAdapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateAndAddFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void architectureDepartment() {

        dbRef=databaseReference.child("Architecture");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Architecture_list = new ArrayList<>();

                if(!snapshot.exists()){

                    ArchitectureNoData.setVisibility(View.VISIBLE);
                    Architecture.setVisibility(View.GONE);
                }else{

                    ArchitectureNoData.setVisibility(View.GONE);
                    Architecture.setVisibility(View.VISIBLE);

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                        FacultyAttributes attributes= dataSnapshot.getValue(FacultyAttributes.class);
                        Architecture_list.add(attributes);

                    }
                    Architecture.setHasFixedSize(true);
                    Architecture.setLayoutManager(new LinearLayoutManager(UpdateAndAddFacultyActivity.this));
                    recyclerAdapter = new FacultyInfoAdapter(Architecture_list, UpdateAndAddFacultyActivity.this,"Architecture");
                    Architecture.setAdapter(recyclerAdapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateAndAddFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void mbaDepartment() {

        dbRef=databaseReference.child("MBA");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                MBA_list = new ArrayList<>();

                if(!snapshot.exists()){

                    MBAnoData.setVisibility(View.VISIBLE);
                    MBA.setVisibility(View.GONE);
                }else{

                    MBAnoData.setVisibility(View.GONE);
                    MBA.setVisibility(View.VISIBLE);

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                        FacultyAttributes attributes= dataSnapshot.getValue(FacultyAttributes.class);
                        MBA_list.add(attributes);

                    }
                    MBA.setHasFixedSize(true);
                    MBA.setLayoutManager(new LinearLayoutManager(UpdateAndAddFacultyActivity.this));
                    recyclerAdapter = new FacultyInfoAdapter(MBA_list, UpdateAndAddFacultyActivity.this,"MBA");
                    MBA.setAdapter(recyclerAdapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateAndAddFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void btDepartment() {

        dbRef=databaseReference.child("BT");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                BT_list = new ArrayList<>();

                if(!snapshot.exists()){

                    BTnoData.setVisibility(View.VISIBLE);
                    BT.setVisibility(View.GONE);
                }else{

                    BTnoData.setVisibility(View.GONE);
                    BT.setVisibility(View.VISIBLE);

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                        FacultyAttributes attributes= dataSnapshot.getValue(FacultyAttributes.class);
                        BT_list.add(attributes);

                    }
                    BT.setHasFixedSize(true);
                    BT.setLayoutManager(new LinearLayoutManager(UpdateAndAddFacultyActivity.this));
                    recyclerAdapter = new FacultyInfoAdapter(BT_list, UpdateAndAddFacultyActivity.this,"BT");
                    BT.setAdapter(recyclerAdapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateAndAddFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void chemistryDepartment() {

        dbRef=databaseReference.child("Chemistry");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Chemistry_list = new ArrayList<>();

                if(!snapshot.exists()){

                    ChemistryNoData.setVisibility(View.VISIBLE);
                    Chemistry.setVisibility(View.GONE);
                }else{

                    ChemistryNoData.setVisibility(View.GONE);
                    Chemistry.setVisibility(View.VISIBLE);

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                        FacultyAttributes attributes= dataSnapshot.getValue(FacultyAttributes.class);
                        Chemistry_list.add(attributes);

                    }
                    Chemistry.setHasFixedSize(true);
                    Chemistry.setLayoutManager(new LinearLayoutManager(UpdateAndAddFacultyActivity.this));
                    recyclerAdapter = new FacultyInfoAdapter(Chemistry_list, UpdateAndAddFacultyActivity.this,"Chemistry");
                    Chemistry.setAdapter(recyclerAdapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateAndAddFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void physicsDepartment() {

        dbRef=databaseReference.child("Physics");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Physics_list = new ArrayList<>();

                if(!snapshot.exists()){

                    PhysicsNoData.setVisibility(View.VISIBLE);
                    Physics.setVisibility(View.GONE);
                }else{

                    PhysicsNoData.setVisibility(View.GONE);
                    Physics.setVisibility(View.VISIBLE);

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                        FacultyAttributes attributes= dataSnapshot.getValue(FacultyAttributes.class);
                        Physics_list.add(attributes);

                    }
                    Physics.setHasFixedSize(true);
                    Physics.setLayoutManager(new LinearLayoutManager(UpdateAndAddFacultyActivity.this));
                    recyclerAdapter = new FacultyInfoAdapter(Physics_list, UpdateAndAddFacultyActivity.this,"Physics");
                    Physics.setAdapter(recyclerAdapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateAndAddFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void mathsDepartment() {

        dbRef=databaseReference.child("Maths");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Maths_list = new ArrayList<>();

                if(!snapshot.exists()){

                    MathsNoData.setVisibility(View.VISIBLE);
                    Maths.setVisibility(View.GONE);
                }else{

                    MathsNoData.setVisibility(View.GONE);
                    Maths.setVisibility(View.VISIBLE);

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                        FacultyAttributes attributes= dataSnapshot.getValue(FacultyAttributes.class);
                        Maths_list.add(attributes);

                    }
                    Maths.setHasFixedSize(true);
                    Maths.setLayoutManager(new LinearLayoutManager(UpdateAndAddFacultyActivity.this));
                    recyclerAdapter = new FacultyInfoAdapter(Maths_list, UpdateAndAddFacultyActivity.this,"Maths");
                    Maths.setAdapter(recyclerAdapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateAndAddFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void ceDepartment() {

        dbRef=databaseReference.child("CE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CE_list = new ArrayList<>();

                if(!snapshot.exists()){

                    CEnoData.setVisibility(View.VISIBLE);
                    CE.setVisibility(View.GONE);
                }else{

                    CEnoData.setVisibility(View.GONE);
                    CE.setVisibility(View.VISIBLE);

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                        FacultyAttributes attributes= dataSnapshot.getValue(FacultyAttributes.class);
                        CE_list.add(attributes);

                    }
                    CE.setHasFixedSize(true);
                    CE.setLayoutManager(new LinearLayoutManager(UpdateAndAddFacultyActivity.this));
                    recyclerAdapter = new FacultyInfoAdapter(CE_list, UpdateAndAddFacultyActivity.this,"CE");
                    CE.setAdapter(recyclerAdapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateAndAddFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void eieDepartment() {

        dbRef=databaseReference.child("EIE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                EIE_list = new ArrayList<>();

                if(!snapshot.exists()){

                    EIEnoData.setVisibility(View.VISIBLE);
                    EIE.setVisibility(View.GONE);
                }else{

                    EIEnoData.setVisibility(View.GONE);
                    EIE.setVisibility(View.VISIBLE);

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                        FacultyAttributes attributes= dataSnapshot.getValue(FacultyAttributes.class);
                        EIE_list.add(attributes);

                    }
                    EIE.setHasFixedSize(true);
                    EIE.setLayoutManager(new LinearLayoutManager(UpdateAndAddFacultyActivity.this));
                    recyclerAdapter = new FacultyInfoAdapter(EIE_list, UpdateAndAddFacultyActivity.this,"EIE");
                    EIE.setAdapter(recyclerAdapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateAndAddFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void iemDepartment() {

        dbRef=databaseReference.child("IEM");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                IEM_list = new ArrayList<>();

                if(!snapshot.exists()){

                    IEMnoData.setVisibility(View.VISIBLE);
                    IEM.setVisibility(View.GONE);
                }else{

                    IEMnoData.setVisibility(View.GONE);
                    IEM.setVisibility(View.VISIBLE);

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                        FacultyAttributes attributes= dataSnapshot.getValue(FacultyAttributes.class);
                        IEM_list.add(attributes);

                    }
                    IEM.setHasFixedSize(true);
                    IEM.setLayoutManager(new LinearLayoutManager(UpdateAndAddFacultyActivity.this));
                    recyclerAdapter = new FacultyInfoAdapter(IEM_list, UpdateAndAddFacultyActivity.this,"IEM");
                    IEM.setAdapter(recyclerAdapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateAndAddFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void teDepartment() {

        dbRef=databaseReference.child("TE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                TE_list = new ArrayList<>();

                if(!snapshot.exists()){

                    TEnoData.setVisibility(View.VISIBLE);
                    TE.setVisibility(View.GONE);
                }else{

                    TEnoData.setVisibility(View.GONE);
                    TE.setVisibility(View.VISIBLE);

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                        FacultyAttributes attributes= dataSnapshot.getValue(FacultyAttributes.class);
                        TE_list.add(attributes);

                    }
                    TE.setHasFixedSize(true);
                    TE.setLayoutManager(new LinearLayoutManager(UpdateAndAddFacultyActivity.this));
                    recyclerAdapter = new FacultyInfoAdapter(TE_list, UpdateAndAddFacultyActivity.this,"TE");
                    TE.setAdapter(recyclerAdapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateAndAddFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void meDepartment() {

        dbRef=databaseReference.child("ME");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ME_list = new ArrayList<>();

                if(!snapshot.exists()){

                    MEnoData.setVisibility(View.VISIBLE);
                    ME.setVisibility(View.GONE);
                }else{

                    MEnoData.setVisibility(View.GONE);
                    ME.setVisibility(View.VISIBLE);

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                        FacultyAttributes attributes= dataSnapshot.getValue(FacultyAttributes.class);
                        ME_list.add(attributes);

                    }
                    ME.setHasFixedSize(true);
                    ME.setLayoutManager(new LinearLayoutManager(UpdateAndAddFacultyActivity.this));
                    recyclerAdapter = new FacultyInfoAdapter(ME_list, UpdateAndAddFacultyActivity.this,"ME");
                    ME.setAdapter(recyclerAdapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateAndAddFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void eceDepartment() {

        dbRef=databaseReference.child("ECE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ECE_list = new ArrayList<>();

                if(!snapshot.exists()){

                    ECEnoData.setVisibility(View.VISIBLE);
                    ECE.setVisibility(View.GONE);
                }else{

                    ECEnoData.setVisibility(View.GONE);
                    ECE.setVisibility(View.VISIBLE);

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                        FacultyAttributes attributes= dataSnapshot.getValue(FacultyAttributes.class);
                        ECE_list.add(attributes);

                    }
                    ECE.setHasFixedSize(true);
                    ECE.setLayoutManager(new LinearLayoutManager(UpdateAndAddFacultyActivity.this));
                    recyclerAdapter = new FacultyInfoAdapter(ECE_list, UpdateAndAddFacultyActivity.this,"ECE");
                    ECE.setAdapter(recyclerAdapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateAndAddFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void cseDepartment() {

        dbRef=databaseReference.child("CSE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CSE_list = new ArrayList<>();

                if(!snapshot.exists()){

                    CSEnoData.setVisibility(View.VISIBLE);
                    CSE.setVisibility(View.GONE);
                }else{

                    CSEnoData.setVisibility(View.GONE);
                    CSE.setVisibility(View.VISIBLE);

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                        FacultyAttributes attributes= dataSnapshot.getValue(FacultyAttributes.class);
                        CSE_list.add(attributes);

                    }
                    CSE.setHasFixedSize(true);
                    CSE.setLayoutManager(new LinearLayoutManager(UpdateAndAddFacultyActivity.this));
                    recyclerAdapter = new FacultyInfoAdapter(CSE_list, UpdateAndAddFacultyActivity.this,"CSE");
                    CSE.setAdapter(recyclerAdapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateAndAddFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void iseDepartment() {

        dbRef=databaseReference.child("ISE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ISE_list = new ArrayList<>();

                if(!snapshot.exists()){

                    ISEnoData.setVisibility(View.VISIBLE);
                    ISE.setVisibility(View.GONE);
                }else{

                    ISEnoData.setVisibility(View.GONE);
                    ISE.setVisibility(View.VISIBLE);

                   for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                       FacultyAttributes attributes= dataSnapshot.getValue(FacultyAttributes.class);
                       ISE_list.add(attributes);

                   }
                   ISE.setHasFixedSize(true);
                   ISE.setLayoutManager(new LinearLayoutManager(UpdateAndAddFacultyActivity.this));
                   recyclerAdapter = new FacultyInfoAdapter(ISE_list, UpdateAndAddFacultyActivity.this,"ISE");
                   ISE.setAdapter(recyclerAdapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateAndAddFacultyActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
}