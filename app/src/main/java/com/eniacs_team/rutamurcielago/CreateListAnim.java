package com.eniacs_team.rutamurcielago;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Clase ayudante que engloba los datos que posee cada item dentro de la galeria de animaciones.
 *
 * @author EniacsTeam
 */
public class CreateListAnim {

    private String anim_title;
    private GifDrawable image_drawable;

    public String getAnim_title() {
        return anim_title;
    }

    public void setAnim_title(String android_version_name) {
        this.anim_title = android_version_name;
    }

    public GifDrawable getAnim_drawable() {
        return image_drawable;
    }

    public void setAnim_drawable(GifDrawable android_image) {
        this.image_drawable = android_image;
    }
}
