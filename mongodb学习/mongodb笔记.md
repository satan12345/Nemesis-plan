# 			mongodb笔记

```java
//切换到test collection
use test 
// 显示数据库下的所有集合
show collections

// 查询集合中的数据
db.stu.find()

//向数据库中插入一个或者多个文档
db.stu.insert({name:"宇智波鼬",age:"18"})

db.stu.insert([{name:"宇智波止水",age:"23"},{nane:"宇智波佐助",age:"6"}])
db.stu.insert({name:"宇智波斑",age:85})
db.stu.find()
//插入一条数据
db.stu.insertOne()
//插入多条数据
db.stu.inserMany()

/*
    查询
    db.collection.find()
    find用来查询集合中所有符合条件的文档
    find()可以接收一个对象作为条件参数
        {}表示查询集合中的所有文档
        {field:value}
    */


db.stu.find()

db.stu.find({name:"旗木卡卡西"})
db.stu.find({age:85})
db.stu.find({name:"旗木卡卡西",age:24})
//返回的是一个数组
db.stu.find({age:85})
// 查询满足条件的第一条数据
db.stu.findOne({age:85}).name
// 查询满足条件的数量
db.stu.find().count();

/*   修改 
    db.collection.update(查询条件，新对象)
        update默认情况下回使用新对象替换就的对象
        如果需要修改指定的属性 而不是替换 要使用修改操作符来完成修改
        update 默认情况下 只会修改一个
        $set 可以用来设置文档中的指定属性
        $unset 可以用来删除文档的指定属性
        
     db.collection.updateMany()
        同时修改多个文档
     db.cloneCollection.updateOne()
        修改一个符合条件的文档
     db.cloneCollection.replaceOne()
      替换一个文档
    */
    db.stu.find({name:"宇智波鼬"})

db.stu.update({name:"旗木卡卡西"},{age:23})

db.stu.update({name:"宇智波鼬"},{$set:{age:28}},{multi:true})
db.stu.updateMany({name:"宇智波鼬"},{$set:{age:16}})

db.stu.find()
db.stu.update({_id:ObjectId("5b964d17f904ff03f3e0cf83")},{$unset:{nane:1}})

/**
    db.collection.remove
    db.collection.deleteOne
    db.collection.deleteMany
*/
db.stu.remove({_id:ObjectId("5b964d17f904ff03f3e0cf83")})

db.stu.remove({age:85})

db.stu.insert([{age:21},{age:21}])
db.stu.remove({age:21},{justOne:true})
db.stu.find()
show collections
-- 删除集合
db.stu.drop();
-- 删除数据库
db.dropDatabase()


use test
show dbs
show databases
db.users.insert({name:"名人",age:16})
show dbs
show collections
db.users.find()

db.users.insert({name:'卡卡西',age:26})
//查询文档的数量
db.users.count()
//查询满足条件的数量
db.users.find({name:"卡卡西"}).count()

db.users.update({name:"卡卡西"},{$set:{skill:"雷切"}})

db.users.replaceOne({name:"名人"},{name:"宇智波鼬"})

db.users.update({name:"卡卡西"},{$unset:{skill:2}})
// mongodb的文档也可以是一个文档 当一个文档的属性是另一个文档 则另一个文档是一个内嵌文档
db.users.update({name:"卡卡西"},{$set:{hobby:{citys:['AA','BB','CC'],movies:['A','B','C']}}})
db.users.update({name:"宇智波鼬"},{$set:{hobby:{movies:['C','D','E']}}})

//mongodb支持通过内嵌文档的属性查询 如果要查询内嵌文档 可以通过.的形式来匹配 如果要通过内嵌文档来对文档进行查询 此时属性必须使用引号
db.users.find({'hobby.movies':"A"})
db.users.find()
//$push 用于向数组中添加一个新的元素
//$addToSet  向数组中添加一个新元素  如果数组中已经存在该元素 则不会添加
db.users.update({name:'宇智波鼬'},{$push:{'hobby.movies':'F'}})

db.users.update({name:"宇智波鼬"},{$addToSet:{'hobby.movies':'G'}})

db.users.deleteOne({"hobby.citys":"A"})

db.users.find()

db.users.remove()

db.users.drop()
for(var i=0;i<=20000;i++){
db.numbers.insert({num:i});
}
var arr=[];
for(var i=20001;i<=40000;i++){
arr.push({'num':i});
}
db.numbers.insertMany(arr);
db.numbers.count()

db.numbers.find({num:500})
db.numbers.find({num:{$eq:500}})
db.numbers.find({num:{$gt:500}}).limit(30);

db.numbers.find({num:{$lt:30}})

db.numbers.find({num:{$gt:40,$lt:50}})

db.numbers.find().limit(10);



mongodb 会调整skip limit 的位置
db.numbers.find().skip(10).limit(10);

db.createCollection('wifeAndHusband')





use study
db.wifeAndHusband.insert({name:"黄蓉",husband:{name:"郭靖"}})

db.wifeAndHusband.find()
db.wifeAndHusband.find({"husband.name":"郭靖"},{"name"})




```