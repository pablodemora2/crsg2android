// author : Pablo Mora G.

package com.istudio.crsurfguide.gallery;

import android.app.Activity;

import com.istudio.list.ImageDescription;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ResourceExplorer {

    ArrayList<Integer> ListResources;
    com.istudio.crsurfguide.obj.Constante Constante;

    public ResourceExplorer() {

        Activity a = new Activity();
        this.Constante = com.istudio.crsurfguide.obj.Constante.getInstance(a);
    }

    public ArrayList<Integer> getListResources() {

        return ListResources;
    }

    public int[] getAllResourceIDs(Class<?> aClass)
            throws IllegalArgumentException {
        /* Get all Fields from the class passed. */
        Field[] IDFields = aClass.getFields();
        int[] filteredIds;
        ArrayList<Integer> listObjs = new ArrayList<Integer>();

        try {
            /* Loop through all Fields and store id to array. */
            for (int i = 0; i < IDFields.length; i++) {

                if (IDFields[i].getName().contains("im_")) {

                    listObjs.add(IDFields[i].getInt(null));

                    this.Constante.getListImages().add(new ImageDescription(i, IDFields[i]
                            .getName(), ""));

                }


            }

            filteredIds = new int[listObjs.size()];

            for (int i = 0; i < listObjs.size(); ++i) {
                filteredIds[i] = listObjs.get(i);
            }

        } catch (Exception e) {
            /* Exception will only occur on bad class submitted. */
            throw new IllegalArgumentException();
        }
        return filteredIds;
    }

}