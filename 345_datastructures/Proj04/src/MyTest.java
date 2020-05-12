
public class MyTest {

	/*
	 * common ops:
	 * 
	 * RedBlack
	 * only need to implement 2nd constructor
	 * constructor_2 -> "appropriate widgets" : "see example code to see how to handle 2-keys-in-node case"
	 * 
	 */
	
	
	public static int[] numbers = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25};
	public static String[] letters = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	public static int[] numbers2 = {0,1,2,3,4,5,6,7};
	public static String[] letters2 = {"a","b","c","d","e","f","g","h"};
	
	public static void main(String[] args) {
		Proj04_RedBlack_student<Integer,String> tree = new Proj04_RedBlack_student<Integer,String>("tree");
		test234();
	}
	
	
	public static void test234() {
		String x = "x";
		// build 234 node 0:
		Integer[] intArray0 = {100,150,null};
		String[] stringArray0 = {x,x,null};
		Proj04_234Node<Integer,String> node0 = new Proj04_234Node<Integer,String>();
		node0 = buildNode(intArray0, stringArray0);
		node0.numKeys = 2;
		// build 234 node 1:
		Integer[] intArray1 = {25,50,75};
		String[] stringArray1 = {x,x,x};
		Proj04_234Node<Integer,String> node1 = new Proj04_234Node<Integer,String>();
		node1 = buildNode(intArray1, stringArray1);
		node1.numKeys = 3;
		// build 234 node 2:
		Integer[] intArray2 = {125,null,null};
		String[] stringArray2 = {x,null,null};
		Proj04_234Node<Integer,String> node2 = new Proj04_234Node<Integer,String>();
		node2 = buildNode(intArray2, stringArray2);
		node2.numKeys = 1;
		// build 234 node 3:
		Integer[] intArray3 = {200,250,300};
		String[] stringArray3 = {x,x,x};
		Proj04_234Node<Integer,String> node3 = new Proj04_234Node<Integer,String>();
		node3 = buildNode(intArray3, stringArray3);
		node3.numKeys = 3;
		// build 234 node 4:
		Integer[] intArray4 = {10,15,20};
		String[] stringArray4 = {x,x,x};
		Proj04_234Node<Integer,String> node4 = new Proj04_234Node<Integer,String>();
		node4 = buildNode(intArray4, stringArray4);
		node4.numKeys = 3;
		// build 234 node 5:
		Integer[] intArray5 = {40,null,null};
		String[] stringArray5 = {x,null,null};
		Proj04_234Node<Integer,String> node5 = new Proj04_234Node<Integer,String>();
		node5 = buildNode(intArray5, stringArray5);
		node5.numKeys = 1;
		// build 234 node 6:
		Integer[] intArray6 = {60,null,null};
		String[] stringArray6 = {x,null,null};
		Proj04_234Node<Integer,String> node6 = new Proj04_234Node<Integer,String>();
		node6 = buildNode(intArray6, stringArray6);
		node6.numKeys = 1;
		// build 234 node 7:
		Integer[] intArray7 = {80,null,null};
		String[] stringArray7= {x,null,null};
		Proj04_234Node<Integer,String> node7 = new Proj04_234Node<Integer,String>();
		node7 = buildNode(intArray7, stringArray7);
		node7.numKeys = 1;
		// build 234 node 8:
		Integer[] intArray8 = {110,null,null};
		String[] stringArray8 = {x,null,null};
		Proj04_234Node<Integer,String> node8 = new Proj04_234Node<Integer,String>();
		node8 = buildNode(intArray8, stringArray8);
		node8.numKeys = 1;
		// build 234 node 9:
		Integer[] intArray9 = {130,null,null};
		String[] stringArray9 = {x,null,null};
		Proj04_234Node<Integer,String> node9 = new Proj04_234Node<Integer,String>();
		node9 = buildNode(intArray9, stringArray9);
		node9.numKeys = 1;
		// build 234 node 10:
		Integer[] intArray10 = {170,180,null};
		String[] stringArray10 = {x,x,null};
		Proj04_234Node<Integer,String> node10 = new Proj04_234Node<Integer,String>();
		node10 = buildNode(intArray10, stringArray10);
		node10.numKeys = 2;
		// build 234 node 11:
		Integer[] intArray11 = {225,null,null};
		String[] stringArray11 = {x,null,null};
		Proj04_234Node<Integer,String> node11 = new Proj04_234Node<Integer,String>();
		node11 = buildNode(intArray11, stringArray11);
		node11.numKeys = 1;
		// build 234 node 12:
		Integer[] intArray12 = {275,null,null};
		String[] stringArray12 = {x,null,null};
		Proj04_234Node<Integer,String> node12 = new Proj04_234Node<Integer,String>();
		node12 = buildNode(intArray12, stringArray12);
		node12.numKeys = 1;
		// build 234 node 13:
		Integer[] intArray13= {325,326,327};
		String[] stringArray13= {x,x,x};
		Proj04_234Node<Integer,String> node13 = new Proj04_234Node<Integer,String>();
		node13 = buildNode(intArray13, stringArray13);
		node13.numKeys = 3;
		// build tree
		Proj04_234Node[] childNodeArray0 = {node1,node2,node3,null};
		node0 = setChildren(node0,childNodeArray0);
		Proj04_234Node[] childNodeArray1 = {node4,node5,node6,node7};
		node1 = setChildren(node1,childNodeArray1);
		Proj04_234Node[] childNodeArray2 = {node8,node9,null,null};
		node2 = setChildren(node2,childNodeArray2);
		Proj04_234Node[] childNodeArray3 = {node10,node11,node12,node13};
		node3 = setChildren(node3,childNodeArray3);
		Proj04_RedBlack_student RBTree = new Proj04_RedBlack_student("tree",node0);
		RBTree.genDebugDot();
	}
	
	
	
	public static Proj04_234Node buildNode (Integer[] keys, String[] values) {
		Proj04_234Node node = new Proj04_234Node();
		node.key1=keys[0];
		node.key2=keys[1];
		node.key3=keys[2];
		node.val1=values[0];
		node.val2=values[1];
		node.val3=values[2];
		return node;
	}
	
	public static Proj04_234Node setChildren(Proj04_234Node node, Proj04_234Node[] nodes) {
		node.child1=nodes[0];
		node.child2=nodes[1];
		node.child3=nodes[2];
		node.child4=nodes[3];
		return node;
	}
	
	
	public static void leftwardTest(Proj04_RedBlack_student<Integer,String> tree) {
		tree.set(numbers[18], letters[18]);
		tree.set(numbers[19], letters[19]);
		tree.set(numbers[14], letters[14]);
		tree.set(numbers[16], letters[16]);
		tree.set(numbers[17], letters[17]);
		tree.set(numbers[15], letters[15]);
		tree.set(numbers[13], letters[13]);
		tree.genDebugDot();
		tree.set(numbers[12], letters[12]);
		tree.genDebugDot();
		
	}
	
	public static void LL_Test(Proj04_RedBlack_student<Integer,String> tree) {
		tree.set(numbers[5], letters[5]);
		tree.set(numbers[3], letters[3]);
		tree.set(numbers[7], letters[7]);
		tree.set(numbers[1], letters[1]);
		tree.genDebugDot();
	}
	
	public static void RR_Test(Proj04_RedBlack_student<Integer,String> tree) {
		tree.set(numbers[5], letters[5]);
		tree.set(numbers[3], letters[3]);
		tree.set(numbers[7], letters[7]);
		
		tree.set(numbers[9], letters[9]);
		tree.genDebugDot();
	}
	
	public static void LR_Test(Proj04_RedBlack_student<Integer,String> tree) {
		tree.set(numbers[5], letters[5]);
		tree.set(numbers[3], letters[3]);
		tree.set(numbers[7], letters[7]);
	
		tree.set(numbers[4], letters[4]);
		tree.genDebugDot();
	}
	
	public static void RL_Test(Proj04_RedBlack_student<Integer,String> tree) {
		tree.set(numbers[5], letters[5]);
		tree.set(numbers[3], letters[3]);
		tree.set(numbers[7], letters[7]);
		
		tree.set(numbers[6], letters[6]);
		tree.genDebugDot();
	}
	

	
	// TRAVERSALS 
	public static void printInOrder(Proj04_AVL_student tree) {
		Integer[] keys = new Integer[5];
		String[] vals = new String[5];
		String[] aux = new String[5];
		tree.inOrder(keys, vals, aux);
		printArray(keys);
		printArray(vals);
		printArray(aux);
	}
	public static void printArray(Integer[] intArray) {
		String printString ="";
		for (int i = 0; i<intArray.length ;i++){
			printString = printString+intArray[i];
		}
		System.out.println(printString);
	}
	public static void printArray(String[] intArray) {
		String printString ="";
		for (int i = 0; i<intArray.length ;i++){
			printString = printString+intArray[i];
		}
		System.out.println(printString);
	}

}
