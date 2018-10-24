""""
循环语句
    循环语句可以使指定的代码块重复指定的次数
    循环语句分为两种 while循环 和 for循环
    while循环
    语法：
        while 条件表达式:
            代码块
    执行流程
        while语句在执行时 先会对while后的条件表达式进行求值判断
        如果判断的结果为true 则执行循环体
        循环体执行完毕 继续对条件表达式进行判断 依次类推
        直到判断结果为false 则终止循环 如果循环有对应的else  则执行else后的代码块
    循环的三个要件
        初始化表达式 通过初始化表达式初始化一个变量
        i=0
        条件表达式 条件表达式用来设置循环执行的条件
        更新表达式 修改初始化变量的值
"""

# i = 1
# while i <= 10:
#     print("i =", i)
#     i += 1
# else:
#     print('终止循环')

"""
循环语句练习
    求 100以内所有的奇数之和
"""

# total = 0
# i = 1
# while i <= 100:
#     if i % 2 != 0:
#         total += i
#     i += 1
# print('100以内所有的奇数之和为', total)

"""
求100以内所有7的倍数之和 以及个数
"""

# total = 0
# count = 0
# i = 7
# while i < 100:
#     if i % 7 == 0:
#         total += i
#         count += 1
#     i += 1
#
# # print('100以内所有7的倍数之和:%d,个数为:%d' % (total, count))
# print('总和为', total, '总数为', count)

""""
水先花数是指一个n位数(n>3) 他的每个位上的数字的n次幂之和等于他本身（例如:1**3+5**3+3**3=153）
求1000以内所有的水仙花数
"""
# num = 100
# while num < 1000:
#     # //取整除 - 向下取接近除数的整数
#     a = num // 100
#     b = num // 10 % 10
#     c = num % 10
#     # ** 幂 - 返回x的y次幂
#     if (a ** 3 + b ** 3 + c ** 3) == num:
#         print('水仙花数为:', num)
#
#     num += 1

""""
获取用户输入的任意数 判断其是否是质数 质数是只能被1和他自身整除的书  1 不是质数也不是合数
"""
num = int(input('请输入任何一个大于1的整数:'))
i = 2
flag = False
while i < num:
    if num % i == 0:
        flag = True
        break
    i += 1

if flag:
    print('不是质数')
else:
    print("是质数")

