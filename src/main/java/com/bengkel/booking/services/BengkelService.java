package com.bengkel.booking.services;

import com.bengkel.booking.models.*;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BengkelService {
    static Scanner input = new Scanner(System.in);
	
	//Silahkan tambahkan fitur-fitur utama aplikasi disini
	
	//Booking atau Reservation
    public static void bookingService(List<Customer> customerList, List<ItemService> itemServiceList, String customerId){
        List<Customer> chosed = DataProcess.filterCustomerListById(customerList, customerId);
        List<ItemService> selectedServices = new ArrayList<>();

        System.out.println("Masukan Vehicle Id:");
        String kendaraanId = input.nextLine();

        if(Validation.searchVehicleId(customerList, customerId, kendaraanId)){
            System.out.println("id vehicle ada");
            String metodeBayar = "Cash";        // default pembayaran

            int vehicleType = cekVehicle(customerList, customerId, kendaraanId);

            if (vehicleType == 1) {
                // Mobil
                PrintService.printService(itemServiceList, "Car");
            } else if (vehicleType == 2) {
                // Motor
                PrintService.printService(itemServiceList, "Motorcyle");
            } else {
                System.out.println("Tipe kendaraan tidak dikenal.");
            }


            int tagihanTotal = 0;

            if (Validation.isMember(customerList, customerId)){
                int serviceCount = 0; // Menghitung jumlah layanan yang sudah dipilih

                boolean cekLooping1 = false;
                boolean cekLooping2 = false;
                do{
                    System.out.println("Silahkan masukan Service Id:");
                    String pilihService = input.nextLine();

                    if(Validation.searchServiceId(itemServiceList, pilihService)){

                        tagihanTotal = tagihanCash(itemServiceList, pilihService);
                        System.out.println("save 1");
                        serviceCount++; // Tambahkan jumlah layanan yang sudah dipilih

                        // Tambahkan layanan ke daftar
                        for (ItemService service : itemServiceList) {
                            if (service.getServiceId().equals(pilihService)) {
                                selectedServices.add(service);
                                break;
                            }
                        }

                        if (serviceCount >= 2) {
                            break;
                        }

                        do{
                            System.out.println("Apakah anda ingin menambahkan Service Lainnya? (Y/T)");
                            String pilihServiceLagi = input.nextLine();

                            if(pilihServiceLagi.equalsIgnoreCase("Y")){
                                System.out.println("Silahkan masukan Service Id kedua:");
                                String pilihService2 = input.nextLine();

                                if(Validation.searchServiceId(itemServiceList, pilihService2)){
                                    // proses save pilihserv1 dan 2
                                    tagihanTotal = tagihanTotal + tagihanCash(itemServiceList, pilihService2);

                                    System.out.println("save 2");
                                    serviceCount++;

                                    // Tambahkan layanan ke daftar
                                    for (ItemService service : itemServiceList) {
                                        if (service.getServiceId().equals(pilihService2)) {
                                            selectedServices.add(service);
                                            break;
                                        }
                                    }

                                    cekLooping2 = true;
                                }else {
                                    System.out.println("ID tidak ditemukan");
                                    cekLooping2 = true;
                                }

                            } else if (pilihServiceLagi.equalsIgnoreCase("T")) {
                                cekLooping2 = true;
                            } else {
                                System.out.println("Pilihan hanya Y/T");
                            }

                        }while (!cekLooping2);
                    }else{
                        System.out.println("ID tidak ditemukan");
                    }

                } while (serviceCount < 2 && !cekLooping2);



                boolean cekBayar = false;
                do{
                    System.out.println("Silahkan Pilih Metode Pembayaran (Saldo Coin atau Cash)");
                    metodeBayar = input.nextLine();
                    if(Objects.equals(metodeBayar, "Saldo Coin")){
                        // bayar dengan saldo koin
                        PrintService.printTagihanCoin(tagihanTotal, customerId, customerList);
                        cekBayar = true;
                    } else if (Objects.equals(metodeBayar, "Cash")) {
                        System.out.println("Booking berhasil!");
                        System.out.println("Total Harga Service: " + tagihanTotal);
                        System.out.println("Total Pembayaran: " + tagihanTotal);
                        cekBayar = true;
                    }else{
                        System.out.println("Pilih pembayaran Saldo Coin atau Cash");

                    }
                }while (!cekBayar);


            }else{
                boolean ceklooping = false;
                do{
                    System.out.println("Silahkan masukan Service Id:");
                    String pilihService = input.nextLine();

                    if(Validation.searchServiceId(itemServiceList, pilihService)){
                        // proses save pilihserv
                        // Tambahkan layanan ke daftar
                        for (ItemService service : itemServiceList) {
                            if (service.getServiceId().equals(pilihService)) {
                                selectedServices.add(service);
                                break;
                            }
                        }

                        tagihanTotal = tagihanCash(itemServiceList, pilihService);

                        System.out.println("save non mem");
                        ceklooping = true; // Setelah pemilihan berhasil, keluar dari loop
                    }else {
                        System.out.println("ID tidak ditemukan");
                    }

                }while (!ceklooping);


            }

            // Setelah pembayaran berhasil, simpan data pesanan
            BookingOrder bookingOrder = new BookingOrder();
            bookingOrder.setBookingId("Book-" + customerId + "-" + kendaraanId); // Contoh ID booking
            bookingOrder.setCustomer(chosed.get(0));
            bookingOrder.setServices(selectedServices);
            bookingOrder.setPaymentMethod(metodeBayar);
            bookingOrder.setTotalServicePrice(tagihanTotal);
            bookingOrder.calculatePayment(); // Menghitung pembayaran dengan diskon

            // Tambahkan ke listAllBookingOrders
            MenuService.listAllBookingOrders.add(bookingOrder);

            /*
            // Setelah pembayaran berhasil, simpan data pesanan
            BookingOrder bookingOrder = new BookingOrder();
            bookingOrder.setBookingId("Book-" + customerId + "-" + kendaraanId); // Contoh ID booking
            bookingOrder.setCustomer(chosed.get(0));
            bookingOrder.setServices(selectedServices);
            bookingOrder.setPaymentMethod(metodeBayar);
            bookingOrder.setTotalServicePrice(tagihanTotal);
            bookingOrder.calculatePayment(); // Menghitung pembayaran dengan diskon
             */

            /*// Cetak detail pesanan
            System.out.println("Booking Id: " + bookingOrder.getBookingId());
            System.out.println("Nama Customer: " + bookingOrder.getCustomer().getName());
            System.out.println("Payment Method: " + bookingOrder.getPaymentMethod());
            System.out.println("Total Service: " + bookingOrder.getTotalServicePrice());
            System.out.println("Total Payment: " + bookingOrder.getTotalPayment());
            System.out.println("List Service: " + bookingOrder.getServices().stream()
                    .map(ItemService::getServiceName)
                    .collect(Collectors.joining(", ")));
            */
            
        }else
            System.out.println("ID vehicle tidak ada");

    }

    public static int cekVehicle(List<Customer> customerList, String customerId, String vehicleId){

        for (Customer customer : customerList) {
            if (Objects.equals(customer.getCustomerId(), customerId)) {
                for (Vehicle vehicle : customer.getVehicles()) {
                    if (Objects.equals(vehicle.getVehiclesId(), vehicleId)) {

                        if (Objects.equals(vehicle.getVehicleType(), "Car")) {
                            return 1;
                        }
                        if (Objects.equals(vehicle.getVehicleType(), "Motorcyle")) {
                            return 2;
                        }
                    }
                }
            }
        }
        return 0; // Mengembalikan 0 jika tipe kendaraan tidak ditemukan

    }

    public static int tagihanCash (List<ItemService> itemServiceList, String id){
        int tagihan = 0;
        for (ItemService itemService : itemServiceList) {
            if (Objects.equals(itemService.getServiceId(), id)) {
                tagihan = (int) itemService.getPrice();
            }
        }
        return tagihan;
    }



	
}
