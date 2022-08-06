package com.mom.momcustomerapp.customviews.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Nishant on 05-05-2016.
 */
public class GenericFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mFragmentTitles = new ArrayList<>();
    private Context mContext;

    public GenericFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    public void removeAllFragments() {
        mFragments.clear();
        mFragmentTitles.clear();
        mFragments = new ArrayList<>();
        mFragmentTitles = new ArrayList<>();
    }

    public void removeAllFragments(FragmentManager supportFragmentManager) {
        for (Fragment fragment : mFragments) {
            if (fragment != null) {
                supportFragmentManager.beginTransaction().remove(fragment).commit();
            }
        }

        mFragments.clear();
        mFragmentTitles.clear();
        mFragments = new ArrayList<>();
        mFragmentTitles = new ArrayList<>();
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mFragmentTitles != null && !mFragmentTitles.isEmpty()) {
//            String title = mFragmentTitles.get(position);
//            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(mContext.getAssets(), "fonts/yoshi.ttf"));
//            SpannableStringBuilder s = new SpannableStringBuilder();
//            s.append(title);
//            s.setSpan(typefaceSpan, 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            return SpannableString.valueOf(s);

            return mFragmentTitles.get(position);
        }
        return null;
    }
}
