package com.halum.torch;


import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private Camera cam;
	private boolean isFlsahOn, isMaxBright;
	private ImageButton flashButton, brightnessButton, aboutButton, colorButton;
	private float screenWidth;
	private LinearLayout mainLayout;
	private int backgroundColor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.fullScreenOn();
		// keep screen on
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.layout_main_activity);
		this.initVariables();
	}
	
	private void setNewColor(int color){
		this.backgroundColor = color;
		this.mainLayout.setBackgroundColor(color);
	}
	
	private void changeScreenColor(){
		switch (this.backgroundColor) {
		case Color.WHITE:
			this.setNewColor(Color.CYAN);
			break;
		case Color.CYAN:
			this.setNewColor(Color.BLUE);
			break;
		case Color.BLUE:
			this.setNewColor(Color.YELLOW);
			break;
		case Color.YELLOW:
			this.setNewColor(Color.GREEN);
			break;
		case Color.GREEN:
			this.setNewColor(Color.MAGENTA);
			break;
		case Color.MAGENTA:
			this.setNewColor(Color.RED);
			break;
		case Color.RED:
			this.setNewColor(Color.WHITE);
			break;

		default:
			break;
		}
	}
	
	private void initVariables(){
		this.isFlsahOn = false;
		this.isMaxBright = false;
		this.screenWidth = this.getScreenWidth();
		this.backgroundColor = Color.WHITE;
		
		this.mainLayout = (LinearLayout) findViewById(R.id.mainLayout); 
		
		this.flashButton = (ImageButton) findViewById(R.id.buttonFlash);
		this.brightnessButton = (ImageButton) findViewById(R.id.buttonBrightness);
		//this.share = (ImageButton) findViewById(R.id.buttonShare);
		this.aboutButton = (ImageButton) findViewById(R.id.buttonAbout);
		this.colorButton = (ImageButton) findViewById(R.id.buttonColor);
		
		this.flashButton.setOnClickListener(this);
		this.brightnessButton.setOnClickListener(this);
		this.colorButton.setOnClickListener(this);
		this.aboutButton.setOnClickListener(this);
		//this.share.setOnClickListener(this);
		
		this.resizeButtonPadding(this.flashButton);
		this.resizeButtonPadding(this.aboutButton);
		this.resizeButtonPadding(this.colorButton);
		this.resizeButtonPadding(this.brightnessButton);
	}
	
	private void resizeButtonPadding(ImageButton btn) {
		LinearLayout.LayoutParams par = (LinearLayout.LayoutParams) btn.getLayoutParams();
		int margin = (int) ((this.screenWidth - 256) / 8);
		par.setMargins(margin, 0, margin, 0);
		btn.setLayoutParams(par);
	}
	
	private float getScreenWidth() {
		float screenDensity = getResources().getDisplayMetrics().density;
		DisplayMetrics mat = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mat);
		return mat.widthPixels / screenDensity;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void setBrightnessNormal() {
		// set maximum brightness
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.screenBrightness = lp.BRIGHTNESS_OVERRIDE_NONE;
		getWindow().setAttributes(lp);
		this.brightnessButton.setImageResource(R.drawable.ic_brightness_normal);
		this.isMaxBright = false;
	}
	
	private void setBrightnessMaxium() {
		// set maximum brightness
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.screenBrightness = lp.BRIGHTNESS_OVERRIDE_FULL;
		getWindow().setAttributes(lp);
		this.brightnessButton.setImageResource(R.drawable.ic_brightness_maximum);
		this.isMaxBright = true;
	}
	
	private void setFlashOff() {
		cam.release();
		this.flashButton.setImageResource(R.drawable.ic_flash_off);
		this.isFlsahOn = false;
	}
	
	private void setFlashOn() {
		// if phone has a flash then turn it on
		if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) == true){
			this.cam = Camera.open();     
			Parameters p = cam.getParameters();
			if(p.getSupportedFlashModes() != null){
				p.setFlashMode(Parameters.FLASH_MODE_TORCH);
				this.cam.setParameters(p);
				this.flashButton.setImageResource(R.drawable.ic_flash_on);
				this.isFlsahOn = true;
			}else{
				this.cam.release();
				this.alert("Flash not supported");
			}
		}else{ // if phone does not has a flash then show a toast
			this.alert("Flash not supported");
		}
	}
	
	private void fullScreenOn(){
		// hide title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// hide notification bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
		                WindowManager.LayoutParams.FLAG_FULLSCREEN);		
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.buttonFlash){
			if(this.isFlsahOn){
				this.setFlashOff();
			}else{
				this.setFlashOn();
			}
		}
		
		else if(v.getId() == R.id.buttonBrightness){
			if(this.isMaxBright == false){
				this.setBrightnessMaxium();
			}else{
				this.setBrightnessNormal();
			}
		}
		
		else if(v.getId() == R.id.buttonColor){
			this.changeScreenColor();
		}
		
		else if(v.getId() == R.id.buttonAbout){
			this.showAbout();
		}
		
	}
	
	private void showAbout(){
		AlertDialog.Builder aboutBuilder = new AlertDialog.Builder(this);
		
		LayoutInflater inflater = this.getLayoutInflater();
		
		aboutBuilder.setView(inflater.inflate(R.layout.layout_about_dialog, null));
		
		AlertDialog aboutDialog = aboutBuilder.create();
		
		aboutDialog.show();
	}
	
	/*static public void copyFileFromAssets(Context context, String file, String dest) throws Exception 
	{
	    InputStream in = null;
	    OutputStream fout = null;
	    int count = 0;

	    try
	    {
	        in = context.getAssets().open(file);
	        fout = new FileOutputStream(new File(dest));

	        byte data[] = new byte[1024];
	        while ((count = in.read(data, 0, 1024)) != -1)
	        {
	            fout.write(data, 0, count);
	        }
	    }
	    catch (Exception e) 
	    {
	        e.printStackTrace();
	    }   
	    finally
	    {
	        if (in != null)
	        {
	            try {
	                in.close();
	            } catch (IOException e) 
	            {
	            }
	        }
	        if (fout != null)
	        {
	            try {
	                fout.close();
	            } catch (IOException e) {
	            }
	        }
	    }
	}*/
	
	protected void onPause(){
		super.onPause();
		// if flash is on then turn it off
		if(this.isFlsahOn) this.setFlashOff();
		// if scrren is set to max bright then set it to normal
		if(this.isMaxBright) this.setBrightnessNormal();
	}
	
	// a simplified method to show toast alerts
	private void alert(String msg){
		Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
	}
	
}
