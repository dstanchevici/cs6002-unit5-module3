
import java.util.*;

public class ActorConnectivity {

    public static void main (String[] argv)
    {
	DrawTool.display ();
	DrawTool.setXYRange (0,10, 0,10);

	// Number of actors desired in list. 
	int N = 8;
	ArrayList<Actor> actors = ActorConnectivityMaker.makeActorConnectivityData (N);

	// Radius of larger circle on which to draw smaller circles.
	double r = 4;

	// Draw the actors as tiny circles.
	for (int i=0; i<N; i++) {
	    Actor a = actors.get (i);
	    double theta = i*(2*Math.PI/N);
	    a.x = 5 + r*Math.cos(theta);
	    a.y = 5 + r*Math.sin(theta);
	    DrawTool.drawCircle (a.x,a.y, 0.1);
	    //DrawTool.drawSmallLabel (a.x, a.y, a.name);
	}

	// Draw the connecting links between actors.
	drawLinks (actors);

	while (true) {
	    String name = IOTool.readStringFromTerminal ("Enter actor name: ");
	    // Now find the actor.
	    Actor which = null;
	    for (Actor a: actors) {
		if (a.name.equals(name)) {
		    // Found.
		    which = a;
		}
	    }
	    if (which != null) {
		which.color = "red";
	    }
	    drawLinks (actors);
	}
    }

    static void drawLinks (ArrayList<Actor> actors) 
    {
	for (int i=0; i<actors.size(); i++) {
	    Actor a = actors.get (i);
	    DrawTool.setLineColor (a.color);
	    for (Actor b: a.actedWith) {
		DrawTool.drawLine (a.x,a.y, b.x,b.y);
	    }
	}
    }

}
