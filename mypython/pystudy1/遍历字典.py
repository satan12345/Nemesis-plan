d = {'name': '旗木卡卡西',
     'age': 23,
     'gender': '男'}

""""
遍历字典
keys 该方法会返回字典的所有的 key
values 返回字典的所有values
items 该方法返回字典中的所有的项
    他会返回一个序列 序列中包含有双值子序列
"""
for i in d.keys():
    print('key=', i)
    print(d[i])

print(d.keys())
print(d.values())
print(d.items())

for k, v in d.items():
    print('k=', k)
    print('v=', v)

for item in d.items():
    print('type=',type(item))