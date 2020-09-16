package fr.afcepf.ad1al35.MspDataGenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import fr.afcepf.ad1al35.MspDataGenerator.dto.Booking;
import fr.afcepf.ad1al35.MspDataGenerator.dto.Product;
import fr.afcepf.ad1al35.MspDataGenerator.utils.ValueGenerator;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainApp {

	private static final LocalDate APP_EARLIEST_DATE = LocalDate.now().plusYears(-5);

	public static void main(String[] args) throws IOException {

		ObjectMapper mapper = new ObjectMapper();

		CollectionType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Product.class);
		List<Product> products = mapper.readValue(new File("src/main/resources/products.json"), javaType);

		/* ******************** generating bookings list ******************** */
		List<Booking> bookings = buildBookings(products);

		/* ******************** creating json file ******************** */
		mapper.writeValue(new File("src/main/resources/bookings.json"), bookings);
	}

	public static List<Booking> buildBookings(List<Product> products) {
		List<Booking> bookings = new ArrayList<>();

		for (int i=0; i<100; i++) {

			/* ******************** Init new booking/product combo ******************** */

			Random rand = new Random();

			Product randomProduct = products.get(rand.nextInt(products.size()));

			Booking booking = new Booking();
			booking.setProduct(randomProduct);

			long generatedUserId = (long) (Math.random() * 300);
			if(generatedUserId < 1 ) generatedUserId = 1L;
			booking.setUserId(generatedUserId);

			booking.setPets(false);
			booking.setCanceled(false);


			/* ******************** setting various dates ******************** */

				// Booking date is generated as a LocalDate to simplify process, then converted in LocalDateTime later

			LocalDate generatedBookingDate = ValueGenerator.generateRandomLocalDate(APP_EARLIEST_DATE, LocalDate.now());
			LocalDateTime generatedBookingDateTime = generatedBookingDate.atTime(
					ValueGenerator.generateRandomNumber(0,23),
					ValueGenerator.generateRandomNumber(0,59),
					ValueGenerator.generateRandomNumber(0,59)
			);
			booking.setBooking_date(generatedBookingDateTime.toString());


				// Check-in date is generated as a LocalDate with Booking date as a min and a 1 year later limit as a max

			LocalDate generatedCheckInDate = ValueGenerator.generateRandomLocalDate(generatedBookingDate, generatedBookingDate.plusYears(1));
			booking.setCheck_in_date(generatedCheckInDate.toString());


				// Check-out date is generated as a LocalDate with Check-in date + 1 day as a min and a 21 days later limit as a max

			LocalDate generatedCheckOutDate = generatedCheckInDate.plusDays(ValueGenerator.generateRandomWeightedDuration());
			booking.setCheck_out_date(generatedCheckOutDate.toString());


			/* ******************** setting guests number ******************** */

			Integer guestsNumber = ValueGenerator.generateRandomNumber(1, randomProduct.getMaxGuests());
			booking.setGuests_number(guestsNumber);


			/* ******************** setting total price ******************** */

			Long totalToPay = ValueGenerator.calculateTotalPrice(randomProduct.getDailyrate(), generatedCheckInDate, generatedCheckOutDate);
			booking.setTotalToPay(totalToPay);


			/* ******************** filling the list ******************** */

			bookings.add(booking);
		}

		return bookings;
	}
}
