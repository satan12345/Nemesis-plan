""""
break
 break 可以用来立即退出循环语句(包括else)

continue
continue 可以用来跳过当次循环

pass :
pass 用来在判断或者循环语句中占位

"""

i = 0
while i < 5:

    i += 1
    if i == 2:
        #         break
        continue
    print("i=", i)


else:
    print('循环结束')

i = 0
if i < 5:
    pass

