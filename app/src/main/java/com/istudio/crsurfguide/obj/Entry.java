package com.istudio.crsurfguide.obj;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pablomorag on 30/3/15.
 * <p/>
 * Esta clase abstrae a mayor nivel de abstaccion la entidad playa, por ejemplo parque, etc.
 * Usa solo enteros que son referencias en memoria.
 */

// Ultima edición: Pablo Mora González
// Fecha: Lunes 18 de Abril 2016.

public class Entry {

    // Place

    private int Name;
    private List<LatLng> BoundsCoordinates;
    private float Zoom;
    private Boolean HiddenSpot;

    // Entry

    private int idImagen;
    private int textoDebajo;

    public Entry() {
    }

    public Entry(int pName, int pDescrip) {

        this.Name = pName;
        this.textoDebajo = pDescrip;
        this.setBoundsCoordinates(null);
        this.idImagen = -1;
        this.HiddenSpot = false;

    }

    public Entry(int pName, int pDescrip, int pImage) {

        this.Name = pName;
        this.textoDebajo = pDescrip;
        this.idImagen = pImage;
        this.setBoundsCoordinates(null);
        this.HiddenSpot = false;

    }

    public Entry(int pName, ArrayList<LatLng> pCoord) {

        this.Name = pName;
        this.setBoundsCoordinates(pCoord);
        this.idImagen = -1;
        this.textoDebajo = -1;
        this.HiddenSpot = false;

    }

    public Entry(int pName, int pDescrip, int pImage, ArrayList<LatLng> pCoord) {


        this.Name = pName;
        this.textoDebajo = pDescrip;
        this.idImagen = pImage;
        this.setBoundsCoordinates(pCoord);
        this.HiddenSpot = false;

    }

    public Entry(int pName, int pDescrip, ArrayList<LatLng> latLngs) {

        this.Name = pName;
        this.textoDebajo = pDescrip;
        this.idImagen = -1;
        this.setBoundsCoordinates(latLngs);
        this.HiddenSpot = false;


    }

    public Entry(int pName, int pDescrip, int pImage, ArrayList<LatLng> latLngs, boolean
            pHiddenSpot) {

        this.Name = pName;
        this.textoDebajo = pDescrip;
        this.idImagen = pImage;
        this.setBoundsCoordinates(latLngs);
        this.HiddenSpot = pHiddenSpot;


    }

    public Entry(int pName, final LatLng pCoordinates, float pZoom) {

        this.Name = pName;
        this.BoundsCoordinates = new ArrayList<LatLng>() {{
            add(pCoordinates);
        }};
        this.setHiddenSpot(false);
        this.idImagen = -1;
        this.textoDebajo = -1;
        this.HiddenSpot = false;

    }

    public int getName() {
        return Name;
    }

    public void setName(int name) {
        Name = name;
    }

    public float getZoom() {
        return Zoom;
    }

    public List<LatLng> getBoundsCoordinates() {
        return BoundsCoordinates;
    }

    public void setBoundsCoordinates(List<LatLng> boundsCoordinates) {
        BoundsCoordinates = boundsCoordinates;
    }

    public Boolean getHiddenSpot() {
        return HiddenSpot;
    }

    public void setHiddenSpot(Boolean hiddenSpot) {
        HiddenSpot = hiddenSpot;
    }

    public int get_textoDebajo() {
        return textoDebajo;
    }

    public int get_idImagen() {
        return idImagen;
    }

}
