package cn.my.yaya.other.accessPriority.a;

/* https://blog.csdn.net/u010876691/article/details/72724415
 *
 */
public class Aclass {
    private int a1 = 10;
    int a2 = 20;
    protected int a3 = 30;
    public int a4 = 40;

    private void showA1(){
        System.out.println(a1);
    }

    void showA2(){
        System.out.println(a2);
    }

    protected void showA3(){
        System.out.println(a3);
    }

    public void showA4(){
        System.out.println(a4);
    }
}
