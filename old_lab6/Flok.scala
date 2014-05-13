//
// LAB 6: using a quadtree to a speed up an n-body simulation
//        or, alternatively, Floks and Boidz!
// 
// Due: Thursday, May 15th, 8am. 
// Hand in: an electronic version of this folder, and
//          a paper printout of your BoidTree.scala code.
//
// DESCRIPTION:
//
// The code below keeps track of a flok of boids for a boid 
// simulation.
//
// The flok lives in a world of width w and height h, whose
// bounds are the half-open rectangle [-w/2,w/2) x [-h/2,h/2).
// 
// All the boids in the flok have the same behavior, and that 
// behavior is specified by parameters that govern how they 
// reason about the boids near them in the flock.  There are
// three main concerns that govern a boid's behavior:
//
//   * SEPARATION: boids don't want to be too close to each other
//   * VELOCITY MATCHING: boids want to mimic nearby birds' headings
//   * COHESION: boids want to be around other boids
//
// For each of these three components of behavior, a boid pays 
// attention to boids within a certain distance of them.  Each
// component has a RADIUS of concern.  Furthermore, each component
// has COEFFicient of influence on their impulse trajectory.
//
// Each boid's determination of its acceleration, at each moment, 
// gets calculated by the Flok.accelerate method below. The three 
// components of behavior described above are encapsulated in a 
// unit vector that summarizes the influence of the nearby boids.
//
// In the code's current state, there is a boid collection built
// in the Flok.step method.  That collection, named tree, is a 
// "spatial data structure" that answers the geometric question: 
// "Which subset of the boids are within a certain distance of this
// particular boid?"  This is sometimes called a "circular range
// query".  The method call is tree.within(d,b), and gives back a 
// list of boids in the tree's collection that are within distance
// d of boid b. 
//
// Presently, the tree is implemented very simply.  It is not 
// implemented as a tree at all.  It is just a "flat structure"
// of a Scala list.  In the code for Flok.step, you'll see that
// a BoidTreeBogus object is built.  All the boids in the flok 
// are inserted into that with tree.insert, and then, within the 
// accelerate calculation method tree.within is used on that
// the flat/bogus structure.
//
// For the assignment, you are to implement the "BoidTree"
// class, using the existing code in BoidTree.scala as a guide.
// You'll want to make these modifications:
//     * change Flok.step below so it instead builds a BoidTree
//       rather than a BoidTreeBogus
//     * complete the code for BoidTree.insert
//     * complete the code for BoidTree.within
// You'll want to look at the BoidTree.scala file for details on
// this implementation.
//
class Flok(size:Int, w:Double, h:Double) {

  // world bounds
  val WIDTH = w;
  val HEIGHT = h;
  var bounds:Quad2d = new Quad2d(-WIDTH/2.0,WIDTH/2.0,-HEIGHT/2.0,HEIGHT/2.0);

  val MAXIMUM_SPEED = 1.0;
  val EPSILON = 0.000001

  // behavior parameters
  val SEPARATION_RADIUS = 3.0;
  val VELOCITY_MATCH_RADIUS = 10.0;
  val COHESION_RADIUS = 7.0;
  val SEPARATION_COEFF = 1.0;
  val VELOCITY_MATCH_COEFF = 1.0;
  val COHESION_COEFF = 1.0;

  // collections of boids
  val boids:Array[Boid] = new Array[Boid](size);
  var tree:BoidTree = null;

  // instrumentation to count force calcs
  var tick = 0;
  var S_calcs = 0;
  var V_calcs = 0;
  var C_calcs = 0;
    
  // initialization: give all the boids a random position and trajectory
  for (i <- 0 until size) {
    val p0 = Point2d.ORIGIN + Vector2d(Math.random()*WIDTH-WIDTH/2.0,Math.random()*HEIGHT-HEIGHT/2.0);
    val v0 = (Vector2d(Math.random()*WIDTH-WIDTH/2.0,Math.random()*HEIGHT-HEIGHT/2.0).unit)*MAXIMUM_SPEED;
    boids(i) = new Boid(p0, v0, this);
    boids(i).reset();
  }

  //
  // acceleration
  //
  // Figure out the acceleration direction of one boid
  // by it looking at the others.
  //
  def acceleration(boid:Boid):Vector2d = {

    var accel = Vector2d.NULL;

    // Try to avoid nearby boids.
    var separate:Vector2d = Vector2d.NULL;
    val toAvoid:List[Boid] = tree.within(SEPARATION_RADIUS,boid);
    for (other <- toAvoid if other != boid) {
      S_calcs = S_calcs + 1;
      val offset = other.position - boid.position;
      val distance = offset.length;
      if (distance > EPSILON) {
	separate = separate - (offset / (distance * distance));
      }
    }
    accel = accel + (separate.unit * SEPARATION_COEFF) ;
      
    // Try to mimic the heading of nearby boids.
    var align:Vector2d = Vector2d.NULL;
    val toMimic:List[Boid] = tree.within(VELOCITY_MATCH_RADIUS,boid);
    for (other <- toMimic if other != boid) {
      V_calcs = V_calcs + 1;
      val offset = other.position - boid.position;
      val distance = offset.length;
      if (distance > EPSILON) {
	align = align + other.velocity;
      }
    }
    accel = accel + (align.unit * VELOCITY_MATCH_COEFF) ;

    // Try to be around other boids.
    var cohere:Vector2d = Vector2d.NULL;
    val toJoin:List[Boid] = tree.within(COHESION_RADIUS,boid);
    for (other <- toJoin if other != boid) {
      C_calcs = C_calcs + 1;
      val offset = other.position - boid.position;
      val distance = offset.length;
      if (distance > EPSILON) {
	cohere = cohere + offset;
      }
    }
    accel = accel + (cohere.unit * COHESION_COEFF) ;
    return accel.unit;
  }

  // step
  //
  // Advances the simulation by one time step.
  //
  def step(dt:Double):Unit = {

    // make an empty quadtree data structure

    // tree = new BoidTree(bounds);
    tree = new BoidTreeBogus(bounds);

    // fill it with boids
    for (b <- boids) {
      tree.insert(b);
    }

    // reset the force counters
    S_calcs = 0;
    V_calcs = 0;
    C_calcs = 0;
    
    // adjust each boid's position
    for (b <- boids) {
      b.step(dt);
    }

    // report, now and then, the calculation count
    if (tick % 256 == 0) {
      print("Force calculations:");
      print(" S="+S_calcs);
      print(" V="+V_calcs);
      print(" C="+C_calcs);
      println(" TOTAL="+(S_calcs+V_calcs+C_calcs));
    }
    tick = tick+1;
  }
}
