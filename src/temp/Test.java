package temp;

//String 依然是符号引用类型
public class Test {

    /*private static String add(int a ,String b){
        return a + b;
    }*/

    public String add(int a ,String b){
        return a + b;
    }

    public void add(){

    }

    public static void main(String[] args) {
        Void v;
        short s1 = 1;
        s1 += 1;
        //s1 = s1 + 1;



       /* Object o;
        int a = 1;
        String b = "2";
        System.out.println(add(a,b));*/
    }
}
