package io.bingbin.bingbinandroid.utils;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    /*
    理解：PagerAdapter
    一，调用 getCount() 获取需要初始化的 ViewGroup 数量
    二，调用 instantiateItem() 实例化页卡，按顺序
    三，调用 destroyItem() 销毁，按顺序
     */
    // 所有 View
    private List<View> views;
    // 上下文
    private Context context;
    // 构造
    public ViewPagerAdapter(List<View> views, Context context) {
        this.views = views;
        this.context = context;
    }
    // 销毁时被调用
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // ViewGroup 所有的View
        // position 位置,第几个
        // 销毁时删除 View
        container.removeView(views.get(position));
        // super.destroyItem(container, position, object);
    }
    // 实例化页卡
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 添加 View
        container.addView(views.get(position));
        // 返回添加的 View 对象
        return views.get(position);
        // return super.instantiateItem(container, position);
    }
    // 所包含的 Item 总个数
    @Override
    public int getCount() {
        // 返回 views 总数
        return views.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object o) {
        return (view == o);
    }
}