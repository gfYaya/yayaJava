package cn.test.generic;

import java.util.Date;

/**
 * Created by Yaya on 2017/4/26.
 */
public class DateInter1 extends Pair1 {

    public Object getValue() {
        return this.value;
    }

    public void setValue(Date value) {
        this.value = value;
    }

    public static void main(String[] args) {
        DateInter1 dateInter=new DateInter1();
        dateInter.setValue(new Date());
        dateInter.setValue(new Object());  //也就是说支持父子类之间的强转,但是为何泛型却不行?
    }
}
