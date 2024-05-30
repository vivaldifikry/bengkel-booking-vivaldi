package com.bengkel.booking.services;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.bengkel.booking.models.*;

public class PrintService {
	
	public static void printMenu(String[] listMenu, String title) {
		String line = "+---------------------------------+";
		int number = 1;
		String formatTable = " %-2s. %-25s %n";
		
		System.out.printf("%-25s %n", title);
		System.out.println(line);
		
		for (String data : listMenu) {
			if (number < listMenu.length) {
				System.out.printf(formatTable, number, data);
			}else {
				System.out.printf(formatTable, 0, data);
			}
			number++;
		}
		System.out.println(line);
		System.out.println();
	}
	
	public static void printVechicle(List<Vehicle> listVehicle) {
		String formatTable = "| %-2s | %-15s | %-10s | %-15s | %-15s | %-5s | %-15s |%n";
		String line = "+----+-----------------+------------+-----------------+-----------------+-------+-----------------+%n";
		System.out.format(line);
	    System.out.format(formatTable, "No", "Vechicle Id", "Warna", "Brand", "Transmisi", "Tahun", "Tipe Kendaraan");
	    System.out.format(line);
	    int number = 1;
	    String vehicleType = "";
	    for (Vehicle vehicle : listVehicle) {
	    	if (vehicle instanceof Car) {
				vehicleType = "Mobil";
			}else {
				vehicleType = "Motor";
			}
	    	System.out.format(formatTable, number, vehicle.getVehiclesId(), vehicle.getColor(), vehicle.getBrand(), vehicle.getTransmisionType(), vehicle.getYearRelease(), vehicleType);
	    	number++;
	    }
	    System.out.printf(line);
	}

	public static void printCostumer(List<Customer> customerList, String id){
		List<Customer> filteredCustomer = DataProcess.filterCustomerListById(customerList, id);

		for (Customer customer : filteredCustomer) {
			System.out.println("Customer Id : " + customer.getCustomerId());
			System.out.println("Nama : " + customer.getName());
			System.out.println("Customer Status : " + (customer instanceof MemberCustomer ? "Member" : "Non Member"));
			System.out.println("Alamat : " + customer.getAddress());

			if(customer instanceof MemberCustomer)
				System.out.println("Saldo Koin : " + ((MemberCustomer) customer).getSaldoCoin());

			printVechicle(customer.getVehicles());
		}
	}

	public static void printService(List<ItemService> itemServiceList, String kendaraan){

		String formatTable = "| %-2s | %-15s | %-15s | %-15s | %-10s |%n";
		String line = "+----+-----------------+-----------------+-----------------+------------+%n";
		System.out.format(line);
		System.out.format(formatTable, "No", "Service Id", "Nama Service", "Tipe Kendaraan", "Harga");
		System.out.format(line);

		int number = 1;
		for (ItemService vehicle : itemServiceList) {
			if (Objects.equals(vehicle.getVehicleType(), kendaraan)) {
				System.out.format(formatTable, number, vehicle.getServiceId(), vehicle.getServiceName(), vehicle.getVehicleType(), vehicle.getPrice());
				number++;
			}
		}
		System.out.printf(line);
	}

	public static void printTagihanCoin (int tagihan, String id, List<Customer> customerList){
		List<Customer> filteredCustomer = DataProcess.filterCustomerListById(customerList, id);
		int saldo = 0;

		for (Customer customer : filteredCustomer) {
			if(customer instanceof MemberCustomer) {
				saldo = (int) ((MemberCustomer) customer).getSaldoCoin();
			}
		}
		int tagihanDiskon = (int) (tagihan-(tagihan*0.1));
		int hasil = saldo - tagihanDiskon;

		if(hasil >= 0){
			// save hasil ke price
			for (Customer customer : filteredCustomer) {
				if(customer instanceof MemberCustomer) {
					((MemberCustomer) customer).setSaldoCoin(hasil);
				}
			}
			System.out.println("Booking berhasil!");
			System.out.println("Total Harga Service: " + tagihan);
			System.out.println("Total Pembayaran: " + tagihanDiskon);
		}else {
			System.out.println("Saldo koin anda tidak mencukupi, dialihkan ke pembayaran cash");
			System.out.println("Booking berhasil!");
			System.out.println("Total Harga Service: " + tagihan);
			System.out.println("Total Pembayaran: " + tagihan);
		}
	}

	public static void printBooking(List<BookingOrder> bookingOrderList) {
		String formatTable = "| %-2s | %-15s | %-15s | %-15s | %-15s | %-13s | %-20s |%n";
		String line = "+----+-----------------+-----------------+-----------------+-----------------+---------------+----------------------+%n";
		System.out.format(line);
		System.out.format(formatTable, "No", "Booking Id", "Nama Customer", "Payment Method", "Total Service", "Total Payment", "List Service");
		System.out.format(line);

		int number = 1;
		for (BookingOrder bookingOrder : bookingOrderList) {
			String services = bookingOrder.getServices().stream()
					.map(ItemService::getServiceName)
					.collect(Collectors.joining(", "));

			System.out.format(formatTable, number++, bookingOrder.getBookingId(), bookingOrder.getCustomer().getName(),
					bookingOrder.getPaymentMethod(), bookingOrder.getTotalServicePrice(), bookingOrder.getTotalPayment(), services);
		}
		System.out.format(line);
	}

	
	//Silahkan Tambahkan function print sesuai dengan kebutuhan.
	
}
