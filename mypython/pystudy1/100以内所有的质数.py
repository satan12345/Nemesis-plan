num = 2
while num < 100:
    flag = True
    count = 2
    while count < num:
        if num % count == 0:
            flag = False
        count += 1

    if flag:
        print("质数:", num)
    num += 1
