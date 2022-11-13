package com.agri.tech;

import android.app.Activity;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.text.*;
import org.json.*;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.content.Intent;
import android.net.Uri;
import android.widget.AdapterView;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.DialogFragment;

public class DevicesActivity extends Activity {
	
	private ArrayList<HashMap<String, Object>> map = new ArrayList<>();
	
	private LinearLayout linear2;
	private LinearLayout linear3;
	private LinearLayout linear4;
	private TextView textview1;
	private ListView listview1;
	
	private BluetoothConnect bt;
	private BluetoothConnect.BluetoothConnectionListener _bt_bluetooth_connection_listener;
	private Intent in = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.devices);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear2 = findViewById(R.id.linear2);
		linear3 = findViewById(R.id.linear3);
		linear4 = findViewById(R.id.linear4);
		textview1 = findViewById(R.id.textview1);
		listview1 = findViewById(R.id.listview1);
		bt = new BluetoothConnect(this);
		
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				in.setClass(getApplicationContext(), ConActivity.class);
				in.putExtra("add", map.get((int)_position).get("address").toString());
				startActivity(in);
				finish();
			}
		});
		
		_bt_bluetooth_connection_listener = new BluetoothConnect.BluetoothConnectionListener() {
			@Override
			public void onConnected(String _param1, HashMap<String, Object> _param2) {
				final String _tag = _param1;
				final HashMap<String, Object> _deviceData = _param2;
				
			}
			
			@Override
			public void onDataReceived(String _param1, byte[] _param2, int _param3) {
				final String _tag = _param1;
				final String _data = new String(_param2, 0, _param3);
				
			}
			
			@Override
			public void onDataSent(String _param1, byte[] _param2) {
				final String _tag = _param1;
				final String _data = new String(_param2);
				
			}
			
			@Override
			public void onConnectionError(String _param1, String _param2, String _param3) {
				final String _tag = _param1;
				final String _connectionState = _param2;
				final String _errorMessage = _param3;
				
			}
			
			@Override
			public void onConnectionStopped(String _param1) {
				final String _tag = _param1;
				
			}
		};
	}
	
	private void initializeLogic() {
		bt.getPairedDevices(map);
		listview1.setAdapter(new Listview1Adapter(map));
		_roundcorner(0, 0, 15, 15, "#000000", linear3);
	}
	
	public void _view(final View _view, final double _width, final double _height) {
		_view.setLayoutParams(new LinearLayout.LayoutParams((int)_width, (int)_height));
	}
	
	
	public void _SetCornerRadius(final View _view, final double _radius, final double _shadow, final String _color) {
		android.graphics.drawable.GradientDrawable ab
		= new
		android.graphics.drawable.GradientDrawable();
		
		ab.setColor(Color.parseColor(_color));
		ab.setCornerRadius((float) _radius);
		_view.setElevation((float) _shadow);
		_view.setBackground(ab);
	}
	
	
	public void _roundcorner(final double _a, final double _b, final double _c, final double _d, final String _BGcolor, final View _view) {
		Double tlr = _a;
		Double trr = _b;
		Double blr = _c;
		Double brr = _d;
		android.graphics.drawable.GradientDrawable s = new android.graphics.drawable.GradientDrawable();
		s.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
		s.setCornerRadii(new float[] {tlr.floatValue(),tlr.floatValue(), trr.floatValue(),trr.floatValue(), blr.floatValue(),blr.floatValue(), brr.floatValue(),brr.floatValue()});
		s.setColor(Color.parseColor(_BGcolor));
		_view.setBackground(s);
	}
	
	public class Listview1Adapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.device, null);
			}
			
			final LinearLayout linear1 = _view.findViewById(R.id.linear1);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			final TextView textview1 = _view.findViewById(R.id.textview1);
			
			textview1.setText(_data.get((int)_position).get("name").toString());
			_SetCornerRadius(linear1, 30, 20, "#ffffff");
			
			return _view;
		}
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}