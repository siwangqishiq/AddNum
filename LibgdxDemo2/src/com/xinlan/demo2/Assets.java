package com.xinlan.demo2;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {
	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {

	}

	@Override
	public void dispose() {

	}

}// end class
