//
// class BoidTreeBogus
//
// This is a poor implementation of a spatial data structure
// that has the same two methods as our BoidTree.  It simply
// tracks a collection of boids as a list.  To insert into
// structure, the additional boid gets placed onto the front.
// To find all the boids within a certain distance of some 
// boid, it simply scans through the whole list and checks
// each one.
//
class BoidTreeBogus(q:Quad2d) extends BoidTree(q) {

  // List of boids.
  var boids:List[Boid] = Nil;

  // insert(b)
  //
  // Add b to the list of boids.
  //
  override def insert(b:Boid):Unit = {
    boids = b::boids;
  }

  // within(d,b0)
  //
  // Find all boids that are within a distance d of some
  // given boid b0.
  //
  override def within(distance:Double,b0:Boid):List[Boid] = {

    // accumulate a list of nearby boids in the list bs
    var bs:List[Boid] = Nil;

    // For each boid...
    for (b <- boids) {
      // if b and b0 are close to each other, then include this b.
      if ((b0.position - b.position).length <= distance) {
	bs = b::bs;
      }
    }
    return bs;
  }
}
