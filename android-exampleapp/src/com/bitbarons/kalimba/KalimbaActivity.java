package com.bitbarons.kalimba;

import java.io.IOException;
import java.util.ArrayList;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.core.PdBase;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import com.unity3d.player.UnityPlayerActivity;

public class KalimbaActivity extends UnityPlayerActivity {
	public static android.app.Activity instance;
	
	private static final String TAG = "Kalimba";
        private static final int MIN_SAMPLE_RATE = 44100;
	
	@Override
	public void onStart() {
		PdAudio.startAudio(this);
		super.onStart();
	}
	
	@Override
	public void onStop() {
		PdAudio.stopAudio();
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		cleanup();
		super.onDestroy();
	}
	
	private void cleanup() {
		// make sure to release all resources
		PdAudio.release();
		PdBase.release();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		
		try {
			initPd();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			finish();
		}
	}

	private void initPd() throws IOException {
                int srate = Math.max(MIN_SAMPLE_RATE, AudioParameters.suggestSampleRate());
		PdAudio.initAudio(srate, 0, 2, 1, true);
        }
	
	@Override
	public void onResume() {
		super.onResume();
	}

	public static ArrayList<String> listAssetsContent(String path)
    {
		ArrayList<String> l = new ArrayList<String>();
		
		try {
			AssetManager am = instance.getResources().getAssets();
			
			String [] list = am.list(path);
			for (String s : list)
			{
				String subPath = path + "/" + s;
				if (subPath.charAt(0) == '/')
				{
					subPath = subPath.substring(1);
				}
				
				ArrayList<String> subList = listAssetsContent(subPath);
				
				if (subList.size() > 0)
				{
					// directory
					l.addAll(subList);
				}
				else
				{
					// file
					l.add(subPath);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return l;
    }
}
