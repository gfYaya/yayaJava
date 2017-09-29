package cn.my.yaya.test4justjava.generic;

import java.util.Date;

/**
 * Created by Yaya on 2017/4/26.
 */
public class DateInter extends Pair<Date>{
    @Override
    public Date getValue() {
        return super.getValue();
    }

    @Override
    public void setValue(Date value) {
        super.setValue(value);
    }

    public static void main(String[] args) {
        DateInter dateInter = new DateInter();
        dateInter.setValue(new Date());
        //dateInter.setValue(new Object()); //Error 编译器 使用桥方法,但是不支持强转!

    }
}
