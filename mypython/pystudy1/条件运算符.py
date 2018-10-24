# 条件运算符（三元运算符）
# 语法： 语句1 if  条件表达式 else 语句2

# print('你好') if False else print('Hello')

a = 10
b = 20

print('a的值比较大') if a > b else print('B的值比较大')

c = max(a, b)
print(c)

d = a if a > b else b
print(d)
g = 100
e = max(a, b, g)
print(e)

# 运算符的优先级 可以根据官方文档查询
