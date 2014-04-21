import scala.io.Source;

class Terrain(filename:String) {

  val lines:Iterator[String] = Source.fromFile(filename).getLines
  val width:Int = lines.next.toInt-1;
  val length:Int = lines.next.toInt-1;
  val hscale:Double = lines.next.toDouble;
  val lscale:Double = lines.next.toDouble;
  val heights:Array[Double] = new Array[Double]((width+1)*(length+1));
  readHeights(lines);
  val dx:Double = 2.0/width;
  val dy:Double = dx*lscale;

  val vertices:Array[Vertex] = new Array[Vertex]((width+1)*(length+1));
  val edges:Array[Edge] = new Array[Edge](6*width*length);
  val faces:Array[Face] = new Array[Face](2*width*length);
  val border:Array[Edge] = new Array[Edge](2*(width+length));
  var outside:Face = null;
  val S:Int = 0;  val N:Int = 1;  val SE:Int = 2;  val NW:Int = 3;  val E:Int = 4;  val W:Int = 5;
  initStructure();

  private def readHeights(ls:Iterator[String]):Unit = {
    var h:Int = 0;
    for (r <- 0 to length) {
      for (c <- 0 to width) {
	val v:Double = ls.next.toDouble;
	heights(h) = v/hscale;
	h = h + 1;
      }
    }
  }

  private def initStructure():Unit = {
    makeVertices();
    makeTriangles();
    linkUp();
    makeBorder();
  }

  private def makeVertices():Unit = {
    var vi:Int = 0;
    for (i <- 0 to length) {
      for (j <- 0 to width) {
	val p:Point3D = new Point3D(dx * (width-j) - 1.0, dy * i - lscale, heights(vi));
	vertices(vi) = new Vertex(p);
	vi = vi + 1;
      }
    }
  }

  private def makeTriangles():Unit = {
    var ei = 0;
    var fi = 0;
    for (i <- 0 until length) {
      for (j <- 0 until width) {
	
	// Make all the edges within a quadrant whose
	// upper-left corner is vertex (i,j).
	//
	// (i,j)
	//     *-*
	//     |\|
	//     *-*
	//
	val s = new Edge(vertex(i,j),vertex(i+1,j));
	val n = new Edge(vertex(i+1,j+1),vertex(i,j+1));
	val se = new Edge(vertex(i,j),vertex(i+1,j+1));
	val nw = new Edge(vertex(i+1,j+1),vertex(i,j));
	val e = new Edge(vertex(i+1,j),vertex(i+1,j+1));
	val w = new Edge(vertex(i,j+1),vertex(i,j));
	
	// Add all six to the edge collection.
	edges(ei+S) = s;
	edges(ei+N) = n;
	edges(ei+SE) = se;
	edges(ei+NW) = nw;
	edges(ei+E) = e;
	edges(ei+W) = w;

	// Make the two triangular faces: lower-left and 
	// upper-right.
	faces(fi) = new Face(Array(nw,s,e));
	faces(fi+1) = new Face(Array(se,n,w));
	
	// Update the index into the edge (face) collection.
	ei = ei + 6;
	fi = fi + 2;
	
      }
    }
  }

  private def linkUp():Unit = {


    // Links all the border vertices to some outgoing edge.
    // This does so in a way that makes sure that the
    // "fanout" edges of a border vertex are ordered from 
    // one border through the surface interior to the other
    // border.

    // Handle the left border. These link with a down edge.
    //    0 
    //  i @--+.
    //    |  |
    //    V  |
    //    +--+.
    //    
    for (i <- 0 until length) {
      val v = vertex(i,0);
      v.first = edge(i,0,S);
    }

    // Handle the right border. These link with an up edge.
    //         w
    //   i .*--+ 
    //      |  ^
    //      |  |
    //     .+--@ 
    //
    for (i <- 0 until length) {
      val v = vertex(i+1,width);
      v.first = edge(i,width-1,N);
    }
    
    // Handle the top border.  These link with a left edge.
    //      j
    //    0 *<-@
    //      |  |
    //      |  |
    //      +--+
    //      '  '
    for (j <- 0 until width) {
      val v = vertex(0,j+1);
      v.first = edge(0,j,W);
    }

    // Handle the bottom border.  These link with a right edge.
    //      j
    //      .  .
    //      *--+
    //      |  |
    //      |  |
    //    l @->+
    //
    for (j <- 0 until width) {
      val v = vertex(length,j);
      v.first = edge(length-1,j,E);
    }

    //
    // Now link all pairs of twin edges in the interior.
    //
    for (i <- 0 until length-1) {
      for (j <- 0 until width-1) {
	edge(i,j+1,S).twinUp(edge(i,j,N));
	edge(i,j,SE).twinUp(edge(i,j,NW));
	edge(i,j,E).twinUp(edge(i+1,j,W));
      }
    }
  }

  private def makeBorder():Unit = {
    var k = 0;
    // Run along the top border...
    for (j <- 0 until width) {
      val e = new Edge(vertex(0,j),vertex(0,j+1));
      val t = edge(0,j,W);
      border(k) = e;
      e.twinUp(t);
      t.isFirst();
      k = k+1;
    }
    // then down the right border...
    for (i <- 0 until length) {
      val e = new Edge(vertex(i,width),vertex(i+1,width));
      val t = edge(i,width-1,N);
      border(k) = e;
      e.twinUp(t);
      t.isFirst();
      k = k+1;
    }
    // then along the bottom border...
    for (j <- width until 0 by (-1)) {
      val e = new Edge(vertex(length,j),vertex(length,j-1));
      val t = edge(length-1,j-1,E);
      border(k) = e;
      e.twinUp(t);
      t.isFirst();
      k = k+1;
    }
    // and then finish up the left border.
    for (i <- length until 0 by (-1)) {
      val e = new Edge(vertex(i,0),vertex(i-1,0));
      val t = edge(i-1,0,S);
      border(k) = e;
      e.twinUp(t);
      t.isFirst();
      k = k+1;
    }
    // Now build the outer "face."
    outside = new Face(border,true); 
  }

  def render(b:Boolean):Iterator[Face] = {
    if (b) {
      return faces.iterator;
    } else {
      return new Iterator[Face] {
	private var i:Int = faces.length;
	def hasNext:Boolean = i > 0;
	def next:Face = {
	  i = i-1;
	  return faces(i);
	}
      }
    }
  }
  
  // Looks up the vertex at the given row and column. 
  private def vertex(row:Int,column:Int):Vertex = {
    return vertices((width + 1) * row + column);
  }

  // Looks up the edge associated with the given 
  // quadrant.
  private def edge(row:Int,column:Int,dir:Int):Edge = {
    return edges(6*(width * row + column)+dir);
  }

  def seeds():List[Vertex] = {
    var ss:List[Vertex] = Nil;
    for (v <- vertices) {
      if (v.isPeak()) {
	ss = v::ss;
      }	
    }
    return ss;
  }

}
