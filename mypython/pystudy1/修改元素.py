"""
列表是可变的序列
"""

stu = ['孙悟空', '猪八戒', '沙和尚', '唐僧', '蜘蛛精', '白骨精', '孙悟空']
"""
修改列表中的元素
 通过索引来修改元素
 通过del删除元素
 
 通过切片修改列表 
 
 通过切片删除元素
 以上操作 只能支持可变序列
 可以通过list()函数将其他序列转变为list
"""
print(stu)
stu[0] = '齐天大圣'
print(stu)
print('长度为:', len(stu))

del stu[2]
print(stu)
print("长度为:", len(stu))
# 在给切片进行赋值的时候 只能使用序列
stu[0:2] = ['七天大神', '天蓬元帅', "卷帘大将", "二郎神"]
# 当我们设置了步长 我们的序列中的元素的个数必须和切片中的元素的个数一致

stu[0:0] = ['赤脚大仙']
print(stu)

del stu[0: 5]
print(stu)

stu[0:1] = []
print(stu)

a = 'hello'
print("a", a)
a = list(a)
print('a',a)