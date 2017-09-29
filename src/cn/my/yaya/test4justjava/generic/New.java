package cn.my.yaya.test4justjava.generic;

import java.util.*;

/**
 * Created by Yaya on 2017/4/27.
 * Thinking in Java , version 4 , P362
 */
public class New {
    public  <K, V> Map<K, V> map(){
        return new HashMap<K ,V>();
    }

    public static <T> List<T> list(){
        return new ArrayList<T>();
    }

    public static <T> LinkedList<T> lList(){
        return new LinkedList<T>();
    }

    public static <T> Set<T> set(){
        return new HashSet<T>();
    }

    public static <T> Queue<T> queue(){
        return new LinkedList<T>();
    }
}
