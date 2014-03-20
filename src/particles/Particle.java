package particles;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import loaders.ShaderHandles;

import android.opengl.GLES20;

public class Particle {

	float baseVertices[] = new float[3000];
	float colors[] = new float[3000];
	
	int positionsBufferIdx;
	int colorCoordsBufferIdx;
	int normbuffidx,tanbuffidx;
	int buffers[] = new int[2];
	FloatBuffer vertexBuffer,colorBuffer;
	
	
	public Particle()
	{
		Random rand = new Random();
		
		int x = 0;
		int y = 0;
		for(int i =0; i < 1000; i++)
		{
			baseVertices[x] = (rand.nextFloat()  * 2.0f - 1.0f) * 100.0f;
			x++;
			baseVertices[x] = (rand.nextFloat()  * 2.0f - 1.0f) * 100.0f;
			x++;
			baseVertices[x] = rand.nextFloat();
			x++;
			
			colors[y] =  0.8f +  rand.nextFloat() * 0.2f;
			y++;
			colors[y] =  0.8f +  rand.nextFloat() * 0.2f;
			y++;
			colors[y] =  0.8f +  rand.nextFloat() * 0.2f;
			y++;
		}
		
		
		// buffer for vertices
		ByteBuffer buffer = ByteBuffer.allocateDirect(4 * baseVertices.length);
		buffer.order(ByteOrder.nativeOrder());
				
		vertexBuffer = buffer.asFloatBuffer();
		vertexBuffer.put(baseVertices);
		vertexBuffer.position(0);
		
		// buffer for colors
		ByteBuffer cBuffer = ByteBuffer.allocateDirect(4 * colors.length);
		cBuffer.order(ByteOrder.nativeOrder());

		colorBuffer = cBuffer.asFloatBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);
		
		
		//create VBO map
		GLES20.glGenBuffers(2, buffers, 0);
						
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexBuffer.capacity() * 4, vertexBuffer, GLES20.GL_STATIC_DRAW);
				
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[1]);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, colorBuffer.capacity() * 4, colorBuffer, GLES20.GL_STATIC_DRAW);
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		
		positionsBufferIdx = buffers[0];
		colorCoordsBufferIdx = buffers[1];
		
		vertexBuffer = null;
		colorBuffer = null;
	}
	
	public void draw( float[] projection, float[] view, float[] model,ShaderHandles Shader, int textures)
	{
		//position
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, positionsBufferIdx);
		GLES20.glEnableVertexAttribArray(Shader.positionHandle);
		GLES20.glVertexAttribPointer(Shader.positionHandle, 3, GLES20.GL_FLOAT, false,	0, 0);

		//normals
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, colorCoordsBufferIdx);
		GLES20.glEnableVertexAttribArray(Shader.colorHandle);
		GLES20.glVertexAttribPointer(Shader.colorHandle, 3, GLES20.GL_FLOAT, false,0, 0);
		
		// Bind the texture to this unit.
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures);
		GLES20.glUniform1i(Shader.mTextureUniformHandle.get(0), 0);
		
		
		//send uniforms
		GLES20.glUniformMatrix4fv(Shader.projectionHandle, 1, false, projection, 0);
		GLES20.glUniformMatrix4fv(Shader.viewHandle, 1, false, view, 0);
		GLES20.glUniformMatrix4fv(Shader.modelHandle, 1, false, model, 0);
		
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, baseVertices.length/3);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
	}
	
	
}
