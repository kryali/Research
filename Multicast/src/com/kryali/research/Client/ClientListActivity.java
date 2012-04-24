package com.kryali.research.Client;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.kryali.research.Phone;
import com.kryali.research.R;
import com.kryali.research.Network.TransferManager;
import com.kryali.research.Network.URLS;

public class ClientListActivity extends Activity {
	private ClientMain clientMainThread = null;
	private ClientTracker clientTrackerThread = null;
	List<Phone> phones;
	JSONArray phoneListData;
	Activity activity;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		setContentView(R.layout.client_list);
		phones = new LinkedList<Phone>();
		GetHostsTask task = new GetHostsTask();
		task.execute();
	}

	public void onDestroy() {
		super.onDestroy();
	}

	private void startActivity(int position) {
		JSONObject phone;
		try {
			phone = (JSONObject) phoneListData.getJSONObject(position);
			Bundle extras = new Bundle();
			extras.putString("phone", phone.toString());
			Intent i = new Intent(getBaseContext(), ClientActivity.class);
			i.putExtras(extras);
			activity.startActivity(i);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private OnItemClickListener listener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			startActivity(position);
		}
	};

	private void initList() {
		ListView list = (ListView) findViewById(R.id.list);
		ArrayAdapter<Phone> adapter = new ArrayAdapter<Phone>(this,
				android.R.layout.simple_list_item_1, phones);
		list.setOnItemClickListener(listener);
		list.setAdapter(adapter);
	}

	private final Handler viewHandler = new Handler() {

	};

	private class GetHostsTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			return TransferManager.GET(URLS.getAllHostsURL());
		}

		@Override
		protected void onPostExecute(String response) {
			try {
				JSONObject responseJson = new JSONObject(response);
				phoneListData = responseJson.getJSONArray("hosts");
				for (int i = 0; i < phoneListData.length(); i++) {
					JSONObject host = phoneListData.getJSONObject(i);
					Phone p = new Phone(host);
					phones.add(p);
				}
				initList();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
