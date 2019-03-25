package com.able;

public class Main {

    public static void main(String[] args) {

        Array array=new Array(20);
        for (int i = 0; i < 10; i++) {
            array.addLast(i);
        }

        System.out.println(array);
//        array.add(1,100);
//        System.out.println(array);
//
//        System.out.println("是否包含"+array.contains(-1));
//        System.out.println("index="+array.find(-1));
        array.remove(0);
        System.out.println(array);
    }
}
