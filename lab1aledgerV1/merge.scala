
object tileSort {
  

  def swap(a:Array[Int],i:Int,j:Int) {
    val temp = a(i);
    a(i) = a(j);
    a(j) = temp;
  }

  def merge(a:Array[Int],index:Int, length:Int) {
    // create arrays L[1...(b-a)] and R[1...(c-d)]
     val L:Array[Int] = new Array[Int](length+1);
     val R:Array[Int] = new Array[Int](length+1);
     for (i <- 0 to (length-1)) {
       L(i) = a(i+index);
     }
     for (j <- 0 to (length-1)) {
       R(j) = a(j+index+length);
     }
     L(length) = 1000000;
     R(length) = 1000000;
     var i = 0;
     var j = 0;
     for (k <- index to (index-1+(2*length))) {
       if (L(i) <= R(j)) {
        a(k) = L(i);
        i = i+1;
       }
       else {
        a(k) = R(j);
        j = j+1;
       }
     }
  }

  def iterativeTileSort(a:Array[Int]) {
    val n = a.length-1;
    var k = 1;
    while (k <= n) {
      for (i <- 0 to n by (2*k)) {
        merge(a,i,k);
      }
      k = 2*k;
      println(k)
      output(a);
    }
  }

  def random(from:Int,until:Int):Int = {
    val size = until - from;
    return from + (math.random * size).toInt;
  }

  def permute(a:Array[Int]) {
    val n = a.length;
    for (i <- 0 until (n-1)) {
      val j = random(i,n);
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

     // Create a new array of the appropriate size.
     val n = if (randomize) args(1).toInt else args.length;
     val ints:Array[Int] = new Array[Int](n);
 
     // Fill in the array's values.
     if (randomize) {
       // Builds an array with a random permutation.
       for (i <- 0 until n) {
         ints(i) = i+1;
       }
       permute(ints);
     } else {
       // Build an array with the given command-line arguments.
       for (i <- 0 until n) {
         ints(i) = args(i).toInt;
       }
     }
 
     // Output that array.
     print("BEFORE: ");
     output(ints);
 
     // Sort that array using bubble sort.
     val T = time(iterativeTileSort(ints));
 
     // Output the sorted array.
     print("AFTER:  ");
     output(ints);
 
     println("TIME: "+T+" msecs");
   }

   def time(f: => Unit) = {
    val s = System.currentTimeMillis
    f
    System.currentTimeMillis-s;
   }
}
