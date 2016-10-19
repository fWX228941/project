package com.android.haobanyi.util.citypickerview.model;



import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

public class CityModel implements IPickerViewData {
	private String name;
	private List<DistrictModel> districtList;
	
	public CityModel() {
		super();
	}

	public CityModel(String name, List<DistrictModel> districtList) {
		super();
		this.name = name;
		this.districtList = districtList;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DistrictModel> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<DistrictModel> districtList) {
		this.districtList = districtList;
	}

	@Override
	public String toString() {
		return "CityModel [name=" + name + ", districtList=" + districtList
				+ "]";
	}
	private String CityID;
	private String ProvinceID;  // name 字段都是有的

	public String getCityID() {
		return CityID;
	}

	public void setCityID(String cityID) {
		CityID = cityID;
	}

	public String getProvinceID() {
		return ProvinceID;
	}

	public void setProvinceID(String provinceID) {
		ProvinceID = provinceID;
	}
	public CityModel(String name, List<DistrictModel> districtList, String cityID, String provinceID) {
		this.name = name;
		this.districtList = districtList;
		CityID = cityID;
		ProvinceID = provinceID;
	}

	@Override
	public String getPickerViewText() {
		return name;
	}
}
