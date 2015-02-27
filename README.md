# QuickKV

Lightweight &amp; Easy-to-use Key-Value Library for Android Projects.

为Android项目提供的轻量且易用的键值数据库

[![BuildStatus](https://travis-ci.org/SumiMakito/QucikKV.png)](https://travis-ci.org/SumiMakito/QuickKV)

### Preface 前言

Map and List is too complex to initialize.Actually, the things we only need to focus on are the key and the value. So ... QuickKV comes up!

Map和List的初始化很复杂。事实上，我们只需要关注键与值就够了。于是，QuickKV诞生了。

### Features 特性

* Allow nearly all types key and value 几乎支持所有类型的键与值

* High performance 高性能

* Multi-database management 多数据库管理

* Persistable 可持久化

* Support AES256 encryption(experimental) 支持AES256加密(实验功能)

### TODO 目标

* AES256 Encryption(Improvement)

### Download Demo 下载演示

[Dropbox](https://www.dropbox.com/s/53b86j9xuhpw9f1/QuickKVDemo.apk?dl=0)

[Google Drive](https://docs.google.com/file/d/0B_-0A4yjEnvMMVFLV0hfVDNUbUU/edit?usp=docslist_api)

[百度云](http://pan.baidu.com/share/link?shareid=1713766086&uk=1479848638)

### Usage 用法

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

#### Operate database 操作数据库

* Add a key-value data 添加一条 键-值 数据

```java
//For common database
kvdb.put(k,v); //k and v are type of Object
//For persistable database
pkvdb.put(k,v); //k and v must be type of String/Integer/Long/Double/Float/Boolean/JSONObject/JSONArray
```

* Get value of the given key 通过键取得值

> This method will return a Object, you can cast it to its original type later.
> 这个方法将返回一个对象，你可以在之后使用形态转换将该对象转为原始类型。

```java
//For common database
kvdb.put(k); //k is type of Object
//For persistable database
pkvdb.put(k); //k must be type of String/Integer/Long/Double/Float/Boolean/JSONObject/JSONArray
```

* Remove data 移除数据

```java
//For common database
kvdb.remove(k); //k is type of Object
//For persistable database
pkvdb.remove(k); //k must be type of String/Integer/Long/Double/Float/Boolean/JSONObject/JSONArray
```

* Persist 持久化

> This method will save persistable database to local storage. In this way, you can read your data and reuse them at any time. QuickKV will automatically load the saved database.
> 这个方法将会将可持久化数据库从内存保存至文件存储器，这样一来你就可以在任何时候读取与复用你持久化后的数据。QuickKV将自动载入已保存的持久化数据库。

```java
//Only persistable database has this method
pkvdb.persist();
```

* Sync 同步

*For multi-instance purpose, but we don't recommend you to do so. Frequently synchronization will affect the performance. 为多实例目的而设计，但不推荐使用。频繁地同步操作将会影响性能。*

> This method will synchronize current database from persisted version(Database dbName must be the same or both are default).
> 这个方法将会使数据从已持久化版本同步至当前数据库，并覆盖当前内容(数据库名称必须相同或均为默认)。

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

* Clear 清除数据

> This method will clear all data in the specified database.
> 这个方法将清除指定数据库中所有的数据。

```java
kvdb.clear();
pkvdb.clear();
```

* Encryption 加密

*Experimental 实验功能*

> Use this method to set an encryption key to protect your persistable database file.
> 使用这个方法设置一个字符串密钥来保护你的持久化数据库文件

*Once an encryption key is set, you cannot change it or disable it.*
*一旦你设定了一个字符串密钥，密钥将不可以被更改或被删除。*

```
Encrypt method: AES256
```

```java
//First time: Call this method on an empty database, or QuickKV will fail to load it.
//Later: Call this method before operations, or modifications will lost!
pkvdb.setEncryptionKey("Your Encryption Key");
```

* Trick: Empty a persisted database 技巧:清空一个已持久化的数据库文件

```java
pkvdb.clear(); //Clear it
pkvdb.persist(); //Then persist it
```

#### Multi-database management 多数据库管理

* Release database 释放数据库

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

* Is database opened? 这个数据库被打开了吗?

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