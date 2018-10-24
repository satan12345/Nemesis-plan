width = 1

while width <= 9:
    height = 1
    while height < (width + 1):
        print(f"{height}*{width}={width*height}  ",end="")
        height += 1
    print("")
    width += 1
