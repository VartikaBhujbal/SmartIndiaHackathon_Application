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
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import com.bumptech.glide.Glide;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.DialogFragment;

public class MainActivity extends Activity {
	
	private Timer _timer = new Timer();
	
	private String url = "";
	private String typeace = "";
	private String fontError = "";
	
	private LinearLayout linear1;
	private LinearLayout linear2;
	private LinearLayout linear3;
	private ImageView imageview1;
	private ProgressBar progressbar1;
	private TextView textview2;
	private Button button1;
	private TextView textview1;
	
	private BluetoothConnect bt;
	private BluetoothConnect.BluetoothConnectionListener _bt_bluetooth_connection_listener;
	private TimerTask t;
	private Intent in = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		linear2 = findViewById(R.id.linear2);
		linear3 = findViewById(R.id.linear3);
		imageview1 = findViewById(R.id.imageview1);
		progressbar1 = findViewById(R.id.progressbar1);
		textview2 = findViewById(R.id.textview2);
		button1 = findViewById(R.id.button1);
		textview1 = findViewById(R.id.textview1);
		bt = new BluetoothConnect(this);
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				textview2.setVisibility(View.GONE);
				button1.setVisibility(View.GONE);
				progressbar1.setVisibility(View.VISIBLE);
				t = new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (bt.isBluetoothActivated()) {
									in.setClass(getApplicationContext(), DevicesActivity.class);
									startActivity(in);
									finishAffinity();
								}
								else {
									progressbar1.setVisibility(View.GONE);
									textview2.setVisibility(View.VISIBLE);
									button1.setVisibility(View.VISIBLE);
								}
							}
						});
					}
				};
				_timer.schedule(t, (int)(1500));
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
		_view(linear1, SketchwareUtil.getDisplayWidthPixels(getApplicationContext()), SketchwareUtil.getDisplayHeightPixels(getApplicationContext()));
		url = "android.resource://" + getPackageName() + "/raw/iskit_gif_1";
		Glide.with(getApplicationContext()).load(Uri.parse(url)).into(imageview1);
		_progressBarColor(progressbar1, "#ffffff");
		textview2.setVisibility(View.GONE);
		button1.setVisibility(View.GONE);
		_SetCornerRadius(button1, 40, 25, "#ffffff");
		t = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (bt.isBluetoothActivated()) {
							in.setClass(getApplicationContext(), DevicesActivity.class);
							startActivity(in);
							finishAffinity();
						}
						else {
							textview2.setVisibility(View.VISIBLE);
							button1.setVisibility(View.VISIBLE);
							progressbar1.setVisibility(View.GONE);
						}
					}
				});
			}
		};
		_timer.schedule(t, (int)(1500));
	}
	
	public void _view(final View _view, final double _width, final double _height) {
		_view.setLayoutParams(new LinearLayout.LayoutParams((int)_width, (int)_height));
	}
	
	
	public void _progressBarColor(final ProgressBar _progressBar, final String _color) {
		if
		(android.os.Build.VERSION.SDK_INT >= 21) {
			
			_progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor(_color),
			PorterDuff.Mode.SRC_IN);
			
		}
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