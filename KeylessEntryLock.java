package lock;

public class KeylessEntryLock extends KeyLock {
	
	public static int MAX_NUM_USER_CODES = 10;
	public static int USER_CODE_LENGTH = 4;
	public static int MASTER_CODE_LENGTH = 6;
	
	private boolean myIsReset;
	private boolean myIsNewUserCode;
	private boolean myIsDeletedUserCode;
	private boolean myIsChangedMasterCode;
	private int[] myMasterCode;
	private int[][] myUserCodes;
	private int[] myAttempt;
	private boolean myAllUserCodesDeleted;
	private String input = "";
	private int count = 0;
	private int[] newUserCode = new int[4];

	public KeylessEntryLock(int keyValue) {
		super(keyValue);
		myMasterCode = new int[]{1,2,3,4,5,6};
		myUserCodes = new int[MAX_NUM_USER_CODES][USER_CODE_LENGTH];
		myAttempt = new int[4];
		myIsReset = false;
		myIsNewUserCode = false;
		myIsDeletedUserCode = false;
		myIsChangedMasterCode = false;
		myAllUserCodesDeleted = false;
		
	}
	
	public boolean pushKey(char key) {
		input = input + key;
		System.out.println(input);
		if (input.length() > 7 && input.charAt(6) == '*' && input.substring(0, 6).equals(intArrayToString(myMasterCode))) {
			if (input.charAt(7) == '1' && input.length() == 16 ) {
				if (input.substring(8, 12).equals(input.substring(12, 16))) {
					for (int i=0; i<4; i++) {
						myUserCodes[count][i] = Character.getNumericValue(input.charAt(i+8));
						newUserCode[i] = Character.getNumericValue(input.charAt(i+8));
					}
					count++;
					myIsNewUserCode = true;
				}
				input = "";
			}
			else if (input.charAt(7) == '2' && input.length() == 16) {
				if (input.substring(8, 12).equals(input.substring(12, 16))) {
					int[] userCode = new int[4];
					for (int i=0; i<4; i++) {
						userCode[i] = Character.getNumericValue(input.charAt(i+8));
					}
					for (int i=0; i<MAX_NUM_USER_CODES; i++) {
						for (int j=0; j<USER_CODE_LENGTH; j++) {
							if (userCode[j] != myUserCodes[i][j])
								break;
							else if (j == 3) {
								myUserCodes[i] = new int[USER_CODE_LENGTH];
								myIsDeletedUserCode = true;
								count--;
								input = "";
								System.out.println("Count "+ count);
								return true;
							}
						}
					}
				}
				input = "";
				return true;
			}
			else if (input.charAt(7) == '6' && input.length() == 14) {
				for (int i=0; i<6; i++) {
					if (myMasterCode[i] != Character.getNumericValue(input.charAt(i+8))) {
						input = "";
						return true;
					}
				}
				myUserCodes = new int[MAX_NUM_USER_CODES][USER_CODE_LENGTH];
				count = 0;
				myAllUserCodesDeleted = true;
				input = "";
			}
			else if (input.charAt(7) == '3' && input.length() == 20 ) {
				if (input.substring(8, 14).equals(input.substring(14, 20))) {
					for (int i=0; i<MASTER_CODE_LENGTH; i++) {
						myMasterCode[i] = Character.getNumericValue(input.charAt(i+8));
					}
					myIsChangedMasterCode = true;
				}
				input = "";
			}
		} else {
			if (myIsNewUserCode && input.length() == 4) {
				for (int i=0; i<4;i++) {
					myAttempt[i] = Character.getNumericValue(input.charAt(i));
				}
			}
		} 
		return true;
	}
	
	public boolean addedUserCode() {
		return myIsNewUserCode;
	}
	
	public boolean deletedUserCode() {
		return myIsDeletedUserCode;
	}
	
	public boolean clearedAllUserCodes() {
		return myAllUserCodesDeleted;
	}
	
	public boolean changedMasterCode() {
		return myIsChangedMasterCode;
	}
	
	public int[] getMasterCode() {
		return myMasterCode;
	}
	
	@Override
	public boolean unlock() {
		// TODO Auto-generated method stub
		
		for (int i=0; i<count; i++) {
			for (int j=0; j<USER_CODE_LENGTH; j++) {
				if (myAttempt[j] != myUserCodes[i][j]) {
					break;
				}
				else if (j == 3) {
					input = "";
					return true;
				}
			}
		}
		input = "";
		return false;
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
