package fr.afcepf.ad1al35.MspDataGenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import fr.afcepf.ad1al35.MspDataGenerator.dto.Booking;
import fr.afcepf.ad1al35.MspDataGenerator.dto.Product;
import fr.afcepf.ad1al35.MspDataGenerator.utils.ValueGenerator;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainApp {

	public static void main(String[] args) throws IOException, ParseException {

		ObjectMapper mapper = new ObjectMapper();

		Calendar calStart = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		calStart.add(Calendar.YEAR, -5);
		calEnd.add(Calendar.YEAR, 1);

		CollectionType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Product.class);
		List<Product> products = mapper.readValue(new File("src/main/resources/products.json"), javaType);

		List<Booking> bookings = new ArrayList<>();

		for (int i=0; i<100; i++) {
			Random rand = new Random();

			Product randomProduct = products.get(rand.nextInt(products.size()));

			Booking booking = new Booking();
			booking.setProduct(randomProduct);

			booking.setPets(false);
			booking.setCanceled(false);

			booking.setBooking_date(
					ValueGenerator.generateRandomDate(
							calStart.getTime(), new Date()
					).toString());
			System.out.println(booking.getBooking_date());
			booking.setCheck_in_date(
					ValueGenerator.generateRandomDate(
							DateFormat.getDateInstance().parse(booking.getBooking_date()), calEnd.getTime()
					).toString());
			booking.setCheck_out_date(
					ValueGenerator.generateRandomDate(
							DateFormat.getDateInstance().parse(booking.getCheck_in_date()), DateFormat.getDateInstance().parse(booking.getCheck_in_date() + (int) ((Math.random()+1) * (1000*60*60*24) * 21))
					).toString());

			booking.setGuests_number(
					ValueGenerator.generateRandomNumber(
							1, randomProduct.getMaxGuests()
					));

			booking.setTotalToPay(ValueGenerator.calculateTotalPrice(
					randomProduct.getDailyrate()
					, DateFormat.getDateInstance().parse(booking.getCheck_in_date())
					, DateFormat.getDateInstance().parse(booking.getCheck_out_date())
			));

			bookings.add(booking);
		}

		mapper.writeValue(new File("src/main/resources/bookings.json"), bookings);

		System.out.println(new Date());
	}


}
