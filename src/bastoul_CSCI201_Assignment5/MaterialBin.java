package bastoul_CSCI201_Assignment5;

public class MaterialBin {
	int available;
	public MaterialBin(int total) {
		available = total;
	}
	public String available() {
		String num = "" + available;
		return num;
	}
	public boolean takeMaterial() {
		if (available>0) {
			available--;
			return true;
		} else {
			return false;
		}
	}
}
