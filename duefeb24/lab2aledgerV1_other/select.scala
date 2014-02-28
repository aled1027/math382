object select {
  // swap
  //
  // Performs a swap of the values at two positions in an array.
  //
  def swap(a:Array[Int],i:Int,j:Int):Unit = {
    val temp = a(i);
    a(i) = a(j);
    a(j) = temp;
  }

 //

  def select(i:Int, a:Array[Int]):Int = {
    return selectHelper(i, a, 0, a.length-1);
  }
  def selectHelper(i:Int, a:Array[Int], l:Int, r:Int):Int = {
    val n:Int = partition(l, a, l, r); // uses left as pivot
    if (l+i == n) {
      return a(n);
    } else if (l+i < n) {
      selectHelper(i, a, l, n-1); 
    } else { 
      selectHelper(i-((n+1)-l), a, n+1, r);
    }
    
  }

  // partition
  //
  // Using a "pivot" value v of a[iv], this rearranges 
  // the array's values so that a[p..i-1] are less 
  // than or equal to v and so that a[i..r] are greater
  // than v.  The order of elements within each half is
  // arbitrary.
  //
  // first arg is pivot
  //
  // returns the new position of the pivot
 
  def partition(iv:Int,a:Array[Int],l:Int,r:Int):Int = {
    val v:Int = a(iv);
    swap(a,iv,r);
    var i:Int = l;
    for (j <- l to r) {
      if (a(j) <= v) {
	      swap(a,i,j);
	      i = i+1;
      }
    }
    return i-1;
  }
 
  // random
  //
  // Selects a value within the set {from,..,to} uniformly
  // at random.
  //
  def random(from:Int,to:Int):Int = {
    val size = to - from + 1;
    return from + (math.random * size).toInt;
  }
    
  //
  // permutes
  //
  // Randomly permutes the given array's elements.
  //
  def permute(a:Array[Int]) {
    val n = a.length;
    for (i <- 0 until (n-1)) {
      val j = random(i,n-1);
      if (i != j) {
	      swap(a,i,j);
      }
    }
  }


  //
  // output
  //
  // Outputs the contents of an array on one line.
  //
  def output(a:Array[Int]) {
    for (v <- a) {
      print(v + " ");
    }
    println();
  }
  //
  // main
  //
  // Tests selection sort.
  //
  def main(args:Array[String]) {

    val randomize = if (args(0).equals("random")) true else false;
    val n = if (randomize) args(1).toInt else args.length-1;
    val ints:Array[Int] = new Array[Int](n);
    val i:Int = if (randomize) args(2).toInt else args(n+1).toInt;

    if (randomize) {
      for (i <- 0 until n)  {
	      ints(i) = i+1;
      }
      permute(ints);
    } else {
      for (i <- 0 until n) {
	      ints(i) = args(i).toInt;
      }
    }
    print("List: ");
    output(ints);

    val selectOutput:Int = select(i, ints);
    println("The ith value of the list where i="+i+" is " + selectOutput); 
  }
}
