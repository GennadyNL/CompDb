package backbase.comp.db;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.support.ui.Select;

@Test
public class App {
	// Creating a driver object referencing WebDriver interface
	public WebDriver driver;
	// Creating a logger instance
	public final Logger logger = Logger.getLogger(App.class.getName());
	public String testURL = "http://computer-database.herokuapp.com/computers";

	public WebDriver cleanUpAddComputer(WebDriver driver) {
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("introduced")).clear();
		driver.findElement(By.id("discontinued")).clear();
		return (driver);
	}

	public WebDriver ScrollPages(WebDriver driver) {
		WebElement linkByPartialText = driver.findElement(By.partialLinkText("Next"));
		linkByPartialText.click();
		return (driver);
	}

	@BeforeTest
	public void beforeTest() {
		PropertyConfigurator.configure("log4j.properties");

		InternetExplorerOptions ieOptions = new InternetExplorerOptions();
		ieOptions.requireWindowFocus();
		ieOptions.ignoreZoomSettings();

		// Setting the webdriver.ie.driver property to its executable's location
		System.setProperty("webdriver.ie.driver", "/Java/lib/IEDriverServer/IEDriverServer.exe");

		// Instantiating driver object
		driver = new InternetExplorerDriver(ieOptions);
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

		logger.info("Internet Explorer WebDriver is successfully initialized");
	}

	@Test(priority = 1)
	public void CompDb_TC_01() throws InterruptedException {
		// Using get() method to open a webpage
		driver.get(testURL);

		// Check page title
		String actualTitle = driver.getTitle();
		String expectedTitle = "Computers database";
		assertEquals(expectedTitle, actualTitle);

		if (driver.getTitle().equals("Computers database")) {
			logger.info("Page title equals \"Computers database\" - OK");
		} else {
			logger.error("Page title is not equal to \"Computers database\" - NOK");
		}
		
		// Check whether filtering field is present
		assertTrue(driver.findElements(By.id("searchbox")).size() != 0);
		if (driver.findElements(By.id("searchbox")).size() != 0)
			logger.info("Filtering field is found - OK");
		else
			logger.info("Filtering field is not found - NOK");

		// Check whether filtering button is present
		assertTrue(driver.findElements(By.id("searchsubmit")).size() != 0);
		if (driver.findElements(By.id("searchsubmit")).size() != 0)
			logger.info("Filtering button is found - OK");
		else
			logger.info("Filtering button is not found - NOK");

		// Check whether Add a new computer button is present
		assertTrue(driver.findElements(By.id("add")).size() != 0);
		if (driver.findElements(By.id("add")).size() != 0)
			logger.info("Add a new computer button is found - OK");
		else
			logger.info("Add a new computer button is not found - NOK");

		// Check whether computer table is present
		assertTrue(driver.findElements(By.xpath(".//table[contains(@class, 'computers zebra-striped')]")).size() != 0);
		if (driver.findElements(By.xpath(".//table[contains(@class, 'computers zebra-striped')]")).size() != 0)
			logger.info("Computers table is found - OK");
		else
			logger.info("Computers table is not found - NOK");

		// Check whether all columns of computer table are present
		try {
			assertTrue(driver
					.findElements(
							By.xpath(".//*[@id=\"main\"]/table/thead/tr/th[1]/a[contains(text(),'Computer name')]"))
					.size() != 0);
			assertTrue(driver
					.findElements(By.xpath(".//*[@id=\"main\"]/table/thead/tr/th[2]/a[contains(text(),'Introduced')]"))
					.size() != 0);
			assertTrue(driver
					.findElements(
							By.xpath(".//*[@id=\"main\"]/table/thead/tr/th[3]/a[contains(text(),'Discontinued')]"))
					.size() != 0);
			assertTrue(driver
					.findElements(By.xpath(".//*[@id=\"main\"]/table/thead/tr/th[4]/a[contains(text(),'Company')]"))
					.size() != 0);
			logger.info("Computer table structure is as expected - OK");
		} catch (Exception e) {
			logger.error("Computer table structure is not as expected - NOK");
		}

		// Check that paging controls are present
		try {
			assertTrue(driver.findElements(By.xpath(".//*[@id=\"pagination\"]/ul/li[1]/a[contains(text(),'Previous')]"))
					.size() != 0);
			assertTrue(driver
					.findElements(
							By.xpath(".//*[@id=\"pagination\"]/ul/li[2]/a[contains(text(),'Displaying 1 to 10 of')]"))
					.size() != 0);
			assertTrue(driver.findElements(By.xpath(".//*[@id=\"pagination\"]/ul/li[3]/a[contains(text(),'Next')]"))
					.size() != 0);
			logger.info("Paging controls are as expected - OK");
		} catch (Exception e) {
			logger.error("Paging controls are not as expected - NOK");
		}
	}

	@Test(priority = 2)
	public void CompDb_TC_02() throws InterruptedException {
		// Using get() method to open a webpage
		driver.get(testURL);

		driver.findElement(By.id("searchbox")).sendKeys("ARRA");
		driver.findElement(By.id("searchsubmit")).click();

		try {
			assertTrue(
					driver.findElements(By.xpath(".//*[@id=\"main\"]/table/tbody/tr/td[1]/a[contains(text(),'ARRA')]"))
							.size() != 0);
			logger.info("Computer ARRA is found - OK");
		} catch (Exception e) {
			logger.error("Computer ARRA is not found - NOK");
		}

		driver.findElement(By.id("searchbox")).clear();
		driver.findElement(By.id("searchbox")).sendKeys("Blue");
		driver.findElement(By.id("searchsubmit")).click();

		try {
			assertTrue(driver.findElements(By.xpath("//a[contains(text(), 'Blue')]")).size() == 4);
			logger.info("4 computers contaning Blue in name are found - OK");
		} catch (Exception e) {
			logger.error("4 computers containing Blue in name are not found - NOK");
		}

		driver.findElement(By.id("searchbox")).clear();
		driver.findElement(By.id("searchsubmit")).click();

		try {
			assertTrue(driver.findElements(By.xpath(".//a[contains(@href,'computers')]")).size() == 16);
			logger.info("10 computers are shown when no filtering applied - OK");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("10 computers are not shown when no filtering applied - NOK");
		}
	}

	@Test(priority = 3)
	public void CompDb_TC_03() throws InterruptedException {
		// Using get() method to open a webpage
		driver.get(testURL);

		String NonExistingComputer = "KkmxnJsh2";

		driver.findElement(By.id("searchbox")).sendKeys(NonExistingComputer);
		driver.findElement(By.id("searchsubmit")).click();

		try {
			assertTrue(
					driver.findElements(By.xpath(".//*[@id=\"main\"]/div[2]/em[contains(text(),'Nothing to display')]"))
							.size() != 0);
			logger.info("No computers found with the name " + NonExistingComputer + " - OK");
		} catch (Exception e) {
			logger.error("An unexpected error condition appeared - NOK");
		}
	}

	@Test(priority = 4)
	public void CompDb_TC_04() throws InterruptedException {
		// Using get() method to open a webpage
		driver.get(testURL);

		DateTimeFormatter dateTimeNow = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime now = LocalDateTime.now();
		String validComputer = "QA PC01 max " + (dateTimeNow.format(now));

		driver.findElement(By.id("add")).click();
		driver.findElement(By.id("name")).sendKeys(validComputer);
		driver.findElement(By.id("introduced")).sendKeys("2018-01-01");
		driver.findElement(By.id("discontinued")).sendKeys("2019-01-01");

		Select company = new Select(driver.findElement(By.id("company")));
		company.selectByVisibleText("Sanyo");

		driver.findElement(By.xpath(".//input[@type='submit']")).click();
		driver.findElement(By.id("searchbox")).sendKeys(validComputer);
		driver.findElement(By.id("searchsubmit")).click();

		try {
			assertTrue(driver.findElements(By.xpath("//a[contains(text(), validComputer)]")).size() != 0);
			logger.info("A new computer named " + validComputer + " is found - OK");
		} catch (Exception e) {
			logger.error("A new computer named " + validComputer + " is not found - NOK");
		}
	}

	@Test(priority = 5)
	public void CompDb_TC_05() throws InterruptedException {
		// Using get() method to open a webpage
		driver.get(testURL);

		DateTimeFormatter dateTimeNow = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime now = LocalDateTime.now();
		String validComputer = "QA PC01 min " + (dateTimeNow.format(now));

		driver.findElement(By.id("add")).click();
		driver.findElement(By.id("name")).sendKeys(validComputer);
		driver.findElement(By.xpath(".//input[@type='submit']")).click();
		driver.findElement(By.id("searchbox")).sendKeys(validComputer);
		driver.findElement(By.id("searchsubmit")).click();

		try {
			assertTrue(driver.findElements(By.xpath("//a[contains(text(), validComputer)]")).size() != 0);
			logger.info("A new computer named " + validComputer + " is found - OK");
		} catch (Exception e) {
			logger.error("A new computer named " + validComputer + " is not found - NOK");
		}
	}

	@Test(priority = 6)
	public void CompDb_TC_06() throws InterruptedException {
		// Using get() method to open a webpage
		driver.get(testURL);

		String invalidComputer1 = "a";
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 99; i++) {
			builder.append(invalidComputer1);
		}
		driver.findElement(By.id("add")).click();
		driver.findElement(By.id("name")).sendKeys(builder.toString());
		driver.findElement(By.xpath(".//input[@type='submit']")).click();

		SoftAssert sa = new SoftAssert();

		try {
			sa.assertTrue(driver.findElements(By.xpath(".//div[@class='clearfix error']")).size() != 0);
			logger.info("An error has been reported while creating too long name computer - OK");
		} catch (Exception e) {
			logger.error("An error was not reported while creating a too long name computer - NOK");
		}

		String invalidComputer2 = " *0l,>>,'\"\\\\¯ \\ _ (ツ) _ / ¯//";
		driver.findElement(By.id("add")).click();
		driver.findElement(By.id("name")).sendKeys(invalidComputer2);
		driver.findElement(By.xpath(".//input[@type='submit']")).click();

		try {
			assertTrue(driver.findElements(By.xpath(".//div[@class='clearfix error']")).size() != 0);
			logger.info("An error has been reported while creating special characters computer name - OK");
		} catch (Exception e) {
			logger.error("An error was not reported while creating special characters computer name - NOK");
		}
	}

	@Test(priority = 7)
	public void CompDb_TC_07() throws InterruptedException {
		// Using get() method to open a webpage
		driver.get(testURL);

		DateTimeFormatter dateTimeNow = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime now = LocalDateTime.now();
		String validComputer = "QA PC02 " + (dateTimeNow.format(now));

		driver.findElement(By.id("add")).click();

		// wrong format
		driver.findElement(By.id("name")).sendKeys(validComputer);
		driver.findElement(By.id("introduced")).sendKeys("01-01-2019");
		driver.findElement(By.id("discontinued")).sendKeys("01-01-2019");
		driver.findElement(By.xpath(".//input[@type='submit']")).click();
		SoftAssert sa = new SoftAssert();
		if (driver.findElements(By.xpath(".//div[@class='clearfix error']")).size() == 2) {
			sa.assertTrue(true);
			logger.info("An error has been reported while creating a computer with wrong format dates - OK");
		} else {
			sa.assertTrue(false);
			logger.error("An error was not reported while creating a computer with wrong format dates - NOK");
		}

		// outside limits
		if ((driver.findElements(By.id("name")).size() == 0))
			driver.findElement(By.id("add")).click();
		cleanUpAddComputer(driver);
		driver.findElement(By.id("name")).sendKeys(validComputer);
		driver.findElement(By.id("introduced")).sendKeys("9999-01-01");
		driver.findElement(By.id("discontinued")).sendKeys("1000-01-01");
		driver.findElement(By.xpath(".//input[@type='submit']")).click();
		if (driver.findElements(By.xpath(".//div[@class='clearfix error']")).size() == 2) {
			sa.assertTrue(true);
			logger.info("An error has been reported while creating a computer with outside limits dates - OK");
		} else {
			sa.assertTrue(false);
			logger.error("An error was not reported while creating a computer with outside limits dates - NOK");
		}

		// all zeros
		if ((driver.findElements(By.id("name")).size() == 0))
			driver.findElement(By.id("add")).click();
		cleanUpAddComputer(driver);
		driver.findElement(By.id("name")).sendKeys(validComputer);
		driver.findElement(By.id("introduced")).sendKeys("0000-00-00");
		driver.findElement(By.id("discontinued")).sendKeys("0000-00-00");
		driver.findElement(By.xpath(".//input[@type='submit']")).click();
		if (driver.findElements(By.xpath(".//div[@class='clearfix error']")).size() == 2) {
			sa.assertTrue(true);
			logger.info("An error has been reported while creating a computer with all zeros dates - OK");
		} else {
			sa.assertTrue(false);
			logger.error("An error was not reported while creating a computer with all zeros dates - NOK");
		}

		// wrong delimiter
		if ((driver.findElements(By.id("name")).size() == 0))
			driver.findElement(By.id("add")).click();
		cleanUpAddComputer(driver);
		driver.findElement(By.id("name")).sendKeys(validComputer);
		driver.findElement(By.id("introduced")).sendKeys("2019.01-01");
		driver.findElement(By.id("discontinued")).sendKeys("2019_02+01");
		driver.findElement(By.xpath(".//input[@type='submit']")).click();
		if (driver.findElements(By.xpath(".//div[@class='clearfix error']")).size() == 2) {
			sa.assertTrue(true);
			logger.info("An error has been reported while creating a computer with wrong delimiter in dates - OK");
		} else {
			sa.assertTrue(false);
			logger.error("An error was not reported while creating a computer with wrong delimiter in dates - NOK");
		}

		// non-existing month, non-existing day
		if ((driver.findElements(By.id("name")).size() == 0))
			driver.findElement(By.id("add")).click();
		cleanUpAddComputer(driver);
		driver.findElement(By.id("name")).sendKeys(validComputer);
		driver.findElement(By.id("introduced")).sendKeys("2019-13-01");
		driver.findElement(By.id("discontinued")).sendKeys("2019-02-29");
		driver.findElement(By.xpath(".//input[@type='submit']")).click();
		if (driver.findElements(By.xpath(".//div[@class='clearfix error']")).size() == 2) {
			sa.assertTrue(true);
			logger.info("An error has been reported while creating a computer with non existing month/day - OK");
		} else {
			sa.assertTrue(false);
			logger.error("An error was not reported while creating a computer with non existing month/day - NOK");
		}

		// chars, special symbols
		if ((driver.findElements(By.id("name")).size() == 0))
			driver.findElement(By.id("add")).click();
		cleanUpAddComputer(driver);
		driver.findElement(By.id("name")).sendKeys(validComputer);
		driver.findElement(By.id("introduced")).sendKeys("xxx");
		driver.findElement(By.id("discontinued")).sendKeys("&*'");
		driver.findElement(By.xpath(".//input[@type='submit']")).click();
		if (driver.findElements(By.xpath(".//div[@class='clearfix error']")).size() == 2) {
			sa.assertTrue(true);
			logger.info(
					"An error has been reported while creating a computer with chars/special symbols in dates - OK");
		} else {
			sa.assertTrue(false);
			logger.error(
					"An error was not reported while creating a computer with chars/special symbols in dates - NOK");
		}

		// Discontinued date is older than Introduction date
		if ((driver.findElements(By.id("name")).size() == 0))
			driver.findElement(By.id("add")).click();
		cleanUpAddComputer(driver);
		driver.findElement(By.id("name")).sendKeys(validComputer);
		driver.findElement(By.id("introduced")).sendKeys("2019-04-12");
		driver.findElement(By.id("discontinued")).sendKeys("2019-04-11");
		driver.findElement(By.xpath(".//input[@type='submit']")).click();
		if (driver.findElements(By.xpath(".//div[@class='clearfix error']")).size() == 2) {
			sa.assertTrue(true);
			logger.info(
					"An error has been reported while creating a computer with Discontinued date older than Introduction date - OK");
		} else {
			sa.assertTrue(false);
			logger.error(
					"An error was not reported while creating a computer with Discontinued date older than Introduction date - NOK");
		}
		sa.assertAll();
	}

	@Test(priority = 8)
	public void CompDb_TC_08() throws InterruptedException {
		// Using get() method to open a webpage
		driver.get(testURL);

		DateTimeFormatter dateTimeNow = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime now = LocalDateTime.now();
		String validComputer = "QA PC03 " + (dateTimeNow.format(now));

		driver.findElement(By.id("add")).click();
		driver.findElement(By.xpath(".//*[@id=\"main\"]/form/div/a[contains(text(),'Cancel')]")).click();

		driver.findElement(By.id("add")).click();
		driver.findElement(By.id("name")).sendKeys(validComputer);
		driver.findElement(By.id("introduced")).sendKeys("2019-01-01");
		driver.findElement(By.id("discontinued")).sendKeys("2019-02-01");
		Select company = new Select(driver.findElement(By.id("company")));
		company.selectByVisibleText("Tandy Corporation");
		driver.findElement(By.xpath(".//*[@id=\"main\"]/form/div/a[contains(text(),'Cancel')]")).click();

		driver.findElement(By.id("searchbox")).sendKeys(validComputer);
		driver.findElement(By.id("searchsubmit")).click();

		try {
			assertTrue(
					driver.findElements(By.xpath(".//*[@id=\"main\"]/div[2]/em[contains(text(),'Nothing to display')]"))
							.size() != 0);
			logger.info("No computers found with the name " + validComputer + " - OK");
		} catch (Exception e) {
			logger.error("An unexpected error condition appeared - NOK");
		}
	}

	@Test(priority = 9)
	public void CompDb_TC_09() throws InterruptedException {
		// Using get() method to open a webpage
		driver.get(testURL);

		DateTimeFormatter dateTimeNow = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime now = LocalDateTime.now();

		String searchComputer = "QA";
		String modifyComputerName = "QA Edited " + (dateTimeNow.format(now));
		String modifyIntroductionDate = "2019-01-01";
		String modifyDiscontinuedDate = "2019-02-02";
		String modifyCompanyName = "ASUS";

		driver.findElement(By.id("searchbox")).sendKeys(searchComputer);
		driver.findElement(By.id("searchsubmit")).click();
		driver.findElement(By.xpath("//*[@id=\"main\"]/table/tbody/tr[1]/td[1]/a[contains(text(),searchComputer)]"))
				.click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(modifyComputerName);
		driver.findElement(By.id("introduced")).clear();
		driver.findElement(By.id("introduced")).sendKeys(modifyIntroductionDate);
		driver.findElement(By.id("discontinued")).clear();
		driver.findElement(By.id("discontinued")).sendKeys(modifyDiscontinuedDate);
		Select company = new Select(driver.findElement(By.id("company")));
		company.selectByVisibleText(modifyCompanyName);
		driver.findElement(By.xpath(".//input[@type='submit']")).click();
		driver.findElement(By.id("searchbox")).sendKeys(modifyComputerName);
		driver.findElement(By.id("searchsubmit")).click();

		if (driver
				.findElements(
						By.xpath("//*[@id=\"main\"]/table/tbody/tr[1]/td[1]/a[contains(text(),modifyComputerName)]"))
				.size() == 1) {
			assertTrue(true);
			logger.info("1 computer found with the name " + modifyComputerName + " - OK");
		} else {
			assertTrue(false);
			logger.error("No computers found with the name " + modifyComputerName + " - NOK");
		}
	}

	@Test(priority = 10)
	public void CompDb_TC_10() throws InterruptedException {
		// Using get() method to open a webpage
		driver.get(testURL);

		String searchComputer = "QA";

		driver.findElement(By.id("searchbox")).sendKeys(searchComputer);
		driver.findElement(By.id("searchsubmit")).click();
		driver.findElement(By.xpath("//*[@id=\"main\"]/table/tbody/tr[1]/td[1]/a[contains(text(),searchComputer)]"))
				.click();
		String currentURL = driver.getCurrentUrl();
		String currentComputerName = driver.findElement(By.id("name")).getAttribute("value");
		String currentIntroductionDate = driver.findElement(By.id("introduced")).getAttribute("value");
		String currentDiscontinuedDate = driver.findElement(By.id("discontinued")).getAttribute("value");
		Select company = new Select(driver.findElement(By.id("company")));
		String currentCompany = company.getFirstSelectedOption().getText();
		driver.findElement(By.xpath(".//input[@type='submit']")).click();

		driver.get(currentURL);
		String retrievedComputerName = driver.findElement(By.id("name")).getAttribute("value");
		String retrievedIntroductionDate = driver.findElement(By.id("introduced")).getAttribute("value");
		String retrievedDiscontinuedDate = driver.findElement(By.id("discontinued")).getAttribute("value");
		Select companyCheck = new Select(driver.findElement(By.id("company")));
		String retrievedCompany = companyCheck.getFirstSelectedOption().getText();

		if (currentComputerName.contentEquals(retrievedComputerName)
				&& (currentIntroductionDate.contentEquals(retrievedIntroductionDate)
						&& (currentDiscontinuedDate.contentEquals(retrievedDiscontinuedDate)
								&& (currentCompany.contentEquals(retrievedCompany))))) {
			assertTrue(true);
			logger.info("The computer properties were not modified - OK");
		} else {
			assertTrue(false);
			logger.info("The computer properties were  modified - NOK");
		}
	}

	@Test(priority = 11)
	public void CompDb_TC_11() throws InterruptedException {
		// Using get() method to open a webpage
		driver.get(testURL);

		String searchComputer = "QA";

		driver.findElement(By.id("searchbox")).sendKeys(searchComputer);
		Thread.sleep(1000);
		driver.findElement(By.id("searchsubmit")).click();
		driver.findElement(By.xpath("//*[@id=\"main\"]/table/tbody/tr[1]/td[1]/a[contains(text(),searchComputer)]"))
				.click();

		String currentURL = driver.getCurrentUrl();
		String currentComputerName = driver.findElement(By.id("name")).getAttribute("value");
		String currentIntroductionDate = driver.findElement(By.id("introduced")).getAttribute("value");
		String currentDiscontinuedDate = driver.findElement(By.id("discontinued")).getAttribute("value");
		Select company = new Select(driver.findElement(By.id("company")));
		String currentCompany = company.getFirstSelectedOption().getText();

		cleanUpAddComputer(driver);
		driver.findElement(By.id("introduced")).sendKeys("'yyyy-MM-dd'");
		driver.findElement(By.id("discontinued")).sendKeys("'yyyy-MM-dd'");
		Select companySet = new Select(driver.findElement(By.id("company")));
		companySet.selectByVisibleText("-- Choose a company --");
		driver.findElement(By.xpath(".//input[@type='submit']")).click();

		if ((driver.findElements(By.xpath(".//div[@class='clearfix error']")).size() == 3)) {
			assertTrue(true);
			logger.info("Validation returned 3 expected errors - OK");
		} else {
			logger.error(driver.findElements(By.className("clearfix error")).size());
			assertTrue(false);
			logger.info("Validation did not return 3 expected errors - NOK");
		}

		driver.get(currentURL);
		String retrievedComputerName = driver.findElement(By.id("name")).getAttribute("value");
		String retrievedIntroductionDate = driver.findElement(By.id("introduced")).getAttribute("value");
		String retrievedDiscontinuedDate = driver.findElement(By.id("discontinued")).getAttribute("value");
		Select companyCheck = new Select(driver.findElement(By.id("company")));
		String retrievedCompany = companyCheck.getFirstSelectedOption().getText();

		if (currentComputerName.contentEquals(retrievedComputerName)
				&& (currentIntroductionDate.contentEquals(retrievedIntroductionDate)
						&& (currentDiscontinuedDate.contentEquals(retrievedDiscontinuedDate)
								&& (currentCompany.contentEquals(retrievedCompany))))) {
			assertTrue(true);
			logger.info("The computer properties were not modified - OK");
		} else {
			assertTrue(false);
			logger.error("The computer properties were  modified - NOK");
		}

	}

	@Test(priority = 12)
	public void CompDb_TC_12() throws InterruptedException {
		// Using get() method to open a webpage
		driver.get(testURL);

		String searchComputer = "QA";

		driver.findElement(By.id("searchbox")).sendKeys(searchComputer);
		Thread.sleep(1000);
		driver.findElement(By.id("searchsubmit")).click();
		driver.findElement(By.xpath("//*[@id=\"main\"]/table/tbody/tr[1]/td[1]/a[contains(text(),searchComputer)]"))
				.click();

		String currentURL = driver.getCurrentUrl();
		driver.findElement(By.xpath(".//*[@id=\"main\"]/form[2]/input")).click();
		driver.get(currentURL);

		String actualTitle = driver.getTitle();
		String expectedTitle = "HTTP 404 Not Found";
		assertEquals(expectedTitle, actualTitle);
	}

	@Test(priority = 13)
	public void CompDb_TC_13() throws InterruptedException {
		// Using get() method to open a webpage
		driver.get(testURL);

		while (driver.findElements(By.xpath(".//a[contains(@href,'computers')]")).size() > 15) {
			ScrollPages(driver);
		}
		if (driver.findElements(By.xpath(".//a[contains(@href,'computers')]")).size() < 15) {
			int Links = driver.findElements(By.xpath(".//a[contains(@href,'computers')]")).size();
			int LinksComputers = Links - 6;
			logger.info("There are " + LinksComputers + " computers detected");
		}
		String pageSource = driver.getPageSource();
		assertEquals(true, pageSource.contains("<li class=\"next disabled\">"));
	}

	@AfterTest
	public void afterTest() {
		driver.close();
		driver.quit();
		logger.info("Test execution is completed");
	}
}