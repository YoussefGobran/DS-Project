# XML GUI Application - Network Visualization and Analysis

This Java-based GUI application allows users to parse, visualize, and analyze data stored in an XML file representing a social network. The application provides various features to enhance the usability and efficiency of working with XML data related to users, posts, and followers.

## Features

### Level 1 Requirements

- **File Selection:**
  - Build a GUI for users to specify the location of the input XML file.

- **XML Consistency Check:**
  - Detect and visually display errors in the XML file, such as missing or mismatched tags.
  - Correct errors in XML consistency.

- **XML Formatting:**
  - Format the XML file for better readability by maintaining proper indentation.

- **XML to JSON Conversion:**
  - Convert XML data into JSON format, facilitating compatibility with JavaScript tools.

- **XML Minification:**
  - Compress XML file by removing unnecessary spaces and newlines.

- **Data Compression:**
  - Implement a compression technique to reduce the size of the XML file, enhancing efficiency.


### Level 2 Requirements

- **Graph Data Structure:**
  - Represent users, their posts, and followers using a graph data structure.

- **Network Analysis:**
  - Identify the most influential user (highest number of followers).
  - Determine the most active user (connected to many users).
  - Find mutual followers between two users.
  - Suggest users to follow based on the followers of their followers.

- **Post Search:**
  - Search for posts containing a specific word or topic.

- **Graph Visualization:**
  - Display the network graph visually.

- **Undo/Redo Operations:**
  - Implement undo and redo functionality to revert or reapply changes during editing.
