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
//    scalac Terrain.scala TerrainSearch.scala
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
// DETAILS OF THE TERRAIN REPRESENTATION
//
// The terrain data structure is based on a "winged edge" and/or "half 
// edge" representation of its service.  This is a link-based structure
// with components corresponding to the geometric features of the
// faceted terrain.  Links among geometric features give the surface's
// topology, and are ordered in a way that respects orientation.  These
// features are defined in the file WingedEdge.scala and are used within
// the class Terrain.
//
// There are three geometric features: (1) a vertex, which is a corner
// point of the faceted surface.  It has a location in 3D space.  (2)
// a half-edge, which is a directed seam between two meeting facets,
// connecting two vertices.  (3) a face, which is an polygonal area
// forming a flat portion of the surface.  A face is defined by a
// border of half-edges, spanning that face's corner vertices. A
// vertex has a "fan" of faces that meet at that corner, and also
// the spine of that fan made up of the edges between consecutive faces 
// on that fan.  Those edges have that vertex as one of their two
// vertices.  The other vertex spanned by each edge is a neighbor
// of that vertex.
//
// Thus, the vertices and edges can be interpreted as a graph.  Search
// within that graph leads to search over the terrain's vertices.
//
// Half-edges come in complementary pairs, called "twins".  If a half-edge
// e runs from a vertex u to a vertex v, then it has a twin e' that runs
// from v to u.  Each half-edge has a face associated with it, the one 
// that falls to the right of that edge.  So e has a face f to its right, 
// and its twin e' has a face f' to its right. Those two faces f/f'
// meet at the e/e' seam between u and v. In addition, the half-edges 
// form a circular linked list around their face, doubly-linked with 
// next/prev links.
// 
// Thus, one can treat faces as nodes in a different (called "dual")
// graph structure.  Two neighboring faces can be seen as neighboring
// nodes in this dual graph, they are related pairwise because they 
// share an edge.  
//
// Finally, with all this, it should be noted that a face and a vertex
// don't directly have access to their "adjacency lists" in their 
// respective dual and primal graph structures.  Instead, a face knows
// about one half-edge on its border, and that links to the remaining
// edges, and so indirectly allow us to find neighboring faces.  Also,
// a vertex knows one of its out-directed edges, and these can be
// used to determine all the neighbor vertices.
//
// GEOMETRIC INFORMATION
//
// The Terrain class takes a text file of elevation data.  These .trrn
// files consist of
//
//    line 1: a number of elevation samples
//    line 2: the width of the sample grid, in terms of samples
//    line 3: the length of the sample grid, in terms of samples
//    line 4: the scaling factor used to interpret the samples
//    line 5: each sample grid cell's aspect ratio
//    lines: the elevation samples, as integer values between 0 and 255
//
// Included in this folder is two sample files helens.trrn and 
// helens_lo.trrn, along with their respective PGM (portable 
// grey map) image files built using freeware image editing
// programs (e.g. GIMP, GraphicConverter) from a TIFF of Mt. St. Helens
// widely available on the internet.  
//
// The samples get used to construct the terrain, using the winged (half)
// edge data structure described above, as a TIN (triangular irregular
// network).  Each grid cell of the sample elevations gets turned into
// a pair of triangular faces, formed as a result of drawing an arbitrary
// edge between two opposite corner vertices.
//
// Associated with each vertex v, then, is 3D position information, an
// instance variable v.location declared as
//
//   val location:Point3D ...
//
// that can be accessed to determine the vertex sample's x,y location on 
// the grid, along with its height/elevation z.  These coordinates provide
// the surface definition shown by TerrainView.
//
// INTERACTION WITH TERRAINVIEW
//
// The TerrainView program serves as a hike route planner.  When brought up
// the surface view can be oriented by clicking and dragging the mouse.
// With instead a quick, single click, a starting vertex can be selected on
// the terrain.  With a SHIFT single click, an ending vertex gets selected.
// The code then immediately runs the search method in class TerrainSearch
// to determine a route over the train (a series of half edges) between
// the two vertices.
//
// ASSIGNMENT
//
// Modify the graph search so that it finds an "efficient and feasible"
// hike between the two endpoints.
//

import scala.collection.mutable.PriorityQueue
import scala.collection.mutable.Queue;
import scala.collection.mutable.HashSet;
import scala.collection.mutable.HashMap;

class TerrainSearch(terrain:Terrain) {

  def weight(v1:Vertex, v2:Vertex):Double = {
      var retVal:Double = v1.z - v2.z;

      // take absolute value
      if (retVal < 0) { retVal = -retVal; }

      return retVal*1000;

  }

  def pathTo(start:Vertex,end:Vertex):List[Edge] = {

        // marked:
        // Set of all vertices whose neighbors have already been
        // considered.
        //
        // prd:
        // Collection of vertex pairs (v,pi[v]), where pi[v] is
        // the vertex that led to v during the search.
        //
        // linkTo:
        // Collection of vertex pairs (v,e), where e is the edge
        // that led into v during the search.  That edge e corresponds
        // to the directed graph edge (pred(v),v).
        // It is a "map" maintained by the search code.  Each vertex
        // v has at most one entry, its "into" edge.

    val marked:HashSet[Vertex] = new HashSet[Vertex]();
    val pred:HashMap[Vertex,Vertex] = new HashMap[Vertex,Vertex]();
    val linkTo:HashMap[Vertex,Edge] = new HashMap[Vertex,Edge]();

    val dist:HashMap[Vertex,Double] = new HashMap[Vertex,Double]();
    dist(start) = 0;

    val H = new Heap[Vertex](dist);
    H.insert(start);

    while (!H.isEmpty) {
     //   println("Size of dist is " + dist.size); 
        val u:Vertex = H.extractMin(); 
	    for (e <- u.outEdges) { 
            // get associated vertex
	        val v:Vertex = e.to; 

            val alt = dist(u) + weight(u,v);

            // inline intializization of dist
            if (!dist.contains(v)) { 
    //            println("adding " + v + " to dist");
                dist(v) = 10000000;
            }
            if (alt < dist(v)) {
                dist(v) = alt;
                pred.update(v,u);
	            linkTo.update(v,e); // and do the same with its link.

                if (H.contains(v)) {
	                H.decreaseKey(v);       
                } else {
                    H.insert(v)
                }
            }
	    }
    }

    var v:Vertex = end;
    var es:List[Edge] = Nil;

    while (pred.contains(v) && linkTo.contains(v)) {

      // ...shove the link into v onto the front,
    es = linkTo(v)::es;

      // and advance to the prior vertex.
      v = pred(v);
      print("from...");
    }

    // Give back that path to the viewer.
    println("done.");
    println("Path of length "+es.length+" found.");
    return es;
  }    
}
