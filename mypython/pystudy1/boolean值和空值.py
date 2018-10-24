# boolean
'''
 bool主要用来做逻辑判断
 bool一共有两个值 True False
    True 表示为真  False表示为假
'''
a = True

b = False
print(a)
print(b)

# bool值实际上也是属于整形  True就相当于1 False就相当于0
print(1 + True)

print(1 + False)

# None 表示不存在
c = None
print(c)

# 类型检查  通过类型检查 可以检查 变量值得类型
d = 123
e = '123'
print(d)
print(e)

# type
print(type(d))
print(type(e))

# 对象 Object
# 程序运行中  所有的数据都是存储到内存中 然后再运行

# 对象的结构
# 每个对象豆包保存三种数据
#  Id(标识)
#  id 用来标识对象的唯一性 每一个对象都有唯一的id
#  可以通过id()函数 查看多想的id
# id是由解析器生成的 在Cpython中 ID就是对象的内存地址
print(id(d))

# type 类型
# 类型决定了对象有哪些功能
# python 是一门强类型的语言 对象一旦创建 类型便不能修改
print("类型：", type(d))

# value 值
# 值就是对象中存储的具体的数据
# 对于有些对象的值是可以改变的
# 对象分成两类 可变对象 不可变对象
# 可变对象的值可以改变
# 不可变对象的值不能改变 之前学习的对象都是不可变对象

# 变量和对象的关系
# 对象并没有直接存储到变量中 在python中变量更像是给对象起了一个别名
# 变量中存储的不是对象的值 而是对象的id(内存地址)
# 当我们使用变量时 实际上就是通过对象的id 查找对象
# 变量中保存的对象 只有在重新赋值的时候才会改变
# 变量与变量之间是相互独立的

a = 123;
print(id(a))
b = a
print(id(b))

# 类型转换
# 所谓的类型转换 就是将一个类型的对象转换成另一个类型的对象

a = 'hello'
b = 123
# print(a + b)
# 不同类型之间不能进行运算
# 类型转换不是改变对象本身的类型 而是将对象的值转换成新的对象 根据当前对象的值创建一个新的对象

# 类型转换的4个函数 int() float str()  bool() 不会对原来的对象产生影响
# 如果需要对原来的对象进行修改 则需要进行重新赋值
# int() 可以用来将其他的对象转成整数
# 规则 ：
# bool： True->1 False->0
# 浮点数： 直接取整 省略小时点后面的内容
# 字符串： 合法的整数字符串 直接转换成对应的数字
#        如果不是一个合法的整数字符串 则报错

# float 与int 基本一致 不同的是他会将对象转换为浮点数
# str() 将对象转换成字符串
# True->'true' False-->'False'
# 123-->'123'
# bool() 可以将对象转换为布尔值 任何对象都可以转换成布尔值
# 所有的表示空性的对象都会转换为False 其他的转换为True
# 哪些表示空性 0 None '' ...
c = str(b)
print(a + c)

a = True
print("a的类型是:", type(a))
b = int(a)
print("b的类型是:", type(b))
print(b)

a = 123
print("a的类型是:", type(a))
a = str(a)
print("a的类型是:", type(a))

a = 11.6
print("a的类型是:", type(a))
a = int(a)
print("a的类型是:", type(a))
print("a=", a)

a = 11
print("a的类型是:", type(a))
a = float(a)
print(a)
print("a的类型是:", type(a))

a = 123
print("a的类型是:", type(a))
a = str(123)
print("a的类型是:", type(a))
print(a)

a = 1
print(bool(1))
a = 0
print(bool(a))
a = ''
print(bool(a))

a = None
print(bool(a))

# 运算符
# 运算符可以对一个值或者多个值进行各种操作
# 比如+-*/
# 算术运算符
# + - * /  //
# // 整除
# 赋值运算符
# 比较运算符
# 逻辑运算符
# 条件运算符(三元运算符)

a = 10 + 5
print(a)
a = 5 / 2
print(a)

a = 10 / 3
print(a)

# 整除 只会保留计算后的整数位
a = 10//3

print(a)

a = 5//2
print(a)

# 次幂运算
a = 2**3

print(a)

print(16**0.5)

# % 取模
print(11 % 4)


