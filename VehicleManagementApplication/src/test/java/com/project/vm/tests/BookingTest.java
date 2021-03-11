package com.project.vm.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.vm.entities.Booking;
import com.project.vm.entities.Customer;
import com.project.vm.entities.Driver;
import com.project.vm.entities.Vehicle;
import com.project.vm.exceptions.DeletionException;
import com.project.vm.exceptions.NotFoundException;
import com.project.vm.exceptions.ValidationException;
import com.project.vm.services.BookingService;

@SpringBootTest
class BookingTest {
	
	Customer customer;
	Driver driver;
	Vehicle vehicle;
	Booking booking;
	
	@Autowired
	BookingService bookingService;
	
	@BeforeEach
	void setUp() {
		customer = new Customer();
		vehicle = new Vehicle();
		booking = new Booking();
	}
	

	@Test
	void testAddBooking() {
		customer.setFirstName("Abhi");
		customer.setLastName("Sharma");
		vehicle.setVehicleNumber("KA053277");
		booking = new Booking(customer, vehicle,LocalDate.now(), LocalDate.of(2021, 03, 11), "Booking for one day", 200);
		Booking b= bookingService.addBooking(booking);
		assertTrue(b != null);
	}
	
	@Test
	void testAddBookingWithInvalidDates() {
		customer.setFirstName("Test");
		customer.setLastName("Cust");
		vehicle.setVehicleNumber("AF786023");
		booking = new Booking(customer, vehicle,LocalDate.of(2021, 03, 9), LocalDate.of(2021, 03, 10), "Booking for one day", 200);
		Exception exception = assertThrows(ValidationException.class, () -> {
			bookingService.addBooking(booking);
	    });
		String expectedMessage = "before current date";
	    String actualMessage = exception.getMessage();
	    assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testViewAllBookings() {
		List<Booking> bookings = bookingService.viewAllBookings();
		System.out.println(bookings);
		assertEquals(5, bookings.size());
	}

	@Test
	void testDeleteBooking() {
		int id = 6;
		Booking b = bookingService.cancelBooking(id);
		assertNotNull(b);
	}
	
	@Test
	void testDeleteInvalidBooking() {
		int id = 0;
		Exception exception = assertThrows(NotFoundException.class, () -> {
			bookingService.cancelBooking(id);
	    });
	    String expectedMessage = "not found";
	    String actualMessage = exception.getMessage();
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testDeleteBookingWithPayment() {
		int id = 3;
		Exception exception = assertThrows(DeletionException.class, () -> {
			bookingService.cancelBooking(id);
	    });
	    String expectedMessage = "cannot be deleted";
	    String actualMessage = exception.getMessage();
	    assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testViewBooking() {
		Booking b = bookingService.viewBooking(4);
		System.out.println(b);
		assertNotNull(b);
	}
	
	@Test
	void testViewBookingByInvalidId() {
		int id = 0;
		Exception exception = assertThrows(NotFoundException.class, () -> {
			bookingService.viewBooking(id);
	    });
	    String expectedMessage = "not found";
	    String actualMessage = exception.getMessage();
	    assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testUpdateBooking() {
		booking.setBookingId(3);
		booking.setBookedTillDate(LocalDate.of(2021, 03, 13));
		booking.setBookingDescription("Booking for 3 days");
		Booking b = bookingService.updateBooking(booking);
		assertEquals("Booking for 3 days", b.getBookingDescription());
		assertEquals(LocalDate.of(2021, 03, 13), b.getBookedTillDate());
	}

	@Test
	void testViewBookingByCustomer() {
		List<Booking> bookings = bookingService.viewAllBooking("Priya");
		assertNotNull(bookings);
	}
	
	@Test
	void testViewBookingByInvalidCustomer() {
		Exception exception = assertThrows(NotFoundException.class, () -> {
			bookingService.viewAllBooking("xxxxx");
	    });
	    String expectedMessage = "No bookings found";
	    String actualMessage = exception.getMessage();
	    assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testViewBookingByVehicle() {
		List<Booking> bookings = bookingService.viewAllBookingByVehicle("KA053279");
		assertNotNull(bookings);
	}

	
	@Test
	void testViewBookingByInvalidVehicle() {
		Exception exception = assertThrows(NotFoundException.class, () -> {
			bookingService.viewAllBookingByVehicle("xxxxx");
	    });
	    String expectedMessage = "not found";
	    String actualMessage = exception.getMessage();
	    assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testViewBookingByBookingDate() {
		List<Booking> bookings = bookingService.viewAllBookingByDate(LocalDate.of(2021, 03, 9));
		assertNotNull(bookings);
	}
	
	@Test
	void testViewBookingByInvalidDate() {
		Exception exception = assertThrows(NotFoundException.class, () -> {
			bookingService.viewAllBookingByDate(LocalDate.of(2000, 03, 01));
	    });
	    String expectedMessage = "No bookings found";
	    String actualMessage = exception.getMessage();
	    assertTrue(actualMessage.contains(expectedMessage));
	}

}
