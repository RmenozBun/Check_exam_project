import java.io.*;
import javax.swing.JOptionPane;

public class Score_File {
    
    private String filePath = "../Check_exam_project_6601567/score_file/score.txt";
    
    public String readFile() {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e, "ERROR to read file!", JOptionPane.ERROR_MESSAGE);
        }
        return content.toString();
    }
    
    public void writeFile(String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e, "ERROR to write file!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void appendToFile(String additionalContent) {
        String currentContent = readFile();
        currentContent += additionalContent + System.lineSeparator();
        writeFile(currentContent);
    }
}
