package fr.afcepf.ad1al35.MspDataGenerator.utils;

import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class ValueGenerator {

	private static final int MILLISECONDS_IN_A_DAY = 1000 * 60 * 60 * 24;

	public static Date generateRandomDate(Date dateMin, Date dateMax) {
		long randomPeriod = ThreadLocalRandom
				.current()
				.nextLong(dateMin.getTime(), dateMax.getTime());
		return new Date(randomPeriod);
	}
	
	public static Integer generateRandomNumber(int min, int max) {
		return (int) (Math.random() * (max - min) + min);
	}

	public static Long calculateTotalPrice(Long dailyPrice, Date start, Date end) {
		long duration = (end.getTime() - start.getTime()) / MILLISECONDS_IN_A_DAY;
		long price = duration * dailyPrice;
		return price;
	}

	// à tester
//	LocalDateTime triggerTime =
//			LocalDateTime.ofInstant(Instant.ofEpochMilli(test_timestamp),
//					TimeZone.getDefault().toZoneId());
}
