"""
集合和列表非常相似
不同点：
    集合中只能存储不可变对象
    集合中存储的对象是无序的( 不是按照元素的插入顺序保存的)
    集合中不能出现重复的元素

使用{}创建集合
使用set()函数来创建集合
"""

s = {1, 2, 3, 67, 9, 1, 1, 1}
# <class 'set'>
print(type(s))
print(s)
"""
可以通过set()将序列与字典转换成集合
使用 set() 将字典转换成集合 只会包含字典中的键
"""
s1 = set([1, 2, 1, 3333, 444, 52, 22])

print(s1)

s2 = set({'name': '卡卡西', 'age': 23})
print(s2)

s3 = set((1, 2, 4, 2, 3))
print(s3)

# 使用 in not in 检查元素是否在集合中
print(1 in s3)

# len 元素的数量
print(len(s3))

# add() 向集合中添加元素
result = s3.add(10)
print(s3)
print(result)

# update 将一个集合中的元素添加到当前的集合中
# update() 可以传递序列或者字典作为参数 字典只会使用key

s = set('hello')
s1 = set('world')
s.update(s1)
s.update((10, 20, 30, 40))
s.update([50, 60])
s.update({70: '卡卡西', 80: '宇智波'})

print(s)
# 随机删除并返回集合中的一个元素
s.pop()
print(s)
# 删除集合中指定元素
s.remove(10)
# 清空集合
s.clear()


