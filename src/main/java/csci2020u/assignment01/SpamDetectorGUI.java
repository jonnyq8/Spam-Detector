package csci2020u.assignment01;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/*
 * SpamDetectorGUI is the graphical user interface for the Spam Detector application.

 * Features:
 * - Displays a home page with an introduction to the application.
 * - Allows users to train a spam classifier using a dataset.
 * - Allows users to classify test emails and display results.
 * - Computes and displays accuracy and precision metrics.
 */
public class SpamDetectorGUI extends JFrame {
    public static void main(String[] args) {
        showHomePage();
    }

    /*
     * Displays the home page of the application.
     * The home page includes a brief introduction to the Spam Detector,
     * along with instructions for using the application.
     */

    private static void showHomePage() {
        JFrame homeFrame = new JFrame("Welcome to Spam Detector");
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeFrame.setSize(600, 400);
        homeFrame.setLayout(new BorderLayout());

        // Main panel for styling and layout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30)); // Dark background for modern look
        homeFrame.setContentPane(panel);

        // Header (Title)
        JLabel welcomeLabel = new JLabel("Spam Detector", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));
        welcomeLabel.setForeground(Color.WHITE);
        panel.add(welcomeLabel, BorderLayout.NORTH);

        // Explanation Panel (Centered Text)
        JPanel centerPanel = new JPanel(new GridBagLayout()); // GridBagLayout helps center content
        centerPanel.setBackground(new Color(30, 30, 30));

        JLabel explanationText = new JLabel(
                "<html><div style='text-align: center; width: 500px;'>" +
                        "Welcome to our Spam Detector application!!.<br><br>" +
                        "This application classifies emails as spam or ham using a Naive Bayes classifier.<br>" +
                        "This application also calculates the accuracy and precision of our spam detector.<br><br>" +
                        "1. Click 'Train' to train the model using provided email datasets.<br>" +
                        "2. Click 'Classify' to analyze test emails.<br>" +
                        "3. View results in the UI.<br><br>" +
                        "Certain information regarding how accuracy & precision can be viewed next to their respective fields. <br><br>" +
                        "Press 'Start' to proceed." +
                        "</div></html>"
        );
        explanationText.setFont(new Font("Arial", Font.PLAIN, 14));
        explanationText.setForeground(Color.WHITE);

        centerPanel.add(explanationText);
        panel.add(centerPanel, BorderLayout.CENTER);

        // Start Button
        JButton startButton = new JButton("Start");
        styleButton(startButton); // Apply button styling
        startButton.addActionListener(e -> {
            homeFrame.dispose(); // Close home page
            showMainGUI(); // Open main spam detector UI
        });

        // Bottom panel for the Start button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(30, 30, 30));
        buttonPanel.add(startButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        homeFrame.setVisible(true);
    }

    /*
     * Displays the main graphical user interface of the spam detector.
     * This window allows users to:
     * - Train the classifier
     * - Classify test emails
     * - View classification results, accuracy, and precision
     */

    private static void showMainGUI() {
        // Define dataset directories
        File trainDirectory = new File("src/main/resources/data/train");
        File testDirectory = new File("src/main/resources/data/test");

        // Create the SpamDetector instance
        SpamDetector detector = new SpamDetector();

        // Create the main application frame
        JFrame frame = new JFrame("Spam Detector");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 600);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(45, 45, 45));
        frame.setContentPane(mainPanel);

        JLabel headerLabel = new JLabel("Spam Detector", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Table for displaying classification results
        String[] columnNames = {"Filename", "ActualClass", "Spam Probability"};
        JTable table = new JTable(new DefaultTableModel(new String[0][3], columnNames));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        JScrollPane sp = new JScrollPane(table);
        mainPanel.add(sp, BorderLayout.CENTER);

        // Bottom Panel (For the Accuracy and Precision Field)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(new Color(60, 60, 60));
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Accuracy Field & Label
        JLabel accuracyLabel = new JLabel("Accuracy:", JLabel.RIGHT);
        accuracyLabel.setForeground(Color.WHITE);
        JTextField accuracyField = new JTextField(8);
        accuracyField.setEditable(false);

        // Precision Field & Label
        JLabel precisionLabel = new JLabel("Precision:", JLabel.RIGHT);
        precisionLabel.setForeground(Color.WHITE);
        JTextField precisionTextField = new JTextField(8);
        precisionTextField.setEditable(false);

        // Info Icons for Accuracy & Precision: User will have to hover over this to see the extra info
        JLabel accuracyInfo = createInfoIcon("Accuracy measures how often our spam detector makes the right classification. "
                + "\nFormula: Accuracy = (Number of Correct Guesses) / (Total Number of Guesses)");

        JLabel precisionInfo = createInfoIcon("Precision measures how often emails classified as spam are actually spam."
                + "\nFormula: Precision = (True Positives) / (False Positives + True Positives)");

        // Adding Accuracy & Precision fields to bottom panel
        bottomPanel.add(accuracyLabel);
        bottomPanel.add(accuracyField);
        bottomPanel.add(accuracyInfo);
        bottomPanel.add(precisionLabel);
        bottomPanel.add(precisionTextField);
        bottomPanel.add(precisionInfo);

        // Top Panel (Train & Classify Buttons)
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(new Color(30, 30, 30));

        JButton trainButton = new JButton("Train");
        JButton classifyButton = new JButton("Classify");

        styleButton(trainButton);
        styleButton(classifyButton);

        topPanel.add(trainButton);
        topPanel.add(classifyButton);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        frame.setVisible(true);

        // Train Button Action
        trainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detector.train(trainDirectory);
                JOptionPane.showMessageDialog(frame, "Training Completed.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Classify Button Action
        classifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detector.classifyTestEmails(testDirectory);

                double accuracy = detector.computeAccuracy();
                double precision = detector.computePrecision();
                accuracyField.setText(String.format("%.5f", accuracy));
                precisionTextField.setText(String.format("%.5f", precision));

                String[][] data2 = new String[detector.getClassifiedEmails().size()][3];
                for (int i = 0; i < detector.getClassifiedEmails().size(); i++) {
                    data2[i][0] = detector.getClassifiedEmails().get(i).getFilename();
                    data2[i][1] = detector.getClassifiedEmails().get(i).getActualClass();
                    data2[i][2] = detector.getClassifiedEmails().get(i).getSpamProbRounded();
                }
                DefaultTableModel model = new DefaultTableModel(data2, columnNames);
                table.setModel(model);

                frame.revalidate();
                frame.repaint();
            }
        });

    }

    /*
     * Styles a JButton to have a modern and visually appealing appearance.

     * - Sets font to bold with a size of 16.
     * - Changes the background color to blue.
     * - Sets the text color to white.
     * - Disables focus and border painting for a cleaner UI.
     * - Adjusts button size for consistency.

     * @param button The JButton to style.
     */
    private static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Set bold font with size 16
        button.setBackground(new Color(50, 150, 250)); // Set background color to blue
        button.setForeground(Color.WHITE); // Set text color to white
        button.setFocusPainted(false); // Disable focus paint for a cleaner look
        button.setBorderPainted(false); // Remove button border
        button.setPreferredSize(new Dimension(120, 40)); // Set button size
    }

    /*
     * Creates an information icon (ℹ️) with a tooltip explaining Accuracy or Precision.

     * - Retrieves the default Swing info icon.
     * - Converts it into an ImageIcon.
     * - Scales the icon to 15x15 pixels for a compact size.
     * - Wraps the provided tooltip text inside an HTML tooltip.

     * @param tooltipText The explanation to be displayed when the user hovers over the icon.
     * @return A JLabel containing the scaled info icon with a tooltip.
     */
    private static JLabel createInfoIcon(String tooltipText) {
        // Retrieve the default Swing information icon
        Icon defaultIcon = UIManager.getIcon("OptionPane.informationIcon");
        // Convert the default icon to an ImageIcon
        ImageIcon infoIcon = (defaultIcon instanceof ImageIcon) ? (ImageIcon) defaultIcon : new ImageIcon();

        Image scaledImage = infoIcon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        ImageIcon smallInfoIcon = new ImageIcon(scaledImage);
        JLabel infoLabel = new JLabel(smallInfoIcon);

        // Set tooltip text, allowing multi-line explanations using HTML formatting
        infoLabel.setToolTipText("<html>" + tooltipText.replace("\n", "<br>") + "</html>");

        return infoLabel;
    }

}