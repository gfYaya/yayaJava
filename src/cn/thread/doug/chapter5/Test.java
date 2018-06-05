package cn.thread.doug.chapter5;
//package com.xqs.concurrent.chapter7;

/* added by ycr
 * P78
 */
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Test {
	public static void main(String[] args) throws InterruptedException {
		ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(1);
		Thread thread = new Thread() {
			public void run() {
				try {
					arrayBlockingQueue.take();
				} catch (InterruptedException e) {
					System.out.println("发生中断，并抛出中断异常后，观察thread的中断状态: " + Thread.currentThread().isInterrupted());
					e.printStackTrace();
					Thread.currentThread().interrupt();// 恢复中断
					MockUtil.longWork(30);//越小越慢 越大越快
				}
			}
		};

		thread.start();
		// 1秒后中断thread
		TimeUnit.SECONDS.sleep(1);
		thread.interrupt();

		TimeUnit.SECONDS.sleep(1);
		System.out.println("在main线程观察thread的中断状态（此时thread还是存活的）：" + thread.isInterrupted());
	}
}