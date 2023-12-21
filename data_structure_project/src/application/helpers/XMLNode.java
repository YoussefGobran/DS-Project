package application.helpers;

import java.util.ArrayList;
import java.util.List;

public class XMLNode {
  String tagName;
  String innerText;
  List<XMLNode> childrenNodes;

  XMLNode() {
    tagName = null;
    innerText = "";
    childrenNodes = new ArrayList<>();
  }

  @Override
  public String toString() {
    String res = "";
    for (int i = 0; i < childrenNodes.size(); i++) {
      res += childrenNodes.get(i);
    }
    return "<" + tagName + ">" + res + ((innerText != null) ? innerText : "") + "</" + tagName + ">";
  }
}

