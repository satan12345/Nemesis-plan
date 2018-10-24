# 创建一个变量来保存名字

name = '孙悟空'
print('欢迎,' + name)
print('欢迎', name)
print('欢迎 %s' % name)
print(f'欢迎 {name}')

# 字符串的复制
a = 'abc'
a = a * 8
# 如果将字符串和数字相乘 则解释器会将字符串重复指定的次数并返回
print(a)
