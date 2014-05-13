// class Quad2d
// 
// Represents a box given by [x0,x1) x [y0,y1).
//
class Quad2d(x0:Double, x1:Double, y0:Double, y1:Double) {

  val left:Double = x0;
  val right:Double = x1;
  val bottom:Double = y0;
  val top:Double = y1;
  val ll:Point2d = new Point2d(x0,y0);
  val lr:Point2d = new Point2d(x1,y0);
  val ul:Point2d = new Point2d(x0,y1);
  val ur:Point2d = new Point2d(x1,y1);
  
  // contains
  // 
  // Is the given point within the quadrant?
  //
  def contains(point:Point2d):Boolean = {
    return (point.x >= left) && (point.x < right) && (point.y >= bottom) && (point.y < top);
  }

  // intersectsDisk
  //
  // Is any point within the quadrant within the distance "radius" from
  // the given "center" point?
  //
  def intersectsDisk(center:Point2d, radius:Double):Boolean = {
    // Is the center point within the rectangle?
    if (contains(center)) return true;
    // Are any of the borders close to the center?
    if (left - center.x <= radius && center.x < left) return true;
    if (center.x - right < radius && center.x >= right) return true;
    if (bottom - center.y <= radius && center.y < bottom) return true;
    if (center.y - top < radius && center.y >= top) return true;
    // Otherwise...
    return false;
  }

  // isCoveredByDisk
  //
  // Is this quadrant entirely contained in the specified disk?
  //
  def isCoveredByDisk(center:Point2d,radius:Double):Boolean = {
    if ((ll - center).length <= radius) return true;
    if ((lr - center).length <= radius) return true;
    if ((ul - center).length <= radius) return true;
    if ((ur - center).length <= radius) return true;
    return false;
  }

  // split
  //
  // Build four subquadrants for the given quadrant.
  //
  def split():Array[Quad2d] = {
    val midx = (left + right)/2.0; 
    val midy = (bottom + top)/2.0; 
    return Array[Quad2d](new Quad2d(left,midx,bottom,midy),
			 new Quad2d(midx,right,bottom,midy),
			 new Quad2d(left,midx,midy,top),
			 new Quad2d(midx,right,midy,top));
  }

  // clip
  // 
  // Adjust a point so that its coordinates lie in this quadrant.
  //
  def clip(p:Point2d):Point2d = {
    var position:Point2d = p;
    while (position.x >= right) position.x = position.x - (right - left);
    while (position.x < left) position.x = position.x + (right - left);
    while (position.y >= top) position.y = position.y - (top - bottom);
    while (position.y < bottom) position.y = position.y + (top - bottom);
    return p;
  }  

}
