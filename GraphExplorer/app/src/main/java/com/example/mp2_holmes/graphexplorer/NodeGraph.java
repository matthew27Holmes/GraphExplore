package com.example.mp2_holmes.graphexplorer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.example.mp2_holmes.graphexplorer.NodeGraphView.customView;


import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;

public class NodeGraph extends AppCompatActivity {
    private customView m_customView;
    private TextView DiagramTitle;
    private String m_companyNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_graph);

        m_customView = (customView) findViewById(R.id.GraphView);

        String Title= getIntent().getStringExtra("title");
        DiagramTitle = (TextView) findViewById(R.id.DiagarmTitle);
        DiagramTitle.setText(Title);

        m_companyNum = getIntent().getStringExtra("companyNum");

        new employeesRequest(this,m_customView,m_companyNum).execute();
    }

    public void Home(View view)
    {
        startActivity(new Intent(NodeGraph.this, MainActivity.class));
    }

    public void ShareResults(View view) {
        Bitmap graphImage = getBitmapFromView(m_customView);

        try {
            File file = new File(NodeGraph.this.getExternalCacheDir(),"logicchip.png");
            FileOutputStream fOut = new FileOutputStream(file);
            graphImage.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.setType("image/*");
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            this.startActivity(Intent.createChooser(share, "Share Your Findings!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
}
