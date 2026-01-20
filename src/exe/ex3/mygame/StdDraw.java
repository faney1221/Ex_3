package exe.ex3.mygame;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Standard drawing library for creating 2D graphics
 */
public final class StdDraw implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

    // Color constants
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
    public static final Color BOOK_BLUE = new Color(9, 90, 166);
    public static final Color BOOK_LIGHT_BLUE = new Color(103, 198, 243);
    public static final Color BOOK_RED = new Color(150, 35, 31);
    public static final Color PRINCETON_ORANGE = new Color(245, 128, 37);

    // Default settings
    private static final Color DEFAULT_PEN_COLOR = BLACK;
    private static final Color DEFAULT_CLEAR_COLOR = WHITE;
    private static final double DEFAULT_PEN_RADIUS = 0.002;
    private static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 16);

    // Canvas settings
    private static int width = 512;
    private static int height = 512;
    private static Color penColor;
    private static double penRadius;
    private static Font font;
    private static boolean defer = false;

    // Coordinate system
    private static double xmin = 0.0;
    private static double ymin = 0.0;
    private static double xmax = 1.0;
    private static double ymax = 1.0;

    // Graphics objects
    private static BufferedImage offscreenImage;
    private static BufferedImage onscreenImage;
    private static Graphics2D offscreen;
    private static Graphics2D onscreen;
    private static JFrame frame;

    // Mouse state
    private static final Object mouseLock = new Object();
    private static boolean isMousePressed = false;
    private static double mouseX = 0.0;
    private static double mouseY = 0.0;

    // Keyboard state
    private static final Object keyLock = new Object();
    private static LinkedList<Character> keysTyped = new LinkedList<>();
    private static TreeSet<Integer> keysDown = new TreeSet<>();
    private static Character lastKeyChar = null;

    private static StdDraw std = new StdDraw();

    static {
        init();
    }

    private StdDraw() {}

    /**
     * Initialize the drawing canvas
     */
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

        // Enable anti-aliasing
        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        offscreen.addRenderingHints(hints);

        // Setup frame
        ImageIcon icon = new ImageIcon(onscreenImage);
        JLabel label = new JLabel(icon);
        label.addMouseListener(std);
        label.addMouseMotionListener(std);

        frame.setContentPane(label);
        frame.addKeyListener(std);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Ex3: PacMan Game!");
        frame.setJMenuBar(createMenuBar());
        frame.pack();
        frame.requestFocusInWindow();
        frame.setVisible(true);
    }

    private static JMenuBar createMenuBar() {
        return new JMenuBar();
    }

    // Canvas size methods
    public static void setCanvasSize() {
        setCanvasSize(512, 512);
    }

    public static void setCanvasSize(int w, int h) {
        if (w <= 0 || h <= 0) {
            throw new IllegalArgumentException("width and height must be positive");
        }
        width = w;
        height = h;
        init();
    }

    // Scale methods
    public static void setXscale() {
        setXscale(0.0, 1.0);
    }

    public static void setYscale() {
        setYscale(0.0, 1.0);
    }

    public static void setScale() {
        setXscale();
        setYscale();
    }

    public static void setXscale(double min, double max) {
        if (min >= max) {
            throw new IllegalArgumentException("min must be less than max");
        }
        xmin = min;
        xmax = max;
    }

    public static void setYscale(double min, double max) {
        if (min >= max) {
            throw new IllegalArgumentException("min must be less than max");
        }
        ymin = min;
        ymax = max;
    }

    public static void setScale(double min, double max) {
        setXscale(min, max);
        setYscale(min, max);
    }

    // Coordinate conversion methods
    private static double scaleX(double x) {
        return width * (x - xmin) / (xmax - xmin);
    }

    private static double scaleY(double y) {
        return height * (ymax - y) / (ymax - ymin);
    }

    private static double factorX(double w) {
        return w * width / Math.abs(xmax - xmin);
    }

    private static double factorY(double h) {
        return h * height / Math.abs(ymax - ymin);
    }

    private static double userX(double x) {
        return xmin + x * (xmax - xmin) / width;
    }

    private static double userY(double y) {
        return ymax - y * (ymax - ymin) / height;
    }

    // Pen methods
    public static double getPenRadius() {
        return penRadius;
    }

    public static void setPenRadius() {
        setPenRadius(DEFAULT_PEN_RADIUS);
    }

    public static void setPenRadius(double radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("pen radius must be non-negative");
        }
        penRadius = radius;
        float scaledRadius = (float) (radius * Math.min(width, height));
        BasicStroke stroke = new BasicStroke(scaledRadius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        offscreen.setStroke(stroke);
    }

    public static Color getPenColor() {
        return penColor;
    }

    public static void setPenColor() {
        setPenColor(DEFAULT_PEN_COLOR);
    }

    public static void setPenColor(Color color) {
        if (color == null) {
            throw new IllegalArgumentException("color cannot be null");
        }
        penColor = color;
        offscreen.setColor(penColor);
    }

    public static void setPenColor(int red, int green, int blue) {
        if (red < 0 || red >= 256) {
            throw new IllegalArgumentException("red must be between 0 and 255");
        }
        if (green < 0 || green >= 256) {
            throw new IllegalArgumentException("green must be between 0 and 255");
        }
        if (blue < 0 || blue >= 256) {
            throw new IllegalArgumentException("blue must be between 0 and 255");
        }
        setPenColor(new Color(red, green, blue));
    }

    // Font methods
    public static Font getFont() {
        return font;
    }

    public static void setFont() {
        setFont(DEFAULT_FONT);
    }

    public static void setFont(Font f) {
        if (f == null) {
            throw new IllegalArgumentException("font cannot be null");
        }
        font = f;
    }

    // Clear methods
    public static void clear() {
        clear(DEFAULT_CLEAR_COLOR);
    }

    public static void clear(Color color) {
        offscreen.setColor(color);
        offscreen.fillRect(0, 0, width, height);
        offscreen.setColor(penColor);
        draw();
    }

    // Drawing methods
    public static void line(double x0, double y0, double x1, double y1) {
        offscreen.draw(new Line2D.Double(scaleX(x0), scaleY(y0), scaleX(x1), scaleY(y1)));
        draw();
    }

    private static void pixel(double x, double y) {
        offscreen.fillRect((int) Math.round(scaleX(x)), (int) Math.round(scaleY(y)), 1, 1);
    }

    public static void point(double x, double y) {
        double xs = scaleX(x);
        double ys = scaleY(y);
        double r = penRadius;
        if (r <= 1) {
            pixel(x, y);
        } else {
            offscreen.fill(new Ellipse2D.Double(xs - r/2, ys - r/2, r, r));
        }
        draw();
    }

    public static void circle(double x, double y, double radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("radius must be non-negative");
        }
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2 * radius);
        double hs = factorY(2 * radius);
        offscreen.draw(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }

    public static void filledCircle(double x, double y, double radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("radius must be non-negative");
        }
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2 * radius);
        double hs = factorY(2 * radius);
        offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }

    public static void ellipse(double x, double y, double semiMajorAxis, double semiMinorAxis) {
        if (semiMajorAxis < 0) {
            throw new IllegalArgumentException("semi-major axis must be non-negative");
        }
        if (semiMinorAxis < 0) {
            throw new IllegalArgumentException("semi-minor axis must be non-negative");
        }
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2 * semiMajorAxis);
        double hs = factorY(2 * semiMinorAxis);
        offscreen.draw(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }

    public static void filledEllipse(double x, double y, double semiMajorAxis, double semiMinorAxis) {
        if (semiMajorAxis < 0) {
            throw new IllegalArgumentException("semi-major axis must be non-negative");
        }
        if (semiMinorAxis < 0) {
            throw new IllegalArgumentException("semi-minor axis must be non-negative");
        }
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2 * semiMajorAxis);
        double hs = factorY(2 * semiMinorAxis);
        offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }

    public static void arc(double x, double y, double radius, double angle1, double angle2) {
        if (radius < 0) {
            throw new IllegalArgumentException("radius must be non-negative");
        }
        while (angle2 < angle1) angle2 += 360;
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2 * radius);
        double hs = factorY(2 * radius);
        offscreen.draw(new Arc2D.Double(xs - ws/2, ys - hs/2, ws, hs, angle1, angle2 - angle1, Arc2D.OPEN));
        draw();
    }

    public static void square(double x, double y, double halfLength) {
        if (halfLength < 0) {
            throw new IllegalArgumentException("half length must be non-negative");
        }
        rectangle(x, y, halfLength, halfLength);
    }

    public static void filledSquare(double x, double y, double halfLength) {
        if (halfLength < 0) {
            throw new IllegalArgumentException("half length must be non-negative");
        }
        filledRectangle(x, y, halfLength, halfLength);
    }

    public static void rectangle(double x, double y, double halfWidth, double halfHeight) {
        if (halfWidth < 0) {
            throw new IllegalArgumentException("half width must be non-negative");
        }
        if (halfHeight < 0) {
            throw new IllegalArgumentException("half height must be non-negative");
        }
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2 * halfWidth);
        double hs = factorY(2 * halfHeight);
        offscreen.draw(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }

    public static void filledRectangle(double x, double y, double halfWidth, double halfHeight) {
        if (halfWidth < 0) {
            throw new IllegalArgumentException("half width must be non-negative");
        }
        if (halfHeight < 0) {
            throw new IllegalArgumentException("half height must be non-negative");
        }
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2 * halfWidth);
        double hs = factorY(2 * halfHeight);
        offscreen.fill(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
        draw();
    }

    public static void polygon(double[] x, double[] y) {
        if (x == null) {
            throw new IllegalArgumentException("x-coordinate array is null");
        }
        if (y == null) {
            throw new IllegalArgumentException("y-coordinate array is null");
        }
        if (x.length != y.length) {
            throw new IllegalArgumentException("arrays must be of the same length");
        }

        int n = x.length;
        if (n == 0) return;

        GeneralPath path = new GeneralPath();
        path.moveTo((float) scaleX(x[0]), (float) scaleY(y[0]));
        for (int i = 0; i < n; i++) {
            path.lineTo((float) scaleX(x[i]), (float) scaleY(y[i]));
        }
        path.closePath();
        offscreen.draw(path);
        draw();
    }

    public static void filledPolygon(double[] x, double[] y) {
        if (x == null) {
            throw new IllegalArgumentException("x-coordinate array is null");
        }
        if (y == null) {
            throw new IllegalArgumentException("y-coordinate array is null");
        }
        if (x.length != y.length) {
            throw new IllegalArgumentException("arrays must be of the same length");
        }

        int n = x.length;
        if (n == 0) return;

        GeneralPath path = new GeneralPath();
        path.moveTo((float) scaleX(x[0]), (float) scaleY(y[0]));
        for (int i = 0; i < n; i++) {
            path.lineTo((float) scaleX(x[i]), (float) scaleY(y[i]));
        }
        path.closePath();
        offscreen.fill(path);
        draw();
    }

    // Image methods
    private static Image getImage(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("filename is null");
        }

        ImageIcon icon = new ImageIcon(filename);
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            return icon.getImage();
        }

        try {
            URL url = new URL(filename);
            icon = new ImageIcon(url);
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                return icon.getImage();
            }
        } catch (Exception e) {
            // Try as resource
        }

        URL url = StdDraw.class.getResource(filename);
        if (url != null) {
            icon = new ImageIcon(url);
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                return icon.getImage();
            }
        }

        url = StdDraw.class.getResource("/" + filename);
        if (url == null) {
            throw new IllegalArgumentException("image " + filename + " not found");
        }

        icon = new ImageIcon(url);
        return icon.getImage();
    }

    public static void picture(double x, double y, String filename) {
        Image image = getImage(filename);
        double xs = scaleX(x);
        double ys = scaleY(y);
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        if (w < 0 || h < 0) {
            throw new IllegalArgumentException("image " + filename + " is corrupt");
        }
        offscreen.drawImage(image, (int) Math.round(xs - w/2.0), (int) Math.round(ys - h/2.0), null);
        draw();
    }

    public static void picture(double x, double y, String filename, double degrees) {
        Image image = getImage(filename);
        double xs = scaleX(x);
        double ys = scaleY(y);
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        if (w < 0 || h < 0) {
            throw new IllegalArgumentException("image " + filename + " is corrupt");
        }

        offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        offscreen.drawImage(image, (int) Math.round(xs - w/2.0), (int) Math.round(ys - h/2.0), null);
        offscreen.rotate(Math.toRadians(degrees), xs, ys);
        draw();
    }

    public static void picture(double x, double y, String filename, double scaledWidth, double scaledHeight) {
        Image image = getImage(filename);
        if (scaledWidth < 0) {
            throw new IllegalArgumentException("width is negative: " + scaledWidth);
        }
        if (scaledHeight < 0) {
            throw new IllegalArgumentException("height is negative: " + scaledHeight);
        }
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(scaledWidth);
        double hs = factorY(scaledHeight);
        offscreen.drawImage(image, (int) Math.round(xs - ws/2.0), (int) Math.round(ys - hs/2.0),
                (int) Math.round(ws), (int) Math.round(hs), null);
        draw();
    }

    public static void picture(double x, double y, String filename, double scaledWidth, double scaledHeight, double degrees) {
        Image image = getImage(filename);
        if (scaledWidth < 0) {
            throw new IllegalArgumentException("width is negative: " + scaledWidth);
        }
        if (scaledHeight < 0) {
            throw new IllegalArgumentException("height is negative: " + scaledHeight);
        }
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(scaledWidth);
        double hs = factorY(scaledHeight);

        offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        offscreen.drawImage(image, (int) Math.round(xs - ws/2.0), (int) Math.round(ys - hs/2.0),
                (int) Math.round(ws), (int) Math.round(hs), null);
        offscreen.rotate(Math.toRadians(degrees), xs, ys);
        draw();
    }

    // Text methods
    public static void text(double x, double y, String text) {
        if (text == null) {
            throw new IllegalArgumentException("text is null");
        }
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = metrics.stringWidth(text);
        int hs = metrics.getDescent();
        offscreen.drawString(text, (float) (xs - ws/2.0), (float) (ys + hs));
        draw();
    }

    public static void text(double x, double y, String text, double degrees) {
        if (text == null) {
            throw new IllegalArgumentException("text is null");
        }
        double xs = scaleX(x);
        double ys = scaleY(y);
        offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        text(x, y, text);
        offscreen.rotate(Math.toRadians(degrees), xs, ys);
    }

    public static void textLeft(double x, double y, String text) {
        if (text == null) {
            throw new IllegalArgumentException("text is null");
        }
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int hs = metrics.getDescent();
        offscreen.drawString(text, (float) xs, (float) (ys + hs));
        draw();
    }

    public static void textRight(double x, double y, String text) {
        if (text == null) {
            throw new IllegalArgumentException("text is null");
        }
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x);
        double ys = scaleY(y);
        int ws = metrics.stringWidth(text);
        int hs = metrics.getDescent();
        offscreen.drawString(text, (float) (xs - ws), (float) (ys + hs));
        draw();
    }

    // Display methods
    @Deprecated
    public static void show(int t) {
        show();
        pause(t);
        enableDoubleBuffering();
    }

    public static void show() {
        onscreen.drawImage(offscreenImage, 0, 0, null);
        frame.repaint();
    }

    private static void draw() {
        if (!defer) {
            show();
        }
    }

    public static void pause(int t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping");
        }
    }

    public static void enableDoubleBuffering() {
        defer = true;
    }

    public static void disableDoubleBuffering() {
        defer = false;
    }

    // Save methods
    public static void save(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("filename is null");
        }
        File file = new File(filename);
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);

        if (suffix.equalsIgnoreCase("png")) {
            try {
                ImageIO.write(onscreenImage, suffix, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (suffix.equalsIgnoreCase("jpg")) {
            try {
                ImageIO.write(onscreenImage, suffix, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid image file type: " + suffix);
        }
    }

    // Mouse methods
    public static boolean isMousePressed() {
        synchronized (mouseLock) {
            return isMousePressed;
        }
    }

    @Deprecated
    public static boolean mousePressed() {
        return isMousePressed();
    }

    public static double mouseX() {
        synchronized (mouseLock) {
            return mouseX;
        }
    }

    public static double mouseY() {
        synchronized (mouseLock) {
            return mouseY;
        }
    }

    // Keyboard methods
    public static boolean hasNextKeyTyped() {
        synchronized (keyLock) {
            return !keysTyped.isEmpty();
        }
    }

    public static char nextKeyTyped() {
        synchronized (keyLock) {
            if (keysTyped.isEmpty()) {
                throw new RuntimeException("your program has already processed all keystrokes");
            }
            return keysTyped.removeLast();
        }
    }

    public static boolean isKeyPressed(int keycode) {
        synchronized (keyLock) {
            return keysDown.contains(keycode);
        }
    }

    public static Character getKeyChar() {
        Character c = lastKeyChar;
        lastKeyChar = null;
        return c;
    }

    // Event handlers
    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("Save")) {
            FileDialog dialog = new FileDialog(frame, "Use a .png or .jpg extension", FileDialog.SAVE);
            dialog.setVisible(true);
            String filename = dialog.getFile();
            if (filename != null) {
                save(dialog.getDirectory() + File.separator + dialog.getFile());
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        synchronized (mouseLock) {
            mouseX = userX(e.getX());
            mouseY = userY(e.getY());
            isMousePressed = true;
            System.out.println("Mouse: " + mouseX + "," + mouseY);
        }
        draw();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        synchronized (mouseLock) {
            isMousePressed = false;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        synchronized (mouseLock) {
            mouseX = userX(e.getX());
            mouseY = userY(e.getY());
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        synchronized (mouseLock) {
            mouseX = userX(e.getX());
            mouseY = userY(e.getY());
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        synchronized (keyLock) {
            lastKeyChar = e.getKeyChar();
            keysTyped.addFirst(lastKeyChar);
            System.out.println(lastKeyChar);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        synchronized (keyLock) {
            keysDown.add(e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        synchronized (keyLock) {
            keysDown.remove(e.getKeyCode());
        }
    }

    // Test client
    public static void main(String[] args) {
        square(0.2, 0.8, 0.1);
        filledSquare(0.8, 0.8, 0.2);
        circle(0.8, 0.2, 0.2);

        setPenColor(BOOK_RED);
        setPenRadius(0.02);
        arc(0.8, 0.2, 0.1, 200.0, 45.0);

        setPenRadius();
        setPenColor(BOOK_BLUE);
        double[] x = {0.1, 0.2, 0.3, 0.2};
        double[] y = {0.2, 0.3, 0.2, 0.1};
        filledPolygon(x, y);

        setPenColor(BLACK);
        text(0.2, 0.5, "black text");
        setPenColor(WHITE);
        text(0.8, 0.8, "white text");
    }
}