package lock;

public class ComboLock implements Lock {
	
	public static final int MAX_NUMBER = 50;
	private int[] myCombination;
	private int[] myAttempt;
	private boolean myIsLocked;
	private boolean myIsReset;
	private int index = 0;
	
	public ComboLock() {
		myCombination = new int[] {5, 3, 9};
		myAttempt = new int[3];
		myIsLocked = true;
		myIsReset = false;
	}
	
	public boolean turnLeft(int number) {
		myIsReset = false;
		if (number >= 0 && number <= MAX_NUMBER) {
			myAttempt[index] = number;
			if (index == 2)
				index = 0;
			else
				index++;
		}
		return false;
	}
	
	public boolean turnRight(int number) {
		myIsReset = false;
		if (number >= 0 && number <= MAX_NUMBER) {
			myAttempt[index] = number;
			if (index == 2)
				index = 0;
			else
				index++;
		}
		return false;
	}
	
	public void reset() {
		myIsReset = true;
		for (int i=0; i<3; i++) {
			myAttempt[i] = (myAttempt[i] + 7) % MAX_NUMBER;
		}
	}
	
	public int[] getCombination() {
		return myCombination;
	}
	
	public boolean getIsReset() {
		return myIsReset;
	}	

	@Override
	public boolean isLocked() {
		return myIsLocked;
	}

	@Override
	public boolean lock() {
		if (!myIsLocked) {
			myIsLocked = true;
			reset();
			return true;
		}
		return false;
	}

	@Override
	public boolean unlock() {
		if (myIsLocked) {
			for (int i=0; i<3; i++) {
				if (myCombination[i] != myAttempt[i]) {
					return false;
				}
			}
			myIsLocked = false;
			return true;
		}
		return false;
	}

}
