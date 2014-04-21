class Point2D(x0:Double, y0:Double) {

  var x:Double = x0;
  var y:Double = y0;

  def +(v:Vector2D):Point2D = {
    return new Point2D(x+v.dx, y+v.dy);
  }

  def -(v:Vector2D):Point2D = {
    return new Point2D(x-v.dx, y-v.dy);
  }

  def -(p:Point2D):Vector2D = {
    return new Vector2D(x-p.x, y-p.y);
  }

  override def toString():String = {
    return "["+x+","+y+"]";
  }
}

object Point2D {
  val ORIGIN = new Point2D(0.0,0.0);
  def apply():Point2D = { 
    return ORIGIN;
  }
  def apply(x:Double, y:Double):Point2D = { 
    return new Point2D(x,y);
  }
}
