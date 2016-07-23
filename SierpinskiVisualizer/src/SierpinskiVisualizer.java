/**
 * Sierpinski Visualizer Class
 * 
 * Description: This code recursively draws smaller triangles within larger triangles
 * according to proportion, also known as the Sierpinski triangle or Sierpinski triangle
 * fractal. Fractals are Mathematical phenomena in nature and such is the name provided to
 * patterns of snow flakes and other naturally occurring repetitive patterns.
 * 
 * @author Edmary Rosado
 * 
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;

public class SierpinskiVisualizer {

	JFrame window;
	Canvas canvas;

	JButton button;
	@SuppressWarnings("rawtypes")
	JComboBox cBox1, cBox2, cBox3, cBox4, cBox5;
	JCheckBox randomizationCheckBox;
	JTextField recursiveDepthTextField;
	JLabel recurseTxt;
	JLabel colorLbl;
	SierpinskiTriangle triangle;
	JPanel userPanel;

	int MAX = 10, width = 512, height = 512, depth = 0;

	private final String[] colorsName = new String[] { "Black", "Cyan",
			"Dark Gray", "Green", "Blue", "Light Gray", "Red", "Magenta",
			"Yellow", "White", "Orange", "Purple" };

	/**
	 * Sierpinski Visualizer Class Constructor
	 */
	public SierpinskiVisualizer() {
		// Setup window skeleton
		window = new JFrame("Sierpinski Visualizer");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(new GridLayout(1, 2));
		window.setSize(780, 558);
		window.setResizable(false);

		// Initialize window components
		initComponents();

		// Add Listeners
		addListeners();

		// Add Canvas to Drawing Panel
		JPanel drawing = new JPanel(new FlowLayout(FlowLayout.CENTER));
		drawing.add(canvas);
		drawing.setSize(512, 512);

		JPanel sierpinskiPanel = new JPanel(new FlowLayout());
		sierpinskiPanel.add(drawing);
		sierpinskiPanel.add(userPanel);

		// Add the panel to the window.
		window.add(sierpinskiPanel);
		window.setVisible(true);
		
		// Start Drawing
		triangle = new SierpinskiTriangle(depth, 0, 0, width);
		
		// Set Initial Colors
		triangle.setColor(0, cBox1.getSelectedIndex());
		triangle.setColor(1, cBox2.getSelectedIndex());
		triangle.setColor(2, cBox3.getSelectedIndex());
		triangle.setColor(3, cBox4.getSelectedIndex());
		triangle.setColor(4, cBox5.getSelectedIndex());
	}

	/**
	 * Initializes the window components
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initComponents() {
		// Set up Input Text Field and Label
		recursiveDepthTextField = new JTextField("0", 5);
		recurseTxt = new JLabel("Recursive Depth:");

		// Set up the Canvas
		canvas = new Canvas();
		canvas.setBackground(Color.BLACK);
		canvas.setSize(width, height);

		// Add the Text Box and Label in Input Panel
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout());
		inputPanel.add(recurseTxt);
		inputPanel.add(recursiveDepthTextField);

		// Add Selection Objects selections Panel
		JPanel selectionsPanel = new JPanel();
		selectionsPanel.setLayout(new GridLayout(5, 0));

		colorLbl = new JLabel("Color " + 1);
		selectionsPanel.add(colorLbl);
		cBox1 = new JComboBox(colorsName);
		cBox1.setSelectedIndex(0);
		selectionsPanel.add(cBox1);

		colorLbl = new JLabel("Color " + 2);
		selectionsPanel.add(colorLbl);
		cBox2 = new JComboBox(colorsName);
		cBox2.setSelectedIndex(1);
		selectionsPanel.add(cBox2);

		colorLbl = new JLabel("Color " + 3);
		selectionsPanel.add(colorLbl);
		cBox3 = new JComboBox(colorsName);
		cBox3.setSelectedIndex(3);
		selectionsPanel.add(cBox3);

		colorLbl = new JLabel("Color " + 4);
		selectionsPanel.add(colorLbl);
		cBox4 = new JComboBox(colorsName);
		cBox4.setSelectedIndex(4);
		selectionsPanel.add(cBox4);

		colorLbl = new JLabel("Color " + 5);
		selectionsPanel.add(colorLbl);
		cBox5 = new JComboBox(colorsName);
		cBox5.setSelectedIndex(5);
		selectionsPanel.add(cBox5);

		// Set up Buttons and Selection Objects
		randomizationCheckBox = new JCheckBox("Randomize colors at each level");
		randomizationCheckBox.setSelected(false);
		button = new JButton("Draw!");

		JPanel randomize = new JPanel();
		randomize.add(randomizationCheckBox);

		JPanel buttonPanel = new JPanel();
		// buttonPanel.setSize(5,5);
		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.add(button, BorderLayout.CENTER);

		// Add Input Panel and Selections Panel into User Panel
		userPanel = new JPanel();
		userPanel.setLayout(new BoxLayout(userPanel, 1));
		userPanel.add(inputPanel);
		userPanel.add(selectionsPanel);
		userPanel.add(randomize);
		userPanel.add(buttonPanel);

	}

	/**
	 * Adds the individual listeners for window components
	 */
	public void addListeners() {
		// Register the event listeners.
		cBox1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
					triangle.setColor(0, cBox1.getSelectedIndex());
			}
		});
		cBox2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
					triangle.setColor(1, cBox2.getSelectedIndex());
			}
		});
		cBox3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {

					triangle.setColor(2, cBox3.getSelectedIndex());
			}
		});
		cBox4.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
					triangle.setColor(3, cBox4.getSelectedIndex());
			}
		});
		cBox5.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
					triangle.setColor(4, cBox5.getSelectedIndex());

			}
		});

		randomizationCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if(randomizationCheckBox.isSelected()){
					triangle.setRandomColorization(true);
				}else
					triangle.setRandomColorization(false);
			}
		});

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvas.getGraphics();
				depth = Integer.parseInt(recursiveDepthTextField.getText());

				if (e.getSource() == button) {
					if (depth > MAX) {
						JOptionPane
								.showMessageDialog(
										window,
										"Invalid Number, "
												+ "try something smaller (less than 10)");
					} else {
						triangle.setGraphics(canvas);
						triangle.setDepth(depth);
						triangle.draw(depth, 0, 0, width);
					}
				}
			}
		});
	}

	/**
	 * Main Program Driver
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new SierpinskiVisualizer();
	}
}