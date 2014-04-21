class Point3D(x0:Double, y0:Double, z0:Double) {

  var x:Double = x0;
  var y:Double = y0;
  var z:Double = z0;

/*
  def oproject(view:Vector3D):Point2D = {

  }
*/

  def oproject(spin:Double, tilt:Double):Point3D = {
    val s = spin;
    val t = tilt;
    val px = x * Math.cos(s) - y * Math.sin(s);
    val py = (x * Math.sin(s) + y * Math.cos(s))*Math.sin(-t) + z * Math.cos(-t);
    return new Point3D(px, py, z * Math.sin(-t));
  }

  def +(v:Vector3D):Point3D = {
    return new Point3D(x+v.dx, y+v.dy, z+v.dz);
  }

  def -(v:Vector3D):Point3D = {
    return new Point3D(x-v.dx, y-v.dy, z-v.dz);
  }

  def -(p:Point3D):Vector3D = {
    return new Vector3D(x-p.x, y-p.y, z-p.z);
  }

  override def toString():String = {
    return "["+x+","+y+","+z+"]";
  }
}

object Point3D {
  val ORIGIN = new Point3D(0.0,0.0,0.0);
  def apply():Point3D = { 
    return ORIGIN;
  }
  def apply(x:Double, y:Double, z:Double):Point3D = { 
    return new Point3D(x,y,z);
  }
}
