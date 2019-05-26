package com.bob.bobguestapp.tools.http.serverbeans.register;


import com.bob.toolsmodule.http.serverbeans.Guest;
import com.bob.toolsmodule.http.serverbeans.Hotel;

public class GuestRegisterRequest {

	private Guest guest;
	private Hotel hotel;

	public GuestRegisterRequest() {
	}

	public Guest getGuest() {
		return guest;
	}

	public void setGuest(Guest guest) {
		this.guest = guest;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
}
