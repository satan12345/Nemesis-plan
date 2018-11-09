"""
可变对象
    每个对象中都保存三个数据
    id（标识）
    type（类型）
    value（值）
    可变对象指的是 对象的值可变
"""
mylist = [1, 2, 3]
print("id", id(mylist))
print("type", type(mylist))
print('value', mylist)

mylist[0] = 10

print("id", id(mylist))
print("type", type(mylist))
print('value', mylist)

"""
== != is is not 
== != 比较的是对象的值是否相等
is is not 比较的是对象的id是否相等
"""

a = [1, 2, 3]
b = [1, 2, 3]
print(id(a))
print(id(b))
# a 与 b不是同一个对象 内存地址不同 使用 is会返回false

print("a is b", a is b)
print('a==b', a == b)

"""
字典(dict)
字典属于一种新的数据结构 称为映射
字典的作用和列表类似 都是用来存储对象的容器
列表存储数据的性能很好 但是查询数据的性能很差
在字典中 每一个元素都有一个唯一的名字 通过这个唯一的名字 可以快速的查询到指定的元素
在查询元素时 字典的效率是非常快的
在字典中也是可以保存多个对象 每个对象都会有一个唯一的名字
这个唯一的名字我们称其为key 这个对象我们称其为value 
所以字典 我们也称为饺子 键值对(key/value)结构
每个字段都可以有多个键值对 而每个键值对 我们称其为一项 item
使用{}来创建字典
{key:value,key:value,.......}
字典的key 可以是任意的不可变对象(int str bool tuble) 一般都是使用的是字符串
字典的键是不能重复的 如果出现重复的 后面的会替换掉前面的

如果使用了字段中不存在的键 会报错
"""

d = {}  # 创建一个空字典
d = {1: 'tom',
     2: 'jac'
     }
print(type(d))

print(d)
# 需要根据键获取值
for i in range(2):
    name = d[i]
    print(name)
