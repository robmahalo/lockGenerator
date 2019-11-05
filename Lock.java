package lock;

public interface Lock {
	
	public boolean isLocked();
	
	public boolean lock();
	
	public boolean unlock();

}
