print(123)
print('hhh')
# python代码在执行的时候是按照自上向下的顺序执行的
# 通过流程控制语句 可以改成程序的执行顺序  可以让指定的程序反复执行多次
# 流程控制语句： 条件判断语句 循环语句

# 条件判断语句  if 语句
# if 条件表达式：语句

a = 4
b = 2
# 默认 情况下 if  语句只会控制紧随其后的那条语句 如果希望if语句可以控制多条语句
# 则可以在if 后面跟着一个代码块
# 代码块
# 代码块中保存着一组代码 同一个代码块中的代码 要么都执行 要么都不执行 代码块就是一种为代码分组的机制
# 如果要编写代码块 语句不能紧随在:后面 而是要卸载下一行
# 代码块已缩进开始
if a > b:

    print('a>b')
else:
    print('a<b')

if a < b:
    print(123)
    print(456)
else:
    print(789)
    print(111)

num = 18

if num > 10 and num < 20:
    print('num比10 大 比20小')
if 10 < num < 20:
    print('num比10 大 比20小')

# 缩进有两种方式 一种是使用 tab键 一种是使用空格键

# input() 函数
# 该函数用来获取用户的输入 input调用后 程序会立即暂停 等待用户输入
# 用户输入完内容以后 点击回车程序才会继续向下执行
# 用户输入完成之后 其所输入的内容会以返回值得形式返回 input的返回值是一个字符串
input = input('请输入任意内容:')
print('用户输入的内容是', input)
if input == 'admin':
    print('欢迎')
else:
    print('你不是管理员啊')
