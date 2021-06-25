package com.example.finalproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class UrunListesiAdapter extends RecyclerView.Adapter<UrunListesiAdapter.MyViewHolder> {

    Context context;
    List<Model.Urun> urunler;
    String listeno;
    public static RecyclerView.Adapter adapter;

    UrunListesiAdapter(Context context , List<Model.Urun> urunler,String listeno){
        this.context=context;
        this.urunler=urunler;
        this.listeno=listeno;
    }
    @NonNull
    @Override
    public UrunListesiAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_item,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final UrunListesiAdapter.MyViewHolder holder, final int position) {

        if(urunler.get(position).alindimi.equals("0")){//alınmadı
            holder.isim.setText(urunler.get(position).getIsim());
            holder.adet.setText(urunler.get(position).getAdet());
        }
        else{//alındı
            holder.isim.setText(urunler.get(position).getIsim());
            holder.adet.setText(urunler.get(position).getAdet());
            holder.btnAlındı.setVisibility(View.GONE);
            holder.btnGuncelle.setBackgroundColor(Color.parseColor("#F3BCC3"));
            holder.btnGuncelle.setText("Bu ürün alındı !!");
            holder.btnGuncelle.setEnabled(false);
            holder.btnGuncelle.setTextColor(Color.parseColor("#000000"));
            holder.card.setCardBackgroundColor(Color.parseColor("#F3BCC3"));
        }

    holder.btnAlındı.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseDatabase.getInstance().getReference().child(String.valueOf(listeno)).child("urunler").child(String.valueOf(position)).child("alindimi").setValue("1");

            notifyDataSetChanged();
            Intent intent = new Intent(context, ListeDetayActivity.class);
            intent.putExtra("listeindex",listeno);
            Toast.makeText(context,"Ürün alındı olarak işaretlendi.",Toast.LENGTH_SHORT).show();
            context.startActivity(intent);
            ((Activity)context).finish();

        }
    });
        holder.btnGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog urunekledialog;
                Button butonkaydet;
                final TextView isim,adet;
                urunekledialog = new Dialog(context);
                urunekledialog.setContentView(R.layout.popup_urunekle);
                urunekledialog.getWindow().setGravity(Gravity.CENTER);

                isim = urunekledialog.findViewById(R.id.isim);
                adet = urunekledialog.findViewById(R.id.adet);
                isim.setText(urunler.get(position).getIsim().toString());
                adet.setText(urunler.get(position).getAdet().toString());
                butonkaydet = urunekledialog.findViewById(R.id.kaydet);

                butonkaydet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String s_urun=isim.getText().toString();
                        String s_adet=adet.getText().toString();

                        Model.Urun temp=new Model.Urun();
                        temp.isim=s_urun;
                        temp.adet=s_adet;
                        temp.alindimi="0";
                        FirebaseDatabase.getInstance().getReference().child(listeno).child("urunler").child(String.valueOf(String.valueOf(position))).setValue(temp);
                        Intent intent = new Intent(context, ListeDetayActivity.class);
                        intent.putExtra("listeindex",listeno);
                        Toast.makeText(context,"Ürün Güncellendi",Toast.LENGTH_SHORT).show();
                        context.startActivity(intent);
                        urunekledialog.dismiss();
                        ((Activity)context).finish();
                    }
                });
                urunekledialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return urunler.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        Button btnAlındı,btnGuncelle;
        TextView isim,adet;
        CardView card;

        public MyViewHolder(View itemview){
            super(itemview);
            btnAlındı=itemview.findViewById(R.id.sil);
            btnGuncelle=itemview.findViewById(R.id.guncelle);
            adet=itemview.findViewById(R.id.adet);
            isim=itemview.findViewById(R.id.isim);
            card=itemview.findViewById(R.id.card);
        }

    }
}
