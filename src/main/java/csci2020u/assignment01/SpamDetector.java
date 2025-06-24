package csci2020u.assignment01;

import java.io.*;
import java.util.*;

/*
 * SpamDetector is a classifier that uses a Naive Bayes model
 * to determine whether emails are spam or not.

 * Features:
 * - Trains using a dataset of labeled spam/ham emails.
 * - Computes word probabilities based on training data.
 * - Classifies test emails and calculates accuracy & precision.
 */

public class SpamDetector {
    // Stores word frequencies for ham emails
    private TreeMap<String, Integer> trainHamFreq;

    // Stores word frequencies for spam emails
    private TreeMap<String, Integer> trainSpamFreq;

    // Stores the probability of a word appearing in a spam email
    private TreeMap<String, Double> spamWordProbabilities;

    private int numHamFiles;
    private int numSpamFiles;

    // List of classified test emails with spam probability
    private List<TestFile> classifiedEmails;

    /*
     * Constructor initializes frequency maps and counters.
     */

    public SpamDetector() {
        trainHamFreq = new TreeMap<>();
        trainSpamFreq = new TreeMap<>();
        spamWordProbabilities = new TreeMap<>();
        numHamFiles = 0;
        numSpamFiles = 0;
    }

    /*
     * Trains the spam detector using a provided training directory.
     * It reads ham and spam emails, counts word occurrences,
     * and computes the probability of words appearing in spam messages.
     *
     * @param trainDir The directory containing 'ham' and 'spam' subfolders.
     */

    public void train(File trainDir) {
        File hamDir = new File(trainDir, "ham");
        File spamDir = new File(trainDir, "spam");

        // If ham directory exists, process its files
        if (hamDir.exists()) {
            processTrainingFiles(hamDir, trainHamFreq);
            numHamFiles = hamDir.listFiles().length;
        }

        // If spam directory exists, process its files
        if (spamDir.exists()) {
            processTrainingFiles(spamDir, trainSpamFreq);
            numSpamFiles = spamDir.listFiles().length;
        }

        // Compute word probabilities after processing files
        computeWordProbabilities();
    }

    /*
     * Processes a directory containing training emails (ham/spam),
     * extracts words from each email, and updates the word frequency map.
     *
     * @param directory    The directory containing email files.
     * @param wordFreqMap  The frequency map to update (ham or spam).
     */

    private void processTrainingFiles(File directory, TreeMap<String, Integer> wordFreqMap) {
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                Set<String> uniqueWords = extractWordsFromFile(file);
                for (String word : uniqueWords) {
                    wordFreqMap.put(word, wordFreqMap.getOrDefault(word, 0) + 1);
                }
            }
        }
    }

    /*
     * Extracts unique words from a given file by splitting text into tokens.
     * Non-word characters are used as delimiters, and words are converted to lowercase.
     *
     * @param file The file to read.
     * @return A set of unique words found in the file.
     */

    private Set<String> extractWordsFromFile(File file) {
        Set<String> words = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.toLowerCase().split("\\W+"); // Split on non-word characters
                words.addAll(Arrays.asList(tokens));
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + file.getName());
        }
        return words;
    }

    /*
     * Computes the probability that a word appears in spam emails.
     * Uses Laplace smoothing to handle words not found in the training data.
     */

    public void computeWordProbabilities() {
        spamWordProbabilities = new TreeMap<>();
        double smoothingFactor = 500.0; // Increase smoothing to significantly reduce extreme values

        // Calculate probability for words appearing in spam emails
        for (String word : trainSpamFreq.keySet()) {
            double pWiGivenS = (trainSpamFreq.getOrDefault(word, 0) + 1.0) / (numSpamFiles + smoothingFactor);
            double pWiGivenH = (trainHamFreq.getOrDefault(word, 0) + 1.0) / (numHamFiles + smoothingFactor);
            double pSiGivenWi = pWiGivenS / (pWiGivenS + pWiGivenH);

            spamWordProbabilities.put(word, pSiGivenWi);
        }

        // Calculate probability for words appearing only in ham emails
        for (String word : trainHamFreq.keySet()) {
            if (!spamWordProbabilities.containsKey(word)) {
                double pWiGivenS = (0 + 1.0) / (numSpamFiles + smoothingFactor);
                double pWiGivenH = (trainHamFreq.get(word) + 1.0) / (numHamFiles + smoothingFactor);
                double pSiGivenWi = pWiGivenS / (pWiGivenS + pWiGivenH);

                spamWordProbabilities.put(word, pSiGivenWi);
            }
        }
    }

    /*
     * Returns the probability of a given word appearing in spam emails.
     *
     * @param word The word to check.
     * @return The probability of the word appearing in spam emails.
     */

    public double getWordProbability(String word) {
        return spamWordProbabilities.getOrDefault(word, 0.0);
    }

    /*
     * Classifies test emails by calculating their spam probability.
     *
     * @param testDir The directory containing test emails.
     */

    public void classifyTestEmails(File testDir) {
        classifiedEmails = new ArrayList<>();
        File hamDir = new File(testDir, "ham");
        File spamDir = new File(testDir, "spam");

        if (hamDir.exists()) {
            classifyEmailsInDirectory(hamDir, "Ham");
        }
        if (spamDir.exists()) {
            classifyEmailsInDirectory(spamDir, "Spam");
        }
    }

    /*
     * Classifies emails in a given directory by computing spam probability.
     *
     * @param directory   The directory containing email files.
     * @param actualClass The actual class (Ham/Spam).
     */

    private void classifyEmailsInDirectory(File directory, String actualClass) {
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                double spamProbability = computeEmailSpamProbability(file);
                classifiedEmails.add(new TestFile(file.getName(), spamProbability, actualClass));
            }
        }
    }

    /*
     * Computes the probability that a given email is spam.
     * Uses a logarithmic approach to prevent floating-point underflow.
     *
     * @param file The email file.
     * @return The probability that the email is spam.
     */

    private double computeEmailSpamProbability(File file) {
        Set<String> words = extractWordsFromFile(file);
        double eta = 0.0;
        int wordCount = words.size();

        if (wordCount == 0) return 0.5; // Neutral probability for empty emails

        double normalizationFactor = Math.sqrt(wordCount);

        for (String word : words) {
            double pSgivenWi = spamWordProbabilities.getOrDefault(word, 0.5);
            pSgivenWi = Math.max(0.1, Math.min(0.9, pSgivenWi));
            eta += (Math.log(1 - pSgivenWi) - Math.log(pSgivenWi)) / normalizationFactor;
        }

        return 1 / (1 + Math.exp(eta)); // Convert eta to probability using sigmoid function
    }

    /*
     * Computes the accuracy of the spam classifier.
     * Accuracy measures how often the classifier correctly identifies spam and ham emails.

     * It is calculated as:
     * Accuracy = (Number of Correct Predictions) / (Total Number of Predictions)

     * A correct prediction means:
     * - If an email is actually spam, and the classifier marks it as spam.
     * - If an email is actually ham, and the classifier marks it as ham.

     * @return The accuracy of the classifier as a value between 0 and 1.
     */
    public double computeAccuracy() {
        int correctPredictions = 0; // Counter for correct classifications

        // Iterate over all classified emails
        for (TestFile email : classifiedEmails) {
            boolean isSpam = email.getSpamProbability() > 0.5; // Our classifier's prediction (threshold = 0.5)
            boolean actualSpam = email.getActualClass().equals("Spam"); // The true classification from dataset

            if (isSpam == actualSpam) {
                correctPredictions++; // Increment count if the classifier's prediction is correct
            }
        }

        // Return accuracy as a fraction of total classified emails
        return (double) correctPredictions / classifiedEmails.size();
    }

    /*
     * Computes the precision of the spam classifier.
     * Precision measures how accurate the classifier is when it predicts an email as spam.

     * It is calculated as:
     * Precision = (True Positives) / (True Positives + False Positives)

     * - True Positives (TP): Number of spam emails correctly classified as spam.
     * - False Positives (FP): Number of ham emails incorrectly classified as spam.

     * A higher precision means fewer ham emails are mistakenly classified as spam.

     * @return The precision of the classifier as a value between 0 and 1.
     */
    public double computePrecision() {
        int truePositives = 0;  // Emails correctly classified as spam
        int falsePositives = 0; // Ham emails incorrectly classified as spam

        // Iterate over all classified emails
        for (TestFile email : classifiedEmails) {
            boolean isSpam = email.getSpamProbability() > 0.5; // Classifier's prediction
            boolean actualSpam = email.getActualClass().equals("Spam"); // The true label from dataset

            if (isSpam) { // If the classifier predicted spam
                if (actualSpam) {
                    truePositives++; // Correctly identified spam
                } else {
                    falsePositives++; // Incorrectly classified ham as spam
                }
            }
        }

        // Avoid division by zero
        if ((truePositives + falsePositives) == 0) return 0.0;

        // Return precision as a fraction of correct spam classifications
        return (double) truePositives / (truePositives + falsePositives);
    }



    public List<TestFile> getClassifiedEmails() {
        return classifiedEmails;
    }

}