1.upload 为自己模仿的代码
  upload2 为netty github提供的代码
  upload_merge 为合并后的代码目录

2. 目前merge目录除了 Server,剩下的代码都是upload,经过检测,将Server替换为upload2中的,server服务端不会出现在运行后立即停止.
由此猜测,很有可能目前 造成立即停止的 仅仅是 HttpUploadServer这一个文件出现了问题.
  找到原因,少如下代码:
  System.err.println("Open your web browser and navigate to " +
                      (SSL? "https" : "http") + "://127.0.0.1:" + PORT + '/');

   ch.closeFuture().sync();
   也就是说 如果没有      ch.closeFuture().sync();  会让服务端刚启动一会就立即结束?
   原因:获取Channel 的CloseFuture，并且阻塞当前线程直到它完成,去除了sync()之后效果是一样的.重点是阻塞.

3.使用upload2的HttpUploadServer.java, upload1 的 HttpUploadClient.java,运行成功,并成功生成了 upload_netty.txt 文件.
说明自己所模仿的upload1的HttpUploadClient.java上传是没问题的.那么可以推测问题在服务端
  3.1:当使用自己模仿的ServerHandler和ServerInitializer报错,猜测很有可能是Handler出的问题
  3.2:使用自己的initializer和netty的handler,可行
