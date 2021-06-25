package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListeDetayActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    RecyclerView rv;
    TextView tarih,not,yer;
    FloatingActionButton uruneklebtn;
    Button foto,map,alarm;
    ImageView imageView;
    Dialog urunekledialog;
    Button butonkaydet;
    TextView isim,adet;
    String listeno;
    Model liste ;
    Model model=new Model();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_detay);
        uruneklebtn=findViewById(R.id.urunekle);
        tarih=findViewById(R.id.tarih);
        not=findViewById(R.id.not);
        yer=findViewById(R.id.yer);
        foto=findViewById(R.id.foto);
        map=findViewById(R.id.map);
        imageView =findViewById(R.id.image);
        alarm=findViewById(R.id.alarm);

        Intent i = getIntent();

            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if(extras == null) {
                    listeno= null;

                } else {
                    liste= (Model) getIntent().getSerializableExtra("listemodel");
                    listeno= extras.getString("listeindex");

                    setTitle(String.valueOf(Integer.parseInt(listeno)+1)+". Alışveriş Listem");
                    DatabaseReference myRef;
                    myRef = FirebaseDatabase.getInstance().getReference();

                    myRef.child(listeno).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            }
                            else {
                                Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                List<Model.Urun> urunler=new ArrayList<>();

                                    for(DataSnapshot urun:task.getResult().child("urunler").getChildren()){
                                        Model.Urun u=new Model.Urun();
                                        u.adet=urun.child("adet").getValue().toString();
                                        u.isim=urun.child("isim").getValue().toString();
                                        u.alindimi=urun.child("alindimi").getValue().toString();

                                        urunler.add(u);
                                    }

                                model.alisveris_yeri=task.getResult().child("alisveris_yeri").getValue().toString();
                                model.not=task.getResult().child("not").getValue().toString();
                                model.tamamlandimi=task.getResult().child("tamamlandimi").getValue().toString();
                                model.tarih=task.getResult().child("tarih").getValue().toString();
                                model.urunler=urunler;
                                liste=model;

                                String filepath = "/storage/emulated/0/Pictures/finalProject/liste"+listeno+".jpg";
                                Bitmap bitmap = BitmapFactory.decodeFile(filepath);
                                if(bitmap==null){

                                }
                                else{
                                    imageView.setImageBitmap(bitmap);
                                    foto.setVisibility(View.GONE);
                                }
                                tarih.setText(model.tarih);
                                not.setText(model.not);
                                yer.setText(model.alisveris_yeri);

                                int alınanlar=0;
                                for (int i=0 ;i<model.urunler.size();i++){
                                   if( model.urunler.get(i).alindimi.equals("1")){
                                       alınanlar=alınanlar+1;
                                   }
                                }
                                if(alınanlar==model.urunler.size()){
                                    FirebaseDatabase.getInstance().getReference().child(String.valueOf(listeno)).child("tamamlandimi").setValue("1");
                                }
                                else{
                                    FirebaseDatabase.getInstance().getReference().child(String.valueOf(listeno)).child("tamamlandimi").setValue("0");
                                }
                                rv=findViewById(R.id.urun_listesi);
                                LinearLayoutManager layoutManager;
                                layoutManager=new LinearLayoutManager(getApplicationContext());
                                rv.setLayoutManager(layoutManager);
                                UrunListesiAdapter adp;
                                adp=new UrunListesiAdapter(ListeDetayActivity.this,model.urunler,listeno);
                                rv.setAdapter(adp);
                            }
                        }
                    });

                }
            } else {
                liste=savedInstanceState.getParcelable("listemodel");
                listeno= (String) savedInstanceState.getSerializable("listeindex");

            }
        uruneklebtn.setOnClickListener(new View.OnClickListener() {
            int size;
            public void onClick(View v) {

                showPopup(ListeDetayActivity.this);
                if(liste.urunler.size()==0){
                    size=0;
                }
                else{
                    size=liste.urunler.size();
                }
            }
        });

            foto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String filepath = "/storage/emulated/0/Pictures/finalProject/liste"+listeno+".jpg";
                    Bitmap bitmap = BitmapFactory.decodeFile(filepath);
                    Intent i=new Intent(ListeDetayActivity.this,ImageActivity.class);
                    i.putExtra("image",bitmap);
                    startActivity(i);
                }
            });


        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ListeDetayActivity.this,LocationActivity.class);
                startActivity(i);



            }
        });

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        String filepath = "/storage/emulated/0/Pictures/finalProject/liste"+listeno+".jpg";
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(selectedImage);
                        foto.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Resim kaydedildi",Toast.LENGTH_SHORT).show();
                        try (FileOutputStream out = new FileOutputStream(filepath)) {
                            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
            }
        }
    }

    public void showPopup(final ListeDetayActivity context) {

        urunekledialog = new Dialog(context);
        urunekledialog.setContentView(R.layout.popup_urunekle);
        urunekledialog.getWindow().setGravity(Gravity.CENTER);

        isim = urunekledialog.findViewById(R.id.isim);
        adet = urunekledialog.findViewById(R.id.adet);
        butonkaydet = urunekledialog.findViewById(R.id.kaydet);

        butonkaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s_urun=isim.getText().toString();
                String s_adet=adet.getText().toString();

                int size;
                    if(liste.urunler.size()==0){
                        size=0;
                    }
                    else{
                        size=liste.urunler.size();
                    }

                Model.Urun temp=new Model.Urun();
                temp.isim=s_urun;
                temp.adet=s_adet;
                temp.alindimi="0";

                FirebaseDatabase.getInstance().getReference().child(listeno).child("urunler").child(String.valueOf(size)).setValue(temp);
                Intent intent = new Intent(ListeDetayActivity.this, ListeDetayActivity.class);
                intent.putExtra("listeindex",listeno);//değiştirme
                startActivity(intent);
                urunekledialog.dismiss();
                Toast.makeText(getApplicationContext(),"Ürün Eklendi",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        urunekledialog.show();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ListeDetayActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        updateTimeText(c);
        startAlarm(c);
    }

    private void updateTimeText(Calendar c) {
        String timeText = "Alarm set for: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
    }
    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        Toast.makeText(getApplicationContext(),"Alarm Kuruldu",Toast.LENGTH_SHORT).show();
    }

}

