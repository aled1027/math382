class Point2d(x0:Double, y0:Double) {

  var x:Double = x0;
  var y:Double = y0;

  def +(v:Vector2d):Point2d = {
    return new Point2d(x+v.dx, y+v.dy);
  }

  def -(v:Vector2d):Point2d = {
    return new Point2d(x-v.dx, y-v.dy);
  }

  def -(p:Point2d):Vector2d = {
    return new Vector2d(x-p.x, y-p.y);
  }

  override def toString():String = {
    return "["+x+","+y+"]";
  }
}

object Point2d {
  val ORIGIN = new Point2d(0.0,0.0);
  def apply():Point2d = { 
    return ORIGIN;
  }
  def apply(x:Double, y:Double):Point2d = { 
    return new Point2d(x,y);
  }
}
