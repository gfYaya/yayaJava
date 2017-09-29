package cn.my.yaya.test4justjava.exception;

/**
 * Created by Yaya Match on 2016/8/11.
 * extract from   http://swiftlet.net/archives/998
 */
public class TestThrow {

    public static void throwChecked(int a) throws Exception{  //=>自己抛出异常,交给调用这处理
        if(a>0){
            //自行抛出Exception异常
            //该代码必须处于try块中,或者处于带throws的方法声明中
            throw new Exception("a的值大于0,不符合要求");
        }
    }

    public static void throwChecked_2(int b){
        if(b>0){
            try{
                throw new Exception("b的值大于0,不符合要求");
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void throwRuntime(int a){
        if(a>0){
            //自行抛出RuntimeException异常,既可以显示捕获异常,也可以完全不用理会,交给该方法的调用者处理
            throw new RuntimeException("a值大于0,不符合要求");
        }
    }

    public static void main(String args[]){
        try {
            //调用带throws的声明方法,必须显示捕获异常,否则必须在main方法中再次声明
            throwChecked(3);
        }catch(Exception e){
            System.out.println(e.getMessage()+" Yaya is here");
        }

        throwChecked_2(3);
        System.out.println("b");
        //调用抛出RuntimeException方法既可以显示捕获异常,也可以不理会该异常
        throwRuntime(3);

        System.out.println("over");
    }
}
