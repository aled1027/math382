//
// class BoidTree
//
// An (unfinished) implementation of a quadtree data
// structure for a collection of boids.
//
// A quadtree is a hierarchical data structure for 
// organizing a collection of points in a bounded
// region of the plane.  It is a recursive data
// structure, with leaf nodes and internal nodes.
// Internal nodes have exactly four nodes as 
// children.  Leaves have no children.
//
// Each quadtree object is a node in this tree, 
// of one of three types:
//
//  * an empty leaf: this governs a region of the 
//       plane that contains no collection points.
//
//  * a filled leaf: this governs a region that 
//       contains exactly one point.
//
//  * an internal node: this governs a region that
//       contains two or more points.  It has four
//       quadtree children, each governing one of
//       the four, equal-sized subquadrants of its
//       quadrant. Quadrant 0 is to the lower left,
//       1 is the lower right, 2 is the upper left,
//       and 3 is the upper right.
// 
// We represent a quadtree node for Boid objects,
// each BoidTree instance, with four pieces of 
// information:
//
//    bounds: the rectangle in the plane governed by
//            the node
//    count: the number of points in governed by this
//            node
//    boid: if count=1 then this should be set to the 
//            boid in this "full leaf"
//    children: if count>1 then these should be set to
//            the four subquadrant's nodes
//
// You'll need to write the code for "insert" and "within",
// below.  I have hints as to how these should be 
// written.  You will also want to check BoidTreeBogus
// to see how lists work for writing "within".  You'll
// also want to look at the methods of the class Quad2d
// to see how to handle geometric queries and quadrant
// subdivision.
//
class BoidTree(q:Quad2d) {

  // the coordinates of the quadrant of this subtree
  private val bounds:Quad2d = q;

  // the total boids living in this quadrant
  private var count:Int = 0;

  // the subquadrants, valid if count > 1
  private var children:Array[BoidTree] = null;

  // the one boid, valid if count == 1
  private var boid:Boid = null;

  // insert
  //
  // Add a boid b to this quadrant, possibly
  // making subquadrants.
  def insert(b:Boid):Unit = {
    if (count == 0) {
      // Add this one boid.

      // ...
    } else if (count == 1) {
      // Split into subquadrants (see Quad2d split)
      // then reinsert the existing boid and then
      // insert this one.

      // ...
    } else {
      // Add to the appropriate subquadrant's child.
      
      // ...
    }
  }

  // within
  //
  // Find all boids within this subtree that are 
  // within the given distance to this one boid.
  //
  def within(distance:Double,other:Boid):List[Boid] = {
    
    // See Quad2d.coversDisk.  That method will help
    // you quickly check whether any boids in this
    // subtree will be within range.

    // If count == 1
    // you'll want to use a Boid's "position" (e.g.
    // boid.position and other.position) to have
    // access to the Point2d where the boid sits.

    // If count > 1
    // you'll want to check the children, recursively,
    // and merge all the lists with the list append
    // operation :::
    //

    // ...bogus return value, for now...
    return List[Boid]();
  }
}
