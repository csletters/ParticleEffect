package com.example.particleeffect;


import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	private GLSurfaceView mGLView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
	}


}
