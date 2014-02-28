object partition {
  // swap
  //
  // Performs a swap of the values at two positions in an array.
  //
  def swap(a:Array[Int],i:Int,j:Int):Unit = {
    val temp = a(i);
    a(i) = a(j);
    a(j) = temp;
  }

 def partition(_iv1:Int, _iv2:Int, a:Array[Int],l:Int,r:Int):Int = {
   var iv1:Int = _iv1;
   var iv2:Int = _iv2;
   // we want v1 < v2, so swap if that isn't true
   if (a(_iv1) > a(_iv2)) {
     iv1 = _iv2;
     iv2 = _iv1;
   }
   val v1:Int = a(iv1);
   val v2:Int = a(iv2);
   println("Partitioning around "+v1+" and "+v2);
   println(l+" "+r);

   swap(a,iv1,r-1);
   swap(a,iv2,r);
   var i:Int = l;
   var j:Int = l;
   for (k <- l to r) {
     println(k);
     if (a(k) <= v2) {
      if (a(k) <= v1) {
        swap(a,j,k);
        swap(a,k,j);
        i+=1;
        j+=1;
      } else {
        swap(a,j,k);
        j+=1;
      }
     }
   }
   output(a)
   return i+1
 }

 def random(from:Int,to:Int):Int = {
    val size = to - from + 1;
    return from + (math.random * size).toInt;
  }
    
  def permute(a:Array[Int]) {
    val n = a.length;
    for (i <- 0 until (n-1)) {
      val j = random(i,n-1);
      if (i != j) {
	      swap(a,i,j);
      }
    }
  }

  def output(a:Array[Int]) {
    for (v <- a) {
      print(v + " ");
    }
    println();
  }

  def main(args:Array[String]) {

    val randomize = if (args(0).equals("random")) true else false;

    val n = if (randomize) args(1).toInt else args.length;
    val ints:Array[Int] = new Array[Int](n);
      
    if (randomize) {
      for (i <- 0 until n) {
	      ints(i) = i+1;
      }
      permute(ints);
    } else {
      for (i <- 0 until n) {
	      ints(i) = args(i).toInt;
      }
    }

    print("BEFORE: ");
    output(ints);

    partition(3,8,ints,0,ints.length-1);
    // Sort that array using selection sort.

    // Output the sorted array.
    print("AFTER:  ");
    output(ints);
  }
}
