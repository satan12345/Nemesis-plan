"""
创建字典
使用{}
语法：{k1:v1,k2:v2,.......}
使用idct()函数来创建字典
"""
# 每个参数都是一个键值对 参数名是键  这种方式创建的字段 key都是字符串
d = dict(name="suwukong", age=18, gender='男')

print(d)

# 也可以将一个包含有双值子序列的 序列转换为字典
# 双值序列 序列中只有两个值[1,2] ('a',3) 'ab'
# 子序列 如果序列中的元素也是序列 那么称这个元素为子序列
# [(1,2),(3,4)]
d = dict([('name', '孙悟空'), ('age', 18)])
print(d)

e = dict([('name', '孙悟饭')])

print("e的长度", len(e))

# in not in
# 检查字典中是否包含指定的键

print('name' in e)

# 获取字典中的值 根据键来获取值
# 语法 d[key]
print(e['name'])

# 通过[]获取值时 如果键不存在 会抛出异常

print("e.get", e.get('name'))
print("e.get", e.get('name1', '暂无数据'))

# 修改字典 key 存在则修改值 key 不存在 则新增值
e['name'] = '旗木卡卡西'
e['age'] = 23
print(e)
# 如果key已经存在于字典中 则返回key的值 不会对字典做任何操作
# 如果key不存在 则向字典中添加这个key  并设置value
a = e.setdefault('name', "zhubajie")
print(e)
print(a)
"""
update([other]) 
Update the dictionary with the key/value pairs from other, overwriting existing keys. Return None.
"""
d1 = {'a': 1, 'b': 2, 'c': 3}
d2 = {'c': 4, 'e': 5, 'f': 6}
d1.update(d2)
print(d1)
# 删除字典中的最后一个键值对
# 删除之后 他会将删除的key-value作为返回值返回
# 返回的是一个元组 元组中有两个元素 第一个元素是删除的key 第二个元素时删除的value
del d1['a']
print(d1)
# 当删除空字典 会抛出异常
reslut = d1.popitem()
print(d1)
print(reslut)
print(type(reslut))

# pop 根据key删除字典中的想
"""
pop(key[, default]) 
If key is in the dictionary, 
remove it and return its value,
 else return default. If default is not given and key is not in the dictionary, a KeyError is raised.
 根据key 删除字典中的key-value
 会将被删除的value返回 删除不存在的key 会抛出异常 如果指定了默认值 再删除不存在的默认值 不会报错 而是返回默认值
"""
d1 = {'a': 1, 'b': 2, 'c': 3}
reslut = d1.pop('a')
print(reslut)
reslut = d1.pop('a1', "default")
print(reslut)

# 清空字典
# d1.clear()
# print(d1)
d3 = d1.copy()
print(d3)
d1['b'] = '卡卡西'
print(d3)
print(d1)
print(id(d1))
print(id(d3))
""""
copy() 该方法用于对字典进行浅复制
复制以后的对象 和原对象是独立的 修改一个不会影响到另一个
浅复制会简单复制对象内部的值 如果值也是一个可变的对象 这个可变对象不会被复制
"""
