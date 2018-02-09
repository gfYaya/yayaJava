package jvm.my.yaya.other;

/**
 * 引用和基本类型变量，如果生命在方法中，就是局部变量存在于stack
 * 如果是生命在类下，就是成员变量，存在于heap中.
 * 如果栈帧frame的操作数栈
 */
public class HeapFieldVariableInOperandStack {
    public int a;

    public int add(int i ,int j){
        return i+j;
    }

    public static void main(String[] args) {
        HeapFieldVariableInOperandStack t = new HeapFieldVariableInOperandStack();
        t.a = 10;
        int b = 20;
        int result = t.add(t.a,b);
    }
}
