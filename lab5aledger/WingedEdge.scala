class Vertex(loc:Point3D) {

  val location:Point3D = loc;
  var first:Edge = null;

  def x:Double = location.x;
  def y:Double = location.y;
  def z:Double = location.z;

  def maybeFirst(e:Edge):Unit = {
    if (first == null) {
      setFirst(e);
    }
  }

  def setFirst(e:Edge):Unit = {
    first = e;
  }

  override def toString:String = {
    return location.toString;
  }

  def neighbors:Iterator[Vertex] = new Iterator[Vertex] {
    private val es:Iterator[Edge] = outEdges;
    def hasNext:Boolean = es.hasNext;
    def next:Vertex = {
      es.next.to;
    }
  }

  def fan:Iterator[Face] = new Iterator[Face] {
    private val es:Iterator[Edge] = outEdges;
    def hasNext:Boolean = es.hasNext;
    def next:Face = {
      es.next.face;
    }
  }

  def outEdges:Iterator[Edge] = new Iterator[Edge] {
    private var hasStarted:Boolean = false;
    private var current:Edge = first;
    def hasNext:Boolean = {
      return current != null && (!hasStarted || current != first);
    }
    def next:Edge = {
      hasStarted = true;
      val e:Edge = current;
      current = current.prev.twin;
      return e;
    }
  }

  def >(other:Vertex):Boolean = {
    return this.z > other.z;
  }

  def >=(other:Vertex):Boolean = {
    return this.z >= other.z;
  }

  def <(other:Vertex):Boolean = {
    return this.z < other.z;
  }

  def <=(other:Vertex):Boolean = {
    return this.z <= other.z;
  }

  def isPeak():Boolean = {
    var isMax:Boolean = true;
    for (v <- neighbors) {
      if (v > this) {
	isMax = false;
      }
    }
    return isMax;
  }
}

class Edge(f:Vertex,t:Vertex) {

  val from:Vertex = f;
  val to:Vertex = t;
  var twin:Edge = null;
  var face:Face = null;
  var next:Edge = null;
  var prev:Edge = null;
  val me:Edge = this;
  from.maybeFirst(this);

  var marker:Boolean = false;
  def clear():Unit = { marker = false; }
  def mark():Unit = { marker = true; }
  def isMarked():Boolean = { return marker; }

  def isFirst():Unit = {
    from.setFirst(this);
  }

  def twinUp(e:Edge):Unit = {
    this.twin = e;
    e.twin = this;
  }

  def iterator:Iterator[Edge] = new Iterator[Edge] {
    private var hasStarted:Boolean = false;
    private var first:Edge = me;
    private var current:Edge = me;
    def hasNext:Boolean = {
      return current != null && (!hasStarted || current != first);
    }
    def next:Edge = {
      hasStarted = true;
      val e:Edge = current;
      current = current.next
      return e;
    }
  }
}

class Face(es:Array[Edge],o:Boolean = false) {

  val isOuter:Boolean = o;
  val side:Edge = es(0);

  linkBorder();

  private def linkBorder():Unit = {
    for (i <- 0 until es.length) {
      val e = es(i);
      e.face = this;
      e.next = es((i+1)%es.length);
      e.prev = es((i+es.length-1)%es.length);
    }
  }

  def border:Iterator[Edge] = {
    return side.iterator;
  }

  def borderFrom(e:Edge):Iterator[Edge] = {
    return e.iterator;
  }

  def vertices:Iterator[Vertex] = new Iterator[Vertex] {
    private val b:Iterator[Edge] = border;
    def hasNext:Boolean = {
      return b.hasNext;
    }
    def next:Vertex = {
      val e:Edge = b.next;
      return e.from;
    }
  }

  def normal():Vector3D = {
    val next:Edge = side.next;
//    return (side.to.location-side.from.location).cross(next.to.location-next.from.location).direction();
//  HACK:
    return (side.from.location-side.to.location).cross(next.to.location-next.from.location).direction();
  }
}
