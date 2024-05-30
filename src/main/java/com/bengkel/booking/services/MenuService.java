package com.bengkel.booking.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.repositories.CustomerRepository;
import com.bengkel.booking.repositories.ItemServiceRepository;

public class MenuService {
	private static List<Customer> listAllCustomers = CustomerRepository.getAllCustomer();
	private static List<ItemService> listAllItemService = ItemServiceRepository.getAllItemService();
	static List<BookingOrder> listAllBookingOrders = new ArrayList<>();
	private static Scanner input = new Scanner(System.in);
	public static void run() {
		String isLogin;

		isLogin = login();
		if (isLogin != null)
			mainMenu(isLogin);
		else
			System.out.println("Anda telah mencoba 3 kali. Program keluar");
	}
	
	public static String login() {
		boolean cekCustomerId;
		int batasLogin = 0;
		do{
			System.out.println("Masukkan Costumer ID:");
			String costumer = input.nextLine();
			System.out.println("Masukkan Password:");
			String password = input.nextLine();

			batasLogin++;
			cekCustomerId = Validation.searchCostumerId(listAllCustomers, costumer);
			if(!cekCustomerId)
				System.out.println("Customer Id Tidak Ditemukan atau Salah");
			else {
				if (!Objects.equals(Validation.searchCostumerPassword(listAllCustomers, costumer), password)) {
					System.out.println("Password yang anda Masukan Salah");
				}
				else {
					return costumer;
				}
			}
		}while (batasLogin < 3);
		return null;
	}
	
	public static void mainMenu(String costumerId) {
		String[] listMenu = {"Informasi Customer", "Booking Bengkel", "Top Up Bengkel Coin", "Informasi Booking", "Logout"};
		int menuChoice = 0;
		boolean isLooping = true;
		
		do {
			PrintService.printMenu(listMenu, "Booking Bengkel Menu");
			menuChoice = Validation.validasiNumberWithRange("Masukan Pilihan Menu:", "Input Harus Berupa Angka!", "^[0-9]+$", listMenu.length-1, 0);
			System.out.println(menuChoice);
			
			switch (menuChoice) {
			case 1:
				//panggil fitur Informasi Customer
				PrintService.printCostumer(listAllCustomers, costumerId);
				break;
			case 2:
				//panggil fitur Booking Bengkel
				BengkelService.bookingService(listAllCustomers, listAllItemService, costumerId);
				break;
			case 3:
				//panggil fitur Top Up Saldo Coin
				DataProcess.topUpSaldo(listAllCustomers, costumerId);
				break;
			case 4:
				//panggil fitur Informasi Booking Order
				PrintService.printBooking(listAllBookingOrders);
				break;
			case 0:
				//logout
				isLooping = false;
				break;

			}
		} while (isLooping);

	}
	
	//Silahkan tambahkan kodingan untuk keperluan Menu Aplikasi
}
