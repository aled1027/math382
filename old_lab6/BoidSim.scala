import javax.swing._;
import java.awt._;
import java.awt.font._;
import java.awt.geom._;
import swing.{Panel, MainFrame, SimpleSwingApplication}


class BoidPanel(size:Int, w:Double, h:Double, ww:Int, wh:Int) extends JPanel {

  val WINDOW_WIDTH:Int = ww;
  val WINDOW_HEIGHT:Int = wh

  val WIDTH: Double = w;
  val HEIGHT:Double = h;

  val flok:Flok = new Flok(size,WIDTH,HEIGHT);

  setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

  // paintComponent
  //
  // This code runs every time the window believes that
  // this panel's content needs to be redrawn.
  //
  override def paintComponent(g: Graphics) {
    g match {
      case g2d:Graphics2D => {

	flok.step(0.5);
	g2d.setColor(Color.black);
	g2d.fillRect(0, 0, WINDOW_WIDTH, WINDOW_WIDTH);
	g2d.scale(WINDOW_WIDTH/WIDTH,WINDOW_HEIGHT/HEIGHT);
	g2d.translate(WIDTH/2.0,HEIGHT/2.0);
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
			     RenderingHints.VALUE_ANTIALIAS_ON);
	for (b <- flok.boids) {
	  g2d.setColor(Color.WHITE);
	  val xform:AffineTransform = g2d.getTransform();
	  val path:Path2D = new Path2D.Double();
	  val h:Vector2d = b.velocity.unit;
	  val hp:Vector2d = h.perp;
	  val p1:Point2d = b.position + h;
	  val p2:Point2d = b.position + hp * 0.5;
	  val p3:Point2d = b.position - hp * 0.5;
	  path.moveTo(p1.x,p1.y);
	  path.lineTo(p2.x,p2.y);
	  path.lineTo(p3.x,p3.y);
	  path.closePath();
	  g2d.fill(path);
	  g2d.setTransform(xform);
	}
      }
      case _ => {}
    }
  }
}

object BoidSim {
  def main(args: Array[String]) {
    val size = if (args.length > 0) args(0).toInt else 100;
    val frame:JFrame = new JFrame("boids");
    frame.setContentPane(new BoidPanel(size,60.0,45.0,1200,900));
    frame.pack();
    frame.setResizable(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    while (true) {
      Thread.sleep(10);
      frame.repaint();
    }
  }
}
