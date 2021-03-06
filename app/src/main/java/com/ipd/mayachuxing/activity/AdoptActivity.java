package com.ipd.mayachuxing.activity;

import android.annotation.SuppressLint;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;
import com.ipd.mayachuxing.R;
import com.ipd.mayachuxing.adapter.AdoptAdapter;
import com.ipd.mayachuxing.base.BaseActivity;
import com.ipd.mayachuxing.base.BasePresenter;
import com.ipd.mayachuxing.base.BaseView;
import com.ipd.mayachuxing.bean.TestBean;
import com.ipd.mayachuxing.common.view.SpacesItemDecoration;
import com.ipd.mayachuxing.common.view.TopView;
import com.ipd.mayachuxing.utils.ApplicationUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Description ：领养
 * Author ： rmy
 * Email ： 942685687@qq.com
 * Time ： 2019/8/3.
 */
public class AdoptActivity extends BaseActivity {

    @BindView(R.id.tv_adopt)
    TopView tvAdopt;
    @BindView(R.id.rv_adopt)
    RecyclerView rvAdopt;
    @BindView(R.id.srl_adopt)
    SwipeRefreshLayout srlAdopt;

    private List<TestBean> testBeanList = new ArrayList<>();
    private AdoptAdapter adoptAdapter;
    private int pageNum = 1;//页数

    @Override
    public int getLayoutId() {
        return R.layout.activity_adopt;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public BaseView createView() {
        return null;
    }

    @SuppressLint("WrongConstant")
    @Override
    public void init() {
        //将每个Activity加入到栈中
        ApplicationUtil.getManager().addActivity(this);
        //防止状态栏和标题重叠
        ImmersionBar.setTitleBar(this, tvAdopt);

        // 设置管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//方向
        rvAdopt.setLayoutManager(layoutManager);
        rvAdopt.addItemDecoration(new SpacesItemDecoration(50));
        rvAdopt.setHasFixedSize(true);// 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        rvAdopt.setItemAnimator(new DefaultItemAnimator());//加载动画
        srlAdopt.setColorSchemeResources(R.color.tx_bottom_navigation_select);//刷新圈颜色
    }

    @Override
    public void initData() {
        if (5 > 0) {
            if (pageNum == 1) {
                testBeanList.clear();
                for (int i = 0; i < 5; i++) {
                    TestBean testBean = new TestBean();
                    testBeanList.add(testBean);
                }
//                testBean.addAll(data.getData().getMessageList());
                adoptAdapter = new AdoptAdapter(testBeanList);
                rvAdopt.setAdapter(adoptAdapter);
                adoptAdapter.bindToRecyclerView(rvAdopt);
                adoptAdapter.setEnableLoadMore(true);
                adoptAdapter.openLoadAnimation();
                adoptAdapter.disableLoadMoreIfNotFullPage();

                //上拉加载
                adoptAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                    @Override
                    public void onLoadMoreRequested() {
                        rvAdopt.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                initData();
                            }
                        }, 1000);
                    }
                }, rvAdopt);

                if (5 > 10) {
                    pageNum += 1;
                } else {
                    adoptAdapter.loadMoreEnd();
                }
            } else {
                if ((5 - pageNum * 10) > 0) {
                    pageNum += 1;
                    for (int i = 0; i < 5; i++) {
                        TestBean testBean = new TestBean();
                        testBeanList.add(testBean);
                    }
                    adoptAdapter.addData(testBeanList);
                    adoptAdapter.loadMoreComplete(); //完成本次
                } else {
                    for (int i = 0; i < 5; i++) {
                        TestBean testBean = new TestBean();
                        testBeanList.add(testBean);
                    }
                    adoptAdapter.addData(testBeanList);
                    adoptAdapter.loadMoreEnd(); //完成所有加载
                }
            }
        } else {
            testBeanList.clear();
            adoptAdapter = new AdoptAdapter(testBeanList);
            rvAdopt.setAdapter(adoptAdapter);
            adoptAdapter.loadMoreEnd(); //完成所有加载
            adoptAdapter.setEmptyView(R.layout.null_adopt_data, rvAdopt);
        }
    }

    @Override
    public void initListener() {
        //下拉刷新
        srlAdopt.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                initData();
                srlAdopt.setRefreshing(false);
            }
        });
    }
}
