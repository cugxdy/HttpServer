package org.cugxdy.mapping;


public class ControllerMappingParameter {
	
	private String name;
	
	private Class<?> dataType;
	
	private ControllerMappingParameterTypeEnum type;
	
	private boolean required; 
	
	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final Class<?> getDataType() {
		return dataType;
	}

	public final void setDataType(Class<?> dataType) {
		this.dataType = dataType;
	}

	public final ControllerMappingParameterTypeEnum getType() {
		return type;
	}

	public final void setType(ControllerMappingParameterTypeEnum type) {
		this.type = type;
	}

	public final boolean isRequired() {
		return required;
	}

	public final void setRequired(boolean required) {
		this.required = required;
	}
}
