package com.xinlan.demo2.asset;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class AssetBunny {
	 public final AtlasRegion head;

     public AssetBunny(TextureAtlas atlas) {
         head = atlas.findRegion("bunny_head");
     }
}
