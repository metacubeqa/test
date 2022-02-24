
package utility;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Ordering;
public class HelperFunctions {
	
	static final String autoITCloseWindowExePath = System.getProperty("user.dir").concat("\\src\\test\\resources\\AutoIT\\CloseDeskTopWindow.exe");
	static final String autoITExePath = System.getProperty("user.dir").concat("\\src\\test\\resources\\AutoIT\\FileUpload.exe ");
	static final String wavFilePath   = System.getProperty("user.dir").concat("\\src\\test\\resources\\audioFiles\\TestSampleWav.wav");
	
	static final String[] areaCodeArray = {"209","213", "279", "310", "323", "408", "415", "424", "442", "510", "239", "305", "321", "352", "386", "407", "561", "727", "754", "772", "786", "813", "850", "863", "904", 
			"201", "551", "609", "640", "732", "848", "856", "862", "908", "973"};
	
	public static Properties readConfigFile(String fileName)
	{
		try {
			Properties config = new Properties();
			ClassLoader classLoader = HelperFunctions.class.getClassLoader();
			InputStream fileData = classLoader.getResourceAsStream(fileName);
			config.load(fileData);
			return config;

		} catch (Exception e) {
			System.out.println("Exception " +e.getMessage());
			return null;
		}
	}
	
	public static void writeConfigFileProperties(Properties config, String fileName)
	{
		try {
			String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\" + fileName;
			config.store(new FileOutputStream(filePath), null);

		} catch (Exception e) {
			System.out.println("Exception " +e.getMessage());
		}
	}
	
	/** This method is to take the screenshot and copy it at a perticular place
	 * @param driver
	 * @param screenshotName
	 */
	public static void captureScreenshot(WebDriver driver,String screenshotName, int testResult){
		try{
			//Taking screenshot of the driver
			TakesScreenshot ts=(TakesScreenshot)driver;
			//Getting the screenshot
			File source=ts.getScreenshotAs(OutputType.FILE);
			//Copying the screenshot at perticular location
			if(testResult == 2){
				FileUtils.copyFile(source, new File("./Screenshots/FailedScreenShots/"+screenshotName+".png"));
			}else if(testResult == 3){
				FileUtils.copyFile(source, new File("./Screenshots/SkippedSreenshots/"+screenshotName+".png"));
			}else{
				FileUtils.copyFile(source, new File("./Screenshots/"+screenshotName+".png"));
			}

			System.out.println("Screenshot taken");
		}catch (Exception e){
			System.out.println("Exception while taking screenshot "+e.getMessage());
		} 
	}
	
	public static File captureElementScreenShot(WebDriver driver, WebElement element) {
		try {
			// Get entire page screenshot
			File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			BufferedImage fullImg = ImageIO.read(screenshot);

			// Get the location of element on the page
			Point point = element.getLocation();

			// Get width and height of the element
			int eleWidth = element.getSize().getWidth();
			int eleHeight = element.getSize().getHeight();
			
			if(eleWidth +  point.getX() > fullImg.getWidth()) {
				eleWidth = fullImg.getWidth() - point.getX() - 20;
			}
			if(eleHeight +  point.getY() > fullImg.getHeight()) {
				eleHeight = fullImg.getHeight() - point.getY() - 20;
			}

			// Crop the entire page screenshot to get only element screenshot
			BufferedImage eleScreenshot= fullImg.getSubimage(point.getX(), point.getY(), eleWidth, eleHeight);
			ImageIO.write(eleScreenshot, "png", screenshot);
			return screenshot;
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public static void deleteAllScreenShots(){
		try {
			File file = new File("./Screenshots");
			if (!file.exists()) {
				file.mkdir();
			} else {
				FileUtils.cleanDirectory(file);

			}
		} catch (IOException e) {
			System.out.println("No Screenshots to Delete");
			e.printStackTrace();
		}
	}
	
	public static void zipScreenshots(){

	}
	
	/**This method is to extract digits from a string
	 * @param number
	 * @return
	 */
	public static String getNumberInSimpleFormat(String number)
	{
		if(number.length() > 0 && number.charAt(0) == '1'){
			number = number.replaceFirst("1", "");
		}
			
		number = number.replace("+1","");
		number = number.replace("-","");
		number = number.replace(")","");
		number = number.replace("(","");
		number = number.replace(" ","");
		number = number.replace("+","");
		return number;
	}
	
	/**This method is to convert number into ring dna format
	 * @param number
	 * @return
	 */
	public static String getNumberInRDNAFormat(String number)
	{
		number = number.trim();
		int length = number.length();
		String newNumber = "(".concat(number.substring(length-10, length-7)).concat(") ");
		newNumber = newNumber.concat(number.substring(length-7, length-4));
		newNumber = newNumber.concat("-").concat(number.substring(length-4, length-0));
		return newNumber;
	}
	
	/** This method is to generate random area code based on String array
	 * @return
	 */
	public static String getRandomAreaCode(){
		int areaCodeIndex = new Random().nextInt(areaCodeArray.length);
		return areaCodeArray[areaCodeIndex];
	}
	
	/** This method is to generate a random string based on number of characters
	 * @param numberOfcharacters
	 * @return
	 */
	public static String GetRandomString(int numberOfcharacters) {
		return RandomStringUtils.randomAlphabetic(numberOfcharacters);
	}
	
	public static String GetRandomIntegers() {
		return String.valueOf(ThreadLocalRandom.current().nextInt());
	}
	
	/** This method is to get the current date and time
	 * @return
	 */
	public String GetCurrentDateTime ()
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date dateobj = new Date();
		return(df.format(dateobj).toString());
	}

	public static Date GetCurrentDateTimeObj()
	{
		Date dateobj = new Date();
		return dateobj;
	}
	
	public static String GetCurrentDateTime (String dateFormat)
	{
		DateFormat df = new SimpleDateFormat(dateFormat);
		Date dateobj = new Date();
		return(df.format(dateobj).toString());
	}
	
	public static String GetCurrentDateTime (String dateFormat, boolean roundOffMin)
	{
		if(roundOffMin) {
			Calendar c = Calendar.getInstance();
		    c.add(Calendar.MINUTE, 1);
			DateFormat df = new SimpleDateFormat(dateFormat);
			return(df.format(c.getTime()).toString());	
		}else {
			DateFormat df = new SimpleDateFormat(dateFormat);
			Date dateobj = new Date();
			return(df.format(dateobj).toString());
		}
	}
	
	public static String getDateTimeInTimeZone(String dateTime, String timeZoneID, String dateTimeFormat){
		try{
			TimeZone timeZone = TimeZone.getTimeZone(timeZoneID);
			SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(dateTimeFormat);
			Date dateTimeObj = dateTimeFormatter.parse(dateTime);
			dateTimeFormatter.setTimeZone(timeZone);
			return dateTimeFormatter.format(dateTimeObj);
		}
		catch(Exception e){
			System.out.println("Not able to fetch date or Time due to error " + e.getMessage());
			return null;
		}
	}
	
	public static Date getDateTimeInDateFormat(String dateTime, String dateTimeFormat) {
		try {
			SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(dateTimeFormat);
			return dateTimeFormatter.parse(dateTime);
		} catch (Exception e) {
			System.out.println("Not able to fetch date or Time due to error " + e.getMessage());
			return null;
		}
	}

	public static int getDateTimeDiffInMinutes(Date startDate, Date endDate, String format) {

		long duration = endDate.getTime() - startDate.getTime();
		long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
		return (int) diffInMinutes;
	}
	
	public static int getDateTimeDiffInSeconds(Date startDate, Date endDate, String format) {

		long duration = endDate.getTime() - startDate.getTime();
		long diffInSecs = TimeUnit.MILLISECONDS.toSeconds(duration);
		return (int) diffInSecs;
	}

	public static Date fetchDateTimeFromStringUsingRegex(String stringToParse, String format, String regex) {
		try {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(stringToParse);
			while (m.find()) {
				SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(format);
				return dateTimeFormatter.parse(m.group(0));
			}
		} catch (ParseException e) {
			System.out.println("Not able to fetch date or Time due to error " + e.getMessage());
			return null;
		}
		return null;
	}

	public static String changeDateTimeFormat(String dateTime, String oldDateTimeFormat, String newDateTimeFormat){
			try{
				SimpleDateFormat oldDateTimeFormatter = new SimpleDateFormat(oldDateTimeFormat);
				Date dateTimeObj = oldDateTimeFormatter.parse(dateTime);
				SimpleDateFormat newDateTimeFormatter = new SimpleDateFormat(newDateTimeFormat);
				return newDateTimeFormatter.format(dateTimeObj);
			}
			catch(Exception e){
				System.out.println("Not able to fetch date or Time due to error " + e.getMessage());
				return null;
			}
	}
	
	public static Date parseStringToDateFormat(String dateTime, String format){
		try {
		    DateFormat df = new SimpleDateFormat(format);
			Date parseddateTime = df.parse(dateTime);
			return parseddateTime;
		} catch (ParseException e) {
			System.out.println("Not able to fetch date or Time due to error " + e.getMessage());
			return null;
		}
	}
	
	public static LocalDate getLocaleDateTimeFormat(String format, String stringDate) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, Locale.ENGLISH);
			LocalDate  date = LocalDate.parse(stringDate, formatter);
			return date;
		} catch (Exception e) {
			System.out.println("Not able to fetch date or Time due to error " + e.getMessage());
			return null;
		}
	}

	public static int getNumberOfDiffInDates(String format, String startDate, String endDate) {
		Date date1 = parseStringToDateFormat(startDate, format);
		Date date2 = parseStringToDateFormat(endDate, format);
		long diffInMillies = Math.abs(date2.getTime() - date1.getTime());
	    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		return (int) diff;
	}

	public static boolean verifyIntegerInGivenRange(int min, int max, int value) {
		Range<Integer> myRange = Range.between(min, max);
		if (myRange.contains(value)) {
			return true;
		}
		return false;
	}
	
	public static Date addSecondsToDate(Date dateTime, int seconds){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		calendar.add(Calendar.SECOND, seconds);
		return calendar.getTime();
	}
	
	public static Date addMinutesToDate(Date dateTime, int minute){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		calendar.add(Calendar.MINUTE, minute);
		return calendar.getTime();
	}
	
	public static ArrayList<Date> getDateListFromStringList(List<String> dateStringList, String newDateTimeFormat){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(newDateTimeFormat);
		ArrayList<Date> dateList = new ArrayList<Date>();
		
	    for (String dateString : dateStringList) {
	        try {
	            dateList.add(simpleDateFormat.parse(dateString));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return dateList;
	}
	
	public static boolean isDateInGivenFormat(String format, String date) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		// To make strict date format validation
		formatter.setLenient(false);
		try {
			formatter.parse(date);
			return true;
		} catch (ParseException e) {
			System.out.println("Date not given in format");
			return false;
		}
	}
	
	public static void closeDesktopWindow(){
		try {
			Runtime.getRuntime().exec(autoITCloseWindowExePath);
		} catch (IOException e) {
			System.out.println("Exception occured while closing desktop window");
			e.printStackTrace();
		}
	}
	
	public static void uploadWavFileRecord() {
		try {
			Runtime.getRuntime().exec(autoITExePath + wavFilePath);
		} catch (IOException e) {
			System.out.println("Exception occured while uploading wav file");
			e.printStackTrace();
		}
	}
	
	public static void uploadMp3FileRecord(String pathOfMp3) {
		try {
			Runtime.getRuntime().exec(autoITExePath + pathOfMp3);
		} catch (IOException e) {
			System.out.println("Exception occured while uploading mp3 file");
			e.printStackTrace();
		}
	}
	
	public static void uploadCSV(String pathOfCSV) {
		try {
			Runtime.getRuntime().exec(autoITExePath + pathOfCSV);
		} catch (IOException e) {
			System.out.println("Exception occured while uploading csv");
			e.printStackTrace();
		}
	}
	
	public static String generateTenDigitNumber() {
		return Long.toString((long)(Math.random()*1000000000 + 9000000000L)); 
	}
	
	public static int getRandomIntegersBetween(int min, int max){
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
	
	public static boolean verifyStringContainsNumbers(String string){
		Pattern p = Pattern.compile("([0-9])");
		Matcher m = p.matcher(string);

		if(m.find())
		    return true;
		else
		    return false;
	}

	public static int getNumberFromString(String string) {
		String value = "";
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(string);

		if (m.find()) {
			value = m.group();
		}
		return Integer.parseInt(value);
	}
	
	public static String getValueAccToRegex(String string, String format) {
		String value = "";

		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(string);

		if (matcher.find())
			value = matcher.group().trim();
		
		return value;
	}

	/**delete files with containing file name
	 * @param downloadPath
	 * @param fileName
	 */
	public static void deletingExistingFiles(String downloadPath, String fileName) {
		File dir = new File(downloadPath);
		File[] dirContents = dir.listFiles();
		for (int i = 0; i < dirContents.length; i++) {
			if (dirContents[i].getName().contains(fileName)) {
				dirContents[i].delete();
			}
		}
	}
	
	/**delete files with extension
	 * @param downloadPath
	 * @param extension
	 */
	public static void deletingFilesWithExtension(String downloadPath, String extension) {
		File dir = new File(downloadPath);
		File[] dirContents = dir.listFiles();
		for (int i = 0; i < dirContents.length; i++) {
			if (dirContents[i].getName().endsWith(extension)) {
				dirContents[i].delete();
			}
		}
	}
	
	/**
	 * @param String
	 * 
	 * This method will check whether a given string is in JSON format or not
	 * returns true or false
	 */
	public static boolean isJSONValid(String Json) {
		try {
			new JSONObject(Json);
		} catch (JSONException ex) {
			try {
				new JSONArray(Json);
			} catch (JSONException ex1) {
				return false;
			}
		}
		return true;
	}

	public static int getNumberOfDaysPreviousMonth() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * @param format
	 * @param days
	 * @param month
	 * @param year
	 * @param oldExpirationDate
	 * @return
	 * 
	 * This method adds date, year, month in digits and changes the date accordingly
	 * 
	 */
	public static String addMonthYearDateToExisting(String format, String oldDate, int days, int month,
			int year) {

		// Specifying date format that matches the given date
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		try {
			// Setting the date to the given date
			calendar.setTime(sdf.parse(oldDate));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Number of Days to add
		if (days != 0)
			calendar.add(Calendar.DATE, days);

		// Number of Months to add
		if (month != 0)
			calendar.add(Calendar.MONTH, month);

		// Number of Years to add
		if (year != 0)
			calendar.add(Calendar.YEAR, year);

		// Date after adding the days to the given date
		String newDate = sdf.format(calendar.getTime());
		// Displaying the new Date after addition
		System.out.println("Date after Updation: " + newDate);
		return newDate;
	}
	
	public static boolean bufferedImagesEqual(File img1, File img2) {
		try {
			FileUtils.copyFile(img1, new File("./Screenshots/"+img1.getName()+".png"));
			BufferedImage bfdImg1 = ImageIO.read(img1);
			BufferedImage bfdImg2 = ImageIO.read(img2);
			if (bfdImg1.getWidth() == bfdImg2.getWidth() && bfdImg1.getHeight() == bfdImg2.getHeight()) {
				for (int x = 0; x < bfdImg1.getWidth(); x++) {
					for (int y = 0; y < bfdImg1.getHeight(); y++) {
						if (bfdImg1.getRGB(x, y) != bfdImg2.getRGB(x, y))
							return false;
					}
				}
			} else {
				return false;
			}
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public static void clearConsoleErrors(WebDriver driver){
	    JavascriptExecutor js = (JavascriptExecutor)driver;
	    String script = "console.clear();";
	    js.executeScript(script);
	}

	public static <T> boolean hasDuplicateItems(Iterable<T> all) {
		boolean hasDuplicates = false;
		final Set<T> setToReturn = new HashSet<>();
		Set<T> set = new HashSet<T>();
		// Set#add returns false if the set does not change, which
		// indicates that a duplicate element has been added.
		for (T each : all) {
			if (!set.add(each)) {
				setToReturn.add(each);
				hasDuplicates = true;
			}
		}
		for (T duplicateString : setToReturn) {
			System.out.println("Duplicate items = " + duplicateString);
		}
		return hasDuplicates;
	}

	
	//***************** List Sorting verification methods starts here **************************
	
	public static <T> boolean verifyAscendingOrderedList(@SuppressWarnings("rawtypes") Iterable<? extends Comparable> inputList) {
		if (Ordering.natural().isOrdered(inputList)) {
			return true;
		}
		return false;
	}

	public static <T> boolean verifyDescendingOrderedList(@SuppressWarnings("rawtypes") Iterable<? extends Comparable> inputList) {
		if (Ordering.natural().reverse().isOrdered(inputList)) {
			return true;
		}
		return false;
	}
	
	public static boolean verifyListAscendingCaseInsensitive(List<String> afterSortList) {
		List<String> afterSortListCopy = new ArrayList<String>(afterSortList);
		Collections.sort(afterSortList, String.CASE_INSENSITIVE_ORDER);
		if(afterSortList.equals(afterSortListCopy)) {
			return true;
		}
		return false;
	}

	public static boolean verifyListDescendingCaseInsensitive(List<String> afterSortList) {
		List<String> afterSortListCopy = afterSortList;
		Collections.sort(afterSortList, Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));
		if(afterSortList.equals(afterSortListCopy)) {
			return true;
		}
		return false;
	}

	
	public static List<Date> getStringListInDateFormat(String format, List<String> dateStringList) {
		SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(format);
		ArrayList<Date> timeList = new ArrayList<>();
		for (String dateString : dateStringList) {
			try {
				timeList.add(dateTimeFormatter.parse(dateString));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return timeList;
	}
	
	public static List<Float> getStringListInNumberFormat(List<String> intStringList) {
		ArrayList<Float> intList = new ArrayList<Float>();
		for (String dateString : intStringList) {

			if (dateString.equals("-")) {
				dateString = dateString.replace("-", "0");
			}

			dateString = dateString.replace(",", "").replace("+", "").replace("%", "");
			intList.add(Float.valueOf(dateString));
		}
		return intList;
	}
	
	public static List<String> getSubstringFromWholeList(List<String> stringList, int index){
		ArrayList<String> answerList = new ArrayList<>();
		for (String expectedString : stringList) {
			answerList.add(expectedString.substring(index-1));
		}
		return answerList;
	}
	
	//***************** List Sorting verification methods ends here **************************

	/**Count no. of rows in a file
	 * @param csv
	 * @return
	 */
	public static int bufferedReadRowsInFile(File csv) {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(csv));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int count = 0;
		try {
			while ((bufferedReader.readLine()) != null) {
				count++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}

	
	/**method to check whether list1 contains all the values of list2
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static boolean isList1ContainsValuesOfList2(List<String> list1, List<String> list2) {
		return list1.stream().anyMatch(list2::contains);
	}
}