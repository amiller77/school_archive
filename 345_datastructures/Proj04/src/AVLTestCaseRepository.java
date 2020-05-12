
public class AVLTestCaseRepository {

	public static int[] numbers = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25};
	public static String[] letters = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	public static int[] numbers2 = {0,1,2,3,4,5,6,7};
	public static String[] letters2 = {"a","b","c","d","e","f","g","h"};
	
	public static void AVL_LLTest(Proj04_AVL_student tree) {
		tree.set(numbers[3], letters[3]);
		tree.set(numbers[2], letters[2]);
		tree.set(numbers[4], letters[4]);
		tree.set(numbers[1], letters[1]);
		tree.genDebugDot();
		tree.set(numbers[0], letters[0]);
		tree.genDebugDot();
	}
	
	public static void AVL_RRTest(Proj04_AVL_student tree) {
		tree.set(numbers[3], letters[3]);
		tree.set(numbers[2], letters[2]);
		tree.set(numbers[4], letters[4]);
		tree.set(numbers[5], letters[5]);
		tree.genDebugDot();
		tree.set(numbers[6], letters[6]);
		tree.genDebugDot();
	}
	
	public static void AVL_LRTest(Proj04_AVL_student tree) {
		tree.set(numbers[3], letters[3]);
		tree.set(numbers[1], letters[1]);
		tree.genDebugDot();
		tree.set(numbers[2], letters[2]);
		tree.genDebugDot();
	}
	
	public static void AVL_RLTest(Proj04_AVL_student tree) {
		tree.set(numbers[3], letters[3]);
		tree.set(numbers[5], letters[5]);
		tree.genDebugDot();
		tree.set(numbers[4], letters[4]);
		tree.genDebugDot();
	}
	
	public static void AVL_complexCaseTest1A(Proj04_AVL_student tree) {
		tree.set(numbers[5], letters[5]);
		tree.set(numbers[6], letters[6]);
		tree.set(numbers[3], letters[3]);
		tree.set(numbers[4], letters[4]);
		tree.set(numbers[2], letters[2]);
		tree.genDebugDot();
		tree.set(numbers[1], letters[1]);
		tree.genDebugDot();
	}
	
	public static void AVL_complexCaseTest1B(Proj04_AVL_student tree) {
		tree.set(numbers[6], letters[6]);
		tree.set(numbers[7], letters[7]);
		tree.set(numbers[4], letters[4]);
		tree.set(numbers[5], letters[5]);
		tree.set(numbers[2], letters[2]);
		tree.genDebugDot();
		tree.set(numbers[3], letters[3]);
		tree.genDebugDot();
	}
	
	public static void AVL_complexCaseTest1C(Proj04_AVL_student tree) {
		tree.set(numbers[6], letters[6]);
		tree.set(numbers[7], letters[7]);
		tree.set(numbers[3], letters[3]);
		tree.set(numbers[5], letters[5]);
		tree.set(numbers[2], letters[2]);
		tree.genDebugDot();
		tree.set(numbers[4], letters[4]);
		tree.genDebugDot();
	}
	
	public static void AVL_complexCaseTest1D(Proj04_AVL_student tree) {
		tree.set(numbers[6], letters[6]);
		tree.set(numbers[7], letters[7]);
		tree.set(numbers[3], letters[3]);
		tree.set(numbers[4], letters[4]);
		tree.set(numbers[2], letters[2]);
		tree.genDebugDot();
		tree.set(numbers[5], letters[5]);
		tree.genDebugDot();
	}
	
	public static void AVL_complexCaseTest2A(Proj04_AVL_student tree) {
		tree.set(numbers[1], letters[1]);
		tree.set(numbers[0], letters[0]);
		tree.set(numbers[5], letters[5]);
		tree.set(numbers[3], letters[3]);
		tree.set(numbers[7], letters[7]);
		tree.genDebugDot();
		tree.set(numbers[2], letters[2]);
		tree.genDebugDot();
	}
	
	public static void AVL_complexCaseTest2B(Proj04_AVL_student tree) {
		tree.set(numbers[1], letters[1]);
		tree.set(numbers[0], letters[0]);
		tree.set(numbers[5], letters[5]);
		tree.set(numbers[3], letters[3]);
		tree.set(numbers[7], letters[7]);
		tree.genDebugDot();
		tree.set(numbers[4], letters[4]);
		tree.genDebugDot();
	}
	
	public static void AVL_complexCaseTest2C(Proj04_AVL_student tree) {
		tree.set(numbers[1], letters[1]);
		tree.set(numbers[0], letters[0]);
		tree.set(numbers[5], letters[5]);
		tree.set(numbers[3], letters[3]);
		tree.set(numbers[7], letters[7]);
		tree.genDebugDot();
		tree.set(numbers[6], letters[6]);
		tree.genDebugDot();
	}
	
	public static void AVL_complexCaseTest2D(Proj04_AVL_student tree) {
		tree.set(numbers[1], letters[1]);
		tree.set(numbers[0], letters[0]);
		tree.set(numbers[5], letters[5]);
		tree.set(numbers[3], letters[3]);
		tree.set(numbers[7], letters[7]);
		tree.genDebugDot();
		tree.set(numbers[8], letters[8]);
		tree.genDebugDot();
	}
	
	public static void AVL_complexCaseTest3A(Proj04_AVL_student tree) {
		tree.set(numbers[23], letters[23]);
		tree.set(numbers[10], letters[10]);
		tree.set(numbers[24], letters[24]);
		tree.set(numbers[25], letters[25]);
		tree.set(numbers[5], letters[5]);
		tree.set(numbers[17], letters[17]);
		tree.set(numbers[13], letters[13]);
		tree.set(numbers[20], letters[20]);
		tree.set(numbers[14], letters[14]);
		tree.genDebugDot();
		tree.set(numbers[21], letters[21]);
		tree.set(numbers[19], letters[19]);
		tree.set(numbers[4], letters[4]);
		tree.set(numbers[3], letters[3]);
		tree.set(numbers[2], letters[2]);
		tree.set(numbers[1], letters[1]);
		tree.set(numbers[0], letters[0]);
		tree.set(numbers[9], letters[9]);
		tree.set(numbers[8], letters[8]);
		tree.set(numbers[7], letters[7]);
		tree.set(numbers[6], letters[6]);
		tree.set(numbers[18], letters[18]);
		tree.set(numbers[19], letters[19]);
		tree.set(numbers[22], letters[22]);
		tree.set(numbers[11], letters[11]);
		tree.set(numbers[12], letters[12]);
		tree.set(numbers[14], letters[14]);
		tree.set(numbers[15], letters[15]);
		tree.set(numbers[16], letters[16]);
		tree.genDebugDot();
	}
	
	
	public static void AVL_complexCaseTest3B(Proj04_AVL_student tree) {
		for (int i =0; i<26;i++) {
			tree.set(numbers[i], letters[i]);
		}
		tree.remove(16);
		tree.remove(24);
		tree.remove(21);
		tree.remove(3);
		tree.remove(15);
		tree.remove(14);
		tree.remove(13);
		tree.genDebugDot();
		tree.remove(23);
		tree.genDebugDot();
		
	}
	
}
