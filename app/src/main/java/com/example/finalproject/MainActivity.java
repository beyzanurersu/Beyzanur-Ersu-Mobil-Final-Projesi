package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements  Serializable {

    Dialog listeekledialog;
    Button butonkaydet;
    TextView not,yer;
    TextView tamamlananlar;
    FloatingActionButton listeekle;
    RecyclerView rv;
    DatabaseReference myRef;
    List<Model> liste_listesi=new ArrayList<Model>();
    List<String> liste_adları=new ArrayList<>();
    int i=1;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tamamlananlar=findViewById(R.id.tamamlananlar_text);
        listeekle=findViewById(R.id.listeekle);
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                }
                else {

                    for (DataSnapshot ds:task.getResult().getChildren() ) {
                        Model model=new Model();
                        List<Model.Urun> urunler=new ArrayList<>();
                        for(DataSnapshot urun:ds.child("urunler").getChildren()){
                            Model.Urun u=new Model.Urun();
                            u.adet=urun.child("adet").getValue().toString();
                            u.isim=urun.child("isim").getValue().toString();
                            u.alindimi=urun.child("alindimi").getValue().toString();

                            urunler.add(u);
                        }

                        model.alisveris_yeri=ds.child("alisveris_yeri").getValue().toString();
                        model.not=ds.child("not").getValue().toString();
                        model.tamamlandimi=ds.child("tamamlandimi").getValue().toString();
                        model.tarih=ds.child("tarih").getValue().toString();
                        model.urunler=urunler;
                        liste_listesi.add(model);
                    }

                    int tamamlandı=0;
                    int tamamlanmadı=0;
                    for (Model m:liste_listesi){
                       if( m.tamamlandimi.equals("0")){
                            tamamlanmadı=tamamlanmadı+1;
                        }
                        else{
                            tamamlandı=tamamlandı+1;
                        }
                        liste_adları.add(i+". Liste");
                        i=i+1;
                    }
                    tamamlananlar.setText(String.valueOf(tamamlandı)+"/"+String.valueOf(liste_listesi.size())+" Tamamlandı");

                    rv=findViewById(R.id.listeler);
                    LinearLayoutManager layoutManager;
                    layoutManager=new LinearLayoutManager(getApplicationContext());
                    rv.setLayoutManager(layoutManager);
                    ListeAdapter adp;
                    adp=new ListeAdapter(MainActivity.this,liste_listesi);
                    rv.setAdapter(adp);
                }
            }
        });

        listeekle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listeekledialog = new Dialog(MainActivity.this);
                listeekledialog.setContentView(R.layout.popup_listeekle);
                listeekledialog.getWindow().setGravity(Gravity.CENTER);

                not = listeekledialog.findViewById(R.id.not);
                yer = listeekledialog.findViewById(R.id.yer);
                butonkaydet = listeekledialog.findViewById(R.id.kaydet);

                butonkaydet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String s_not=not.getText().toString();
                        String s_yer=yer.getText().toString();
                        final Calendar c = Calendar.getInstance();

                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);

                        String tt=String.valueOf(day+"."+String.valueOf(month)+"."+String.valueOf(year));
                        Model m=new Model();
                        m.tamamlandimi="0";
                        m.tarih=tt;
                        m.not=not.getText().toString();
                        m.alisveris_yeri=yer.getText().toString();
                        m.urunler=null;

                        FirebaseDatabase.getInstance().getReference().child(String.valueOf(String.valueOf(liste_listesi.size()))).setValue(m);
                        FirebaseDatabase.getInstance().getReference().child(String.valueOf(String.valueOf(liste_listesi.size()))).child("urunler").setValue("0");

                        Intent refresh = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(refresh);
                        finish();

                        listeekledialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Liste Oluşturuldu",Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });
                listeekledialog.show();

            }
        });


    }
}