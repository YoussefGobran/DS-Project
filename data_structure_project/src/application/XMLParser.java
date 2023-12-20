package application;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {
  XMLNode rootNode;

  void parseXMLFile(String filePath) {
    try {
      String fileContent = new String(Files.readAllBytes(Paths.get("sample.xml")));

      // responsible for removing any extra space
      fileContent = fileContent.trim();
      fileContent = fileContent.replaceAll(" +", " ");
      fileContent = fileContent.replaceAll("< ?([A-Za-z]+) ?>", "<$1>");
      fileContent = fileContent.replaceAll("< ?/ ?([A-Za-z]+) ?>", "</$1>");

      rootNode = new XMLNode();
      int leftBracketIndexOpening = fileContent.indexOf('<', 0);
      int rightBracketIndexOpening = fileContent.indexOf('>', leftBracketIndexOpening + 1);
      String tagName = fileContent.substring(leftBracketIndexOpening + 1, rightBracketIndexOpening);
      String closingTag = "</" + tagName + ">";
      int closingTagIndex = fileContent.indexOf(closingTag, rightBracketIndexOpening + 1);

      rootNode.tagName = tagName;
      rootNode.innerText = fileContent.substring(rightBracketIndexOpening + 1, closingTagIndex).trim();
      parseFileContent(rootNode);
      // System.out.println(rootNode);
    } catch (Exception e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
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
}
