package com.android.haobanyi.util.citypickerview.model;



import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

public class ProvinceModel implements IPickerViewData {
	private String name;
	private List<com.android.haobanyi.util.citypickerview.model.CityModel> cityList;
	
	public ProvinceModel() {
		super();
	}

	public ProvinceModel(String name, List<com.android.haobanyi.util.citypickerview.model.CityModel> cityList) {
		super();
		this.name = name;
		this.cityList = cityList;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<com.android.haobanyi.util.citypickerview.model.CityModel> getCityList() {
		return cityList;
	}

	public void setCityList(List<com.android.haobanyi.util.citypickerview.model.CityModel> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "ProvinceModel [name=" + name + ", cityList=" + cityList + "]";
	}

	public String getProvinceID() {
		return ProvinceID;
	}

	public void setProvinceID(String provinceID) {
		ProvinceID = provinceID;
	}

	private String ProvinceID;


	public ProvinceModel(String name, List<CityModel> cityList, String provinceID) {
		this.name = name;
		this.cityList = cityList;

		ProvinceID = provinceID;
	}
	//这个用来显示在PickerView上面的字符串,PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
	@Override
	public String getPickerViewText() {
		return name;
	}
}
