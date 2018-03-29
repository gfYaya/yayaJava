package jvm.my.yaya.other;

/*
   局部变量都是存储在stack中(frame栈帧中),全局变量都是存储在heap中.
   如果一个方法调用的是全局变量的参数会怎么样?
 */
public class FieldVariableTest {
    public int a = 10;  //field variable

    public int add(int i, int j){
        return i+j;
    }

    public static void main(String[] args) {
        FieldVariableTest t = new FieldVariableTest();
        int b = 11; //local variable
        int result = t.add(t.a, b);
    }
}
