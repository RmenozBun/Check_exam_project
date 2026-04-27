import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import java.io.*;

public class App {

    static class Node {
        String data;
        Node next;
        Node(String value) {
            data = value;
            next = null;
        }
    }

    static Node answerHead = null;
    static String F_st_name = "";

    static void addAnswerFromPDF(String filePath) {
        String answers = readPDF(filePath);
        Pattern pattern = Pattern.compile("\\d+\\.([a-dA-D]|yes|no|[A-Za-z]+)");
        Matcher matcher = pattern.matcher(answers);
        while (matcher.find()) {
            addAnswer(matcher.group(1).toLowerCase());
        }
    }
    
    static void addAnswer(String value) {
        Node newNode = new Node(value);
        if (answerHead == null) {
            answerHead = newNode;
        } else {
            Node current = answerHead;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }
    
    private static String readPDF(String filePath) {
        String text = "";
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            text = pdfStripper.getText(document);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e, "ERROR to read PDF!", JOptionPane.ERROR_MESSAGE);
        }
        return text;
    }
    
    private static Node extractStudnetAnswersFromPDF(String filePath) {
        String answers = readPDF(filePath);
        Node studentHead = null, current = null;
        Pattern pattern = Pattern.compile("\\d+\\.([a-dA-D]|yes|no|[A-Za-z]+)");
        Matcher matcher = pattern.matcher(answers);
        while (matcher.find()) {
            Node newNode = new Node(matcher.group(1).toLowerCase());
            if (studentHead == null) {
                studentHead = newNode;
                current = studentHead;
            } else {
                current.next = newNode;
                current = current.next;
            }
        }
        return studentHead;
    }
    
    private static int checkAnswers(Node studentHead) {
        Node correctNode = answerHead;
        Node studentNode = studentHead;
        int correctCount = 0;
        while (correctNode != null && studentNode != null) {
            if (correctNode.data.equals(studentNode.data)) {
                correctCount++;
            }
            correctNode = correctNode.next;
            studentNode = studentNode.next;
        }
        return correctCount;
    }
    
    private static int getLength(Node answerHead) {
        int length = 0;
        Node current = answerHead;
        while (current != null) {
            length++;
            current = current.next;
        }
        return length;
    }

    private static String getFilepath() {
        String filePath = "";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose file");
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName();
            F_st_name = fileName.substring(0, fileName.lastIndexOf("."));
            filePath = selectedFile.getAbsolutePath();
            JOptionPane.showMessageDialog(null, fileName, "get file path", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Please choose file!","ERROR!",JOptionPane.ERROR_MESSAGE);
        }
        return filePath;
    }

    public static void main(String[] args) {
        while (true) {
            answerHead = null;
            String userInput1 = JOptionPane.showInputDialog("Please enter answer file : \n o to scan answer file : \n e to exit program :");
            if (userInput1.equalsIgnoreCase("o")) {
                String answer_path = getFilepath();
                addAnswerFromPDF(answer_path);
                while (true) {
                    String userInput2 = JOptionPane.showInputDialog("Please enter student file : \n o to scan student answer file : \n e to return add answer :");
                    if (userInput2.equalsIgnoreCase("o")) {
                        String student_path = getFilepath();
                        Node studentHead = extractStudnetAnswersFromPDF(student_path);

                        int correctCount = checkAnswers(studentHead);
                        int totalQuestions = getLength(answerHead);

                        String save_score = F_st_name+" score = "+correctCount+"/" + totalQuestions;
                        JOptionPane.showMessageDialog(null, save_score, F_st_name, JOptionPane.INFORMATION_MESSAGE);

                        Score_File s_f = new Score_File();
                        s_f.appendToFile(save_score);
                    }
                    else if (userInput2.equalsIgnoreCase("e")) {
                        break;
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Please enter again", "ENTER ERROR", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            else if (userInput1.equalsIgnoreCase("e")) {
                System.exit(0);
            }
            else {
                JOptionPane.showMessageDialog(null, "Please enter again", "ENTER ERROR", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}