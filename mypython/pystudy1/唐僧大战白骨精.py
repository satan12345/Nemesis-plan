""""
小游戏 唐僧大战白骨精
    1 身份选择
        1 显示提示信息
            欢迎xxx光临
            请选择你的身份
                1 xxxx
                2 xxx
                请选择
        2 根据用户选择来分配身份




"""

print("=" * 20, "欢迎来到游戏<<唐僧大战白骨精>>", "=" * 20)
print("请选择你的身份:")
print("\t 1 唐僧")
print("\t 2 白骨精")

role = input("请选择(1-2):")
print("-" * 40)
if role == "1":
    print("你已经选择唐僧,恭喜你以唐僧的身份进行游戏!")

elif role == "2":
    print("你竟然选择了白骨精,太不要脸了,你将以->唐僧<-的身份进行游戏")
else:
    print('你的输入有误,系统将自动分配身份,你将以->唐僧<-的身份进行游戏')

role = 1
# 创建变量 保存玩家的生命值与攻击力
player_life = 2
player_attack = 2
# boss的生命值与攻击力
boss_life = 10
boss_attack = 10
# print("你的身份是->唐僧<-,你的攻击力是:", player_attack, "你的生命值是:", player_life)
print("-" * 40)
print(f"你的身份是->唐僧<-,你的攻击力是 :{player_attack},你的生命值是 :{player_life}")
print("-" * 40)
while True:

    print("请选择你要做的操作:")
    print("             1 练级")
    print("             2 打BOSS")
    print("             3 逃跑")
    opera = input("请选择(1-3):")

    if opera == "1":
        player_life += 2
        player_attack += 2
        print(f"恭喜你升级了,你现在的生命值是{player_life},你的攻击力是{player_attack}")
        print("-" * 40)
    elif opera == "2":
        # 玩家攻击 boss
        boss_life -= player_attack
        if boss_life <= 0:
            print(f"->唐僧<-攻击了白骨精,白骨精受到{player_attack}伤害,重伤死了,->唐僧<-赢得了胜利")
            break
        player_life -= boss_attack
        if player_life <= 0:
            print(f"->白骨精<-攻击了玩家,玩家受到{boss_attack}伤害,重伤死了")
            break
    elif opera == '3':
        print("唐僧想跑,被白骨精追上来一掌拍死了白骨精")
        break
    else:
        print('你的输入有误 请重新输入')

