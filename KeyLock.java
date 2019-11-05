package lock;

public class KeyLock implements Lock {
	
	private int myKey;
	private boolean myIsLocked;
	private boolean myIsInserted;
	
	public KeyLock(int keyValue) {
		this.myKey = keyValue;
		myIsLocked = true;
		myIsInserted = false;
	}
	
	public boolean insert(int keyValue) {
		if (keyValue == myKey) {
			myIsInserted = true;
			return true;
		}
		return false;
	}
	
	public boolean turn() {
		if (myIsInserted) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isLocked() {
		// TODO Auto-generated method stub
		return myIsLocked;
	}

	@Override
	public boolean lock() {
		// TODO Auto-generated method stub
		if (myIsInserted && !myIsLocked) {
			myIsLocked = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean unlock() {
		// TODO Auto-generated method stub
		if (myIsInserted && myIsLocked) {
			myIsLocked = false;
			return true;
		}
		return false;
	}

}
