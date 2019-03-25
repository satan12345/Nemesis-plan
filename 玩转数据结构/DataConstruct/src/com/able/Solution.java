package com.able;

/**
 * @author jipeng
 * @date 2019-03-25 10:07
 * @description
 */
public class Solution {

    public ListNode removeElements(ListNode head,int val){
        while (head!=null&&head.val==val) {
            ListNode delNode=head;
            head=head.next;
            head.next=null;

        }
        return null;
    }
}
class ListNode{
    int val;
    ListNode next;
    ListNode(int x){
        val=x;
    }
}

