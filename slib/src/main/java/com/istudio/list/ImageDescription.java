package com.istudio.list;

/**
 * @author Edgar Ríos
 *         <p/>
 *         Esta clase se usa por las listas implementadas en los modulos.
 */

public class ImageDescription {

    int IdImage;
    String ImageName;
    String Description;

    public ImageDescription() {
    }

    public ImageDescription(int pIdImage, String pImageName, String pMessage) {

        this.IdImage = pIdImage;
        this.ImageName = pImageName;
        this.Description = pMessage;

    }

    public ImageDescription(String pImageName, String pMessage) {

        this.ImageName = pImageName;
        this.Description = pMessage;

    }

    public int getIdImage() {
        return IdImage;
    }

    public void setIdImage(int idImage) {
        IdImage = idImage;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

}
