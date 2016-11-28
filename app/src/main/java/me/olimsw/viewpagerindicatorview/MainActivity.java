package me.olimsw.viewpagerindicatorview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.olimsw.viewpagerindicatorviewlibrary.ViewPagerIndicatorView;

import static me.olimsw.viewpagerindicatorviewlibrary.AnimationMode.COMPRESSSLIDE;
import static me.olimsw.viewpagerindicatorviewlibrary.AnimationMode.GRADIENTCOLOR;
import static me.olimsw.viewpagerindicatorviewlibrary.AnimationMode.RECTSLIDE;
import static me.olimsw.viewpagerindicatorviewlibrary.AnimationMode.SLIDE;
import static me.olimsw.viewpagerindicatorviewlibrary.AnimationMode.SMALLRECTSLIDE;
import static me.olimsw.viewpagerindicatorviewlibrary.AnimationMode.STANDARD;
import static me.olimsw.viewpagerindicatorviewlibrary.AnimationMode.ZOOM;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private ViewPagerIndicatorView vpiv;
    private Toolbar toolbar;

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
//        vpiv.setAnimationMode(SLIDE);
        vpiv.setAnimationMode(STANDARD);
        vpiv.setUnselectedColor(Color.parseColor("#77ffffff"));
        vpiv.setSelectedColor(Color.parseColor("#ffffff"));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("STANDARD");
        toolbar.setSubtitleTextColor(Color.WHITE);

        toolbar.setTitle("VierPagerIndicatorView");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_1:
                        vpiv.setAnimationMode(STANDARD);
                        vpiv.setUnselectedColor(Color.parseColor("#77ffffff"));
                        vpiv.setSelectedColor(Color.parseColor("#ffffff"));
                        toolbar.setSubtitle("STANDARD");
                        break;
                    case R.id.action_2:
                        vpiv.setAnimationMode(SLIDE);
                        vpiv.setUnselectedColor(Color.parseColor("#77ffffff"));
                        vpiv.setSelectedColor(Color.parseColor("#ffffff"));
                        toolbar.setSubtitle("SLIDE");
                        break;
                    case R.id.action_3:
                        toolbar.setSubtitle("COMPRESSSLIDE");
                        vpiv.setAnimationMode(COMPRESSSLIDE);
                        vpiv.setUnselectedColor(Color.parseColor("#ffffff"));
                        break;
                    case R.id.action_4:
                        toolbar.setSubtitle("GRADIENTCOLOR");
                        vpiv.setAnimationMode(GRADIENTCOLOR);
                        vpiv.setUnselectedColor(Color.parseColor("#ffffff"));
                        vpiv.setSelectedColor(Color.parseColor("#ffffff"));
                        break;
                    case R.id.action_5:
                        toolbar.setSubtitle("RECTSLIDE");
                        vpiv.setAnimationMode(RECTSLIDE);
                        vpiv.setUnselectedColor(Color.parseColor("#77ffffff"));
                        vpiv.setSelectedColor(Color.parseColor("#ffffff"));
                        break;
                    case R.id.action_6:
                        toolbar.setSubtitle("SMALLRECTSLIDE");
                        vpiv.setAnimationMode(SMALLRECTSLIDE);
                        vpiv.setUnselectedColor(Color.parseColor("#77ffffff"));
                        vpiv.setSelectedColor(Color.parseColor("#ffffff"));
                        break;
                    case R.id.action_7:
                        toolbar.setSubtitle("ZOOM");
                        vpiv.setAnimationMode(ZOOM);
                        vpiv.setUnselectedColor(Color.WHITE);
                        break;
                    case R.id.action_8:
                        toolbar.setSubtitle("");
                        break;
                }

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private List<View> createPageList() {
        List<View> pageList = new ArrayList<>();
        pageList.add(createPageView(Color.RED));
        pageList.add(createPageView(Color.BLUE));
        pageList.add(createPageView(Color.GRAY));
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
