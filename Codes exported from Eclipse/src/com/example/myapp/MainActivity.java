package com.example.myapp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

@SuppressLint({ "ShowToast", "DefaultLocale" })
public class MainActivity extends Activity {
	public static ListView lv;
	CellAdapter cellAdapter;
	public static DBHelper dbhelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		lv = (ListView)findViewById(R.id.list_view);

		System.out.println("onCreated called");
		try {
			readTwitter("oscar");		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		dbhelper = new DBHelper(this);
		dbhelper.refreshDB();
		for (Item itr : items) {
			dbhelper.saveItem(itr);
		}
		
		items = (ArrayList<Item>) dbhelper.getAllItems();

		cellAdapter = new CellAdapter(this, items);
		lv.setAdapter(cellAdapter);
	}

	//	https://api.twitter.com/1/users/show.json?user_id=
	//	https://api.twitter.com/1/users/show.json?screen_name=
	public static String[] texts;
	public static String[] imgUrls;
	public static String[] hds;
	public static String[] ltns;
	public static ArrayList<Item> items;
	public void readTwitter(String kw) throws IOException, JSONException {
		URL address = new URL("http://search.twitter.com/search.json?q="+kw+"&rpp=4&include_entities=true&result_type=recent");
		BufferedReader in = new BufferedReader( new InputStreamReader( address.openStream() ) );
		String line = in.readLine();
		in.close();
		JSONObject response = new JSONObject(line);
		JSONArray results = response.getJSONArray("results");

		texts = new String[results.length()];
		imgUrls = new String[results.length()];
		hds = new String[results.length()];
		ltns = new String[results.length()];
		items = new ArrayList<Item>();
		for (int i=0; i<results.length(); i++) {
			JSONObject c = results.getJSONObject(i);
			hds[i] = c.getString("from_user_name");
			String id = c.getString("from_user_id_str");
			imgUrls[i] = c.getString("profile_image_url");
			texts[i] = c.getString("text");

			try {
				URL subaddress = new URL("https://api.twitter.com/1/users/show.json?user_id="+id);
				BufferedReader subin = new BufferedReader( new InputStreamReader( subaddress.openStream() ) );

				String subline = subin.readLine();
				if (subline == null) continue;
				subin.close();

				JSONObject user = new JSONObject(subline);
				ltns[i] = user.getString("location");
				items.add(new Item(texts[i], hds[i], imgUrls[i], ltns[i]));
			} catch (FileNotFoundException e){
				System.out.println(i + " exception");
				System.out.println(e.getMessage());
			}
		}
		steps = new Vector< ArrayList<Item> >();
		steps.add(items);
		round = 0;
	}
	
	public static Vector< ArrayList<Item> > steps;
	
	// deep copy
	public void cpItems() {
		ArrayList<Item> newItems = new ArrayList<Item>();
		for (Item itr: items) {
			newItems.add(new Item(itr));
		}
		steps.add( newItems );
	}

	public void sendMessage(View view) throws IOException, JSONException {

		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString().toLowerCase();
		if (round == steps.size()) {
			round++;
		} else {
			round = steps.size();
		}
		
		for (int i=0; i<items.size(); i++) {
			if (items.get(i).getLtn().toLowerCase().compareTo(message)!=0) {
				dbhelper.deletItem( items.get(i).getHd() );
			}
		}
		
		items = (ArrayList<Item>) dbhelper.getAllItems();
		cpItems();

		cellAdapter = new CellAdapter(this, items);
		lv.setAdapter(cellAdapter);
//		cellAdapter.notifyDataSetChanged();
	}

	public static int round;
	
	public void backPerform(View view) throws IOException {
		int origin_size = items.size();
		if (round != 0) {
			round--;
			items = steps.get(round);
		}
		cellAdapter = new CellAdapter(this, items);
		lv.setAdapter(cellAdapter);
//		cellAdapter.notifyDataSetChanged();
	}

	public void nextPerform(View view) throws IOException {
		int origin_size = items.size();
		if (round != steps.size()-1) {
			round++;
			items = steps.get(round);
		}
		cellAdapter = new CellAdapter(this, items);
		lv.setAdapter(cellAdapter);
//		cellAdapter.notifyDataSetChanged();
	}
}