package temp;

// https://mp.weixin.qq.com/s?__biz=MjM5ODI5Njc2MA==&mid=2655819162&idx=1&sn=78bff9fb855f4d081b67348abd221f14&chksm=bd74d84d8a03515b27b93d1b95a096035630b0cb60a52e377dc1378af3c0754a506f1c9144b7&mpshare=1&scene=1&srcid=0926d9BsqF3gVbl5IqagveyM#rd
public class RegexTest {

    public static void main(String[] args) {
        //String badRegex = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~_%\\\\/])+$";
        String badRegex = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+$";
        String bugUrl = "http://www.fapiao.com/dddp-web/pdf/download?request=6e7JGxxxxx4ILd-kExxxxxxxqJ4-CHLmqVnenXC692m74H38sdfdsazxcUmfcOH2fAfY1Vw__%5EDadIfJgiEf";
        if (bugUrl.matches(badRegex)) {
            System.out.println("match!!");
        } else {
            System.out.println("no match!!");
        }
    }

}
