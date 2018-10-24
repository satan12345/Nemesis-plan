# 逻辑运算符
# not  and  or
# not 逻辑非  对符号右侧的值进行非运算
#  对于bool值 非bool值会对其进行取反操作  True 转成False False 转成True
#   对于非Bool值 非boolean值会对其转换成bool值 然后再取反

# and 逻辑与
# or 逻辑或

a = True
print(a)
print(not a)

print(a)


True and print('看得见')

False and print('懒得加')
# 非 波 Boolean值得与或运算
# 当我们对非bool值进行与或运算时 Python 会将其当做bool值运算 最终会返回原值
result = 1 and 2

print(result)

result = 2 and 1
print(result)
