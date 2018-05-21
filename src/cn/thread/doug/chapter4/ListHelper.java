package cn.thread.doug.chapter4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

// 4.4.1   P61  from 微信 . 叶次然
public class ListHelper<E> {
	public List<E> list = Collections.synchronizedList(new ArrayList<E>());

	public synchronized boolean putIfAbsent(E x) {
		boolean absent = !list.contains(x);// check
		// between check and modify, you don't handle lock on list
		if (absent) {
			list.add(x);// modify
		}
		return absent;
	}

	public static void main(String[] args) throws InterruptedException {
		final ListHelper<String> list = new ListHelper<String>();
		final CountDownLatch countDownLatch = new CountDownLatch(1);
		
		Thread thread = new Thread() {
			public void run() {
				try {
					countDownLatch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(list.putIfAbsent("a"));
			};
		};
		thread.start();
		
		Thread thread2 = new Thread() {
			public void run() {
				try {
					countDownLatch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(list.list.add("a"));
			};
		};
		thread2.start();
		
		countDownLatch.countDown();
		TimeUnit.SECONDS.sleep(1);
		System.out.println(list.list.size());// not always 1 or 2
	}
}
