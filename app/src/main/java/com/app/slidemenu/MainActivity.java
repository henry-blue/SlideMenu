package com.app.slidemenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import slidemenu.app.com.library.SlideMenuLayout;

public class MainActivity extends AppCompatActivity {

    List<SlideMenuLayout> slideMenuLayoutList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.list_id);
        listView.setAdapter(new MyAdapter());
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Engine.testStrings.length;
        }

        @Override
        public Object getItem(int position) {
            return Engine.testStrings[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (convertView == null) {
                view = View.inflate(MainActivity.this, R.layout.list_item, null);
            }
            ViewHolder holder = ViewHolder.getHolder(view);
            holder.mTvName.setText(Engine.testStrings[position]);

            final SlideMenuLayout slideMenuLayout = (SlideMenuLayout) view;
            slideMenuLayout.SetOnSlideMenuStatusListener(new SlideMenuLayout.OnSlideMenuStatusListener() {
                @Override
                public void OnStartOpenMenu() {
                    for (SlideMenuLayout layout : slideMenuLayoutList) {
                        layout.closeMenu(true);
                    }

                    slideMenuLayoutList.clear();
                }

                @Override
                public void OnOpenedMenu() {
                    slideMenuLayoutList.add(slideMenuLayout);
                }

                @Override
                public void OnStartCloseMenu() {

                }

                @Override
                public void OnClosedMenu() {
                    slideMenuLayoutList.remove(slideMenuLayout);
                }

                @Override
                public void OnSlidingMenu() {

                }
            });
            return view;
        }
    }

    private static class ViewHolder {
        private static TextView mTvName;
        private static TextView mTvIndex;
        private static TextView mTvDelete;

        public static ViewHolder getHolder(View view) {
            Object tag = view.getTag();
            if (tag == null) {
                ViewHolder holder = new ViewHolder();
                holder.mTvName = (TextView) view.findViewById(R.id.text_name);
                holder.mTvIndex = (TextView) view.findViewById(R.id.tv_index);
                holder.mTvDelete = (TextView) view.findViewById(R.id.tv_delete);
                tag = holder;
                view.setTag(tag);
            }
            return (ViewHolder) tag;
        }
    }
}
