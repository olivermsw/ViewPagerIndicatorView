package me.olimsw.viewpagerindicatorview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.olimsw.viewpagerindicatorviewlibrary.ViewPagerIndicatorView;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private ViewPagerIndicatorView vpiv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        vpiv = (ViewPagerIndicatorView) findViewById(R.id.vpiv);
        List<View> pageList = createPageList();

        HomeAdapter adapter = new HomeAdapter();
        adapter.setData(pageList);
        viewpager.setAdapter(adapter);
        vpiv.setViewPager(viewpager);
    }


    private List<View> createPageList() {
        List<View> pageList = new ArrayList<>();
        pageList.add(createPageView(Color.RED));
        pageList.add(createPageView(Color.BLUE));
        pageList.add(createPageView(Color.YELLOW));
        pageList.add(createPageView(Color.GREEN));
        return pageList;
    }

    private View createPageView(int color) {
        View view = new View(this);
        view.setBackgroundColor(color);
        return view;
    }

    class HomeAdapter extends PagerAdapter {

        private List<View> viewList;

        HomeAdapter() {
            this.viewList = new ArrayList<>();
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            View view = viewList.get(position);
            collection.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public void setData(@Nullable List<View> viewList) {
            if (viewList == null) {
                this.viewList.clear();
            } else {
                this.viewList.addAll(viewList);
            }
            notifyDataSetChanged();
        }

        @NonNull
        public List<View> getData() {
            if (viewList == null) {
                viewList = new ArrayList<>();
            }

            return viewList;
        }
    }
}
