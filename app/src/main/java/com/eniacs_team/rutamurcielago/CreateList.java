package com.eniacs_team.rutamurcielago;

import android.graphics.drawable.Drawable;

/**
 * Clase ayudante que engloba los datos que posee cada item dentro de la galeria.
 *
 * @author EniacsTeam
 */
public class CreateList {

    private String image_title;
    private Drawable image_drawable;

    public String getImage_title() {
        return image_title;
    }

    public void setImage_title(String android_version_name) {
        this.image_title = android_version_name;
    }

    public Drawable getImage_drawable() {
        return image_drawable;
    }

    public void setImage_drawable(Drawable android_image) {
        this.image_drawable = android_image;
    }
}
