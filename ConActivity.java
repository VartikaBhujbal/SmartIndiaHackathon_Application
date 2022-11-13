package com.agri.tech;

import android.app.Activity;
import android.os.*;
import android.provider.Settings;
import android.widget.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.util.*;
import java.util.*;
import java.text.*;
import org.json.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

import com.agri.tech.databinding.ActivityConBinding;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.Locale;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;


public class ConActivity<lflag> extends Activity {

	private ActivityConBinding binding;
	EditText etCity;
	TextView tvResult;
	private final String url = "https://api.openweathermap.org/data/2.5/weather";
	private final String appid = "e53301e27efa0b66d05045d91b2742d3";
	DecimalFormat df = new DecimalFormat("#.##");
	
	private String typeace = "";
	private String fontError = "";
	private double bulb = 0;
	private double fan = 0;
	private double condenser = 0;
	private double c=0;

	private LinearLayout linear1;
	private LinearLayout linear4;
	private LinearLayout linear3;
	private TextView textview2;
	private LinearLayout linear5;
	private LinearLayout linear6;
	private LinearLayout linear7;
	private TextView textview3;
	private TextView textview4;
	private TextView textview5;
	double temp=0;
	int humidity=0;
	boolean hflag= Boolean.parseBoolean(null);
	boolean tflag= Boolean.parseBoolean(null);
	boolean lflag= false;
	String lang="en";
	Context context;
	Resources resources;
	private BluetoothConnect bt;
	private BluetoothConnect.BluetoothConnectionListener _bt_bluetooth_connection_listener;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.activity_con);
		binding= ActivityConBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		initialize(_savedInstanceState);
		initializeLogic();
		binding.connect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ConActivity.this,DevicesActivity.class);
				startActivity(intent);
			}
		});
		binding.btnGet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String city = etCity.getText().toString().trim();
				getWeatherDetails(city);
			}
		});
		loadLocale();
		// setting up on click listener event over the button
		// in order to change the language with the help of
		// LocaleHelper class
		binding.translate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(lang == "en")
				{setLocale("hi");
					lang="hi";
					recreate();
					setflag(!lflag);
				}
				else {
					lang="en";
					setLocale("en");
					recreate();
					//SharedPreferences preferences=getSharedPreferences("Settings",MODE_PRIVATE);
					//String city=preferences.getString("city","");
					setflag(!lflag);
				}
			}
		});

	}
	private void setLocale(String s) {

		Locale myLocale = new Locale(s);
		Locale.setDefault(myLocale);
		Configuration configuration=new Configuration();
		configuration.locale=myLocale;
		getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
		SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
		editor.putString("app_lang",s);
		editor.apply();
	}
	private void loadLocale(){
		SharedPreferences preferences=getSharedPreferences("Settings",MODE_PRIVATE);
		String language=preferences.getString("app_lang","");
		setLocale(language);
	}

	private void initialize(Bundle _savedInstanceState) {
		boolean lflag=getflag();
		etCity = findViewById(R.id.etCity);
		//etCountry = findViewById(R.id.etCountry);
		tvResult = findViewById(R.id.tvResult);
		linear1 = findViewById(R.id.linear1);
		linear4 = findViewById(R.id.linear4);
		linear3 = findViewById(R.id.linear3);
		textview2 = findViewById(R.id.textview2);
		linear5 = findViewById(R.id.linear5);
		linear6 = findViewById(R.id.linear6);
		linear7 = findViewById(R.id.linear7);
		textview3 = findViewById(R.id.textview3);
		textview4 = findViewById(R.id.textview4);
		textview5 = findViewById(R.id.textview5);
		bt = new BluetoothConnect(this);
		linear5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				bulb++;
				if (bulb == 1 && temp<25.00 || humidity>70 ) {
					hflag=true;
					tflag=false;
					if(lflag)
					{textview3.setText("बल्ब ऑन");}
					else{textview3.setText("BULB ON");}
					linear5.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)50, (int)2, 0xFFFFF59D, 0xFFFFF59D));
					textview3.setTextColor(0xFF000000);
					bt.sendData(_bt_bluetooth_connection_listener, "1", "A");
				}
				else if(temp>30.00 || humidity<65){
					tflag=true;
					hflag=false;
					bulb = 0;
					if(lflag)
					{textview3.setText("बल्ब बंद");}
					else{
					textview3.setText("BULB OFF");}
					linear5.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)50, (int)2, 0xFF000000, 0xFFFFFFFF));
					textview3.setTextColor(0xFF000000);
					bt.sendData(_bt_bluetooth_connection_listener, "2", "A");
				}
				else if(temp>30.00 && humidity>70){
					tflag=true;
					hflag=true;
				}
			}
		});
		
		linear6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {

				fan++;
				if (fan == 1) {
					if(lflag)
					{textview3.setText("पंखा बंद");}
					else{
						textview4.setText("Fan On");
					}
					linear6.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)50, (int)2, 0xFF64B5F6, 0xFF64B5F6));
					textview4.setTextColor(0xFFFFFFFF);
					bt.sendData(_bt_bluetooth_connection_listener, "3", "A");
				}
				else {

					fan = 0;
					if(lflag)
					{textview3.setText("पंखा बंद");}
					else{
					textview4.setText("Fan Off");}
					linear6.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)50, (int)2, 0xFF000000, 0xFFFFFFFF));
					textview4.setTextColor(0xFF000000);
					bt.sendData(_bt_bluetooth_connection_listener, "4", "A");
				}
			}
		});
		
		linear7.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				condenser++;
				if (condenser == 1 && hflag && tflag) {
					if(lflag)
					{textview3.setText("कंडेनसर बंद");}
					else{
					textview5.setText("Condenser On");}
					linear7.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)50, (int)2, 0xFFAED581, 0xFFAED581));
					textview5.setTextColor(0xFFFFFFFF);
					bt.sendData(_bt_bluetooth_connection_listener, "5", "A");
				}
				else {

					condenser = 0;
					if(lflag)
					{textview3.setText("कंडेनसर बंद");}
					else{
					textview5.setText("Condenser Off");}
					linear7.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)50, (int)2, 0xFF000000, 0xFFFFFFFF));
					textview5.setTextColor(0xFF000000);
					bt.sendData(_bt_bluetooth_connection_listener, "6", "A");
				}
			}
		});
		
		_bt_bluetooth_connection_listener = new BluetoothConnect.BluetoothConnectionListener() {
			@Override
			public void onConnected(String _param1, HashMap<String, Object> _param2) {
				final String _tag = _param1;
				final HashMap<String, Object> _deviceData = _param2;
				SketchwareUtil.showMessage(getApplicationContext(), "Connected Successfully");
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
				SketchwareUtil.showMessage(getApplicationContext(), _errorMessage);
			}
			
			@Override
			public void onConnectionStopped(String _param1) {
				final String _tag = _param1;
				
			}
		};
	}

	private boolean getflag() {
		return lflag;
	}
	private void setflag(boolean f){
		lflag=f;
	}

	private void initializeLogic() {
		android.bluetooth.BluetoothAdapter adapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
		adapter.enable();
		bt.startConnection(_bt_bluetooth_connection_listener, getIntent().getStringExtra("add"), "A");
		bt.readyConnection(_bt_bluetooth_connection_listener, "A");
		_view(linear1, SketchwareUtil.getDisplayWidthPixels(getApplicationContext()), SketchwareUtil.getDisplayHeightPixels(getApplicationContext()));
		linear5.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)50, (int)2, 0xFF000000, 0xFFFFFFFF));
		linear6.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)50, (int)2, 0xFF000000, 0xFFFFFFFF));
		linear7.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)50, (int)2, 0xFF000000, 0xFFFFFFFF));
		_roundcorner(0, 0, 30, 30, "#000000", linear4);
		bulb = 0;
		fan = 0;
		condenser = 0;
		lflag=false;
		lang="en";
	}
	
	public void _view(final View _view, final double _width, final double _height) {
		_view.setLayoutParams(new LinearLayout.LayoutParams((int)_width, (int)_height));
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


	public void getWeatherDetails(String city) {
		String tempUrl = "";

		String country = "india";
		if(city.equals("")){
			tvResult.setText("City field can not be empty!");
		}else{
			if(!country.equals("")){
				tempUrl = url + "?q=" + city + "," + country + "&appid=" + appid;
			}else{
				tempUrl = url + "?q=" + city + "&appid=" + appid;
			}
			StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					String output = "";
					try {
						JSONObject jsonResponse = new JSONObject(response);
						JSONArray jsonArray = jsonResponse.getJSONArray("weather");
						JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
						String description = jsonObjectWeather.getString("description");
						JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
						temp = jsonObjectMain.getDouble("temp") - 273.15;
						humidity = jsonObjectMain.getInt("humidity");
						JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
						String countryName = jsonObjectSys.getString("country");
						String cityName = jsonResponse.getString("name");
						tvResult.setTextColor(Color.rgb(68,134,199));
						boolean lflag=getflag();
						lflag=!lflag;
						setflag(lflag);
						if(!lflag)
						{output += "Current weather of " + cityName + " (" + countryName + ")"
								+ "\n Temp: " + df.format(temp) + " °C"
								+ "\n Humidity: " + humidity + "%";}
						else
						{output += "वर्तमान मौसम: " + cityName + " (" + countryName + ")"
								+ "\n तापमान: " + df.format(temp) + " °C"
								+ "\n नमी: " + humidity + "%";}
						tvResult.setText(output);
						changecolor();

					} catch (JSONException e) {
						e.printStackTrace();

					}
				}
			}, new Response.ErrorListener(){

				@Override
				public void onErrorResponse(VolleyError error) {
					Toast.makeText(getApplicationContext(), "Please Enter Correct City", Toast.LENGTH_SHORT).show();
				}
			});
			RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
			requestQueue.add(stringRequest);
		}
	}

	private void changecolor() {
		boolean lflag=getflag();
		if (bulb == 1 && temp<=25.00 || humidity>=70 ) {
			hflag=true;
			tflag=false;
			if(lflag)
			{textview3.setText("बल्ब ऑन");}
			else{textview3.setText("BULB ON");}
			linear5.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)50, (int)2, 0xFFFFF59D, 0xFFFFF59D));
			textview3.setTextColor(0xFF000000);
			bt.sendData(_bt_bluetooth_connection_listener, "1", "A");
		}
		else if(temp>=30.00 || humidity<=65){
			tflag=true;
			hflag=false;
			bulb = 0;
			if(lflag)
			{textview3.setText("बल्ब बंद");}
			else{
				textview3.setText("BULB OFF");}
			linear5.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)50, (int)2, 0xFF000000, 0xFFFFFFFF));
			textview3.setTextColor(0xFF000000);
			bt.sendData(_bt_bluetooth_connection_listener, "2", "A");
		}
		else if(temp>=30.00 && humidity>=70){
			tflag=true;
			hflag=true;
		}
		fan++;
		if (fan == 1) {
			if(lflag)
			{textview3.setText("पंखा बंद");}
			else{
				textview4.setText("Fan On");
			}
			linear6.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)50, (int)2, 0xFF64B5F6, 0xFF64B5F6));
			textview4.setTextColor(0xFFFFFFFF);
			bt.sendData(_bt_bluetooth_connection_listener, "3", "A");
		}
		else {

			fan = 0;
			if(lflag)
			{textview3.setText("पंखा बंद");}
			else{
				textview4.setText("Fan Off");}
			linear6.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)50, (int)2, 0xFF000000, 0xFFFFFFFF));
			textview4.setTextColor(0xFF000000);
			bt.sendData(_bt_bluetooth_connection_listener, "4", "A");
		}
		condenser++;
		if (condenser == 1 && hflag && tflag) {
			if(lflag)
			{textview3.setText("कंडेनसर बंद");}
			else{
				textview5.setText("Condenser On");}
		linear7.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)50, (int)2, 0xFFAED581, 0xFFAED581));
		textview5.setTextColor(0xFFFFFFFF);
		bt.sendData(_bt_bluetooth_connection_listener, "5", "A");
		}
		else {

		condenser = 0;
			if(lflag)
			{textview5.setText("कंडेनसर बंद");}
			else{
				textview5.setText("Condenser Off");}
		linear7.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)50, (int)2, 0xFF000000, 0xFFFFFFFF));
		textview5.setTextColor(0xFF000000);
		bt.sendData(_bt_bluetooth_connection_listener, "6", "A");
		}

	}


}
