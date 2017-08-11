# AndroidUiKit
**安卓常用UI组件库。**
总结、沉淀、封装优化；为避免重复造轮子，此项目会收集优秀的三方库，或直接引用，或修改源码；目标很明确：**快速集成开发，提高效率。**

## ISwipeRefreshLayout
 参考官方SwipeRefreshLayout源码实现，用法一致。支持自定义RefreshView。

### 经典下拉刷新效果
推荐一个[动画库](https://github.com/81813780/AVLoadingIndicatorView),效果很棒，代码简洁，本文ISwipeRefreshLayout组件可直接使用。代码在手，想怎么搞都行。
<img src="art/refreshview/avi.gif" width=216/> <img src="art/refreshview/20170618-212852-refresh.gif" width=216/> <img src="art/av-loading-line.gif" width=216/> <img src="art/loading_test_001.gif" width=216/>

### 参考资料
- [IRecyclerView](https://github.com/Aspsine/IRecyclerView)
- http://blog.csdn.net/lmj623565791/article/details/24252901
- http://blog.csdn.net/guolin_blog/article/details/17357967


## IDividerItemDecoration 
  RecyclerView分割线，支持自定义height、color、padding。
### 用法
```
IDividerItemDecoration divierDecoration = new IDividerItemDecoration(this, IDividerItemDecoration.VERTICAL);
        divierDecoration.setVerticalDividerHeight(3);
        divierDecoration.setDividerColor(Color.BLUE);
        divierDecoration.setDividerPadding(30);
        recyclerView.addItemDecoration(divierDecoration);

```
  
## TabLayout

**推荐两个库：**

- [FlycoTabLayout](https://github.com/H07000223/FlycoTabLayout)

- [MagicIndicator](https://github.com/hackware1993/MagicIndicator)

本项目在**FlycoTabLayout**基础上进行修改和裁剪而来。

![tablayout](/art/tablayout/QQ20170625-213831-tablayout.gif)


## MultiTypeLoadMoreAdapter
在[MultiType](https://github.com/drakeet/MultiType)库的基础上，实现的支持上拉加载更多的Adapter，加载效果简单，这里就不贴出来了。另外，MultiType是一个很优秀的库，完爆各类对Adapter的封装库，值得查阅源码。

## FrameAnimDrawable
一个解决安卓帧动画OOM的组件。[详情](http://www.jianshu.com/p/3a8861678a45)


## Thanks
感谢所有开源项目作者。


