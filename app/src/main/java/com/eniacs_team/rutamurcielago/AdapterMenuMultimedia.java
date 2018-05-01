package com.eniacs_team.rutamurcielago;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Adaptador que controla los elementos del recyclerView.
 */

public class AdapterMenuMultimedia extends RecyclerView.Adapter<AdapterMenuMultimedia.ViewHolder> {


    private List<listItemMenuMultimedia> listItems;
    private Context context;

    public AdapterMenuMultimedia(List<listItemMenuMultimedia> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    /**
     * Método que crea el layout para un item del view holder.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_multimedia, parent,false);

        int height = parent.getMeasuredHeight() / getItemCount();
        int width = parent.getMeasuredWidth();

        v.setLayoutParams(new RecyclerView.LayoutParams(width, height));

        return new ViewHolder(v);
    }

    /**
     *
     *
     * @param holder   El viewHolder que es modificado segun información deseada.
     * @param position posición en el viewHolder.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        listItemMenuMultimedia listItem = listItems.get(position);

        if(listItem.getTitulo().equals("Descripción")){
            Log.d("HayDescripción","true");
            holder.cardView.setBackgroundColor(ContextCompat.getColor(context,R.color.dot_light_screen0));
            holder.iconoMultimedia.setImageResource(R.mipmap.imagen_multimedia);
            holder.titulo.setText(listItem.getTitulo());
        }else if(listItem.getTitulo().equals("Imagen")){
            holder.cardView.setBackgroundColor(ContextCompat.getColor(context,R.color.dot_light_screen4));
            holder.iconoMultimedia.setImageResource(R.mipmap.imagen_multimedia);
            holder.titulo.setText(listItem.getTitulo());
        }else if(listItem.getTitulo().equals("Audio")){
            holder.cardView.setBackgroundColor(ContextCompat.getColor(context,R.color.dot_light_screen3));
            holder.iconoMultimedia.setImageResource(R.mipmap.audio_multimedia);
            holder.titulo.setText(listItem.getTitulo());
        }else if(listItem.getTitulo().equals("Video")){
            holder.cardView.setBackgroundColor(ContextCompat.getColor(context,R.color.dot_light_screen2));
            holder.iconoMultimedia.setImageResource(R.mipmap.video_multimedia);
            holder.titulo.setText(listItem.getTitulo());
        }else if(listItem.getTitulo().equals("Animación")){
            holder.cardView.setBackgroundColor(ContextCompat.getColor(context,R.color.dot_light_screen1));
            holder.iconoMultimedia.setImageResource(R.mipmap.animacion_multimedia);
            holder.titulo.setText(listItem.getTitulo());
        }



    }

    /**
     * Devuelve el numero de items en el data set.
     *
     * @return Tnumero de items en el adapter.
     */
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView titulo;
        public CardView cardView;
        public ImageView iconoMultimedia;

        public ViewHolder(View itemView) {
            super(itemView);

            titulo = (TextView) itemView.findViewById(R.id.titulo_multimedia);
            cardView = (CardView) itemView.findViewById(R.id.cv);
            iconoMultimedia = (ImageView) itemView.findViewById(R.id.iconoMultimedia);
            cardView.setOnClickListener(this);


            cardView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {


                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {


                    ViewGroup.LayoutParams Lp =  v.getLayoutParams();


                    int imageRealHeight = right / 3;


                    Lp.height = imageRealHeight;
                    v.setLayoutParams(Lp);




                }

            });



        }

        /**
         * Llamado cuando un cardView es seleccionado,
         * Entra a galeria, o a reproducir el audio.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if(listItems.get(getAdapterPosition()).getTitulo().equals("Descripción")){
                Intent intent = new Intent(context, DescriptionViewer.class);
                intent.putExtra("id", Integer.parseInt(listItems.get(getAdapterPosition()).getId()));
                intent.putExtra("nombre", listItems.get(getAdapterPosition()).getNombre());
                context.startActivity(intent);
            }else if(listItems.get(getAdapterPosition()).getTitulo().equals("Audio")){
                Intent intent = new Intent(context, ReproductorAudio.class);
                intent.putExtra("id", Integer.parseInt(listItems.get(getAdapterPosition()).getId()));
                intent.putExtra("nombre", listItems.get(getAdapterPosition()).getNombre());
                context.startActivity(intent);
            }else if(listItems.get(getAdapterPosition()).getTitulo().equals("Video")) {
                Intent intent = new Intent(context, VideoPlayerController.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", Integer.parseInt(listItems.get(getAdapterPosition()).getId()));
                //intent.putExtra("nombre", listItems.get(getAdapterPosition()).getNombre());
                context.startActivity(intent);
            }
            else {
                Intent intent = new Intent(context, Gallery.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (listItems.get(getAdapterPosition()).getTitulo().equals("Imagen"))
                {
                    intent.putExtra("image", true);
                }
                else
                {
                    intent.putExtra("image", false);
                }
                intent.putExtra("id", Integer.parseInt(listItems.get(getAdapterPosition()).getId()));
                intent.putExtra("nombre", listItems.get(getAdapterPosition()).getNombre());
                context.startActivity(intent);
            }
        }
    }
}

