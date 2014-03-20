package com.example.particleeffect;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import particles.Particle;


import loaders.RawResourceReader;
import loaders.ShaderHandles;
import loaders.ShaderHelper;
import loaders.TextureHelper;

import android.content.Context;
import android.opengl.Matrix;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.util.Log;

public class MyRenderer implements Renderer {

	int widthView,heightView;
	Context mActivityContext;

	float[] projection = new float[16];
	float[] view = new float[16];
	float[] model = new float[16];
	float[] rotMatrix = new float[16];
	Particle part;
	ArrayList<ShaderHandles> shaderPrograms = new ArrayList<ShaderHandles>();
	int timeHandle;
	
	float time = System.nanoTime();
	float frameTime = 0;
	float unitTime = 0;
	float startTime = 0;
	float elapsedTime = 0;
	int fps = 0;
	
	public MyRenderer(final Context activityContext)
	{
		mActivityContext = activityContext;
	}
	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		float t = System.nanoTime()/290000000.0f;
		 t *= 0.1f;
		// Log.w("time",Float.toString( (float) Math.floor(t)));
	     t -=(float) Math.floor(t);
	      
		GLES20.glUseProgram(shaderPrograms.get(0).programHandle);
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glUniform1f(timeHandle, t);
		part.draw(projection, view, model, shaderPrograms.get(0), shaderPrograms.get(0).mTextureDataHandle.get(0));
		GLES20.glDisable(GLES20.GL_BLEND);
		fps++;
		fpsCounter();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		GLES20.glViewport(0, 0, width, height);
		float ratio = (float)width/height;
		Matrix.perspectiveM(projection, 0, 60f, ratio, 0.1f, 600f);
		Matrix.setLookAtM(view, 0, 0.0f, 0.0f, 2, 0, 0.0f, 0, 0, 1, 0);
		Matrix.setIdentityM(model, 0);
		widthView = width;
		heightView = height;
		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		 GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		 GLES20.glClearDepthf(1.0f);  
		 GLES20.glEnable( GLES20.GL_DEPTH_TEST );
		 GLES20.glDepthFunc( GLES20.GL_LESS);
		 GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);
		 GLES20.glDepthMask( true );
		 
		//create shader program handles and program for display textures
		ShaderHandles shader = new ShaderHandles();
		shader.programHandle = createShader(R.raw.vertextexture,R.raw.fragmenttexture);
		shader.mTextureDataHandle.add(TextureHelper.loadTexture(mActivityContext, R.drawable.star));
		initBasicHandlesWTexture(shader.programHandle,shader);
		shaderPrograms.add(shader);
		
		part = new Particle();

	}
	
	public void initBasicHandlesWTexture(int mProgram, ShaderHandles shader)
	{
		//attributes
		shader.positionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		shader.colorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
									
		//uniforms
		shader.modelHandle =  GLES20.glGetUniformLocation(mProgram, "model");
		shader.viewHandle =  GLES20.glGetUniformLocation(mProgram, "view");
		shader.projectionHandle =  GLES20.glGetUniformLocation(mProgram, "projection");
		shader.mTextureUniformHandle.add(GLES20.glGetUniformLocation(mProgram, "uTexture"));
		timeHandle = GLES20.glGetUniformLocation(mProgram, "time");
		
	}
	
	public int createShader(int vertex, int fragment) {
		String vertexShaderCode = RawResourceReader.readTextFileFromRawResource(mActivityContext, vertex);
		String fragmentShaderCode = RawResourceReader.readTextFileFromRawResource(mActivityContext, fragment);

		int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
			
		int mProgram;
		
		mProgram = ShaderHelper.createAndLinkProgram(vertexShaderHandle,fragmentShaderHandle);

		return mProgram;
	}
	
	public void fpsCounter() {
		float diff = ((System.nanoTime() - startTime) / 1000000000.0f);
		startTime = System.nanoTime();
		elapsedTime += diff;
		if (elapsedTime > 1) {
			elapsedTime = 0;
			Log.w("fps", Integer.toString(fps));
			fps = 0;
		}

	}

	
}
