package application.helpers;

import java.util.ArrayList;
import java.util.List;

public class XMLNode {
  public String tagName;
  public String innerText;
  public List<XMLNode> childrenNodes;

  XMLNode() {
    tagName = null;
    innerText = "";
    childrenNodes = new ArrayList<>();
  }
  public String getInnerText() {
    return innerText;
  }
  public String getTagName() {
    return tagName;
  }
  public List<XMLNode> getChildrenNodes() {
    return childrenNodes;
  }
  public boolean haveChildren() {
	  return childrenNodes.size()>0;
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

