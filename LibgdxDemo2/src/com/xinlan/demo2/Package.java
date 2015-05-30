package com.xinlan.demo2;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class Package {
	public static void main(String agrs[]){
		 Settings settings = new Settings();
         settings.maxWidth = 1024;
         settings.maxHeight = 1024;
         TexturePacker.process(settings, "D:\\assets-raw\\images",
                 "D:\\", "images");
	}	
}//end class
