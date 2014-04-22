class Vector3D(x0:Double, y0:Double, z0:Double) {

  val dx:Double = x0;
  val dy:Double = y0;
  val dz:Double = z0;

  def +(v:Vector3D):Vector3D = {
    return this.plus(v);
  }

  def -(v:Vector3D):Vector3D = {
    return this.minus(v);
  }

  def unary_-():Vector3D = {
    return this.neg;
  }

  def *(s:Double):Vector3D = {
    return this.times(s);
  }

  def /(s:Double):Vector3D = {
    return this.over(s);
  }

  def unit():Vector3D = {
    return this.direction();
  }

  def length():Double = {
    return this.magnitude();
  }

  def x(v:Vector3D):Vector3D = {
    return this.cross(v);
  }

  def cross(v:Vector3D):Vector3D = {
    return new Vector3D(dy*v.dz - dz*v.dy,dz*v.dx - dx*v.dz,dx*v.dy - dy*v.dx);
  }

  def dot(v:Vector3D):Double = {
    return dx*v.dx + dy*v.dy + dz*v.dz;
  }

  def plus(v:Vector3D):Vector3D = {
    return new Vector3D(dx+v.dx,dy+v.dy,dz+v.dz);
  }

  def minus(v:Vector3D):Vector3D = {
    return new Vector3D(dx-v.dx,dy-v.dy,dz-v.dz);
  }

  def neg():Vector3D = {
    return new Vector3D(0.0-dx, 0.0-dy, 0.0-dz);
  }

  def times(s:Double):Vector3D = {
    return new Vector3D(s*dx,s*dy,s*dz);
  }

  def over(s:Double):Vector3D = {
    return new Vector3D(dx/s,dy/s,dz/s);
  }

  def magnitude():Double = {
    return math.sqrt(this.dot(this));
  }

  def direction():Vector3D = {
    return this.times(1.0/this.magnitude());
  }

  override def toString():String = {
    return "<"+dx+","+dy+","+dz+">";
  }
}

object Vector3D {
  val NULL:Vector3D = new Vector3D(0.0,0.0,0.0);
  def apply():Vector3D = {
    return NULL;
  }
  def apply(dx:Double,dy:Double,dz:Double):Vector3D = {
    return new Vector3D(dx,dy,dz);
  }
}
