package com.oyty.swiperefreshlistview;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oyty.refreshlistview.PullToRefreshBase;
import com.oyty.refreshlistview.PullToRefreshListView;
import com.oyty.swipelistview.SwipeMenu;
import com.oyty.swipelistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SimpleActivity extends Activity {

	private List<String> mAppList;
	private AppAdapter mAdapter;
	private PullToRefreshListView ptrlv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		mAppList = new ArrayList<String>();
		for(int i=0; i<20; i++){
			mAppList.add("item " + i);
		}

        initListView();
		

	}

    /**
     * ptrlv相关的一些初始化
     * setFunctionConfig  设置该控件的功能
     *  REFRESH        下拉刷新，上拉加载更多
     *  SWIPE          侧滑删除
     *  ALL            以上两个功能都有
     */
    private void initListView() {
        ptrlv = (PullToRefreshListView) findViewById(R.id.listView);
        ptrlv.init(this);
        ptrlv.setFunctionConfig(PullToRefreshListView.FunctionConfig.ALL);

        /**
         * 下拉刷新，上拉加载更多时有用
         */
        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<SwipeMenuListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
                loadUpMore();
                ptrlv.setOnRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
                loadBottomMore();
                ptrlv.setOnRefreshComplete();
            }
        });

        /**
         * 设置listview条目的点击事件
         */
        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "条目" + position + "被点击了", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 设置侧滑删除事件
         */
        ptrlv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                mAppList.remove(position);
                mAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "delete" + position, Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        mAdapter = new AppAdapter();
        ptrlv.setAdapter(mAdapter);

    }

    protected void loadUpMore() {
		SystemClock.sleep(5000);
	}
	protected void loadBottomMore() {
		SystemClock.sleep(5000);

        /**
         * 根据获取的数据判断，上拉加载是否可用，没有更多数据时可以禁止
         */
        ptrlv.setPullLoadEnabled(false);

	}

	class AppAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mAppList.size();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.item_list_app, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
//			holder.iv_icon.setImageDrawable(item.loadIcon(getPackageManager()));
//			holder.tv_name.setText(item.loadLabel(getPackageManager()));
			holder.tv_name.setText(mAppList.get(position));
			holder.iv_icon.setImageResource(R.drawable.ic_launcher);
			return convertView;
		}

		class ViewHolder {
			ImageView iv_icon;
			TextView tv_name;

			public ViewHolder(View view) {
				iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				tv_name = (TextView) view.findViewById(R.id.tv_name);
				view.setTag(this);
			}
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
	}


	
}
