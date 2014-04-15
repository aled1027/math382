// MATH 382: Spring 2014 
// // LAB 4: a linked list based priority queue
// Alex Ledger
// Due: 4/2/14
//
// The code below gives an implementation of // a queue data structure that uses a doubly 
// linked list to store its items.
//
// The code is partially adapted to implement,
// instead, a queue of items with associated
// "key" values that get interpreted as the
// items' priorities.  Items with lower
// priorities are pulled from the queue before
// those with higher priorities.  
//
// Following CLRS' treatment of "min queues"
// the methods are instead:
//
//    isEmpty
//    insert    (instead of enqueue
//    min                   head
//    removeMin             dequeue)
//
// You are to complete this implementation
// by fixing the "insert" method so that
// it places the inserted value's node
// in order within the linked list.
//
// In addition, I would like you to implement
// two more methods:
//
//    decreaseKey
//    delete
//    
// These each take a "Handle" into the queue
// data structure.  The first moves an item
// closer to the front of the queue, due to
// a decrease in its key value.  The second
// deletes an item from the queue.
//
// A Handle is a kind of receipt from the
// insert operation, one that allows a 
// client to refer to items that it has
// placed into the queue.  In this linked
// list implementation, a Handle object is
// just a wrapper around that item's linked
// list node.
//
// So, for example, suppose you insert a data
// value 107 with priority 3 into a priority
// queue P, you'd get back a handle h like
// so:
//
//    val h:Handle = P.insert(3,107);
//
// Maybe, later on, you decide that the value
// 107 should have a new key/priority 2, one
// that's lower.  You could modify its place
// in the queue with:
//
//    P.decreaseKey(2,h);
//
// which pulls 107 closer to the front of the
// queue.
//
// ASSIGNMENT
//
// Complete the code for those three methods.
// When you hand in the code, include a README
// that contains a transcript of the tests
// you used to convince yourself that the
// code worked.
//
// NOTE:
// I've made first/last fields of priority queues
// private.  You may want to remove the "private" 
// designation when you are debugging your code.
//

class Handle(n:LNode) {
  val lNode:LNode = n;
}

class LNode(k:Int,d:Int) {
  var key:Int = k;
  val data:Int = d;
  var next:LNode = null;
  var prev:LNode = null;
  override def toString:String = {
    return data+"@"+key;
  }
}
 
class PQueue {

  private var last:LNode = null;
  private var first:LNode = null;

  def isEmpty():Boolean = {
    return (first == null);
  }

  override def toString:String = {
    if (isEmpty()) {
      return "[ ]";
    } else {
      var s:String = "[";
      s = "[ ( " + first + " ) ";
      var x:LNode = first.next;
      while (x != null) {
      	s = s + x + " ";
	      x = x.next;
      }
      return s + "]"
    }
  }

  def insert(key:Int,obj:Int):Handle = {
    val item:LNode = new LNode(key,obj); // our new node 

    if (first == null) { // aka, if queue is empty:
      first = item;
      last = item;
    } else if (item.key < first.key) { // if item should go first
      item.next = first;
      item.prev = null;
      first.prev = item;
      first = item;
    } else if (item.key > last.key) { // if item should go last
      item.prev = last;
      item.next = null;
      last.next = item;
      last = item;
    } else { // if item should go in the middle
      var x:LNode = last;
      while ((x != null) && (item.key < x.key)) {
        x = x.prev; 
      } 
      x.next.prev = item;
      item.next = x.next;
      x.next = item;
      item.prev = x;
    }
    return new Handle(item);
  }

  def decreaseKey(key:Int,item:Handle):Unit = {
    item.lNode.key = key;
    if (item.lNode == first) {
      return;
    } else if (item.lNode.key < first.key) {
      // if it should be moved to front
      last = item.lNode.prev;
      last.next = null;

      item.lNode.next = first;
      item.lNode.prev = null;
      first.prev = item.lNode;
      first = item.lNode;
      return;
    } else if (item.lNode.key > last.prev.key) {
      // if the node is going to stay last
      return;
    } else {
      // all other cases
      var x:LNode = item.lNode.prev;
      // stitch together linked list
      if (item.lNode == last) {
        item.lNode.prev.next = null;
        last = item.lNode.prev;
      } else if (item.lNode == first) {
        item.lNode.next.prev = null;
      } else {
        item.lNode.prev.next = item.lNode.next;
        item.lNode.next.prev = item.lNode.prev;
      }
      while ((x != null) && (item.lNode.key < x.key)) {
        x = x.prev;
      } 
      // put it in there:
      x.next.prev = item.lNode;
      item.lNode.next = x.next;
      x.next = item.lNode;
      item.lNode.prev = x;
      
      if (item.lNode.key < first.key) {
        first = item.lNode;
      }
    }
  }

  def delete(item:Handle):Unit = {
    if (item.lNode == first) {
      first = first.next;
    } else if (item.lNode.next == null) {
      last = item.lNode.prev;
      last.next = null;
    } else {
      item.lNode.prev.next = item.lNode.next;
      item.lNode.next.prev = item.lNode.prev;
    }
    // these are here but not necessary
    item.lNode.prev = null;
    item.lNode.next = null;
  }

  def min():Int = {
    return first.data;
  }

  def removeMin():Int = {
    val v:Int = first.data;
    first = first.next;
    if (first != null) {
      first.prev = null;
    }
    return v;
  }
}

object pqObj {
  def main(args:Array[String]) {
    val pq:PQueue = new PQueue();
    var f:Handle = pq.insert(11,11);
    var d:Handle = pq.insert(7,7);
    var a:Handle = pq.insert(5,5);
    var e:Handle = pq.insert(9,9);
    var c:Handle = pq.insert(3,3);
    println(pq.toString);
    pq.decreaseKey(10,f);
    println(pq.toString);

  }
}
