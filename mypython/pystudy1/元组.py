"""
元组 ：tuble
元组是一个不可变序列
操作元组的方法基本上和列表是一致的
所以在操作元组时 就可以把元组当成一个不可变的列表就ok了
一般当我们需要数据不改变时 就用元组 其余情况使用列表
"""
# 创建元组
mytuble = ()  # 创建一个空元组
print(mytuble)
print(type(mytuble))

mytuble = (1, 2, 3, 4, 5)  # 创建一个5个元素的元组
# 元组是不可变对象 不可以为元组中的元素从新赋值

print(mytuble)

"""
当我们的元组不为空的时候 可以省略括号
如果元组不是空元组 他里面至少有一个,
"""
mytuble = 10, 20, 30
print(type(mytuble))

print(mytuble)

# 元组的解包（解构）
# 解包：指就是讲元组当中的每一个元素都赋值给一个变量
a, b, c = mytuble
print(a)
print(b)
print(c)

# 交换 a b的值 这时我们就可以利用元组的 解包
a = 100
b = 300
print(a, b)

a, b = b, a
print(a, b)
# 在对一个元组进行解包时 变量的数量必须和元组中元素的数量一致
# 也可以在变量前添加一个* 这样变量就会获取元组中所有剩余的元素
mytuble = 10, 20, 30, 40

a, b, *c = mytuble
print(c)
print(type(c))

a, *b, c = mytuble
print(b)

a,b,*c ='hello world'
print(a)
print(b)
print(c)