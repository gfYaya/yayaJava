package cn.test.generic;

import java.util.List;
import java.util.Map;

/**
 * Created by Yaya on 2017/4/27.
 * Thinking in Java ,version 4 ,Page 363
 */
public class LimitOfInference {
    static void f(Map<String, List<? extends Object>>  petPeople){
        return ;
    }

    public static void main(String[] args) {
        Class cls=New.class;
        System.out.println(cls.getName());
        f(new New().<String, List<? extends Object>>map());
        //f(new New().map());  //Error   等同于 ArrayList<Object> list = new ArrayList<String>();报错?
    }
}
