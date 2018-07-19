package jvm.my.yaya.other;

/*
 * https://mp.weixin.qq.com/s?__biz=MzU2NjIzNDk5NQ==&mid=2247484832&idx=1&sn=c2b1e39a8a339daf7bede5243ac11441&chksm=fcaedfbccbd956aa54c603dbcba1b6262335551024862fdbe5fc45415cde6fcf7d19a28fcbe7&mpshare=1&scene=1&srcid=07191mhX2261GRaj5uWvt5MK#rd
 */
public class StaticTest {

    public static void main(String[] args) {
        staticFunction();
    }

    static StaticTest st = new StaticTest();

    static {
        System.out.println("1");
    }

    {
        System.out.println("2");
    }

    StaticTest() {
        System.out.println("3");
        System.out.println("a=" + a + ",b=" + b);
    }

    public static void staticFunction() {
        System.out.println("4");
    }

    int a = 110;
    static int b = 112;
}
