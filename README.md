# QuickKV

[![GitHub license](https://img.shields.io/github/license/SumiMakito/QuickKV.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![](https://jitpack.io/v/SumiMakito/QuickKV.svg)](https://jitpack.io/#SumiMakito/QuickKV)

Lightweight &amp; Easy-to-use Key-Value Library for Android Projects.

为Android项目提供的轻量且易用的键值数据库

*English &amp; Chinese API Documentation Available! 含有中文&amp;英文API文档*

### Preface 前言

Map and List are too complex to initialize. Actually, the things we only need to focus on are the key and the value. So ... QuickKV comes up!

Map和List的初始化很复杂。事实上，我们只需要关注键与值就够了。于是，QuickKV诞生了！

* HashMap with JSON

```java
//Put data in a hashmap and save it to the local storage.
//在HashMap中放入数据并存储至本地存储器。
Map<Object, Object> map = new HashMap<Object, Object>();
map.put("Key","Value");
JSONObject json = new JSONObject();
Iterator iter = map.entrySet().iterator();
while (iter.hasNext())
{
    Map.Entry entry = (Map.Entry) iter.next();
    Object key = entry.getKey();
    Object value = entry.getValue();
    json.put(key.toString(), value.toString());
}
FileOutputStream fos = this.openFileOutput("data.json", Context.MODE_PRIVATE);
fos.write(json.toString().getBytes());
fos.close();
//Load saved data from local storage and parse it, then convert it to a HashMap is more complex.
//从存储器中载入已保存的数据并解析、转换为HashMap就更加复杂了。
```

* QuickKV

```java
//Do the same thing with QuickKV
//用QuickKV做同样的事情
QuickKV quickKv = new QuickKV(this);
KeyValueDatabase pkvdb1 = quickKv.getDatabase("Foo");
pkvdb1.put("Key", "Value");
pkvdb1.persist();
//Done! Saved to local storage!
//完成！已保存至本地存储器！
//Let try to load this saved database!
//让我们来试试载入这个保存好的数据库！
KeyValueDatabase pkvdb2 = quickKv.getDatabase("Foo");
pkvdb2.get("Key");
//Output: "Value"
//输出: "Value"
```

> Do more tasks in fewer words. This QuickKV!
> 代码虽短，却能完成多个任务，这就是QuickKV！

### Current version 当前版本

##### 1.0.5 (Library 库) / 0.8 (Demo 演示)

[Changelog / 更新说明 →](https://github.com/SumiMakito/QuickKV#changelog-更新说明)

### TODO 任务与目标

* 数据分级缓存机制

### Add to your project 添加到你的项目中

##### Use JitPack.io(recommended) 使用JitPack.io(推荐)

Add JitPack repository to build.gradle in project root. / 在项目根目录下的build.gradle中加入以下内容
```
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

Add the dependency to build.gradle in app root. / 在应用目录下的build.gradle中加入以下依赖
```
dependencies {
	compile 'com.github.SumiMakito:QuickKV:1.0.5'
}
```

##### JAR File(~1.0.3, deprecated) JAR文件(~1.0.3, 已弃用)

[Releases](https://github.com/SumiMakito/QuickKV/releases)

##### Demo APK 演示APK (0.8)

[Dropbox](https://www.dropbox.com/s/53b86j9xuhpw9f1/QuickKVDemo.apk?dl=0)

[Google Drive](https://docs.google.com/file/d/0B_-0A4yjEnvMNTJMLTMwcEZ2Njg/edit?usp=docslist_api)

[百度云](http://pan.baidu.com/share/link?shareid=3375366656&uk=1479848638)

#### Get an instance / 新建实例

```java
QuickKV quickKv = new QuickKV(this);
```

#### Set workspace(you can skip this step) / 设定工作目录(可略过此步骤)

> Default workspace / 默认工作目录: ```Context.getFilesDir();```

```java
quickKv.setWorkspace("/tmp");
```

#### Get database 取得数据库

> In the new version 0.8, we removed PersistableKVDB. We merged main functions in the KeyValueDatabase. Now QuickKV is more easy to use!
> 在0.8新版本中，我们移除了可持续化KV数据库。我们将主要功能合并进了KeyValueDatabase。现在，QuickKV变得更易用！

##### Normal KVDB 普通键值数据库

```java
kvdb = quickKv.getDatabase(); // --> database.qkv
kvdb = quickKv.getDatabase(true); // --> database.qkv (gzip)
kvdb = quickKv.getDatabase("CustomName"); // --> CustomName.qkv
kvdb = quickKv.getDatabase("CustomName.db"); // --> CustomName.db
kvdb = quickKv.getDatabase("CustomName", true); // --> CustomName.qkv (gzip)
kvdb = quickKv.getDatabase("", "Password"); // --> database.qkv (encrypted)
kvdb = quickKv.getDatabase("CustomName", "Password"); // --> CustomName.qkv (encrypted)
kvdb = quickKv.getDatabase("CustomName", "Password", true); // --> CustomName.qkv (encrypted+gzip)
```

##### High Capacity KVDB 大容量键值数据库(1.0.0+)

*Less OOM, Experimental. 更少的内存溢出问题，试验性功能。*

```java
quickKv.setWorkspace("/tmp"); // IMPORTANT: Must set workspace first!!
hckvdb = quickKv.getHCKVDB("CustomName"); // --> /tmp/CustomName
hckvdb = quickKv.getHCKVDB("CustomName.db"); // --> /tmp/CustomName.db
```

[See also:Appendix1 参见:附录 1](https://github.com/SumiMakito/QuickKV#appendix-1something-about-hckvdb-附录-1关于大容量键值数据库的说明)

#### Return values after operations 操作后返回值 (0.8+)

* true = Success :)
* false = Failed :(

#### Operate Normal KVDB 操作普通数据库

##### Add a key-value data 添加一条KV数据

```java
qkvdb.put(k,v); //Put everything as you like
```

##### Add plenty of data in HashMap 将HashMap中的多条数据加入

```java
qkvdb.put(hashMap); //Auto filled
```

##### Get value of the given key 通过键取得值

> This method will return a Object, you can cast it to its original type later.
> 这个方法将返回一个对象，你可以在之后使用形态转换将该对象转为原始类型。

*Trick:Use instanceof to know the data type. 技巧:通过instanceof来了解对象的数据类型。*

```java
qkvdb.get("something_key");
qkvdb.getString("a_string");
qkvdb.getInt("an_int");
//... and more specific getters were added after v1.0.2
```

##### Remove data 移除数据

```java
qkvdb.remove("key");
```

##### Persist 持久化

> This method will save persistable database to local storage. In this way, you can read your data and reuse them at any time. QuickKV will automatically load the saved database.
> 这个方法将会将可持久化数据库从内存保存至文件存储器，这样一来你就可以在任何时候读取与复用你持久化后的数据。QuickKV将自动载入已保存的持久化数据库。

*Warning: ONLY String/Integer/Long/Double/Float/Boolean/JSONObject/JSONArray can be persisted to local storage. 注意：只有String/Integer/Long/Double/Float/Boolean/JSONObject/JSONArray类型的数据才可被持久化。*

*After 0.8.1, async is supported. 0.8.1版本后引入异步支持*

```java
qkvdb.persist(); //return boolean
//With a callback
qkvdb.persist(new KeyValueDatabase.Callback(){
    @Override
    public void onSuccess(){
        //Do something...
    }
    @Override
    public void onFailed(){
        //Do something...
    }
}); //return void(nothing)
```

##### Synchronize 同步

*Trick:You can use this method for multi-instance purpose, but we don't recommend you to do so. Frequently synchronization will affect the performance. 技巧：你可以用这个方法来实现多实例目的，但不推荐使用。频繁地同步操作将会影响性能。*

> This method will synchronize current database from persisted version.
> 这个方法将会使数据从已持久化版本同步至当前数据库。

*After 0.8.1, async is supported. 0.8.1版本后引入异步支持*

###### Sync mode 同步模式

> Merge(true) is default. 合并模式为默认.

+ Merge(true):Synchronize persisted version to current database and overwrite duplicate items. 合并模式：将已持久化的部分覆盖至当前数据库，其他数据保持原样。

+ Merge(false):Clear database first. Then synchronize persisted version to current database. 非合并模式：先清除数据库内容，再将已持久化的数据载入至当前数据库。

###### Code Snippet 代码片段

```java
qkvdb1.put("key", "value");
qkvdb1.persist();
qkvdb2.get("key");
//Output: null (Key doesn't exist)
qkvdb2.get("qkv2");
//Output: "another_value" (qkvdb1 doesn't have this key-value)
qkvdb2.sync(true); //Merge mode is true
qkvdb2.get("key").toString();
//Output: "value"
qkvdb2.get("qkv2");
//Output: "another_value"
qkvdb2.sync(false); //Merge mode is false
qkvdb2.get("qkv2");
//Output: null (because qkvdb doesn't have this key-value)
//Async (0.8.1+)
qkvdb2.sync(new KeyValueDatabase.Callback(){
    @Override
    public void onSuccess(){
        //Do something...
    }
    @Override
    public void onFailed(){
        //Do something...
    }
});
//Specify a mode for sync with callback
qkvdb2.sync(true, new KeyValueDatabase.Callback(){
    @Override
    public void onSuccess(){
        //Do something...
    }
    @Override
    public void onFailed(){
        //Do something...
    }
});
```

##### Encrypt/Decrypt 加密/解密

> After successfully called this method, database will persist itself autimatically in order to apply the encryption/decryption.
> 成功调用此方法后，数据库会自动持久化一次以应用加密/解密。

*You won't need to decrypt the database before persist, everything is automatic! 你不需要在持久化前人工解密，一切都将自动进行。*

* Encrypt 加密

```java
//You can find the first method in the "Get database" section.
//Here is the second method
qkvdb.enableEncryption("mykey123"); //"" or null as a key is not allowed!
```

* Decrypt 解密

```java
qkvdb.disableEncryption();
```

##### Clear 清除数据

> This method will clear all data in the specified database.
> 这个方法将清除指定数据库中所有的数据。

```java
qkvdb.clear();
```

##### Trick: Empty a persisted database 技巧:清空一个已持久化的数据库文件

```java
qkvdb.clear(); //Clear it
qkvdb.persist(); //Then persist it
```

#### Multi-database management 多数据库管理

##### Release database 释放数据库

> This method will release non-default database.
> 这个方法将从内存中释放非默认数据库。

*You'll lose any unsaved data.*
*所有未保存的数据都将丢失*

```java
qucikKv.releaseDatabase("a_database");
qucikKv.releaseAllDatabases(); //One-click-destroy :P
```

##### Is database opened? 这个数据库被打开了吗?

> This method will return a boolean.
> 这个方法将返回一个布尔值。

```java
qucikKv.isDatabaseOpened("dbAlias");
```

#### Appendix 1:Something about HCKVDB 附录 1:关于大容量键值数据库的说明

* ```hckvdb.containsValue();``` is disabled(will returns false forever) 该方法已禁用

* Because HCKVDB is aimed for avoiding OOM issue, searching/persisting performance is still need to improve. 因HCKVDB的目标是避免OOM问题，搜索与持续化的性能仍待提高。

### Changelog 更新说明

##### 1.0.5+

* Add JitPack support. 此版本起可以使用JitPack添加依赖

##### 1.0.3+

* Added: Supported filling the database by HashMap. 新增: 现在可以使用HashMap来填充数据库了。

##### 1.0.2+

* Added: Specific getters, less cast! 新增: 特定类型的Getter，免去Cast烦恼。

##### 1.0.0+

* New type: High capacity key-value database 新增数据库类型: 大容量键值数据库

##### 0.9.5+

* Support GZip compression 支持GZip压缩

* Support custom workspace 可设置工作目录

* Use MaglevIO to speed up reading and writing 使用MaglevIO提升数据库读取与写入速度

##### 0.9+

* Package name is changed to ``` com.github.sumimakito.quickkv ``` 包名已变更为 ``` com.github.sumimakito.quickkv ```

* Use MaglevIO to improve reading speed(reading only) 使用MaglevIO提升数据库读取速度(仅读取)

##### 0.8.1+

* Support async method for heavy operations 支持繁重操作的异步执行

* More convenient 操作更便捷

* Improved reading speed 文件读取解析更迅速

* Generic supprot(Improving) 泛型支持(改进中)

* AES256 encryption support 支持AES256加密

##### 0.7+

* High performance 高性能

##### 0.6+

* Allow nearly all type of keys and values 几乎支持所有类型的键与值

* Multi-database management 多数据库管理

* Persistable 可持久化

### Special Thanks 特别感谢

* Json-smart-v2 (Apache License 2.0) http://netplex.github.io/json-smart/

### Contributors 贡献者

[SumiMakito](https://github.com/SumiMakito)

### Support 支持

Makito是一个自由的软件开发者，也是一名在校大学生。

欢迎您去查看我的其他开源项目，同时也欢迎通过打赏支持我一下。感谢

##### 支付宝/AliPay

826257232#qq.com (#请换成@)

<img src="art/aliqr.png" width="230">

##### 微信支付

<img src="art/wechat_qr.png" width="230">

##### PayPal

sumimakito#hotmail.com (replace # with @)


### Copyright &amp; License 版权信息与授权协议

Copyright 2014-2016 Sumi Makito

Licensed under Apache License 2.0 License.

```
Copyright 2014-2015 Sumi Makito

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[LICENSE 授权许可文件](LICENSE)

[NOTICE 授权声明](NOTICE)
