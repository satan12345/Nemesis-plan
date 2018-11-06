student = ['孙悟空', '猪八戒', '沙和尚', '唐僧', '蜘蛛精', '白骨精','孙悟空']

# + *
# 可以将两个列表拼接
mylist = [1, 2, 3] + [4, 5, 6]

print(mylist)

# * 可以对列表进行重复
print(student * 2)

"""
 in 和 not in 
 in 用来检测指定元素是否在指定列表中
 not in 用来检测指定元素是否不在列表中
"""

print('沙和尚' in student)
print('牛魔王' in student)

print('沙和尚' not in student)
print('牛魔王' not in student)

"""
len() 用来获取列表中元素的长度
min() 用来获取列表中元素的最小值
max() 用来获取列表中元素的最大值
"""
arr = [1, 2, 3, 4, 5]
print(len(arr))
print(min(arr))
print(max(arr))
print(min(student))

"""
两个方法
方法其实就是和对象关系紧密的函数
index() 获取元素在列表中的位置
count() 获取袁术在列表中的数量
"""
print(arr.index(2))
print(student.index('孙悟空'))
# 获取列表中没有的元素 会抛出异常
#print(student.index('牛魔王'))
print(student.count('孙悟空'))
print(student.count('数量'))


