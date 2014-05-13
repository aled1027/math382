class Vector2d(x0:Double, y0:Double) {

  val EPSILON = 0.000001

  val dx:Double = x0;
  val dy:Double = y0;

  def +(v:Vector2d):Vector2d = {
    return this.plus(v);
  }

  def -(v:Vector2d):Vector2d = {
    return this.minus(v);
  }

  def unary_-():Vector2d = {
    return this.neg;
  }

  def *(s:Double):Vector2d = {
    return this.times(s);
  }

  def /(s:Double):Vector2d = {
    return this.over(s);
  }

  def unit():Vector2d = {
    return this.direction();
  }

  def perp():Vector2d = {
    return Vector2d(-dy,dx);
  }

  def length():Double = {
    return this.magnitude();
  }

  def x(v:Vector2d):Double = {
    return this.cross(v);
  }

  def cross(v:Vector2d):Double = {
    return dx*v.dy - dy*v.dx;
  }

  def dot(v:Vector2d):Double = {
    return dx*v.dx + dy*v.dy;
  }

  def plus(v:Vector2d):Vector2d = {
    return new Vector2d(dx+v.dx,dy+v.dy);
  }

  def minus(v:Vector2d):Vector2d = {
    return new Vector2d(dx-v.dx,dy-v.dy);
  }

  def neg():Vector2d = {
    return new Vector2d(0.0-dx, 0.0-dy);
  }

  def times(s:Double):Vector2d = {
    return new Vector2d(s*dx,s*dy);
  }

  def over(s:Double):Vector2d = {
    return new Vector2d(dx/s,dy/s);
  }

  def magnitude():Double = {
    return Math.sqrt(this.dot(this));
  }

  def direction():Vector2d = {
    val m:Double = this.magnitude();
    if (m > EPSILON) {
      return this.times(1.0/m);
    } else {
      return Vector2d.NULL;
    }
  }

  override def toString():String = {
    return "["+dx+","+dy+"]";
  }
}

object Vector2d {
  val NULL:Vector2d = new Vector2d(0.0,0.0);
  def apply():Vector2d = {
    return NULL;
  }
  def apply(dx:Double,dy:Double):Vector2d = {
    return new Vector2d(dx,dy);
  }
}
