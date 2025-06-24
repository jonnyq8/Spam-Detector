# Spam Detector

## Project Information
The **Spam Detector** is a Java-based email classification system that uses a **Naive Bayes classifier** to determine whether an email is **spam** or **ham** (not spam). It includes a **graphical user interface (GUI)** built with **Java Swing**, allowing users to train the classifier, analyze test emails, and evaluate classification performance using **accuracy and precision metrics**. This project demonstrates key concepts in **machine learning, natural language processing (NLP), and probability-based classification**, making it a practical introduction to spam filtering techniques.

## Improvement/Key Features

- Added a **home screen** with two buttons: one to **train** and one to **classify** the emails.
- Changed the **background to black** and the **text to white**.
- Implemented a **start screen** that explains how to use the program using **HTML and CSS text formatting**.
- Used **GridBagLayout** and **HTML & CSS** to center the text and added a **start button** to enter the Spam Detector.
- Designed a **top panel** with a black background and added two buttons with **white text and blue background**.
- Used **FlowLayout** to center the buttons.
- Displayed a **message when the Train button is clicked**, informing the user that the model has been trained.
- Implemented a **Classify button** that takes the training information and classifies the emails.
  - Emails in the **ham directory** are classified as **ham**.
  - Emails in the **spam directory** are classified as **spam**.
- Estimated the **probability** of whether the emails are spam or ham and updated the **JTable** with all the results.
- Used **FlowLayout** for the bottom panel to **center the components** and added two **JTextFields**.
- When emails are classified, the text fields display **accuracy percentage** and **precision percentage**.
- Added **info icons** next to accuracy and precision fields, displaying details on hover about how precision and accuracy are calculated.
- Overall, we **improved the UI, added a start screen, updated aesthetics, and provided enhanced user experience.**

## Project Structure
```
├── src/main/java/csci2020u.assignment01/
│   ├── SpamDetector.java        # Core classifier logic (Naive Bayes)
│   ├── SpamDetectorGUI.java     # Graphical User Interface
│   ├── TestFile.java            # Class representing a test email file
│
├── src/main/resources/data/
│   ├── train/
│   │   ├── ham/                 # Training ham emails
│   │   ├── spam/                # Training spam emails
│   ├── test/
│       ├── ham/                 # Test ham emails
│       ├── spam/                # Test spam emails
│
└── README.md                    # Documentation
```

## Installation
```sh
git clone https://github.com/OntarioTech-CS-program/w25-csci2020u-assignment01-a1-jojijohn-forrester.git
```
1. Open the project in **IntelliJ IDEA, VS Code, or Eclipse**.
2. Ensure **Java 8+** is installed on your system.

## Running the Application
1. Navigate to the project's `src` folder.
2. Compile and run the `SpamDetectorGUI` class:
```sh
javac csci2020u.assignment01/SpamDetectorGUI.java
java csci2020u.assignment01.SpamDetectorGUI
```
3. The application window will appear, allowing you to **train and classify emails**.

## How to Use
### **Step 1: Train the Model**
- Click the **Train** button to process labeled spam and ham emails.
- The classifier will extract **word frequencies** and compute **probabilities**.

### **Step 2: Classify Test Emails**
- Click the **Classify** button to analyze test emails.
- The system will display each email's **filename, actual classification, and spam probability**.

### **Step 3: View Accuracy & Precision**
- **Accuracy**: Measures how often the classifier makes the correct decision.
  - **Formula:** `Accuracy = (Correct Predictions) / (Total Predictions)`
- **Precision**: Measures how often emails predicted as spam are actually spam.
  - **Formula:** `Precision = (True Positives) / (True Positives + False Positives)`
 
  ### Demo

  https://github.com/user-attachments/assets/dfc3a71e-3567-465b-ad46-1e2d43ddcf11


## Resources Used
- **Java Swing** for GUI
- **Java Collections (TreeMap, ArrayList)** for data processing
- **File I/O (BufferedReader, FileReader)** for reading emails
- **Naive Bayes Classifier** for spam detection
- **Javadoc** for code documentation
- **Git & GitHub** for version control and collaboration
- **HTML & CSS (via JLabel HTML formatting)** for tooltip styling and UI text rendering

### References
[1] https://en.wikipedia.org/wiki/Bag-of-words_model

[2] https://en.wikipedia.org/wiki/Naive_Bayes_spam_filtering

## Authors
- **Manu Joji John** - Developer
- **Jonathan Forrester** - Developer 
