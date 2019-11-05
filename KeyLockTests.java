package locktests;

import static org.junit.Assert.*;
import lock.KeyLock;

import org.junit.Before;
import org.junit.Test;

/**
 * KeyLock test cases using those from the class and some of my
 * modifications to get them cleaned up and complete.
 * 
 * Date: 	02/15/2014
 * 
 * @author CSCI142 class
 */
public class KeyLockTests 
{
	private KeyLock myKeyLock;
	
	@Before
	public void setup()
	{
		myKeyLock = new KeyLock(6);
	}

    /**Tests if the Wrong key has been inserted by attempting to 
     * call the turn() method with the wrong key value inserted.
     */
    @Test
    public void testIfWrongKey()
    {
        myKeyLock.insert(7);
        boolean works = myKeyLock.turn();
        
        assertFalse("Opened Lock with wrong key", works);
    }

    /**Tests if the Correct key has been inserted by attempting to 
     * call the turn() method with the correct key value inserted.
     */
	@Test
	public void testIfCorrectKey() 
	{
		myKeyLock.insert(6);
		boolean turns = myKeyLock.turn();
		
		assertTrue("Correct key, so should turn",turns);
	}
	    
    /**
     * Tests if the turn() method can be called when no key value has been
     * inserted.
     * 
     **/
    @Test
    public void testIfTurnsWithNoKey()
    {
        boolean turns = myKeyLock.turn();
        assertFalse("It is Not Supposed to Turn Without a Key", turns);
    }

    /**
     * Tests if the unlock() method works when all requirements are fulfilled
     * (insert correct key, and turn key)
     */
    @Test    
    public void testIfUnlocks(){
	    myKeyLock.insert(6);
	    myKeyLock.turn();
	    myKeyLock.unlock();
	    assertFalse("It is Supposed to unlock successfully", myKeyLock.isLocked());
    }

    /**
     * Tests if Lock() method works when the myKeyLock is unlocked 
     */
    @Test    
    public void testIfLocks(){
    myKeyLock.insert(6);
    assertTrue("It is Supposed to turn with the correct key", myKeyLock.turn());
    assertTrue("It is Supposed to unlock", myKeyLock.unlock());
    assertFalse("It is Supposed to be unlocked", myKeyLock.isLocked());
    assertTrue("It is Supposed to lock", myKeyLock.lock());
    assertTrue("It is Supposed to turn with the correct key", myKeyLock.isLocked());
    }

    /**
     * Tests if the unlock() method works when no key has been inserted or turned
     */
	@Test
	public void testUnlockWithNoKey() 
	{
		boolean unlocks = myKeyLock.unlock();
		
		assertFalse("No inserted key: should not unlock",unlocks);
	}
	
	/**
	 * Once key inserted, should be able to relock then unlock again since
	 * key is in. (NOTE: In our original design, we should have had a remove() 
	 * method to take the key out.)
	 */
	@Test
	public void testUnlockWithCorrectKey() 
	{
        myKeyLock.insert(6);
        myKeyLock.turn();
		myKeyLock.lock();
		myKeyLock.turn();
		boolean unlocks = myKeyLock.unlock();
		
		assertTrue("Correct key: should unlock",unlocks);
	}
	
	/**
	 * Should be able to lock after unlocking.
	 */
	@Test
	public void testLockWithCorrectKey() 
	{
		myKeyLock.insert(6);
		myKeyLock.turn();
		myKeyLock.unlock();
		myKeyLock.turn();
		boolean locks = myKeyLock.lock();
		
		assertTrue("Correct key: should lock",locks);
	}

}
