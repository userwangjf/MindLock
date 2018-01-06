[ ![Download](https://api.bintray.com/packages/azhon/azhon/suspension-fab/images/download.svg?version=1.1.0) ](https://bintray.com/azhon/azhon/suspension-fab/1.1.0/link)
### 一： 一个可以展示多个悬浮按钮的菜单

### 二：效果图

<img src="https://github.com/azhong1011/SuspensionFAB/blob/master/screenshot/suspension.gif"/>

### 三：使用介绍
xml属性   | 描述 | 值
-------- | --- | ---
a_zhon:fab_spacing | 两个按钮之间的间距 | dimension
a_zhon:fab_orientation| 菜单的展开方向 | top bottom left right

### 四：在gradle依赖这个library
```
<dependency>
  <groupId>com.azhon</groupId>
  <artifactId>suspension-fab</artifactId>
  <version>1.1.0</version>
</dependency>

compile 'com.azhon:suspension-fab:1.1.0'
```
### 五：布局使用
```
<com.azhon.suspensionfab.SuspensionFab
    android:id="@+id/fab_top"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    a_zhon:fab_orientation="top"
    a_zhon:fab_spacing="10dp" />
```
### 需要在SuspensionFab的父级ViewGroup中添加android:clipChildren="false" 属性

### 六：添加展开菜单
```
SuspensionFab fabTop = (SuspensionFab) findViewById(R.id.fab_top);
//构建展开按钮属性
FabAttributes collection = new FabAttributes.Builder()
        .setBackgroundTint(Color.parseColor("#2096F3"))
        .setSrc(getResources().getDrawable(R.drawable.like))
        .setFabSize(FloatingActionButton.SIZE_MINI)
        .setPressedTranslationZ(10)
        .setTag(1)
        .build();
FabAttributes email = new FabAttributes.Builder()
        .setBackgroundTint(Color.parseColor("#FF9800"))
        .setSrc(getResources().getDrawable(R.drawable.mail))
        .setFabSize(FloatingActionButton.SIZE_MINI)
        .setPressedTranslationZ(10)
        .setTag(2)
        .build();
FabAttributes news = new FabAttributes.Builder()
        .setBackgroundTint(Color.parseColor("#03A9F4"))
        .setSrc(getResources().getDrawable(R.drawable.news))
        .setFabSize(FloatingActionButton.SIZE_MINI)
        .setPressedTranslationZ(10)
        .setTag(3)
        .build();
//添加菜单
fabTop.addFab(collection, email, news);
//设置菜单点击事件
fabTop.setFabClickListener(this);
```
### 七：提供自定义每个按钮的动画（AnimationManager）
> 继承（extends）AnimationManager 并实现里面的方法实现自己想要的动画 （可以查看Demo里的使用案例）
`fabTop.setAnimationManager(new FabAlphaAnimate(fabTop));`//设置自定义的动画
### 如果觉得不错就留下你的star吧 [博文地址](http://blog.csdn.net/a_zhon/article/details/74086025)......
### LICENSE
   Copyright [2016-09-21] [阿钟]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
