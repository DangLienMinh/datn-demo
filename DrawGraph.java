import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

@SuppressWarnings("serial")
public class DrawGraph extends JPanel {

	private static final int PREF_W = 800;
	private static final int PREF_H = 600;
	private static final int BORDER_GAP = 50;
	private static final Color GRAPH_COLOR = Color.red;
	private static final Color GRAPH_POINT_COLOR = Color.black;
	private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
	private static final int GRAPH_POINT_WIDTH = 4;
	private static final int Y_HATCH_CNT = 15;
	private List<Integer> scores;
	private List<Integer> gtt;
	private int min;
	private int max;
	private List<String> printDay;
	private int valueY;

	private float deltaY = 0;// ((float)((getHeight() - BORDER_GAP * 2) /
								// Y_HATCH_CNT))/valueY;

	public DrawGraph(List<Integer> scores, List<Integer> gtt, List<String> day) {

		printDay = new ArrayList<String>();

		for (int i = 0; i < day.size(); i++) {
			printDay.add(day.get(i));
		}
		this.gtt = gtt;
		this.scores = scores;
		System.out.println(deltaY);

	}

	public DrawGraph() {
		// this.scores = scores;
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		min = Integer.MAX_VALUE;
		max = Integer.MIN_VALUE;
		int gama = 100;

		for (int i = 0; i < gtt.size(); i++) {
			int vl = gtt.get(i);
			if (vl > max)
				max = vl;
			if (vl < min)
				min = vl;
		}

		for (int i = 0; i < scores.size(); i++) {
			int vl = scores.get(i);
			if (vl > max)
				max = vl;
			if (vl < min)
				min = vl;
		}

		min -= (min % gama);
		max += (gama - max % gama);

		int delta = max - min;

		valueY = delta / Y_HATCH_CNT;

		deltaY = (float) ((getHeight() - BORDER_GAP * 2) * 1.0 / Y_HATCH_CNT / valueY);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// Ve 2 truc ox va oy
		g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);// Truc oy
		g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);// Truc ox

		// ve cac toa do tren truc oy
		for (int i = 0; i < Y_HATCH_CNT; i++) {
			int x0 = BORDER_GAP;
			int x1 = GRAPH_POINT_WIDTH + BORDER_GAP;
			int y0 = getHeight()
					- (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
			int y1 = y0;
			g2.drawLine(x0, y0, x1, y1);
			int a = (i) * valueY + min;
			g2.drawString(Integer.toString(a), BORDER_GAP - 40, y0); 																
		}

		// ve cac toa do tren truc ox
		for (int i = 0; i < printDay.size(); i++) {
			int x0 = (i+1) * (getWidth() - BORDER_GAP * 2) / (12)
					+ BORDER_GAP;
			int x1 = x0;
			int y0 = getHeight() - BORDER_GAP;
			int y1 = y0 - GRAPH_POINT_WIDTH;
			g2.drawLine(x0, y0, x1, y1);

			// int a=day +1;
			
				g2.drawString(printDay.get(i), x0 - 10, y0 + 20);
			
		}

		// Ve bieu do cua gia tri that
		Stroke oldStroke = g2.getStroke();
		g2.setStroke(GRAPH_STROKE);
		for (int i = 0; i < gtt.size() - 1; i++) {
			g2.setColor(GRAPH_COLOR);
			int x1 = (i + 1) * (getWidth() - BORDER_GAP * 2) / (11 - 1)
					+ BORDER_GAP;
			int x2 = (i + 2) * (getWidth() - BORDER_GAP * 2) / (11 - 1)
					+ BORDER_GAP;
			int y1 = (int) (getHeight() - (gtt.get(i) - min) * deltaY
					- BORDER_GAP - ((getHeight() - BORDER_GAP * 2) / Y_HATCH_CNT));
			int y2 = (int) (getHeight() - (gtt.get(i + 1) - min) * deltaY
					- BORDER_GAP - ((getHeight() - BORDER_GAP * 2) / Y_HATCH_CNT));

			g2.drawLine(x1, y1, x2, y2);
		}

		// Ve bieu do cua gia tri du doan
		g2.setStroke(GRAPH_STROKE);
		for (int i = 0; i < scores.size() - 1; i++) {
			g2.setColor(Color.GREEN);
			int x1 = (i + 1) * (getWidth() - BORDER_GAP * 2) / (11 - 1)
					+ BORDER_GAP;
			int x2 = (i + 2) * (getWidth() - BORDER_GAP * 2) / (11 - 1)
					+ BORDER_GAP;
			int y1 = (int) (getHeight() - (1 + scores.get(i) - min) * deltaY
					- BORDER_GAP - ((getHeight() - BORDER_GAP * 2) / Y_HATCH_CNT));
			int y2 = (int) (getHeight() - (1 + scores.get(i + 1) - min)
					* deltaY - BORDER_GAP - ((getHeight() - BORDER_GAP * 2) / Y_HATCH_CNT));

			g2.drawLine(x1, y1, x2, y2);
		}

		g2.setStroke(oldStroke);
		g2.setColor(Color.RED);
		for (int i = 0; i < scores.size(); i++) {
			int x = (i + 1) * (getWidth() - BORDER_GAP * 2) / (11 - 1)
					+ BORDER_GAP;
			int y = (int) (getHeight() - (scores.get(i) - min) * deltaY
					- BORDER_GAP - ((getHeight() - BORDER_GAP * 2) / Y_HATCH_CNT));
			int ovalW = GRAPH_POINT_WIDTH;
			int ovalH = GRAPH_POINT_WIDTH;
			g2.fillOval(x, y, ovalW, ovalH);
		}

		g2.setStroke(oldStroke);
		g2.setColor(GRAPH_POINT_COLOR);
		for (int i = 0; i < gtt.size(); i++) {
			int x = (i + 1) * (getWidth() - BORDER_GAP * 2) / (11 - 1)
					+ BORDER_GAP;
			int y = (int) (getHeight() - (gtt.get(i) - min) * deltaY
					- BORDER_GAP - ((getHeight() - BORDER_GAP * 2) / Y_HATCH_CNT));
			int ovalW = GRAPH_POINT_WIDTH;
			int ovalH = GRAPH_POINT_WIDTH;
			g2.fillOval(x, y, ovalW, ovalH);

			/*
			 * if(i>gtt.size()-scores.size()){ x =(i+1)* (getWidth() -
			 * BORDER_GAP * 2) / (20 - 1) + BORDER_GAP; y =(int)( getHeight() -
			 * (scores.get(i- gtt.size()+scores.size())-min)*deltaY -
			 * BORDER_GAP); g2.fillOval(x, y, ovalW, ovalH); }
			 */
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PREF_W, PREF_H);
	}
	/*
	 * private void createAndShowGui() { List<Integer> scores = new
	 * ArrayList<Integer>(); Random random = new Random(); int maxDataPoints =
	 * 16; int maxScore = 20; for (int i = 0; i < maxDataPoints ; i++) {
	 * scores.add(i); } DrawGraph mainPanel = new DrawGraph(scores,5,8);
	 * 
	 * JFrame frame = new JFrame("DrawGraph");
	 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 * frame.getContentPane().add(mainPanel); frame.pack();
	 * frame.setLocationByPlatform(true); frame.setVisible(true); }
	 */

}