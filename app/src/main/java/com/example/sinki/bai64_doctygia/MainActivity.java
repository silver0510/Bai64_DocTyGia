package com.example.sinki.bai64_doctygia;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.sinki.adapter.TiGiaAdapter;
import com.example.sinki.model.TiGia;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvTiGia;
    ArrayList<TiGia>dsTiGia;
    TiGiaAdapter tiGiaAdapter;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
    }

    private void addControls() {
        lvTiGia = (ListView) findViewById(R.id.lvTiGia);
        dsTiGia = new ArrayList<>();
        tiGiaAdapter = new TiGiaAdapter(MainActivity.this,R.layout.item,dsTiGia);
        lvTiGia.setAdapter(tiGiaAdapter);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Thông báo");
        progressDialog.setMessage("Đang tải dữ liệu...");
        progressDialog.setCanceledOnTouchOutside(false);
        TiGiaTask task = new TiGiaTask();
        task.execute();
    }

    private class TiGiaTask extends AsyncTask<Void,Void,ArrayList<TiGia>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            tiGiaAdapter.clear();
        }

        @Override
        protected void onPostExecute(ArrayList<TiGia> tiGias) {
            super.onPostExecute(tiGias);
            tiGiaAdapter.clear();
            tiGiaAdapter.addAll(tiGias);
            progressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<TiGia> doInBackground(Void... params) {
            ArrayList<TiGia>ds = new ArrayList<>();
            try
            {
                URL url=new URL("http://dongabank.com.vn/exchange/export");
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-type", "application/json; charset=utf-8");
                connection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
                connection.setRequestProperty("Accept", "*/*");

                //lấy dữ liệu mà server trả về
                InputStream is= connection.getInputStream();
                InputStreamReader isr=new InputStreamReader(is,"UTF-8");
                BufferedReader br=new BufferedReader(isr);
                String line=br.readLine();
                StringBuilder builder=new StringBuilder();
                while (line!=null)
                {
                    builder.append(line);
                    line=br.readLine();
                }
                String json=builder.toString();
                json=json.replace("(", "");
                json=json.replace(")", "");
                JSONObject jsonObject=new JSONObject(json);
                JSONArray jsonArray= jsonObject.getJSONArray("items");
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject item=jsonArray.getJSONObject(i);
                    TiGia tiGia=new TiGia();
                    if(item.has("type"))
                        tiGia.setType(item.getString("type"));
                    if(item.has("imageurl")) {
                        tiGia.setImageurl(item.getString("imageurl"));
                        url=new URL(tiGia.getImageurl());
                        connection= (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
                        connection.setRequestProperty("Accept", "*/*");
                        Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                        tiGia.setBitmap(bitmap);
                    }
                    if(item.has("muatienmat")) {
                        tiGia.setMuatienmat(item.getString("muatienmat"));
                    }
                    if(item.has("muack")) {
                        tiGia.setMuack(item.getString("muack"));
                    }
                    if(item.has("bantienmat")) {
                        tiGia.setBantienmat(item.getString("bantienmat"));
                    }
                    if(item.has("banck")) {
                        tiGia.setBanck(item.getString("banck"));
                    }
                    ds.add(tiGia);
                }
                Log.d("JSON_DONGA",json);
            }
            catch (Exception ex)
            {
                Log.e("LOI",ex.toString());
            }
            return ds;
        }
    }
}
