package org.jboss.tools.examples.test;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SelemiumTest {

	@Test
	public void seleniumTest() {
		// Tutorial http://www.frontendtest.org/blog/selenium-webdriver-tutorial-java/
		// https://www.ebay.de/itm/Samsung-SSD-840-Pro-Series-MZ-7PD128-128GB-Intern/302627566575?hash=item46760233ef:g:kfgAAOSwuShadyE6

		// WebDriver driver = new HtmlUnitDriver();
		
/*		ProfilesIni allProfiles = new ProfilesIni();
		FirefoxProfile profile = allProfiles.getProfile("WebDriver");
		profile.setPreferences("foo.bar", 23);*/
		WebDriver driver = new FirefoxDriver();
		

		driver.get("http://www.google.com");
		WebElement element = driver.findElement(By.id("coolestWidgetEvah"));

		/*
		 * WebDriver driver = new ChromeDriver();
		 * driver.get("http://www.google.com/xhtml"); Thread.sleep(5000); // Let the
		 * user actually see something! WebElement searchBox =
		 * driver.findElement(By.name("q")); searchBox.sendKeys("ChromeDriver");
		 * searchBox.submit(); Thread.sleep(5000); // Let the user actually see
		 * something! driver.quit();
		 */

		/*
		 * WebDriver driver = new ChromeDriver();
		 * 
		 * // WebDriver driver = new ChromeDriver();
		 * 
		 * // driver.navigate().to(
		 * "https://www.ebay.de/itm/Samsung-SSD-840-Pro-Series-MZ-7PD128-128GB-Intern/302627566575?hash=item46760233ef:g:kfgAAOSwuShadyE6"
		 * );
		 * 
		 * 
		 * // Setting the browser size // driver.manage().window().setSize(new
		 * Dimension(1024, 768)); driver.close(); driver.quit();
		 */
		// And now use this to visit Google

		// Find the text input element by its name
		// WebElement element = driver.findElement(By.name("q"));
		// <h3>Your application can run on:</h3>
		// Example how to look by id <div id="coolestWidgetEvah">...</div>

		// Enter something to search for
		// element.sendKeys("Cheese!");

		// How to make a click
		// driver.findElement(By.id("submit")).click();
		// Type in the search-field: "WebDriver"
		// driver.findElement(By.id("searchInput")).sendKeys("WebDriver");

		// Now submit the form. WebDriver will find the form for us from the element
		// element.submit();

		// Google's search is rendered dynamically with JavaScript.
		// Wait for the page to load, timeout after 10 seconds
		/*
		 * (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
		 * public Boolean apply(WebDriver d) { return
		 * d.getTitle().toLowerCase().startsWith("cheese!"); } });
		 */

		// Waiting a little bit before closing
		// Thread.sleep(7000);

		// Closing the browser and webdriver
		/*
		 * driver.close(); driver.quit();
		 */
	}

}
