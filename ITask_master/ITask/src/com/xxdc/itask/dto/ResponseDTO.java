package com.xxdc.itask.dto;

public class ResponseDTO {
	private String message;
	private boolean status;
	private Object response;
	
	public ResponseDTO(boolean status,String message,Object response){
		super();
		
		this.status = status;
		this.message = message;
		this.response = response;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public Object getResponse() {
		return response;
	}
	public void setResponse(Object response) {
		this.response = response;
	}
	
	public String toString(){
		String info="ResponseDTO:[";
		info+="status:"+status;
		info+="message:"+message;
		info+="response:"+response;
		return null;
	}
	
	
	
}
