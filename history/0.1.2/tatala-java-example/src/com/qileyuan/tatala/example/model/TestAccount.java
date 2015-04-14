package com.qileyuan.tatala.example.model;

import java.io.Serializable;

/**
 * This class is a customization object.
 * @author JimT
 *
 */
public class TestAccount implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String address;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String toString(){
		return "[id: "+id+"][name: "+name+"][address: "+address+"]";
	}
}
