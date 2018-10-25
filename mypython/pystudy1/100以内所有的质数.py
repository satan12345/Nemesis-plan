""""
模块 通过模块可以对python进行扩展
引入一个time模块 来统计程序的执行时间
"""
from time import *

start = time()
num = 2
while num < 100000:
    flag = True
    count = 2
    while count <= num**0.5:
        if num % count == 0:
            flag = False
            break
        count += 1


    if flag:
        pass
        # print("质数:", num)
    num += 1
end = time()

print('耗时:', (end - start))
