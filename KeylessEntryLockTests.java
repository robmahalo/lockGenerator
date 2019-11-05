package locktests;

import static org.junit.Assert.*;

import lock.KeylessEntryLock;

import org.junit.Before;
import org.junit.Test;

/**
 * KeylessEntryLock test cases using those from the class and some of my
 * modifications to get them cleaned up and complete.
 * 
 * Date: 	02/15/2014
 * 
 * @author CSCI142 class
 */

public class KeylessEntryLockTests 
{
	private KeylessEntryLock myLock;
	private int[] myMasterCombo;
	private String myMasterComboString;

	@Before
	public void setup()
	{
		myLock = new KeylessEntryLock(5);
		myMasterCombo = myLock.getMasterCode();
		myMasterComboString = this.intArrayToString(myMasterCombo);
	}

	@Test
	public void testOneUserCodeCorrect() 
	{
		boolean valid, unlocks;
		
		/*
		 * Enter 6DPC then create new 4DUC
		 */
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*113211321");
		System.out.println("here");
		assertTrue("Add User Code should work", myLock.addedUserCode());
		
		/*
		 * Open lock
		 */
		valid = valid && this.pushSequence("1321");
		
		unlocks = myLock.unlock();
		assertTrue("All sequences fine, should be true", valid);
		assertTrue("Lock should be unlocked", unlocks);
	}
	
	@Test
	public void testTwoUserCodesCorrect() 
	{
		boolean valid, unlocks;
		
		/*
		 * Enter 6DPC then create new 4DUC
		 */
		valid = this.pushSequence(myMasterComboString);
		// FIXED ERROR IN SEQUENCE!
		valid = valid && this.pushSequence("*166546654");
		assertTrue("Add First User Code should work", myLock.addedUserCode());
		
		/*
		 * Add second 4DUC
		 */
		valid = this.pushSequence(myMasterComboString);
		// FIXED ERROR IN SEQUENCE!
		valid = valid && this.pushSequence("*100000000");
		assertTrue("Add Second User Code should work", myLock.addedUserCode());
		
		/*
		 * Open lock with both
		 */
		valid = valid && this.pushSequence("6654");		
		unlocks = myLock.unlock();
		assertTrue("First unlock should work", unlocks);
		
		myLock.lock();
		
		valid = valid && this.pushSequence("0000");		
		unlocks = myLock.unlock();
		assertTrue("Second unlock should work", unlocks);
		
		assertTrue("All sequences fine, should be true", valid);
	}
	
	@Test
	public void testWrongMasterCode() 
	{
		boolean valid, unlocks;
		
		/*
		 * Enter 6DPC then create new 4DUC
		 */
		String badMasterCode = "888888";
		int diff = myMasterComboString.compareTo(badMasterCode);
		
		// FIXED ERROR IN BAD MASTER CODE!
		if (diff == 0)
		{
			badMasterCode = "777777";
		}
		valid = this.pushSequence(badMasterCode);
		valid = valid && this.pushSequence("*113211321");
		
		/*
		 * Open lock
		 */
		valid = valid && this.pushSequence("1321");
		
		unlocks = myLock.unlock();
		assertTrue("All sequences fine, should be true", valid);
		assertFalse("User code should fail since bad master code used", unlocks);
	}

	
	@Test
	public void testWrongUserCodeFirstKey() 
	{
		boolean valid, unlocks;
		
		/*
		 * Enter 6DPC then create new 4DUC
		 */
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*113211321");
		
		/*
		 * Open lock
		 */
		valid = valid && this.pushSequence("2321");
		
		unlocks = myLock.unlock();
		assertTrue("All sequences fine, should be true", valid);
		assertFalse("Incorrect user code, should fail", unlocks);
	}

	@Test
	public void testWrongUserCodeSecondKey() 
	{
		boolean valid, unlocks;
		
		/*
		 * Enter 6DPC then create new 4DUC
		 */
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*113211321");
		
		/*
		 * Open lock
		 */
		valid = valid && this.pushSequence("1421");
		
		unlocks = myLock.unlock();
		assertTrue("All sequences fine, should be true", valid);
		assertFalse("Incorrect user code, should fail", unlocks);
	}
	
	@Test
	public void testWrongUserCodeThirdKey() 
	{
		boolean valid, unlocks;
		
		/*
		 * Enter 6DPC then create new 4DUC
		 */
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*113211321");
		
		/*
		 * Open lock
		 */
		valid = valid && this.pushSequence("1351");
		
		unlocks = myLock.unlock();
		assertTrue("All sequences fine, should be true", valid);
		assertFalse("Incorrect user code, should fail", unlocks);
	}

	@Test
	public void testWrongUserCodeFourthKey() 
	{
		boolean valid, unlocks;
		
		/*
		 * Enter 6DPC then create new 4DUC
		 */
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*113211321");
		
		/*
		 * Open lock
		 */
		valid = valid && this.pushSequence("1325");
		
		unlocks = myLock.unlock();
		assertTrue("All sequences fine, should be true", valid);
		assertFalse("Incorrect user code, should fail", unlocks);
	}


	@Test
	public void testChangeMasterCode() 
	{
		boolean valid;
		
		/*
		 * Enter 6DPC then change 6DPC
		 */
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*3009933009933");
		assertTrue("All sequences fine, should be true", valid);
		assertTrue("Master code should have changed", myLock.changedMasterCode());
	}

	/**
	 * More detailed approach to check if master code changed. First change it,
	 * then see if new user code can be added and used.  NOTE: while we can
	 * get the default master code from the KeylessEntryLock class, we cannot 
	 * get any new master code once it has been changed. If we forget the new 
	 * master code, we are out of luck.  For a real lock to reset it, we must
	 * take the lock off the door and disconnect the battery.  When battery is
	 * plugged back in, the default code is set and no user codes are set.
	 */
	@Test
	public void testChangeMasterCodeAlternate() 
	{
		boolean valid;
		String newMasterCode = "123987";
		
		/*
		 * Enter 6DPC then change 6DPC
		 */
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*3");
		valid = valid && this.pushSequence(newMasterCode);
		valid = valid && this.pushSequence(newMasterCode);
		
		/*
		 * Enter 6DPC then create new 4DUC
		 */
		valid = this.pushSequence(newMasterCode);
		valid = valid && this.pushSequence("*113211321");
		
		/*
		 * Open lock
		 */
		valid = valid && this.pushSequence("1321");
		
		boolean unlocks = myLock.unlock();
		assertTrue("All sequences fine, should be true", valid);
		assertTrue("Should unlock new master code changed", unlocks);
	}
	
	@Test
	public void testDeleteOneUserCode() 
	{
		boolean valid, unlocks;
		
		/*
		 * Enter 6DPC then create new 4DUC
		 */
		// FIXED ERROR IN SEQUENCE!
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*166546654");
		assertTrue("Add First User Code should work", myLock.addedUserCode());
		
		/*
		 * Add second 4DUC
		 */
		// FIXED ERROR IN SEQUENCE!
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*100000000");
		assertTrue("Add Second User Code should work", myLock.addedUserCode());
		
		/*
		 * Now delete first user codes
		 */
		// FIXED ERROR IN SEQUENCE!
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*2");
		valid = valid && this.pushSequence("66546654");
		
		/*
		 * Open lock with both combos, with first failing and second passing
		 */
		valid = valid && this.pushSequence("6654");		
		unlocks = myLock.unlock();
		assertFalse("First unlock should not work", unlocks);
		
		myLock.lock();
		
		valid = valid && this.pushSequence("0000");		
		unlocks = myLock.unlock();
		assertTrue("Second unlock should work", unlocks);
		assertTrue("All sequences fine, should be true", valid);
	}
	
	@Test
	public void testDeleteUserCodesIncorrectly() 
	{
		boolean valid, unlocks;
		
		/*
		 * Enter 6DPC then create new 4DUC
		 */
		// FIXED ERROR IN SEQUENCE!
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*166546654");
		assertTrue("Add First User Code should work", myLock.addedUserCode());
		
		/*
		 * Add second 4DUC
		 */
		// FIXED ERROR IN SEQUENCE!
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*100000000");
		assertTrue("Add Second User Code should work", myLock.addedUserCode());
		
		/*
		 * Now delete first user code incorrectly
		 */
		// FIXED ERROR IN SEQUENCE!
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*2");
		valid = valid && this.pushSequence("66536654");
		
		/*
		 * Now delete second user code incorrectly
		 */
		// FIXED ERROR IN SEQUENCE!
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*2");
		valid = valid && this.pushSequence("00001000");
		
		/*
		 * Open lock with both combos, both passing
		 */
		System.out.println("Valid "+ valid);
		valid = valid && this.pushSequence("6654");		
		unlocks = myLock.unlock();
		assertTrue("First unlock should still work", unlocks);
		
		myLock.lock();
		
		valid = valid && this.pushSequence("0000");		
		unlocks = myLock.unlock();
		assertTrue("Second unlock should work", unlocks);
		assertTrue("All sequences fine, should be true", valid);
	}
	
	@Test
	public void testDeleteOneUserCodeAlternate() 
	{
		boolean valid;
		
		/*
		 * Enter 6DPC then create new 4DUC
		 */
		// FIXED ERROR IN SEQUENCE!
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*166546654");
		assertTrue("Add First User Code should work", myLock.addedUserCode());
		
		/*
		 * Add second 4DUC
		 */
		// FIXED ERROR IN SEQUENCE!
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*100000000");
		assertTrue("Add Second User Code should work", myLock.addedUserCode());
		
		/*
		 * Now delete first user code
		 */
		// FIXED ERROR IN SEQUENCE!
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*2");
		valid = valid && this.pushSequence("66546654");
		
		boolean deleted = myLock.deletedUserCode();
		
		assertTrue("Should have deleted a user code", deleted);
		assertTrue("All sequences fine, should be true", valid);
	}
	
	@Test
	public void testDeleteTwoUserCodes() 
	{
		boolean valid, unlocks;
		
		/*
		 * Enter 6DPC then create new 4DUC
		 */
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*166546654");
		assertTrue("Add First User Code should work", myLock.addedUserCode());
		
		/*
		 * Add second 4DUC
		 */
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*100000000");
		assertTrue("Add Second User Code should work", myLock.addedUserCode());
		
		/*
		 * Now delete first user codes
		 */
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*2");
		valid = valid && this.pushSequence("66546654");
		
		/*
		 * Now delete second user codes
		 */
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*2");
		valid = valid && this.pushSequence("00000000");
		
		/*
		 * Open lock with both combos, with both failing
		 */
		valid = valid && this.pushSequence("6654");		
		unlocks = myLock.unlock();
		assertFalse("First unlock should not work", unlocks);
		
		myLock.lock();
		
		valid = valid && this.pushSequence("0000");		
		unlocks = myLock.unlock();
		assertFalse("Second unlock should not work", unlocks);
		assertTrue("All sequences fine, should be true", valid);
	}
	
	@Test
	public void testDeleteAllUserCodes() 
	{
		boolean valid;
		
		/*
		 * Enter 6DPC then create new 4DUC
		 */
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*166546654");
		assertTrue("Add First User Code should work", myLock.addedUserCode());
		
		/*
		 * Add second 4DUC
		 */
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*100000000");
		assertTrue("Add Second User Code should work", myLock.addedUserCode());
		
		/*
		 * Now delete all user codes
		 */
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*6");
		valid = valid && this.pushSequence(myMasterComboString);
		
		assertTrue("All sequences fine, should be true", valid);
		
		/*
		 * Check if all user codes deleted
		 */
		boolean deleted = myLock.clearedAllUserCodes();
		
		assertTrue("All user codes should be deleted", deleted);
	}
		
	@Test
	public void testDeleteAllUserCodesAlternate() 
	{
		boolean valid, unlocks;
		
		/*
		 * Enter 6DPC then create new 4DUC
		 */
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*166546654");
		assertTrue("Add First User Code should work", myLock.addedUserCode());
		
		/*
		 * Add second 4DUC
		 */
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*100000000");
		assertTrue("Add Second User Code should work", myLock.addedUserCode());
		
		/*
		 * Now delete all user codes
		 */
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*6");
		valid = valid && this.pushSequence(myMasterComboString);
		
		/*
		 * Open lock with both
		 */
		valid = valid && this.pushSequence("6654");		
		unlocks = myLock.unlock();
		assertFalse("First unlock should not work", unlocks);
		
		myLock.lock();
		
		valid = valid && this.pushSequence("0000");		
		unlocks = myLock.unlock();
		assertFalse("Second unlock should not work", unlocks);
		
		assertTrue("All sequences fine, should be true", valid);
	}
	
	@Test
	public void testIfNoUserCode() 
	{
		boolean valid = myLock.addedUserCode();
		boolean unlocks = myLock.unlock();
		assertFalse("No user code added", valid);
		assertFalse("Should no unlock with no user code", unlocks);
	}

	@Test
	public void testIfLockWorks() 
	{
		boolean valid;
		
		/*
		 * Enter 6DPC then create new 4DUC
		 */
		valid = this.pushSequence(myMasterComboString);
		valid = valid && this.pushSequence("*113211321");
		assertTrue("Add User Code should work", myLock.addedUserCode());
		
		/*
		 * Open lock
		 */
		valid = valid && this.pushSequence("1321");
		
		myLock.unlock();
		myLock.lock();
		boolean locks = myLock.isLocked();
		assertTrue("Lock should be locked", locks);
		assertTrue("All sequences fine, should be true", valid);
	}

	@Test
	public void testIfNoMasterCodeEntered() 
	{
		boolean valid, unlocks;
		
		/*
		 * Enter 6DPC then create new 4DUC
		 */
		valid = this.pushSequence("*113211321");
		assertFalse("Add User Code should not work", myLock.addedUserCode());
		
		/*
		 * Open lock
		 */
		valid = valid && this.pushSequence("1321");
		
		unlocks = myLock.unlock();
		assertFalse("Lock should not be unlocked", unlocks);
		assertTrue("All sequences fine, should be true", valid);
	}
	
	/*
	 * Helper function to allow sequence of pushKey() calls without 
	 * cluttering main code.
	 */
	private boolean pushSequence(String sequence)
	{
		boolean valid = true;
		char key;
		
		for(int i=0; i<sequence.length(); i++)
		{
			key = sequence.charAt(i);
			valid = valid && myLock.pushKey(key);
		}
		return valid;
	}
	
	/*
	 * Create a string of characters in sequence from an integer array.
	 */
	private String intArrayToString(int[] values)
	{
		String string = "";
		
		for(int value : values)
		{
			string += Integer.toString(value);
		}
		return string;
	}
}
