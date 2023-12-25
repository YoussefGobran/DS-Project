package application.helpers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

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

  /**************************************************************************
   *                       Xml Correction/Detection                         *
   **************************************************************************/
  public static List<Object> xmlCorrection(String xml) {
    //next line to remove any spaces at the beginning of the line in the xml
    xml = xml.replaceAll("(?m)^\\s+", "");
    StringBuffer xmlBuffer = new StringBuffer(xml);
    Stack<String> stack = new Stack<String>(); //stack to hold openning tags
    String Tag = "";
    StringBuffer Tagbuffer = new StringBuffer();
    char[] chars = xml.toCharArray();
    int xmlcount = 0;
    int xmlbuffercount = 0;
    boolean endOfFile = false;
    List<Integer> errorIndex = new ArrayList<>();

    while (xmlcount != chars.length) {
        if (chars[xmlcount] == '<') { //start by going through every tag in xml , tags start with '<' and ends with '>'
            Tag = "" + chars[xmlcount];
        } else if (chars[xmlcount] == '>') {
            Tag += chars[xmlcount];
            if (Tag.contains("/")) { //check if the tag is a closing tag
                if (isMatchingTag(stack.peek(), Tag)) { //if it matches the tag on top of the stack pop the last tag in stack
                    stack.pop();
                } else { //if it doesn't match then form a closing tag for the openning tag in the stack and insert it 
                    Tagbuffer = new StringBuffer(stack.peek());
                    Tagbuffer = Tagbuffer.insert(1, '/');
                    xmlBuffer = xmlBuffer.insert(xmlBuffer.indexOf(Tag, xmlbuffercount - Tag.length()), Tagbuffer.toString() + "\n");
                    errorIndex.add(xml.indexOf(Tag, xmlcount - Tag.length())); //index of the error to be used later
                    stack.pop();
                    xmlbuffercount = xmlbuffercount + Tagbuffer.length() - Tag.length();
                    xmlcount = xmlcount - Tag.length();
                }
            } else {
                if (!stack.empty()) {
                    //handling different cases of openning tags


                } else { //if it is an openning tag push it in stack
                    stack.push(Tag);
                    Tag = "";
                }
            }
        } else if (!Tag.isEmpty()) { //forming tags by taking characters between < >
            Tag += chars[xmlcount];
        }
        xmlcount++;
        xmlbuffercount++;
    }
    
    //lastly check if the stack is not empty and close all the openning tags remaining
    if (!stack.empty()) {
        while (!stack.empty()) {

            Tagbuffer = new StringBuffer(stack.peek());
            Tagbuffer = Tagbuffer.insert(1, '/');
            xmlBuffer = xmlBuffer.insert(xmlBuffer.length(), Tagbuffer.toString() + "\n");
            stack.pop();
        }
    }
    //creating a list of objects to return the xml containing error, the index of each error in that xml and last object is the corrected xml
            List<Object> result =new ArrayList<>();
            result.add(xml);
            result.add(errorIndex);
            result.add(xmlBuffer.toString());
    return result;
}

//finction checks if this closing tag matches the current openning tag
public static boolean isMatchingTag(String openTag, String closingTag) {
    StringBuffer opentagbuffer = new StringBuffer(openTag);
    opentagbuffer = opentagbuffer.insert(1, '/');
    return opentagbuffer.toString().equals(closingTag);
}


}
