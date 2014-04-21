//
// MATH 382
// Spring 2014
// Lab 6: hike planner
// Due: 4/25/2014
// 
// OVERVIEW
//
// Enclosed is this folder is a series of Scala classes that
// implement:
//    * basic geometric privileges of points and vectors (Point*.scala, Vector*.scala)
//    * the components of a winged-edge data structure (WingedEdge.scala)
//    * a terrain representation from a raster image of elevation data (Terrain.scala)
//    * a terrain viewer that includes a path search engine (TerrainView.scala)
//    * a draft terrain search engine (TerrainSearch.scala)
// The program itself is provided as the "main" procedure in the 
// class TerrainView.  To run it, you'll want to compile the files 
// in order with the series
//    scalac Point3D.scala Vector3D.scala Point2D.scala Vector2D.scala 
//    scalac WingedEdge.scala
//    scalac TerrainSearch.scala
//    scalac TerrainView.scala
// and then execute scala with the command
//    scala TerrainView helens_lo.trrn
// This loads the given elevation data, a data set of the area in and 
// immediately around the Mt. St. Helens volcano, and displays it in
// relief.
//
// Within this viewer, clicking and dragging the mouse allows you to
// spin the terrain so that it can be examined from different angles.
// You can also plot two marks within the terrain: a start and an end.  
// Select a start point by a single click.  Select an end point by
// a single click, made while holding the SHIFT key.
//
// With the end points set, the code executes TerrainSearch.fromTo
// which, at the moment, runs BFS on the graph of the terrain to
// find a path between the two vertices.
//
// ASSIGNMENT
//
// Modify the graph search so that it finds an "efficient and feasible"
// hike between the two endpoints.
//

import scala.collection.mutable.Queue;
import scala.collection.mutable.HashSet;
import scala.collection.mutable.HashMap;

class TerrainSearch(terrain:Terrain) {

  def pathTo(start:Vertex,end:Vertex):List[Edge] = {
    val marked:HashSet[Vertex] = new HashSet[Vertex]();
    val pred:HashMap[Vertex,Vertex] = new HashMap[Vertex,Vertex]();
    val linkTo:HashMap[Vertex,Edge] = new HashMap[Vertex,Edge]();
    val Q:Queue[Vertex] = new Queue[Vertex]();
    Q.enqueue(start);
    while (!Q.isEmpty) {
      val u:Vertex = Q.dequeue;
      if (!marked.contains(u)) {
	marked.add(u);
	for (e <- u.outEdges) {
	  val v:Vertex = e.to;  
	  if (!marked.contains(v)) {
	    Q.enqueue(v);
	    pred.update(v,u);
	    linkTo.update(v,e);
	  }
	}
      }   
    }

    var v:Vertex = end;
    var es:List[Edge] = Nil;
    print("Building path...");
    while (pred.contains(v) && linkTo.contains(v)) {
      es = linkTo(v)::es;
      v = pred(v);
      print("from...");
    }
    println("done.");
    println("Path of length "+es.length+" found.");
    return es;
  }    
}
