package kr.or.ddit.vo;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class BupjungdongVO implements Serializable{
	@NotBlank
	private String code;
	private String name;
	private String flag;
}
