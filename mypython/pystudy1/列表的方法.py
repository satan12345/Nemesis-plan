"""
列表的方法
 append 向列表的最后添加元素
 insert 指定位置插入元素
 extend 扩展
 clear 清空序列
 pop  删除指定位置的元素 并将删除的元素返回

"""
boys = ['孙悟空', '猪八戒', '沙和尚', '猪八戒']
print("原列表", boys)
#
# boys.append('唐僧')
# print('新列表', boys)
# copy = boys.copy()
# print('拷贝', copy)
# copy.clear();
# print('clear', copy)
# copy.insert(0, '二郎神')
# print('insert', copy)
#
# copy.insert(0, '玉皇大帝')
# print(copy)
#
# copy.extend(['123', '456'])
# print("extend", copy)
# copy.extend('789')
# copy += ['你好', '卡卡西']
# print(copy)
#
# node = boys.pop()
# print(node)
# print(boys)

"""
remove 删除指定值得元素  如果相同的元素有多个 则只会删除第一个

"""
# boys.remove('沙僧')
print(boys)
boys.reverse();
print(boys)

"""
sort 对列表中的元素进行排序
"""

mylist = ['a', 'b', 'd', 'c']

mylist.sort(reverse=True)
print(mylist)
