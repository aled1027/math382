//
// class Boid
//
// Implements one of the flocking agents, one of Reynolds' "boids".
// A boid has a velocity and a position.  These get updated over
// time using first-order Euler integration.  This is handled in
// the method "step", which relies on the Point2D and Vector2D
// methods to modify a boid's position according to its velocity.
// Its velocity is also updated, according to a behaviorial model
// that's specified within class Flok.
//
class Boid(p0:Point2d, v0:Vector2d, flok:Flok) {

  var position:Point2d = p0;
  var velocity:Vector2d = v0;

  // reset
  //
  def reset():Unit = {
    position = p0;
    velocity = v0;
  }

  // step
  //
  // This computes the boid's physical presence in the next time step.
  //
  def step(dt:Double):Unit = {

    // determine direction boid would like to head
    val a:Vector2d = flok.acceleration(this);
    
    // adjust velocity according to that thrust, but trim the speed (here, we make it constant)
    velocity = velocity + (a * dt);
    velocity = velocity.direction() * flok.MAXIMUM_SPEED;

    // update the position, but keep within the bounds of the world
    position = flok.bounds.clip(position + (velocity * dt));
 }

  // toString
  //
  // Boid gets output as its position.
  //
  override def toString:String = {
    return position.toString();
  }

}
