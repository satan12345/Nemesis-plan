package com.able;

/**
 * @author jipeng
 * @date 2019-03-25 10:43
 * @description
 */
public class Sum {

    public  static  int sum(int[] arr){
        return sum(arr,0);
    }
    private static  int sum(int[]arr, int i){
        if (i==arr.length-1) {
            return arr[i];
        }
        return arr[i]+sum(arr,i+1);
    }
    public static void main(String[] args){
      int[] nums={1,2,3,4,5,6,7,8};
        System.out.println(sum(nums));
    }
}

