package com.oyty.swiperefreshlistview;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oyty.refreshlistview.PullToRefreshBase;
import com.oyty.refreshlistview.PullToRefreshBase.OnRefreshListener;
import com.oyty.refreshlistview.PullToRefreshListView;
import com.oyty.swipelistview.SwipeMenu;
import com.oyty.swipelistview.SwipeMenuCreator;
import com.oyty.swipelistview.SwipeMenuItem;
import com.oyty.swipelistview.SwipeMenuListView;
import com.oyty.swipelistview.SwipeMenuListView.OnMenuItemClickListener;
import com.oyty.swipelistview.SwipeMenuListView.OnSwipeListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class SimpleActivity extends Activity {

	private List<String> mAppList;
	private AppAdapter mAdapter;
	private SwipeMenuListView mListView;
	private PullToRefreshListView ptrlv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		mAppList = new ArrayList<String>();
		for(int i=0; i<20; i++){
			mAppList.add("item " + i);
		}
		
		// 1,获取ptrlv和mListView
		ptrlv = (PullToRefreshListView) findViewById(R.id.listView);
		mListView = ptrlv.getRefreshableView();
		
		// 2,设置adapter
		mAdapter = new AppAdapter();
		mListView.setAdapter(mAdapter);

		// 3
		initView();

		// 4
		initSwipeMenu();
	}

	private void initView() {
		// 1-设置上拉加载不可用（国内一般是滚动到最后一个条目，加载更多）
		ptrlv.setPullLoadEnabled(true);
		// 2-滑动到底部是否自动加载更多数据
		ptrlv.setScrollLoadEnabled(false);

		//3-设置刷新的监听器
		ptrlv.setOnRefreshListener(new OnRefreshListener<SwipeMenuListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<SwipeMenuListView> refreshView) {
				loadUpMore();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<SwipeMenuListView> refreshView) {
				loadBottomMore();
			}
		});

		ptrlv.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Toast.makeText(getApplicationContext(), "条目" + position + "被点击了", Toast.LENGTH_SHORT).show();
					}
				});
		// 8-设置最后更新的时间文本
		ptrlv.setLastUpdatedLabel(getStringDate());
	}

	private void initSwipeMenu(){
		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
				// set item width
				openItem.setWidth(dp2px(90));
				// set item title
				openItem.setTitle("Open");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);
				
				
				
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		mListView.setMenuCreator(creator);

		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				switch (index) {
				case 0:
					Toast.makeText(getApplicationContext(), "open" + position, Toast.LENGTH_SHORT).show();
					break;
				case 1:
					// delete
					// delete(item);
					mAppList.remove(position);
					mAdapter.notifyDataSetChanged();
					Toast.makeText(getApplicationContext(), "delete" + position, Toast.LENGTH_SHORT).show();
					break;
				}
				return false;
			}
		});

		// set SwipeListener
		mListView.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

		// other setting
		// listView.setCloseInterpolator(new BounceInterpolator());

		// test item long click
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(),
						position + " long click", 0).show();
				return false;
			}
		});
	}
	
	protected void loadUpMore() {
		new Thread(){
			@Override
			public void run() {
				SystemClock.sleep(5000);
				//7-设置下拉刷新和滚动加载更多完成的
				ptrlv.onPullDownRefreshComplete();  
				ptrlv.onPullUpRefreshComplete();    

			}
		}.start();
		
		//6-设置是否有更多数据的标志        
//		if(!TextUtils.isEmpty(moreUrl)){    
//			ptrlv.setHasMoreData(true);     
//		} else {                            
//			ptrlv.setHasMoreData(false);    
//		}                                   
		                                    
		
		
	}
	protected void loadBottomMore() {
		new Thread(){
			@Override
			public void run() {
				SystemClock.sleep(2000);
				//7-设置下拉刷新和滚动加载更多完成的
				ptrlv.onPullDownRefreshComplete();  
				ptrlv.onPullUpRefreshComplete();    

			}
		}.start();
		
		//6-设置是否有更多数据的标志        
//		if(!TextUtils.isEmpty(moreUrl)){    
//			ptrlv.setHasMoreData(true);     
//		} else {                            
//			ptrlv.setHasMoreData(false);    
//		}                                   
		                                    
		
		
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

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	
	/**
	 * 获取现在时间
	 * 
	 * @return 返回短时间字符串格式yyyy-MM-dd HH:mm:ss
	 */
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
}
