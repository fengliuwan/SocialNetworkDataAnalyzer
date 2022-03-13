package util;

public class test {

	public static void main(String[] args) {
		test dummy = new test();
		int[] a = {3,6, 9997,9997};
		int k = dummy.maxSumDivThree(a);
		System.out.println(k);
		

	}
	
	 public int maxSumDivThree(int[] nums) {
         int res = 0, leftOne = 20000, leftTwo = 20000;
    for(int n:nums){
        res+=n;
        if(n%3==1){
            leftTwo = Math.min(leftTwo,leftOne+n);
            leftOne = Math.min(leftOne,n);
            System.out.println("leftTwo is " +  leftTwo + "leftOne is " + leftOne);
        }
        if(n%3==2) {
            leftOne = Math.min(leftOne,leftTwo+n);
            leftTwo = Math.min(leftTwo,n);
        }
    }
    if(res%3==0) return res;
    if(res%3==1) return res-leftOne;
    return res - leftTwo;
    
}

}
