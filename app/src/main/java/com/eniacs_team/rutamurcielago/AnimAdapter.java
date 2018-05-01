package com.eniacs_team.rutamurcielago;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

/**
 * Clase ayudante para crear la galeria compuesta de una matriz donde cada celda tiene una animacion (gif).
 *
 * @author EniacsTeam
 */
public class AnimAdapter extends RecyclerView.Adapter<AnimAdapter.ViewHolder> {
    private ArrayList<CreateListAnim> galleryList;
    private Context context;
    private int idPunto;

    public AnimAdapter(Context context, ArrayList<CreateListAnim> galleryList, int idPunto) {
        this.galleryList = galleryList;
        this.context = context;
        this.idPunto = idPunto;
    }

    @Override
    public AnimAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.anim_cell_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    /*
     * Metodo responsable de cargar cada celda con sus datos correspondientes y de manejar cliqueos a ellas.
     */
    @Override
    public void onBindViewHolder(final AnimAdapter.ViewHolder viewHolder, int i) {

        StringBuilder sb = new StringBuilder(galleryList.get(i).getAnim_title());
        if (sb.length() > 21)
        {
            sb = new StringBuilder(sb.substring(0, 20));
            sb.append("...");
        }

        viewHolder.title.setText(sb);
        viewHolder.img.setScaleType(GifImageView.ScaleType.CENTER_CROP);
        viewHolder.img.setImageDrawable((galleryList.get(i).getAnim_drawable()));
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), FullscreenAnim.class);
                intent.putExtra("idPunto", idPunto);
                intent.putExtra("pos", viewHolder.getAdapterPosition());
                intent.putExtra("text", galleryList.get(viewHolder.getAdapterPosition()).getAnim_title());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return galleryList.size(); }

    /**
     * Define la composicion de cada celda (una imagen y una descripcion).
     *
     * @author EniacsTeam
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private GifImageView img;
        public ViewHolder(View view) {
            super(view);

            title = (TextView)view.findViewById(R.id.title);
            img = (GifImageView) view.findViewById(R.id.img);
        }
    }
}
