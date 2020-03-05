package example.java.selenium.util;

import org.apache.log4j.Logger;

public class CommonWait {
	private static final Logger LOGGER = Logger.getLogger(CommonWait.class);

	private void sleep(int milisecond) {
		try {
			Thread.sleep(milisecond);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage());
		}
	}
}
