package com.halum.torch;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class ShareApp extends ContentProvider {
	
	@Override
    public AssetFileDescriptor openAssetFile(Uri uri, String mode) throws FileNotFoundException {
		Log.d("Halum", "open asset");
		AssetManager am = getContext().getAssets();
        String file_name = uri.getLastPathSegment();
        if(file_name == null){
        	Log.d("Halum", "null file");
            throw new FileNotFoundException();
        }
        AssetFileDescriptor afd = null;
        try {
            afd = am.openFd(file_name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return afd;//super.openAssetFile(uri, mode);
    }

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		Log.d("Halum", "delet");
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		Log.d("Halum", "gettyp");
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		Log.d("Halum", "insert");
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		Log.d("Halum", "oncreat");
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		Log.d("Halum", "query");
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		Log.d("Halum", "update");
		return 0;
	}

}
