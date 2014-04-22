import scala.collection.mutable.HashMap;

class Heap[T:ClassManifest](priority:HashMap[T,Double]) {

  var capacity:Int = 100;
  val indexOf:HashMap[T,Int] = new HashMap[T,Int]();
  var elements:Array[T] = new Array[T](capacity+1);
  var size:Int = 0;

  def insert(item:T):Unit = {
    if (size == capacity) {
      val n:Array[T] = new Array[T](2*capacity+1);
      for (i <- 1 to capacity) {
	n(i) = elements(i);
      }
      elements = n;
      capacity = 2*capacity;
    }
    size = size + 1;
    elements(size) = item;
    indexOf.update(item,size);
    decreasedKeyAt(size);
  }

  def contains(item:T):Boolean = {
    return indexOf.contains(item);
  }

  def isEmpty():Boolean = {
    return size == 0;
  }

  def decreaseKey(item:T):Unit = {
    decreasedKeyAt(indexOf(item));
  }

  def extractMin():T = {
    val x:T = elements(1);
    indexOf.remove(x);
    swap(1,size);
    size = size-1;
    heapifyAt(1);
    return x;
  }

  private def left(i:Int):Int = 2*i;
  private def right(i:Int):Int = 2*i+1;
  private def parent(i:Int):Int = i/2;
  private def heapifyAt(i:Int):Unit = {
    var mini:Int = i;
    val l:Int = left(i);
    val r:Int = right(i);
    if (l <= size && priority(elements(l)) < priority(elements(mini))) {
      mini = l;
    }       
    if (r <= size && priority(elements(r)) < priority(elements(mini))) {
      mini = r;
    }       
    if (mini != i) {
      swap(i,mini);
      heapifyAt(mini);
    }
  }
  private def decreasedKeyAt(index:Int):Unit = {
    var i:Int = index;
    val p:Double = priority(elements(i));
    while (i > 1 && priority(elements(parent(i))) > p) {
      swap(i,parent(i));
      i = parent(i);
    }
  }
  private def swap(i:Int,j:Int):Unit = {
    var x:T = elements(i);
    var y:T = elements(j);
    elements(i) = y;
    elements(j) = x;
    indexOf(x) = j;
    indexOf(y) = i;
  }
}
