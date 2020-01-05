package solver;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

/**
 * <i>Standard draw</i>. Our class StdDraw provides a basic capability for
 * creating drawings with your programs. It uses a simple graphics model that
 * allows you to create drawings consisting of points, lines, and curves in a
 * window on your computer and to save the drawings to a file.
 * <p>
 * For documentation, see Section 1.5 of <i>Introduction to Programming in Java:
 * An Interdisciplinary Approach, Spring 2007 preliminary version</i> and
 * <a href=
 * "http://www.cs.princeton.edu/introcs/15inout">http://www.cs.princeton.edu/introcs/15inout</a>
 */
public final class StdDraw implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

	// pre-defined colors
	public static final Color BLACK = Color.BLACK;
	public static final Color BLUE = Color.BLUE;
	public static final Color CYAN = Color.CYAN;
	public static final Color DARK_GRAY = Color.DARK_GRAY;
	public static final Color GRAY = Color.GRAY;
	public static final Color GREEN = Color.GREEN;
	public static final Color LIGHT_GRAY = Color.LIGHT_GRAY;
	public static final Color MAGENTA = Color.MAGENTA;
	public static final Color ORANGE = Color.ORANGE;
	public static final Color PINK = Color.PINK;
	public static final Color RED = Color.RED;
	public static final Color WHITE = Color.WHITE;
	public static final Color YELLOW = Color.YELLOW;

	// default colors
	private static final Color DEFAULT_PEN_COLOR = BLACK;
	private static final Color DEFAULT_CLEAR_COLOR = WHITE;

	// current pen color
	private static Color penColor;

	// default canvas size is SIZE-by-SIZE
	private static final int DEFAULT_SIZE = 512;
	private static int width = DEFAULT_SIZE;
	private static int height = DEFAULT_SIZE;

	// default pen radius
	private static final double DEFAULT_PEN_RADIUS = 0.002;

	// current pen radius
	private static double penRadius;

	// show we draw immediately or wait until next show?
	private static boolean defer = false;

	// boundary of drawing canvas, 5% border
	private static final double BORDER = 0.05;
	private static final double DEFAULT_XMIN = 0.0;
	private static final double DEFAULT_XMAX = 1.0;
	private static final double DEFAULT_YMIN = 0.0;
	private static final double DEFAULT_YMAX = 1.0;
	private static double xmin, ymin, xmax, ymax;

	// default font
	private static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 16);

	// current font
	private static Font font;

	// double buffered graphics
	private static BufferedImage offscreenImage, onscreenImage;
	private static Graphics2D offscreen, onscreen;

	// singleton for callbacks: avoids generation of extra .class files
	private static StdDraw std = new StdDraw();

	// the frame for drawing to the screen
	private static JFrame frame;

	// mouse state
	private static boolean mousePressed = false;
	private static double mouseX = 0;
	private static double mouseY = 0;

	// keyboard state
	private static LinkedList<Character> keysTyped = new LinkedList<>();

	// static initializer
	static {
		init();
	}

	/**
	 * Draw an arc of radius r, centered on (x, y), from angle1 to angle2 (in
	 * degrees).
	 *
	 * @param x      the x-coordinate of the center of the circle
	 * @param y      the y-coordinate of the center of the circle
	 * @param r      the radius of the circle
	 * @param angle1 the starting angle. 0 would mean an arc beginning at 3 o'clock.
	 * @param angle2 the angle at the end of the arc. For example, if you want a 90
	 *               degree arc, then angle2 should be angle1 + 90.
	 * @throws RuntimeException if the radius of the circle is negative
	 */
	public static void arc(final double x, final double y, final double r, final double angle1, double angle2) {
		if (r < 0) {
			throw new RuntimeException("arc radius can't be negative");
		}
		while (angle2 < angle1) {
			angle2 += 360;
		}
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final double ws = factorX(2 * r);
		final double hs = factorY(2 * r);
		if ((ws <= 1) && (hs <= 1)) {
			pixel(x, y);
		} else {
			offscreen.draw(new Arc2D.Double(xs - (ws / 2), ys - (hs / 2), ws, hs, angle1, angle2 - angle1, Arc2D.OPEN));
		}
		show();
	}

	/**
	 * Draw circle of radius r, centered on (x, y); degenerate to pixel if small
	 *
	 * @param x the x-coordinate of the center of the circle
	 * @param y the y-coordinate of the center of the circle
	 * @param r the radius of the circle
	 * @throws RuntimeException if the radius of the circle is negative
	 */
	public static void circle(final double x, final double y, final double r) {
		if (r < 0) {
			throw new RuntimeException("circle radius can't be negative");
		}
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final double ws = factorX(2 * r);
		final double hs = factorY(2 * r);
		if ((ws <= 1) && (hs <= 1)) {
			pixel(x, y);
		} else {
			offscreen.draw(new Ellipse2D.Double(xs - (ws / 2), ys - (hs / 2), ws, hs));
		}
		show();
	}

	/**
	 * Clear the screen with the default color, white
	 */
	public static void clear() {
		clear(DEFAULT_CLEAR_COLOR);
	}

	/**
	 * Clear the screen with the given color.
	 *
	 * @param color the Color to make the background
	 */
	public static void clear(final Color color) {
		offscreen.setColor(color);
		offscreen.fillRect(0, 0, width, height);
		offscreen.setColor(penColor);
		show();
	}

	/*************************************************************************
	 * User and screen coordinate systems
	 *************************************************************************/

	// create the menu bar (changed to private)
	private static JMenuBar createMenuBar() {
		final JMenuBar menuBar = new JMenuBar();
		final JMenu menu = new JMenu("File");
		menuBar.add(menu);
		final JMenuItem menuItem1 = new JMenuItem(" Save...   ");
		menuItem1.addActionListener(std);
		menuItem1.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.add(menuItem1);
		return menuBar;
	}

	private static double factorX(final double w) {
		return (w * width) / Math.abs(xmax - xmin);
	}

	private static double factorY(final double h) {
		return (h * height) / Math.abs(ymax - ymin);
	}

	/**
	 * Draw filled circle of radius r, centered on (x, y); degenerate to pixel if
	 * small
	 *
	 * @param x the x-coordinate of the center of the circle
	 * @param y the y-coordinate of the center of the circle
	 * @param r the radius of the circle
	 * @throws RuntimeException if the radius of the circle is negative
	 */
	public static void filledCircle(final double x, final double y, final double r) {
		if (r < 0) {
			throw new RuntimeException("circle radius can't be negative");
		}
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final double ws = factorX(2 * r);
		final double hs = factorY(2 * r);
		if ((ws <= 1) && (hs <= 1)) {
			pixel(x, y);
		} else {
			offscreen.fill(new Ellipse2D.Double(xs - (ws / 2), ys - (hs / 2), ws, hs));
		}
		show();
	}

	/**
	 * Draw a filled polygon with the given (x[i], y[i]) coordinates
	 *
	 * @param x an array of all the x-coordindates of the polygon
	 * @param y an array of all the y-coordindates of the polygon
	 */
	public static void filledPolygon(final double[] x, final double[] y) {
		final int N = x.length;
		final GeneralPath path = new GeneralPath();
		path.moveTo((float) scaleX(x[0]), (float) scaleY(y[0]));
		for (int i = 0; i < N; i++) {
			path.lineTo((float) scaleX(x[i]), (float) scaleY(y[i]));
		}
		path.closePath();
		offscreen.fill(path);
		show();
	}

	/**
	 * Draw a filled square of side length 2r, centered on (x, y); degenerate to
	 * pixel if small
	 *
	 * @param x the x-coordinate of the center of the square
	 * @param y the y-coordinate of the center of the square
	 * @param r radius is half the length of any side of the square
	 * @throws RuntimeException if r is negative
	 */
	public static void filledSquare(final double x, final double y, final double r) {
		if (r < 0) {
			throw new RuntimeException("square side length can't be negative");
		}
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final double ws = factorX(2 * r);
		final double hs = factorY(2 * r);
		if ((ws <= 1) && (hs <= 1)) {
			pixel(x, y);
		} else {
			offscreen.fill(new Rectangle2D.Double(xs - (ws / 2), ys - (hs / 2), ws, hs));
		}
		show();
	}

	/*************************************************************************
	 * Drawing images.
	 *************************************************************************/

	// get an image from the given filename
	private static Image getImage(final String filename) {

		// to read from file
		ImageIcon icon = new ImageIcon(filename);

		// try to read from URL
		if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
			try {
				final URL url = new URL(filename);
				icon = new ImageIcon(url);
			} catch (final Exception e) {
				/* not a url */ }
		}

		// in case file is inside a .jar
		if ((icon == null) || (icon.getImageLoadStatus() != MediaTracker.COMPLETE)) {
			final URL url = StdDraw.class.getResource(filename);
			if (url == null) {
				throw new RuntimeException("image " + filename + " not found");
			}
			icon = new ImageIcon(url);
		}

		return icon.getImage();
	}

	/**
	 * Has the user typed a key?
	 *
	 * @return true if the user has typed a key, false otherwise
	 */
	public static boolean hasNextKeyTyped() {
		return !keysTyped.isEmpty();
	}

	// init
	private static void init() {
		if (frame != null) {
			frame.setVisible(false);
		}
		frame = new JFrame();
		offscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		onscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		offscreen = offscreenImage.createGraphics();
		onscreen = onscreenImage.createGraphics();
		setXscale();
		setYscale();
		offscreen.setColor(DEFAULT_CLEAR_COLOR);
		offscreen.fillRect(0, 0, width, height);
		setPenColor();
		setPenRadius();
		setFont();
		clear();

		// add antialiasing
		final RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		offscreen.addRenderingHints(hints);

		// frame stuff
		final ImageIcon icon = new ImageIcon(onscreenImage);
		final JLabel draw = new JLabel(icon);

		draw.addMouseListener(std);
		draw.addMouseMotionListener(std);

		frame.setContentPane(draw);
		frame.addKeyListener(std); // JLabel cannot get keyboard focus
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // closes all windows
		// frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // closes only
		// current window
		frame.setTitle("Standard Draw");
		frame.setJMenuBar(createMenuBar());
		frame.pack();
		frame.requestFocusInWindow();
		frame.setVisible(true);
	}

	/**
	 * Draw a line from (x0, y0) to (x1, y1)
	 *
	 * @param x0 the x-coordinate of the starting point
	 * @param y0 the y-coordinate of the starting point
	 * @param x1 the x-coordinate of the destination point
	 * @param y1 the y-coordinate of the destination point
	 */
	public static void line(final double x0, final double y0, final double x1, final double y1) {
		offscreen.draw(new Line2D.Double(scaleX(x0), scaleY(y0), scaleX(x1), scaleY(y1)));
		show();
	}

	/**
	 * @deprecated
	 */
	// test client
	@Deprecated
	public static void main(final String[] args) {
		StdDraw.square(.2, .8, .1);
		StdDraw.filledSquare(.8, .8, .2);
		StdDraw.circle(.8, .2, .2);

		StdDraw.setPenColor(StdDraw.MAGENTA);
		StdDraw.setPenRadius(.02);
		StdDraw.arc(.8, .2, .1, 200, 45);

		// draw a blue diamond
		StdDraw.setPenRadius();
		StdDraw.setPenColor(StdDraw.BLUE);
		final double[] x = { .1, .2, .3, .2 };
		final double[] y = { .2, .3, .2, .1 };
		StdDraw.filledPolygon(x, y);

		// text
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.text(0.2, 0.5, "black text");
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.text(0.8, 0.8, "white text");
	}

	/**
	 * Is the mouse being pressed?
	 *
	 * @return true or false
	 */
	public static boolean mousePressed() {
		return mousePressed;
	}

	/**
	 * Where is the mouse?
	 *
	 * @return the value of the x-coordinate of the mouse
	 */
	public static double mouseX() {
		return mouseX;
	}

	/**
	 * Where is the mouse?
	 *
	 * @return the value of the y-coordinate of the mouse
	 */
	public static double mouseY() {
		return mouseY;
	}

	/**
	 * What is the next key that was typed by the user?
	 *
	 * @return the next key typed
	 */
	public static char nextKeyTyped() {
		return keysTyped.removeLast();
	}

	/**
	 * Draw picture (gif, jpg, or png) centered on (x, y).
	 *
	 * @param x the center x-coordinate of the image
	 * @param y the center y-coordinate of the image
	 * @param s the name of the image/picture, e.g., "ball.gif"
	 * @throws RuntimeException if the image's width or height are negative
	 */
	public static void picture(final double x, final double y, final String s) {
		final Image image = getImage(s);
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final int ws = image.getWidth(null);
		final int hs = image.getHeight(null);
		if ((ws < 0) || (hs < 0)) {
			throw new RuntimeException("image " + s + " is corrupt");
		}

		offscreen.drawImage(image, (int) Math.round(xs - (ws / 2.0)), (int) Math.round(ys - (hs / 2.0)), null);
		show();
	}

	/**
	 * Draw picture (gif, jpg, or png) centered on (x, y), rotated given number of
	 * degrees
	 *
	 * @param x       the center x-coordinate of the image
	 * @param y       the center y-coordinate of the image
	 * @param s       the name of the image/picture, e.g., "ball.gif"
	 * @param degrees is the number of degrees to rotate counterclockwise
	 * @throws RuntimeException if the image's width or height are negative
	 */
	public static void picture(final double x, final double y, final String s, final double degrees) {
		final Image image = getImage(s);
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final int ws = image.getWidth(null);
		final int hs = image.getHeight(null);
		if ((ws < 0) || (hs < 0)) {
			throw new RuntimeException("image " + s + " is corrupt");
		}

		offscreen.rotate(Math.toRadians(-degrees), xs, ys);
		offscreen.drawImage(image, (int) Math.round(xs - (ws / 2.0)), (int) Math.round(ys - (hs / 2.0)), null);
		offscreen.rotate(Math.toRadians(+degrees), xs, ys);

		show();
	}

	/**
	 * Draw picture (gif, jpg, or png) centered on (x, y). Rescaled to w-by-h.
	 *
	 * @param x the center x coordinate of the image
	 * @param y the center y coordinate of the image
	 * @param s the name of the image/picture, e.g., "ball.gif"
	 * @param w the width of the image
	 * @param h the height of the image
	 */
	public static void picture(final double x, final double y, final String s, final double w, final double h) {
		final Image image = getImage(s);
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final double ws = factorX(w);
		final double hs = factorY(h);
		if ((ws < 0) || (hs < 0)) {
			throw new RuntimeException("image " + s + " is corrupt");
		}
		if ((ws <= 1) && (hs <= 1)) {
			pixel(x, y);
		} else {
			offscreen.drawImage(image, (int) Math.round(xs - (ws / 2.0)), (int) Math.round(ys - (hs / 2.0)),
					(int) Math.round(ws), (int) Math.round(hs), null);
		}
		show();
	}

	/*************************************************************************
	 * Drawing geometric shapes.
	 *************************************************************************/

	/**
	 * Draw picture (gif, jpg, or png) centered on (x, y), rotated given number of
	 * degrees, rescaled to w-by-h.
	 *
	 * @param x       the center x-coordinate of the image
	 * @param y       the center y-coordinate of the image
	 * @param s       the name of the image/picture, e.g., "ball.gif"
	 * @param w       the width of the image
	 * @param h       the height of the image
	 * @param degrees is the number of degrees to rotate counterclockwise
	 * @throws RuntimeException if the image's width or height are negative
	 */
	public static void picture(final double x, final double y, final String s, final double w, final double h,
			final double degrees) {
		final Image image = getImage(s);
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final double ws = factorX(w);
		final double hs = factorY(h);
		if ((ws < 0) || (hs < 0)) {
			throw new RuntimeException("image " + s + " is corrupt");
		}
		if ((ws <= 1) && (hs <= 1)) {
			pixel(x, y);
		}

		offscreen.rotate(Math.toRadians(-degrees), xs, ys);
		offscreen.drawImage(image, (int) Math.round(xs - (ws / 2.0)), (int) Math.round(ys - (hs / 2.0)),
				(int) Math.round(ws), (int) Math.round(hs), null);
		offscreen.rotate(Math.toRadians(+degrees), xs, ys);

		show();
	}

	/**
	 * Draw one pixel at (x, y)
	 *
	 * @param x the x-coordinate of the pixel
	 * @param y the y-coordinate of the pixel
	 */
	public static void pixel(final double x, final double y) {
		offscreen.fillRect((int) Math.round(scaleX(x)), (int) Math.round(scaleY(y)), 1, 1);
	}

	/**
	 * Draw a point at (x, y)
	 *
	 * @param x the x-coordinate of the point
	 * @param y the y-coordinate of the point
	 */
	public static void point(final double x, final double y) {
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final double r = penRadius;
		// double ws = factorX(2*r);
		// double hs = factorY(2*r);
		// if (ws <= 1 && hs <= 1) pixel(x, y);
		if (r <= 1) {
			pixel(x, y);
		} else {
			offscreen.fill(new Ellipse2D.Double(xs - (r / 2), ys - (r / 2), r, r));
		}
		show();
	}

	/**
	 * Draw a polygon with the given (x[i], y[i]) coordinates
	 *
	 * @param x an array of all the x-coordindates of the polygon
	 * @param y an array of all the y-coordindates of the polygon
	 */
	public static void polygon(final double[] x, final double[] y) {
		final int N = x.length;
		final GeneralPath path = new GeneralPath();
		path.moveTo((float) scaleX(x[0]), (float) scaleY(y[0]));
		for (int i = 0; i < N; i++) {
			path.lineTo((float) scaleX(x[i]), (float) scaleY(y[i]));
		}
		path.closePath();
		offscreen.draw(path);
		show();
	}

	/**
	 * Save to file - suffix must be png, jpg, or gif.
	 *
	 * @param filename the name of the file with one of the required suffixes
	 */
	public static void save(final String filename) {
		final File file = new File(filename);
		final String suffix = filename.substring(filename.lastIndexOf('.') + 1);

		// png files
		if (suffix.toLowerCase().equals("png")) {
			try {
				ImageIO.write(offscreenImage, suffix, file);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		// need to change from ARGB to RGB for jpeg
		// reference:
		// http://archives.java.sun.com/cgi-bin/wa?A2=ind0404&L=java2d-interest&D=0&P=2727
		else if (suffix.toLowerCase().equals("jpg")) {
			final WritableRaster raster = offscreenImage.getRaster();
			WritableRaster newRaster;
			newRaster = raster.createWritableChild(0, 0, width, height, 0, 0, new int[] { 0, 1, 2 });
			final DirectColorModel cm = (DirectColorModel) offscreenImage.getColorModel();
			final DirectColorModel newCM = new DirectColorModel(cm.getPixelSize(), cm.getRedMask(), cm.getGreenMask(),
					cm.getBlueMask());
			final BufferedImage rgbBuffer = new BufferedImage(newCM, newRaster, false, null);
			try {
				ImageIO.write(rgbBuffer, suffix, file);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		else {
			System.out.println("Invalid image file type: " + suffix);
		}
	}

	// helper functions that scale from user coordinates to screen coordinates and
	// back
	private static double scaleX(final double x) {
		return (width * (x - xmin)) / (xmax - xmin);
	}

	private static double scaleY(final double y) {
		return (height * (ymax - y)) / (ymax - ymin);
	}

	/**
	 * Set the font to be the default for all string writing
	 */
	public static void setFont() {
		setFont(DEFAULT_FONT);
	}

	/**
	 * Set the font as given for all string writing
	 *
	 * @param f the font to make text
	 */
	public static void setFont(final Font f) {
		font = f;
	}

	/**
	 * Set the pen color to the default which is BLACK.
	 */
	public static void setPenColor() {
		setPenColor(DEFAULT_PEN_COLOR);
	}

	/**
	 * Set the pen color to the given color. The available pen colors are BLACK,
	 * BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, MAGENTA, ORANGE, PINK, RED,
	 * WHITE, and YELLOW.
	 *
	 * @param color the Color to make the pen
	 */
	public static void setPenColor(final Color color) {
		penColor = color;
		offscreen.setColor(penColor);
	}

	/**
	 * Set the pen size to the default
	 */
	public static void setPenRadius() {
		setPenRadius(DEFAULT_PEN_RADIUS);
	}

	/**
	 * Set the pen size to the given size
	 *
	 * @param r the radius of the pen
	 * @throws RuntimeException if r is negative
	 */
	public static void setPenRadius(final double r) {
		if (r < 0) {
			throw new RuntimeException("pen radius must be positive");
		}
		penRadius = r * DEFAULT_SIZE;
		final BasicStroke stroke = new BasicStroke((float) penRadius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		offscreen.setStroke(stroke);
	}

	/**
	 * Set the X scale to be the default
	 */
	public static void setXscale() {
		setXscale(DEFAULT_XMIN, DEFAULT_XMAX);
	}

	/**
	 * Set the X scale (a border is added to the values)
	 *
	 * @param min the minimum value of the X scale
	 * @param max the maximum value of the X scale
	 */
	public static void setXscale(final double min, final double max) {
		final double size = max - min;
		xmin = min - (BORDER * size);
		xmax = max + (BORDER * size);
	}

	/*************************************************************************
	 * Drawing text.
	 *************************************************************************/

	/**
	 * Set the Y scale to be the default
	 */
	public static void setYscale() {
		setYscale(DEFAULT_YMIN, DEFAULT_YMAX);
	}

	/**
	 * Set the Y scale (a border is added to the values)
	 *
	 * @param min the minimum value of the Y scale
	 * @param max the maximum value of the Y scale
	 */
	public static void setYscale(final double min, final double max) {
		final double size = max - min;
		ymin = min - (BORDER * size);
		ymax = max + (BORDER * size);
	}

	/**
	 * Display on-screen; calling this method means that the screen WILL be redrawn
	 * after each line(), circle(), or square(). This is the default.
	 */
	public static void show() {
		if (!defer) {
			onscreen.drawImage(offscreenImage, 0, 0, null);
		}
		if (!defer) {
			frame.repaint();
		}
	}

	/*************************************************************************
	 * Save drawing to a file.
	 *************************************************************************/

	/**
	 * Display on screen and pause for t milliseconds. Calling this method means
	 * that the screen will NOT be redrawn after each line(), circle(), or square().
	 * This is useful when there are many methods to call to draw a complete
	 * picture.
	 *
	 * @param t number of milliseconds
	 * @throws InterruptedException
	 */
	public static void show(final int t) throws InterruptedException {
		defer = true;
		onscreen.drawImage(offscreenImage, 0, 0, null);
		frame.repaint();
		Thread.sleep(t);
		System.out.println("Error sleeping");
	}

	/**
	 * Draw squared of side length 2r, centered on (x, y); degenerate to pixel if
	 * small
	 *
	 * @param x the x-coordinate of the center of the square
	 * @param y the y-coordinate of the center of the square
	 * @param r radius is half the length of any side of the square
	 * @throws RuntimeException if r is negative
	 */
	public static void square(final double x, final double y, final double r) {
		if (r < 0) {
			throw new RuntimeException("square side length can't be negative");
		}
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final double ws = factorX(2 * r);
		final double hs = factorY(2 * r);
		if ((ws <= 1) && (hs <= 1)) {
			pixel(x, y);
		} else {
			offscreen.draw(new Rectangle2D.Double(xs - (ws / 2), ys - (hs / 2), ws, hs));
		}
		show();
	}

	/*************************************************************************
	 * Mouse interactions.
	 *************************************************************************/

	/**
	 * Write the given text string in the current font, center on (x, y).
	 *
	 * @param x the center x coordinate of the text
	 * @param y the center y coordinate of the text
	 * @param s the text
	 */
	public static void text(final double x, final double y, final String s) {
		offscreen.setFont(font);
		final FontMetrics metrics = offscreen.getFontMetrics();
		final double xs = scaleX(x);
		final double ys = scaleY(y);
		final int ws = metrics.stringWidth(s);
		final int hs = metrics.getDescent();
		offscreen.drawString(s, (float) (xs - (ws / 2.0)), (float) (ys + hs));
		show();
	}

	private static double userX(final double x) {
		return xmin + ((x * (xmax - xmin)) / width);
	}

	private static double userY(final double y) {
		return ymax - ((y * (ymax - ymin)) / height);
	}

	// not instantiable
	public StdDraw() {
	}

	/**
	 * @deprecated Open a save dialog when the user selects "Save As" from the menu
	 */
	@Deprecated
	@Override
	public void actionPerformed(final ActionEvent e) {
		final FileDialog chooser = new FileDialog(StdDraw.frame, "Use a .png or .jpg extension", FileDialog.SAVE);
		chooser.setVisible(true);
		final String filename = chooser.getFile();
		if (filename != null) {
			StdDraw.save(chooser.getDirectory() + File.separator + chooser.getFile());
		}
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public void keyPressed(final KeyEvent e) {
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public void keyReleased(final KeyEvent e) {
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public void keyTyped(final KeyEvent e) {
		keysTyped.addFirst(e.getKeyChar());
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public void mouseClicked(final MouseEvent e) {
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public void mouseDragged(final MouseEvent e) {
		mouseX = StdDraw.userX(e.getX());
		mouseY = StdDraw.userY(e.getY());
	}

	/*************************************************************************
	 * Keyboard interactions.
	 *************************************************************************/

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public void mouseEntered(final MouseEvent e) {
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public void mouseExited(final MouseEvent e) {
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public void mouseMoved(final MouseEvent e) {
		mouseX = StdDraw.userX(e.getX());
		mouseY = StdDraw.userY(e.getY());
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public void mousePressed(final MouseEvent e) {
		mouseX = StdDraw.userX(e.getX());
		mouseY = StdDraw.userY(e.getY());
		mousePressed = true;
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public void mouseReleased(final MouseEvent e) {
		mousePressed = false;
	}

	/**
	 * Set the window size to w-by-h pixels
	 *
	 * @param w the width as a number of pixels
	 * @param h the height as a number of pixels
	 */
	public void setCanvasSize(final int w, final int h) {
		if ((w < 1) || (h < 1)) {
			throw new RuntimeException("width and height must be positive");
		}
		width = w;
		height = h;
		init();
	}

}