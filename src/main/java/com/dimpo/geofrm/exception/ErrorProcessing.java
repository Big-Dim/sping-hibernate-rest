package com.dimpo.geofrm.exception;

public class ErrorProcessing extends Exception {

	private boolean badRequest = false;

	public ErrorProcessing() {
		super();
	}

	public ErrorProcessing(String message) {
		super(message);
	}

	public ErrorProcessing(String message, boolean badRequest) {
		super(message);
		this.badRequest = badRequest;
	}

	public boolean isBadRequest() {
		return badRequest;
	}
}
