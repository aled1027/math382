
object bubble {
  def swap(a:Array[Int],i:Int,j:Int) {
    val temp = a(i);
    a(i) = a(j);
    a(j) = temp;
  }

  
  // bubbleSort is popular sorting algorithm known as bubble sort
  // in essence, bubbleSort works by repeatedly swapping adjacent elements that
  // are out of order. 
  //
  // It does by looping through the entire list, and then at each index, 
  // looping from the end of the list to the index, swapping out of order elements along the way

  def bubbleSort(a:Array[Int]) {
    val size = a.length-1;
    for (i <- 0 to size) {
      for (j <- size to (i+1) by -1) {
        if (a(j) < a(j-1)) {
          swap(a, j, j-1);
        }
      }
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
     val T = time(bubbleSort(ints));
 
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
