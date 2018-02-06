package bastoul_CSCI201_Assignment5;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ToolBin {
	private int available;
	private int total;
	private Lock lock = new ReentrantLock();
	private Condition notEmpty = lock.newCondition();
		
	public ToolBin(int total) {
		this.total = total;
		this.available = total;
	}
	
	public String available() {
		return "" + available;
	}
	
	public String total() {
		return "" + total;
	}
	
	public void takeTool() {
		lock.lock();
		try {
			while (available==0) {
				notEmpty.await();
			}
			available--;
		} catch (InterruptedException ie) {
			System.out.println("IE: " + ie.getMessage());
		} finally {
			lock.unlock();
		}
	}
	
	public void returnTool() {
		lock.lock();
		available++;
		lock.unlock();
	}
	
}
