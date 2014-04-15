object queue {
  def main(args:Array[String]):Unit = {
    println("main");
  }
}

class Stack(c:Int) {

  val capacity:Int = c;
  var size:Int = 0;
  val elements:Array[Int] = new Array[Int](c);

  def getSize():Int = {
    return size;
  }

  def isEmpty():Boolean = {
    return (size == 0);
  }

  def push(x:Int):Unit = {
    elements(size) = x;
    size = size + 1;
  }

  def pop():Int = {
    size = size - 1;
    return elements(size);
  }
}

class Queue(c:Int) {
  val capacity:Int = c;
  var size:Int = 0;
  var isLeft:Boolean = true; // tells us if the values are in the left or right stack

  val leftStack:Stack = new Stack(c); // always in correct order
  val rightStack:Stack = new Stack(c); // always in reverse order

  def isEmpty():Boolean = {
    return (size == 0);
  }

  def getSize():Int = {
    return size;
  }

  def enqueue(x:Int):Unit = {
    if (isLeft) {
      // if values are in the left stack, move everything to right stack
      while (!leftStack.isEmpty()) {
        rightStack.push(leftStack.pop());
      }
      rightStack.push(x);
    } else  {
      // if values are in the right stack, put the new value on the bottom of the right stack
      leftStack.push(x);
      while (!rightStack.isEmpty()) {
        leftStack.push(rightStack.pop());
      }
    }
    size+=1;
    isLeft = !isLeft;
  }

  def dequeue():Int = {
    if (size == 0) {
      return 0;
    }

    // if the values aren't on the left stack, move them there
    if (!isLeft) {
      while (!rightStack.isEmpty()) {
        leftStack.push(rightStack.pop());
      }
      isLeft = true;
    }
    size-=1;
    return leftStack.pop();
  }

}
