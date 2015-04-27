# SwipeRefreshListView
This listview integrates some functions, pull-down refresh and swipe to delete.Just like the QQ App does.

# 该组件集成了下拉刷新，上拉加载更多和侧滑删除功能，可以只要其中一种，也可以两个功能都要

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
