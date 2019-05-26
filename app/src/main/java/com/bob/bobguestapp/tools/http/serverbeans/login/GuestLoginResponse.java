package com.bob.bobguestapp.tools.http.serverbeans.login;


import com.bob.toolsmodule.http.serverbeans.ApplicativeResponse;
import com.bob.toolsmodule.http.serverbeans.Guest;
import com.bob.toolsmodule.http.serverbeans.Hotel;
import com.bob.toolsmodule.http.serverbeans.User;

public class GuestLoginResponse {

	private ApplicativeResponse statusResponse;
	private Guest guest;
	private Hotel hotel;


	public GuestLoginResponse() {
		this.statusResponse = new ApplicativeResponse();
	}

	public ApplicativeResponse getStatusResponse() {
		return statusResponse;
	}

	public void setStatusResponse(ApplicativeResponse statusResponse) {
		this.statusResponse = statusResponse;
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
