package com.example.sinki.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sinki.bai64_doctygia.R;
import com.example.sinki.model.TiGia;

import java.util.List;

/**
 * Created by Sinki on 9/10/2017.
 */

public class TiGiaAdapter extends ArrayAdapter<TiGia> {
    Activity context;
    int resource;
    List<TiGia> objects;

    public TiGiaAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<TiGia> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View item = inflater.inflate(this.resource,null);

        TiGia tiGia = this.objects.get(position);
        ImageView imgHinh = (ImageView)item.findViewById(R.id.imgHinh);
        TextView txtType = (TextView)item.findViewById(R.id.txtType);
        EditText txtMuaTM = (EditText)item.findViewById(R.id.txtMuaTM);
        EditText txtBanTM = (EditText)item.findViewById(R.id.txtBanTM);
        EditText txtMuaCK = (EditText)item.findViewById(R.id.txtMuaCK);
        EditText txtBanCk = (EditText)item.findViewById(R.id.txtBanCK);

        imgHinh.setImageBitmap(tiGia.getBitmap());
        txtType.setText(tiGia.getType().toString());
        txtMuaCK.setText(tiGia.getMuack().toString());
        txtBanCk.setText(tiGia.getBanck().toString());
        txtBanTM.setText(tiGia.getBantienmat().toString());
        txtMuaTM.setText(tiGia.getMuatienmat().toString());

        return item;
    }
}
