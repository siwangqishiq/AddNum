package com.xinlan.demo2;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class MainApp {
	public static void main(String agrs[]){
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.fullscreen = false;
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new CanyonBunnyMain(), config);
	}
}//end class
