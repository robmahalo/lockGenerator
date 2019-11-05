package locktests;

import static org.junit.Assert.*;

import java.util.Random;

import lock.*;

import org.junit.Before;
import org.junit.Test;

/**
 * ComboLock test cases using those from the class and some of my
 * modifications to get them cleaned up and complete.
 * 
 * Date: 	02/15/2014
 * 
 * @author CSCI142 class
 */
public class ComboLockTests 
{
	private ComboLock myComboLock;
	private int[] myCombo;
	
	@Before
	public void setup()
	{
    	myComboLock = new ComboLock();
    	myCombo = myComboLock.getCombination();   
        myComboLock.reset();
	}
	
    /**
     * Tests if the combination is compliant with the formula stated in the
     * Lab guidelines
     */
    @Test
    public void testIfComboCompliant() 
    {
        boolean compliant = false;
        if ((myCombo[0] % 4) == (myCombo[2] % 4) && (myCombo[1] % 4) == ((myCombo[0] % 4) + 2) % 4)
        {
            compliant = true;
        }
        
        assertTrue("The combination is not compliant with the guidelines.", compliant);
    }

    /**
     * Tests if the Lock() method works after the myComboLock is already unlocked    
     */
	@Test
    public void testIfLocks() 
    {
        myComboLock.turnRight(myCombo[0]);
        myComboLock.turnLeft(myCombo[1]);
        myComboLock.turnRight(myCombo[2]);
        myComboLock.unlock();
        
        boolean locks = myComboLock.lock();
        assertTrue("Should Lock, but didn't.", locks);
    }

	 /**
	  * Tests if the unlock() method works after the myComboLock is already unlocked
	  */
    @Test
	public void testIfUnlocks () 
	{
		myComboLock.turnRight(myCombo[0]);
		myComboLock.turnLeft(myCombo[1]);
		myComboLock.turnRight(myCombo[2]);
		
		boolean unlocks = myComboLock.unlock();
		assertTrue("Correct combo so should work.", unlocks);
	}
    
    /**
     * Tests if the unlock() method works if no turns or input has been made
     */
    @Test
    public void testIfUnlocksWithoutEntries() 
    {
        boolean unlocks = myComboLock.unlock();
        assertFalse("Should Not Unlock, No numbers were input.", unlocks);
    }

    /**
     * Tests if the unlock() method will work with only one turn/entry being made
     */
    @Test
    public void testIfOpensWithOneEntry() 
    {
        myComboLock.turnRight(myCombo[0]);
        boolean unlocks = myComboLock.unlock();
        assertFalse("Should Not Unlock, Only One number entered.", unlocks);
    }
	  
    /**
     * Tests if the unlock() method will work with only two turn/entry being made
     */
    @Test
    public void testIfOpensWithTwoEntries() 
    {
        myComboLock.turnRight(myCombo[0]);
        myComboLock.turnLeft(myCombo[1]);
        boolean unlocks = myComboLock.unlock();
        assertFalse("Should Not Unlock, Only Two numbers entered.", unlocks);
    }

    /**
     * Tests if the unlock() method will work with the 
     * first turn/entry being incorrect
     */
    @Test
    public void testIfOpensWithOneWrongEntry1st() 
    {
        myComboLock.turnRight((myCombo[0]+1)%ComboLock.MAX_NUMBER);
        myComboLock.turnLeft(myCombo[1]);
        myComboLock.turnRight(myCombo[2]);
        boolean unlocks = myComboLock.unlock();
        assertFalse("Should Not Unlock, The first number entered is incorrect.", unlocks);
    }

    /**
     * Tests if the unlock() method will work with the 
     * second turn/entry being incorrect
     */
    @Test
    public void testIfOpensWithOneWrongEntry2nd() 
    {
        myComboLock.turnRight(myCombo[0]);
        myComboLock.turnLeft((myCombo[0]+1)%ComboLock.MAX_NUMBER);
        myComboLock.turnRight(myCombo[2]);
        boolean unlocks = myComboLock.unlock();
        assertFalse("Should Not Unlock, The second number entered is incorrect.", unlocks);
    }

    /**
     * Tests if the unlock() method will work with the 
     * third turn/entry being incorrect
     */
    @Test
    public void testIfOpensWithOneWrongEntry3rd() 
    {
        myComboLock.turnRight(myCombo[0]);
        myComboLock.turnLeft(myCombo[1]);
        myComboLock.turnRight((myCombo[0]+1)%ComboLock.MAX_NUMBER);
        boolean unlocks = myComboLock.unlock();
        assertFalse("Should Not Unlock, The third number entered is incorrect.", unlocks);
    }


    /**
     * Tests if the unlock() method will work after two
     * left turns having being made
     * 
     * NOTE: this test case is correct, but we decided as a class not to 
     * test this and allow as many turns both left and right to get to
     * the next number.  So just don't include it.
     */
//    @Test
//    public void testIfOpensAfterTwoLeftTurns() 
//    {
//        myComboLock.turnRight(myCombo[0]);
//        myComboLock.turnLeft(myCombo[1]);
//        myComboLock.turnLeft(myCombo[1]);
//        myComboLock.turnRight(myCombo[2]);
//        boolean unlocks = myComboLock.unlock();
//        assertFalse("Should Not Unlock, The dial was rotated twice to the Left.", unlocks);
//    }

    /**
     * Tests if the unlock() method will work after two
     * right turns, at the end, having being made
     * 
     * NOTE: this test case is correct, but we decided as a class not to 
     * test this and allow as many turns both left and right to get to
     * the next number.  So just don't include it.
     */
//    @Test
//    public void testIfOpensWithTwoFinalRightTurns() 
//    {
//        myComboLock.turnRight(myCombo[0]);
//        myComboLock.turnLeft(myCombo[1]);
//        myComboLock.turnRight(myCombo[2]);
//        myComboLock.turnRight(myCombo[2]);
//        boolean unlocks = myComboLock.unlock();
//        assertFalse("Should Not Unlock, The Dial was rotated twice to the right, at the end).", unlocks);
//    }
       
    /**
     * Tests if the unlock() method will work after multiple, initial,
     * right turns having being made
     */
    @Test
    public void testIfOpensAfterMultipleInitialRightTurns() 
    {
    	Random r = new Random();

        myComboLock.turnRight(r.nextInt(ComboLock.MAX_NUMBER));
        myComboLock.turnRight(r.nextInt(ComboLock.MAX_NUMBER));
        myComboLock.turnRight(r.nextInt(ComboLock.MAX_NUMBER));
        myComboLock.turnRight(myCombo[0]);
        myComboLock.turnLeft(myCombo[1]);
        myComboLock.turnRight(myCombo[2]);
        boolean unlocks = myComboLock.unlock();
        assertTrue("Should Unlock, turning the dial to the Right multiple times, initially, should not affect the lock.", unlocks);
    }

    /**
     * Once lock is unlocked, trying to unlock again should fail since locks 
     * reset.
     */
	@Test
	public void testUnlockOnUnlocked() 
	{
        myComboLock.turnRight(myCombo[0]);
        myComboLock.turnLeft(myCombo[1]);
        myComboLock.turnRight(myCombo[2]);
        boolean unlocks = myComboLock.unlock();
	        
	    unlocks = myComboLock.unlock();
	    assertFalse("Lock is already unlocked: should not unlock.", unlocks);
	}
	
	/**
	 * Test when combo is correct, but then relock.
	 */
	@Test
	public void testDoubleUnlock() 
	{
        myComboLock.turnRight(myCombo[0]);
        myComboLock.turnLeft(myCombo[1]);
        myComboLock.turnRight(myCombo[2]);
        boolean unlocks = myComboLock.unlock();
		
        myComboLock.lock();
		
        unlocks = myComboLock.unlock();
		
		assertFalse("Unlocking, reclocking, then unlocking again without re-entering combo.", unlocks);
		
	}
}
