"""
"""
print("=" * 10, "欢迎使用员工管理系统", "=" * 10)
emps = ['\t孙悟空\t5000\t男\t花果山', '\t猪八戒\t3500\t男\t高老庄']
while True:
    print("请选择要做的操作:")
    print("\t1.查询员工")
    print("\t2.添加员工")
    print("\t3.删除员工")
    print("\t4.退出系统")
    result = input('请选择(1-4):')
    print("=" * 20)
    if result == '1':
        print("序号\t姓名\t年龄\t性别\t住址")
        index = 1
        for ele in emps:
            print(str(index) + ele)
            index += 1
    elif result == '2':
        # 添加员工
        name = input('请输入要添加的员工的姓名：')
        age = input('请输入员工的年龄：')
        gender = input('请输入员工性别：')
        address = input('请输入员工住址')
        info = "\t" + name + "\t" + age + "\t" + gender + "\t" + address
        choose = input(f'是否确定添加用户{name}(y/n)')
        if choose == 'y':
            # emps.append(info)
            emps.append(f"\t{name}\t{age}\t{gender}\t{address}")
            print('用户添加成功')
        else:
            print('插入已经取消')
            pass
        pass
    elif result == '3':
        # 删除员工
        num = input('请输入要删除的序号')
        index = 1
        for node in emps:
            if num == str(index):
                # del emps[index - 1]
                emps.pop(index - 1)
                print('删除成功')

    elif result == '4':
        print('退出成功')
        break
    else:
        print('输入有误,请重新选择')
    print("=" * 20)
