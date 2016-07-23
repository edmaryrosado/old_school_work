import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 * Sierpinski Triangle Class
 * 
 * @author Edmary Rosado
 *
 */
public class SierpinskiTriangle {

	private Graphics g;
	private Color lineColor = Color.lightGray;
	private boolean randomColorization;
	private int recursiveDepth;

	private final Color[] colors = new Color[] { Color.black, Color.cyan,
			Color.darkGray, Color.green, Color.blue, Color.lightGray,
			Color.red, Color.magenta, Color.yellow, Color.white, Color.orange };
	
	private int selectedColors[];
	
	/**
	 * Sierpinski Triangle Class Constructor
	 * 
	 * @param depth
	 * @param x
	 * @param y
	 * @param s
	 */
	public SierpinskiTriangle(int depth, int x, int y, int s) {
		selectedColors = new int[10];
		recursiveDepth = depth;
	}
	
	/**
	 * Sets graphics given the canvas from parent object.
	 * @param canvas
	 */
	public void setGraphics(Canvas canvas) {
		g = canvas.getGraphics();
	}
	
	/**
	 * Sets the random value. Allows for colors to be
	 * disbursed at random levels of recursion.
	 * @param val
	 */
	public void setRandomColorization(boolean val) {
		randomColorization = val;
	}
	
	/**
	 * Sets the recursive depth for the triangle drawing
	 * @param depth
	 */
	public void setDepth(int depth){
		recursiveDepth = depth;
	}
	
	/**
	 * Sets the colors per level. Selected colors are added twice 
	 * in the array to be represented in two different layers of 
	 * recursion.
	 * 
	 * @param index
	 * @param colorIndex
	 */
	public void setColor(int index, int colorIndex){
		selectedColors[index] = colorIndex;
		if(index<5)
			selectedColors[index+5] = colorIndex;
	}

	/**
	 * Drawing method for Triangle class
	 * 
	 * @param d
	 * @param x
	 * @param y
	 * @param S
	 */
	public void draw(int d, int x, int y, int S) {

		int[] xPoints = { x, x + S / 2, x + S };
		int[] yPoints = { y + S, y, y + S };

		// Draw New
		if (randomColorization == true) {
			// Set up Random Color from colors array
			Random randColor = new Random();
			int indexSelect = randColor.nextInt(colors.length);

			if (indexSelect > 4) {
				g.setColor(colors[indexSelect]);
				g.fillPolygon(xPoints, yPoints, 3);
			} else {
				g.setColor(colors[indexSelect]);
				g.fillPolygon(xPoints, yPoints, 3);
			}
		} else {
			if (d > 4) {
				int tmp = Math.abs(d % recursiveDepth);
				g.setColor(colors[selectedColors[tmp]]);
				g.fillPolygon(xPoints, yPoints, 3);
			} else {
				g.setColor(colors[selectedColors[d]]);
				g.fillPolygon(xPoints, yPoints, 3);
			}
		}

		// Done drawing, stop.
		if (d == 0) {
			g.setColor(lineColor);
			g.drawPolyline(xPoints, yPoints, 3);
			return;
		} else {
			draw(d - 1, x + S / 4, y, S / 2);
			draw(d - 1, x, y + S / 2, S / 2);
			draw(d - 1, x + S / 2, y + S / 2, S / 2);
		}
	}
}// End of Triangle Class