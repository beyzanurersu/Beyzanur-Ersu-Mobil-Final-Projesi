package com.example.finalproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class ListeAdapter extends RecyclerView.Adapter<ListeAdapter.MyViewHolder> {

    Context context;
    List<Model> listeler;
    ListeAdapter(Context context , List<Model> listeler){

        this.context=context;
        this.listeler=listeler;

    }
    @NonNull
    @Override
    public ListeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rv_item_liste_icin,parent,false);
        ListeAdapter.MyViewHolder holder=new ListeAdapter.MyViewHolder(view);
        return holder;
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ListeAdapter.MyViewHolder holder, final int position) {
            holder.isim.setText(String.valueOf(position+1)+". Liste");

            if(listeler.get(position).tamamlandimi.equals("1")){
                holder.card.setCardBackgroundColor(Color.parseColor("#F3BCC3"));
                holder.btnSil.setVisibility(View.GONE);
            }
            else{

            }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListeDetayActivity.class);
                intent.putExtra("listeindex",String.valueOf(position));//değiştirme
                context.startActivity(intent);
                ((Activity)context).finish();

            }
        });
        holder.btnPaylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Paylaşılıyor..",Toast.LENGTH_SHORT).show();
                String message="";
                message+=String.valueOf(position+1)+". Liste \nAlışveriş Tarihi: "+
                        listeler.get(position).tarih
                        +"\nAlışveriş Notu: "
                        +listeler.get(position).not
                +"\nAlışveriş Yeri: "+listeler.get(position).alisveris_yeri+"\n";
                for( Model.Urun u:listeler.get(position).urunler){
                    message+=("\nÜrün: "+u.isim);
                    message+=("\nAdet: "+u.adet);
                    message+=("\n_______________________");
                }
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,  message);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                context.startActivity(shareIntent);
            }
        });

        holder.btnSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listeler.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, listeler.size());
                holder.itemView.setVisibility(View.GONE);
                Toast.makeText(context,"Liste Silindi",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listeler.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        Button btnSil,btnPaylas;
        TextView isim;
        CardView card;

        public MyViewHolder(View itemview){
            super(itemview);
            btnSil=itemview.findViewById(R.id.sil);
            btnPaylas=itemview.findViewById(R.id.paylas);
            isim=itemview.findViewById(R.id.isim);
            card=itemview.findViewById(R.id.card);
        }

    }
}
