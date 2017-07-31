package com.android.poesiamonstruosa.customs;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.android.poesiamonstruosa.activities.ReadFragment;
import com.android.poesiamonstruosa.utils.Constants;

/**
 * Adaptador para el ViewPager
 */
public class CustomPagerAdapter extends FragmentStatePagerAdapter {
	private final int mSize;
	
	public CustomPagerAdapter(FragmentManager fm, int size) {
        super(fm);
        
        Log.v(Constants.Log.METHOD, "CustomPagerAdapter - new()");
        
        mSize = size;
    }

    @Override
    public int getCount() {
    	//Log.v(Constants.Log.METHOD, "getCount CustomPagerAdapter");
    	
        return mSize;
    }

    @Override
    public Fragment getItem(int position) {
    	Log.v(Constants.Log.METHOD, "CustomPagerAdapter - getItem()");
    	
        return ReadFragment.newInstance(position);
    }
    
}
