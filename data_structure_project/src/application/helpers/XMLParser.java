package application.helpers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {
  public XMLNode rootNode;

  public String cleanFileContent(String content) {
    // responsible for removing any extra space
    content = content.trim();
    content = content.replaceAll("\n|\t|\r", "");
    content = content.replaceAll(" +", " ");
    content = content.replaceAll("< ?([A-Za-z]+) ?>", "<$1>");
    content = content.replaceAll("< ?/ ?([A-Za-z]+) ?>", "</$1>");
    content = content.replaceAll("> +<", "><");
    return content;
  }

  public String readFileContent(String filePath) {
    String fileContent = "";
    try {
      fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
    } catch (Exception e) {
      System.out.println("File doesn't exist");
      e.printStackTrace();
    }
    return fileContent;
  }

  public void parseXMLFile(String fileContent) {

    fileContent = cleanFileContent(fileContent);

    rootNode = new XMLNode();
    int leftBracketIndexOpening = fileContent.indexOf('<', 0);
    int rightBracketIndexOpening = fileContent.indexOf('>', leftBracketIndexOpening + 1);
    String tagName = fileContent.substring(leftBracketIndexOpening + 1, rightBracketIndexOpening);
    String closingTag = "</" + tagName + ">";
    int closingTagIndex = fileContent.indexOf(closingTag, rightBracketIndexOpening + 1);

    rootNode.tagName = tagName;
    rootNode.innerText = fileContent.substring(rightBracketIndexOpening + 1, closingTagIndex).trim();
    parseFileContent(rootNode);

  }

  private void parseFileContent(XMLNode root) {
    List<XMLNode> childrenNode = new ArrayList<>();
    int i = 0;
    while (i < root.innerText.length()) {
      XMLNode res = new XMLNode();
      int leftBracketIndexOpening = root.innerText.indexOf('<', i);
      if (leftBracketIndexOpening == -1) {
        break;
      }
      int rightBracketIndexOpening = root.innerText.indexOf('>', leftBracketIndexOpening + 1);
      String tagName = root.innerText.substring(leftBracketIndexOpening + 1, rightBracketIndexOpening);
      String closingTag = "</" + tagName + ">";
      int closingTagIndex = root.innerText.indexOf(closingTag, rightBracketIndexOpening + 1);
      i = closingTagIndex + closingTag.length();

      res.tagName = tagName;
      res.innerText = root.innerText.substring(rightBracketIndexOpening + 1, closingTagIndex).trim();

      parseFileContent(res);
      childrenNode.add(res);
    }
    if (!childrenNode.isEmpty()) {
      root.innerText = null;
    }
    root.childrenNodes = childrenNode;
  }

  public String compressFile(String fileContent) {
    // System.out.println(fileContent);
    String temp = fileContent.toString();
    String res = cleanFileContent(temp);
    System.out.println(res);
    return res;
  }

  public boolean saveFile(String fileContent, String filePath) {
    try (
      
        // Create a FileOutputStream to write to the file
        OutputStream outputStream = new FileOutputStream(filePath);

        // Create an OutputStreamWriter with US-ASCII encoding
        Writer writer = new OutputStreamWriter(outputStream)) {
      // Write the content to the file
      writer.write(fileContent);
          writer.close();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

}
