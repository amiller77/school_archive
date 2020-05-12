
public class Test_a {

	public static void main(String[] args) {
		Integer[] arr = {129, 60, 258, 688, 64, 123, 568, 641, 203, 195, 203, 243, 602, 743, 296, 314, 36, 117, 726, 2, 78, 490, 601, 627, 749, 540, 375, 378};
		//Proj02_MaxHeap heap = new Proj02_MaxHeap(false,arr);
		Proj02_HeapSort sorter = new Proj02_HeapSort(true);
		sorter.sort(arr);

	}

}
