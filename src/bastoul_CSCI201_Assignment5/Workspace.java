package bastoul_CSCI201_Assignment5;

import java.util.Vector;
import java.util.concurrent.Semaphore;

import javax.swing.JLabel;

public class Workspace {
	int openSpaces;
	int numSpaces;
	String [] spaceStatus;
	Semaphore sema;
	
	public Workspace(int n) {
		numSpaces = n;
		openSpaces = n;
		sema = new Semaphore(n);
		spaceStatus = new String[n];
		for (int i=0; i<n; i++) {
			spaceStatus[i] = "Open";
		}
	}
	
	public void setStatus(String s, int ind) {
		spaceStatus[ind] = s;
	}
	
	public void occupy(int t) {
		try {
			sema.acquire();
			String time = "" + t;
			spaceStatus[numSpaces-openSpaces] = time + "s";
			openSpaces--;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void unoccupy() {
			sema.release();
			spaceStatus[openSpaces] = "Open";
			openSpaces++;
	}
}
