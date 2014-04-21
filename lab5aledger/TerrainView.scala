//import java.awt._;
import java.awt.event._;
import java.awt.geom._;
import javax.swing._;
//import java.util._;

class TerrainView(t:Terrain) extends JPanel with MouseListener with MouseMotionListener {

  val WIDTH = 750;
  val HEIGHT = 750;
  val BACK_COLOR = new java.awt.Color(0.4f,0.4f,0.6f);
  val HIKE_COLOR = new java.awt.Color(0.4f,0.3f,0.1f);
  val START_COLOR = new java.awt.Color(0.3f,0.8f,0.3f);
  val FINISH_COLOR = new java.awt.Color(0.8f,0.5f,0.5f);
  val scale:Double = 1.0;

  var click:Boolean = false;
  var clickEnd:Boolean = false;
  var clickx:Double = 0.0;
  var clicky:Double = 0.0;
  var selectedStart:Vertex = null;
  var selectedEnd:Vertex = null;

  var pressed:Boolean = false;
  var spin:Double = 0.0;
  var spin0:Double = 0.0;
  var spin1:Double = 0.0;

  var tilt:Double = 0.0;
  var tilt0:Double = 0.0;
  var tilt1:Double = 0.0;

  val terrain:Terrain = t;
  val search:TerrainSearch = new TerrainSearch(terrain);
  setBackground(BACK_COLOR);
  setPreferredSize(new java.awt.Dimension(WIDTH,HEIGHT));
  addMouseListener(this);
  addMouseMotionListener(this);

  def currentSpin():Double = {
    return -(spin + (spin1 - spin0));
  }
  def currentTilt():Double = {
    var t = tilt + (tilt1 - tilt0);
    if (t > 1.0) t = 1.0;
    if (t < 0.0) t = 0.0;
    return t*Math.PI/2.0;
  }

  def light():Vector3D = {
    val s = currentSpin();
    return (new Vector3D(Math.cos(-s-Math.PI/2),Math.sin(-s-Math.PI/2.0),-0.5)).unit();
  }

  def project(v:Vertex):Point2D = {
    val s:Double = currentSpin();
    val t:Double = currentTilt();
    val p:Point3D = v.location.oproject(s,t);
    return new Point2D(p.x,p.y);
  }

  def shade(f:Face):Double = {
    val s:Double = f.normal().dot(light())
    return if (s > 0.0) s else 0.0;
  }

  def selects(v:Vertex):Option[Double] = {
    val p:Point3D = v.location.oproject(currentSpin(),currentTilt());
    val p2d:Point2D = Point2D(p.x,p.y);
    val q:Point2D = Point2D(clickx,clicky);
    if ((p2d - q).length() < 0.025) {
      return Some(p.z);
    } else {
      return None;
    }
  }

  def handleClick:Unit = {

    var selectv:Vertex = null;
    var selectd:Double = Double.MinValue;
  
    for (v <- terrain.vertices) {
      selects(v) match {
	case None => { }
	case Some(d) => {
	  if (selectv == null || d < selectd) {
	    selectv = v;
	    selectd = d;
	  }
	}
      }
    }

    if (selectv != null && click) {
      if (clickEnd) {
	selectedEnd = selectv;
      } else {
	selectedStart = selectv;
      }
    }

    if (selectedStart != null && selectedEnd != null) {
      for (e <- terrain.edges) {
	if (e != null) {
	  e.clear();
	}
      }
      val es:List[Edge] = search.pathTo(selectedStart,selectedEnd);
      for (e <- es) {
	e.mark()
      }
    }
  }

  override def paintComponent(g:java.awt.Graphics):Unit = g match {
    case g2d:java.awt.Graphics2D => {

    super.paintComponent(g); 
    
    // center the origin, make the coordinate system right-handed
    g2d.translate((WIDTH/2.0),(HEIGHT/2.0));
    g2d.scale(WIDTH / 2.0 * Math.sqrt(0.5), HEIGHT / 2.0 * Math.sqrt(0.5));
    g2d.scale(1.0,-1.0);
    
    g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, 
			 java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
    
    //
    // draw the terrain, marking the hike
    //

    // for each facet of the terrain
    for (f <- terrain.render(Math.cos(-spin+Math.PI) < 0.0)) {

      // determine the projected outline of the facet
      val path:GeneralPath = new GeneralPath();
      var isStart:Boolean = false;
      var isEnd:Boolean = false;
      var first:Point2D = null;
      for (v <- f.vertices) {

	if (v == selectedStart) isStart = true;
	if (v == selectedEnd) isEnd = true;

	val p:Point2D = project(v);
	if (first == null) {
	  first = p; 
	  path.moveTo(p.x.toFloat, p.y.toFloat);
	} else {
	  path.lineTo(p.x.toFloat, p.y.toFloat);
	}
      }
      path.lineTo(first.x.toFloat, first.y.toFloat);

      // determine the shading and draw the facet
      if (isStart) {
	g2d.setColor(new java.awt.Color(0.0f,1.0f,0.0f));
      } else if (isEnd) {
	g2d.setColor(new java.awt.Color(1.0f,0.0f,0.0f));
      } else{
	val s:Double = shade(f);
	g2d.setColor(new java.awt.Color(s.toFloat/2.0f+0.5f,s.toFloat/2.0f+0.5f,1.0f));
      }
      g2d.fill(path);
      if (!pressed) {
	g2d.setStroke(new java.awt.BasicStroke(0.003f,
					       java.awt.BasicStroke.CAP_ROUND,
					       java.awt.BasicStroke.JOIN_ROUND));   
	g2d.draw(path);
      }

      // draw any hike edges
      for (e <- f.border) {
	if (e.isMarked() || (e.twin != null && e.twin.isMarked())) {
	  val segment:GeneralPath = new GeneralPath();
	  val p:Point2D = project(e.from);
	  val q:Point2D = project(e.to);
	  segment.moveTo(p.x.toFloat, p.y.toFloat);
	  segment.lineTo(q.x.toFloat, q.y.toFloat);
	  g2d.setColor(HIKE_COLOR);
	  g2d.setStroke(new java.awt.BasicStroke(0.01f,
						 java.awt.BasicStroke.CAP_ROUND,
						 java.awt.BasicStroke.JOIN_ROUND));   
	  g2d.draw(segment);
	}
      }
    }

    }
  }

  val threshold:Double = 0.1
  var smove:Boolean = false;
  var tmove:Boolean = false;

  def mousePressed(evt:MouseEvent):Unit = { 
    pressed = true;
    spin0 = evt.getX()/WIDTH.toDouble;
    spin1 = spin0;
    smove = false;
    tilt0 = evt.getY()/HEIGHT.toDouble; 
    tilt1 = tilt0;
    tmove = false;
  }
  def mouseReleased(evt:MouseEvent):Unit = { 
    pressed = false;
    spin = spin + (spin1 - spin0);
    spin0 = spin1;
    tilt = tilt + (tilt1 - tilt0);
    tilt0 = tilt1;
    repaint();
  }
  def mouseClicked(evt:MouseEvent):Unit = { 
    clickx = (2.0*evt.getX()/WIDTH.toDouble - 1.0)*Math.sqrt(2.0);
    clicky = -(2.0*evt.getY()/HEIGHT.toDouble - 1.0)*Math.sqrt(2.0);
    click = true; 
    if (evt.isShiftDown()) {
      clickEnd = true;
    }
    handleClick;
    click = false;
    clickEnd = false;

    repaint();
  }
  def mouseEntered(evt:MouseEvent):Unit = { }
  def mouseExited(evt:MouseEvent):Unit = { }
  def mouseDragged(evt:MouseEvent):Unit = {
    val s = evt.getX()/WIDTH.toDouble;
    val t = evt.getY()/HEIGHT.toDouble;
    if (smove || Math.abs(s - spin0) > threshold) {
      spin1 = s;
      smove = true;
    }
    if (tmove || Math.abs(t - tilt0) > threshold) {
      tilt1 = t;
      tmove = true;
    }
    repaint();
  }
  def mouseMoved(evt:MouseEvent):Unit = { }
}

object TerrainView {
  def main(args:Array[String]):Unit = {
    if (args.length == 0) {
      println("usage: scala TerrainView <terrain file>");
    } else {
      val filename:String = args(0);
      val window:JFrame = new JFrame("Terrain View: "+filename);
      val t:Terrain = new Terrain(filename);
      val tv:TerrainView = new TerrainView(t);
      window.setContentPane(tv);
      window.pack();
      window.setResizable(false);
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      window.setVisible(true);
    }
  }
}
