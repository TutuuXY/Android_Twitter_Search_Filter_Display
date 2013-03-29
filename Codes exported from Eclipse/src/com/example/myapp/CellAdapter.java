package com.example.myapp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CellAdapter extends BaseAdapter {

	private LayoutInflater inflator;
	private static ArrayList<Item> items;

	CellAdapter(Context context, ArrayList<Item> items) {
		inflator = LayoutInflater.from(context);
		CellAdapter.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return items.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View cv, ViewGroup parent) {
		ViewHolder holder;
		if (cv == null) {
			cv = inflator.inflate(R.layout.cell, null);
			holder = new ViewHolder();
			holder.txt_title = (TextView) cv.findViewById(R.id.title);
			holder.txt_detail = (TextView) cv.findViewById(R.id.detail);
			holder.img = (ImageView) cv.findViewById(R.id.img);

			cv.setTag(holder);
		} else {
			holder = (ViewHolder) cv.getTag();
		}
		String title;
		if (items.get(pos).getLtn().compareTo("")==0) {
			title = items.get(pos).getHd();
		} else {
			title = items.get(pos).getLtn()+", "+items.get(pos).getHd();
		}
		holder.txt_title.setText(title);
		holder.txt_detail.setText(items.get(pos).getText());
		Bitmap bmp = loadBitmap(items.get(pos).getImgUrl());
	    holder.img.setImageBitmap(bmp);
		return cv;
	}
	
	// source http://stackoverflow.com/questions/5296704/grid-view-image-source-issue
	public static Bitmap loadBitmap(String url) 
	{
	    Bitmap bitmap = null;
	    InputStream in = null;
	    BufferedOutputStream out = null;

	    try {
	        in = new BufferedInputStream(new URL(url).openStream(),  4 * 1024);

	        final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
	        out = new BufferedOutputStream(dataStream,  4 * 1024);

	        int byte_;
	        while ((byte_ = in.read()) != -1)
	            out.write(byte_);
	        out.flush();

	        final byte[] data = dataStream.toByteArray();
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        //options.inSampleSize = 1;

	        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
	    } catch (IOException e) {
	        Log.e("","Could not load Bitmap from: " + url);
	    } finally {
	        try{
	            in.close();
	            out.close();
	        }catch( IOException e )
	        {
	            System.out.println(e);
	        }
	    }
	    return bitmap;
	}

	static class ViewHolder {
		TextView txt_title;
		TextView txt_detail;
		ImageView img;
	}
}