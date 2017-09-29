package cn.my.yaya.test4justjava.generic;

/**
 * Created by Yaya on 2017/4/26.
 */
public class Pair<T> {
    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
