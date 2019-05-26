package com.bob.bobguestapp.tools.http.serverbeans.checksession;


import com.bob.toolsmodule.http.serverbeans.Guest;
import com.bob.toolsmodule.http.serverbeans.Hotel;
import com.bob.toolsmodule.http.serverbeans.User;

public class GuestCheckSessionRequest {

	private Guest guest;
	private Hotel hotel;

	public GuestCheckSessionRequest() {
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
