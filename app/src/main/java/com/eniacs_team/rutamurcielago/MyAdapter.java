package com.eniacs_team.rutamurcielago;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import java.util.ArrayList;

/**
 * Clase ayudante para crear la galeria compuesta de una matriz donde cada celda tiene una imagen y una descripcion.
 *
 * @author EniacsTeam
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<CreateList> galleryList;
    private Context context;
    private int idPunto;

    public MyAdapter(Context context, ArrayList<CreateList> galleryList, int idPunto) {
        this.galleryList = galleryList;
        this.context = context;
        this.idPunto = idPunto;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    /*
     * Metodo responsable de cargar cada celda con sus datos correspondientes y de manejar cliqueos a ellas.
     */
    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder viewHolder, int i) {

        StringBuilder sb = new StringBuilder(galleryList.get(i).getImage_title());
        if (sb.length() > 21)
        {
            sb = new StringBuilder(sb.substring(0, 20));
            sb.append("...");
        }

        viewHolder.title.setText(sb);
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.img.setImageDrawable((galleryList.get(i).getImage_drawable()));
        viewHolder.img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), FullscreenImage.class);
                intent.putExtra("idPunto", idPunto);
                intent.putExtra("pos", viewHolder.getAdapterPosition());
                intent.putExtra("text", galleryList.get(viewHolder.getAdapterPosition()).getImage_title());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    /**
     * Define la composicion de cada celda (una imagen y una descripcion).
     *
     * @author EniacsTeam
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView img;
        public ViewHolder(View view) {
            super(view);

            title = (TextView)view.findViewById(R.id.title);
            img = (ImageView) view.findViewById(R.id.img);
        }
    }
}
