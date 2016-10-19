package com.android.haobanyi.util.citypickerview.model;

import com.bigkoo.pickerview.model.IPickerViewData;

public class DistrictModel implements IPickerViewData {
	private String name;
	private String zipcode;
	
	public DistrictModel() {
		super();
	}

	public DistrictModel(String name, String zipcode) {
		super();
		this.name = name;
		this.zipcode = zipcode;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	@Override
	public String toString() {
		return "DistrictModel [name=" + name + ", zipcode=" + zipcode + "]";
	}

	//添加字段
	private String DistrictID;
	private String CityID;


	public String getDistrictID() {
		return DistrictID;
	}

	public void setDistrictID(String districtID) {
		DistrictID = districtID;
	}

	public String getCityID() {
		return CityID;
	}

	public void setCityID(String cityID) {
		CityID = cityID;
	}

	public DistrictModel(String name, String districtID, String cityID) {
		this.name = name;
		DistrictID = districtID;
		CityID = cityID;
	}

	@Override
	public String getPickerViewText() {
		return name;
	}
}
