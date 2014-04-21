class Vector2D(x0:Double, y0:Double) {

  val dx:Double = x0;
  val dy:Double = y0;

  def +(v:Vector2D):Vector2D = {
    return this.plus(v);
  }

  def -(v:Vector2D):Vector2D = {
    return this.minus(v);
  }

  def unary_-():Vector2D = {
    return this.neg;
  }

  def *(s:Double):Vector2D = {
    return this.times(s);
  }

  def /(s:Double):Vector2D = {
    return this.over(s);
  }

  def unit():Vector2D = {
    return this.direction();
  }

  def length():Double = {
    return this.magnitude();
  }

  def x(v:Vector2D):Double = {
    return this.cross(v);
  }

  def cross(v:Vector2D):Double = {
    return return dx*v.dy - dy*v.dx;
  }

  def dot(v:Vector2D):Double = {
    return dx*v.dx + dy*v.dy;
  }

  def plus(v:Vector2D):Vector2D = {
    return new Vector2D(dx+v.dx,dy+v.dy);
  }

  def minus(v:Vector2D):Vector2D = {
    return new Vector2D(dx-v.dx,dy-v.dy);
  }

  def neg():Vector2D = {
    return new Vector2D(0.0-dx, 0.0-dy);
  }

  def times(s:Double):Vector2D = {
    return new Vector2D(s*dx,s*dy);
  }

  def over(s:Double):Vector2D = {
    return new Vector2D(dx/s,dy/s);
  }

  def magnitude():Double = {
    return Math.sqrt(this.dot(this));
  }

  def direction():Vector2D = {
    return this.times(1.0/this.magnitude());
  }
}

object Vector2D {
  val NULL:Vector2D = new Vector2D(0.0,0.0);
  def apply():Vector2D = {
    return NULL;
  }
  def apply(dx:Double,dy:Double,dz:Double):Vector2D = {
    return new Vector2D(dx,dy);
  }
}
