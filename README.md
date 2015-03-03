# QuickKV [![Build Status](https://travis-ci.org/SumiMakito/QuickKV.svg?branch=master)](https://travis-ci.org/SumiMakito/QuickKV)

Lightweight &amp; Easy-to-use Key-Value Library for Android Projects.

为Android项目提供的轻量且易用的键值数据库

*English &amp; Chinese API Documentation Available! 含有中文/英文API文档*

### Preface 前言

Map and List are too complex to initialize. Actually, the things we only need to focus on are the key and the value. So ... QuickKV comes up!

Map和List的初始化很复杂。事实上，我们只需要关注键与值就够了。于是，QuickKV诞生了。

* HashMap &amp; JSON

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
PersistableKeyValueDatabase pkvdb1 = quickKv.getPersistableKVDB("Foo");
pkvdb1.put("Key", "Value");
pkvdb1.persist();
//Done! Saved to local storage!
//完成！已保存至本地存储器！
//Let try to load this saved database!
//让我们来试试载入这个保存好的数据库！
PersistableKeyValueDatabase pkvdb2 = quickKv.getPersistableKVDB("Foo");
pkvdb2.get("Key");
//Output: "Value"
//输出: "Value"
```

> Do more tasks in fewer words. This QuickKV!
> 代码更短，却能完成多个任务，这就是QuickKV！

### Current version 当前版本

##### 0.7 

### Features 特性

* Allow nearly all type of keys and values 几乎支持所有类型的键与值

* High performance 高性能

* Multi-database management 多数据库管理

* Persistable 可持久化

* Support AES256 encryption(experimental) 支持AES256加密(实验功能)

### TODO 任务与目标

* Improve performance 提高处理性能

* Solutions for processing huge data 考虑处理巨量数据的方案

### Download 下载

##### JAR File JAR文件 (v0.6+)

[CR-α 代码仓库](http://repo.keep.moe/static/?dir=QuickKV)

##### Demo 演示 (v0.6)

[Dropbox](https://www.dropbox.com/s/53b86j9xuhpw9f1/QuickKVDemo.apk?dl=0)

[Google Drive](https://docs.google.com/file/d/0B_-0A4yjEnvMMVFLV0hfVDNUbUU/edit?usp=docslist_api)

[百度云](http://pan.baidu.com/share/link?shareid=1713766086&uk=1479848638)

### Usage 用法

*First add jar file to your build path. 首先在Build Path中加入JAR文件。*

#### Import 导入

```java
import sumimakito.android.quickkv.*;
```

#### Initialize 实例化

```java
QuickKV quickKv = new QuickKV(this);
```

#### Get database 取得数据库

*Differences between databases:*

Database|Types Allowed|Persistable
--------|-------------|-----------
KeyValueDatabase(Common KVDB)|All|No
PersistableKeyValueDatabase|String,Integer,Long,Float,Double,Boolean,JSONArray,JSONObject|Yes

```java
//get Default common database
kvdb = quickKv.getDefaultKVDB();
//get Persistable database
pkvdb = quickKv.getDefaultPersistableKVDB();

//Or get a custom-name/alias database
kvdb = quickKv.getKVDB("dbAlias");
pkvdb = quickKv.getPersistableKVDB("dbName");
```

#### QKVCallback 简易回调 (0.7+)

> To be serious, this is not the real callback. The only reason for its existence is: you can easily track the status of an operation.
> 认真地说，这并不是一个真正意义上的回调。它存在的唯一理由是：你可以通过它来简单地追踪操作的状态。

##### (En)/(Dis)able 启用/禁用

> If the callback is unnecessary, you can disable it. Callback is disabled by default. 如果你不需要这个简单回调，你可以禁用它。禁用是默认设置的。

```java
//You can find the method setCallbackEnabled(boolean bool) in *Persistable* named classes.
//For common kvdb this method is unnecessary.
//If you forget to enable it, operations which return callback will return null instead.
pkvdb.setCallbackEnabled(true); //Enabled it
pkvdb.setCallbackEnabled(false); //Disable it
```
##### Get Callback Information 获得回调信息

```java
//For example:
QKVCallback callback = pkvdb.put("Key", "Value");
callback.success(); //Returns true for success, false for failure.
callback.code(); //Returns a int status code. Under construction.
callback.msg(); //Returns a String value as the reason for failure.

//Style 1:
if(callback.success()){
    //Do something to celebrate. :)
}else{
    //Failed ... So sad... :(
}
//Style 2:
if(!callback.success()){
    System.out.println("Oops... "+callback.msg);
}

//More and more styles...
```

#### Operate database 操作数据库

##### Add a key-value data 添加一条 键-值 数据

```java
//For common database
kvdb.put(k,v); //k and v are type of Object
//For persistable database
pkvdb.put(k,v); //k and v must be type of String/Integer/Long/Double/Float/Boolean/JSONObject/JSONArray
```

##### Get value of the given key 通过键取得值

> This method will return a Object, you can cast it to its original type later.
> 这个方法将返回一个对象，你可以在之后使用形态转换将该对象转为原始类型。

```java
//For common database
kvdb.put(k); //k is type of Object
//For persistable database
pkvdb.put(k); //k must be type of String/Integer/Long/Double/Float/Boolean/JSONObject/JSONArray
```

##### Remove data 移除数据

```java
//For common database
kvdb.remove(k); //k is type of Object
//For persistable database
pkvdb.remove(k); //k must be type of String/Integer/Long/Double/Float/Boolean/JSONObject/JSONArray
```

##### Persist 持久化

> This method will save persistable database to local storage. In this way, you can read your data and reuse them at any time. QuickKV will automatically load the saved database.
> 这个方法将会将可持久化数据库从内存保存至文件存储器，这样一来你就可以在任何时候读取与复用你持久化后的数据。QuickKV将自动载入已保存的持久化数据库。

```java
//Only persistable database has this method
pkvdb.persist();
```

##### Sync 同步

*For multi-instance purpose, but we don't recommend you to do so. Frequently synchronization will affect the performance. 为多实例目的而设计，但不推荐使用。频繁地同步操作将会影响性能。*

> This method will synchronize current database from persisted version(Database dbName must be the same or both are default).
> 这个方法将会使数据从已持久化版本同步至当前数据库，并覆盖当前内容(数据库名称必须相同或均为默认)。

> You should decrypt the database before sync(). Or something strange will happen... 在调用sync()方法之前你应该将数据库解密，否则将发生奇怪的事情...

```java
PersistableKeyValueDatabase pkvdb1 = new QuickKV(this).getDefaultPersistableKVDB();
PersistableKeyValueDatabase pkvdb2 = new QuickKV(this).getDefaultPersistableKVDB();
pkvdb1.put("key", "value");
pkvdb1.persist();
pkvdb2.sync();
String value = pkvdb2.get("key").toString();
System.out.println(value);
//Output: "value"
```

##### Clear 清除数据

> This method will clear all data in the specified database.
> 这个方法将清除指定数据库中所有的数据。

```java
kvdb.clear();
pkvdb.clear();
```

##### Trick: Empty a persisted database 技巧:清空一个已持久化的数据库文件

```java
pkvdb.clear(); //Clear it
pkvdb.persist(); //Then persist it
```

#### Multi-database management 多数据库管理

##### Encryption 加密

> Due to the known issues, this feature might be removed in the future.
> 由于已知的逻辑性问题，这个功能可能会在将来被移除。

*Experimental 实验功能*

> Use this method to set an encryption key to protect your persistable database file.
> 使用这个方法设置一个字符串密钥来保护你的持久化数据库文件。

*Default Persistable KVDB CANNOT be encrypted! You can only encrypt a custom Persistable KVDB. 默认提供的可持久化数据库不可加密。只能加密自定义数据库。*

*Persist your database before en/decrypt it, or you'll lose unsaved data! 加密前请先持久化，否则将丢失未保存的数据。*

```
Encrypt method: AES256
```

```java
//Encrypt
quickKv.encryptPersistableKVDB("dbName", "Encryption key");
//Decrypt
quickKv.decryptPersistableKVDB("dbName", "Encryption key");
```

###### Read encrypted database safely 安全地读取已加密数据库

1. quickKv.decryptPersistableKVDB("Foo", "Bar");

2. eckvdb = qucikKv.getPersistableKVDB("Foo");

3. quickKv.encryptPersistableKVDB("Foo", "Bar");

4. eckvdb.put("Key", "Value");

5. eckvdb.persist();

6. quickKv.encryptPersistableKVDB("Foo", "Bar");

##### Release database 释放数据库

> This method will release non-default database.
> 这个方法将从内存中释放非默认数据库。

*All unsaved data will be destroied.*
*所有未保存的数据都将丢失*

```java
qucikKv.releaseKVDB("dbAlias"); //Release single KVDB 
qucikKv.releasePersistableKVDB("dbName"); //Release single persistable KVDB
//Or release all...
qucikKv.releaseAllKVDB();
qucikKv.releaseAllPersistableKVDB(); 
```

##### Is database opened? 这个数据库被打开了吗?

> This method will return a boolean.
> 这个方法将返回一个布尔值。

```java
qucikKv.isKVDBOpened("dbAlias");
qucikKv.isPersistableKVDBOpened("dbName");
```

### Copyright 版权信息

Copyright (C) 2014-2015 SumiMakito(RDGroup).

Licensed under GNU GPL v3.

[Read License](LICENSE)