package com.example.loginsmartwatchsse;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;



/**public class SliderPage extends AppCompatActivity{
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.slider_page);

ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
tabLayout.setupWithViewPager(viewPager, true);

viewPager.setOffscreenPageLimit(1);
SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
viewPager.setAdapter(swipeAdapter);
viewPager.setCurrentItem(0);
}

private class SwipeAdapter extends FragmentStatePagerAdapter {
public SwipeAdapter(FragmentManager fm){
super (fm);
}

@NonNull
@Override
public Fragment getItem(int i) {
Fragment frag = null;

switch (i) {
case 0:
frag = new RequestDetails();
break;
case 1:
frag = new OrderDetails_sospeso();
break;

}
return frag;
}

@Override
public int getCount() {
return 2;
}
}
}*/

