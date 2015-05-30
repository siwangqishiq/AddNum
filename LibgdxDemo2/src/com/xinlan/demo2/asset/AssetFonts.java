package com.xinlan.demo2.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class AssetFonts {
	 public final BitmapFont defaultSmall;
     public final BitmapFont defaultNormal;
     public final BitmapFont defaultBig;

	public AssetFonts() {
		// create three fonts using Libgdx's 15px bitmap font
        defaultSmall = new BitmapFont(
                Gdx.files.internal("data/images/arial-15.fnt"), true);
        defaultNormal = new BitmapFont(
                Gdx.files.internal("data/images/arial-15.fnt"), true);
        defaultBig = new BitmapFont(
                Gdx.files.internal("data/images/arial-15.fnt"), true);
        // set font sizes
        defaultSmall.getData().setScale(0.75f);
        defaultNormal.getData().setScale(1.0f);
        defaultBig.getData().setScale(2.0f);
        // enable linear texture filtering for smooth fonts
        defaultSmall.getRegion().getTexture()
                .setFilter(TextureFilter.Linear, TextureFilter.Linear);
        defaultNormal.getRegion().getTexture()
                .setFilter(TextureFilter.Linear, TextureFilter.Linear);
        defaultBig.getRegion().getTexture()
                .setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	public void dispose(){
		defaultSmall.dispose();
		defaultNormal.dispose();
		defaultBig.dispose();
	}
}// end class
