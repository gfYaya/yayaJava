package cn.thread.doug.chapter5;

import java.util.Random;

/* added by ycr
 * P78
 */
public class MockUtil {
	public static void longWork(int seed) {
		Random r = new Random();
		int j = Integer.MIN_VALUE;
		while (j < 0) {
			j += r.nextInt(seed);
		}
	}
	
	public static void main(String[] args) {
		MockUtil.longWork(50);
	}
}
