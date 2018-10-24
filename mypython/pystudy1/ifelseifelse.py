""""
if -elif-else
    if 条件表达式：
        代码快
    else if 条件表达式:
        代码块
    ....
    else:
        代码块
执行流程：
if elif else 语句在执行时 会自上向下一次对条件表达式进行求值判断
    如果表达式的结果为true 则执行当前的代码块 然后语句结束
    如果表达式的结果为false 则继续向下判断 直到找到true为止
"""
a = 5
if a > 1:
    print('1')
elif a > 1:
    print('2')
else:
    print(3)
