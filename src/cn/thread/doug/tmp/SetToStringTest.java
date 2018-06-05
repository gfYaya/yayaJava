package cn.thread.doug.tmp;

import java.util.HashSet;
import java.util.Set;

public class SetToStringTest {
    public static void main(String[] args) {
        Set<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(2);
        System.out.println("set:" + set);
    }
}
