# Goal
Manual and automated test cases for the web-application "Computer database" based on the bundled "Computer-Database-Test-Cases.xlsx" document

# Requirements
1 - Please access the following sample aplication - http://computer-database.herokuapp.com/computers

2 - Create a series of manual test cases that cover the CRUD operation plus the edge cases. Make sure you give detailed instructions for each test case (pre conditions, steps, expected results). You can use any format you want.

3 - Write scripts that would automate the manual test cases that you see fit to be included in a regression test set. You can use any scripting language and tools you want.

4 - When the assessment is completed, please push the file containing the manual testcases and the automation project to github and provide us a link to the repository.

# Internet Explorer troubleshooting
Follow these instructions: http://www.michael-whelan.net/selenium-webdriver-and-ie11/ in case of the following error: OpenQA.Selenium.NoSuchWindowException : Unable to get browser
- All security zones should be set to the same Protected Mode setting
- Specific registry entries must be created

# Solution description
Test cases are automated using Selenium, Java, TestNG, log4j, Maven

# Needed software tools
1. Eclipse IDE (used 2018-02 (4.10.0)
2. Internet Explorer Web Driver 3.14.0. (x86: https://goo.gl/9Cqa4q or x64: https://goo.gl/9Cqa4q)

# Project build steps
Right click on Project in Eclipse
Run As -> Maven build -> set Goals = clean install -> Press Run button

# Execution steps
CompDb -> src/test/java -> backbase.comp.db -> App.java -> Run As -> TestNG Test
