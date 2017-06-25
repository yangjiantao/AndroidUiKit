# AndroidUiKit
**安卓常用UI组件库。**
总结、沉淀、封装优化；为避免重复造轮子，此项目会收集优秀的三方库，或直接引用，或修改源码；目标很明确：**快速集成开发，提高效率。**

## ISwipeRefreshLayout
 参考官方SwipeRefreshLayout源码实现，用法一致。支持自定义RefreshView。

### 经典下拉刷新效果
![refresh-01](/art/refreshview/20170618-212852-refresh.gif)

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

 ![FlycoTabLayout](https://github.com/H07000223/FlycoTabLayout/blob/master/preview_1.gif)

- [MagicIndicator](https://github.com/hackware1993/MagicIndicator)

 ![MagicIndicator](https://github.com/hackware1993/MagicIndicator/blob/master/magicindicator.gif)

本项目在**FlycoTabLayout**基础上进行修改和裁剪而来。


## Thanks
感谢所有开源项目作者。


