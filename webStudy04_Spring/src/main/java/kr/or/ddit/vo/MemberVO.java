package kr.or.ddit.vo;

import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * 마이페이지 조회 시 , 그동안 구매한 상품 기록을 함께 조회
 *
 */
@Data
public class MemberVO implements Serializable {
	
	
	
	public MemberVO() {
		super();
	}
	public MemberVO(String mem_id, String mem_pass) {
		this.mem_id = mem_id;
		this.mem_pass = mem_pass;
	}
	@NotBlank
	private String mem_id;
	@NotBlank
	@Length(min=4, max=12)
	private String mem_pass;
	@NotBlank
	private String mem_name;
	@Length(min=6,max=6)
	private transient String mem_regno1;  //직렬화 제외
	@Length(min=7,max=7)
	private transient String mem_regno2;  //직렬화 제외 (transient)
	private String mem_bir;
	private String mem_zip;
	private String mem_add1;
	private String mem_add2;
	private String mem_hometel;
	private String mem_comtel;
	private String mem_hp;
	@Email
	@NotBlank
	private String mem_mail;
	private String mem_job;
	private String mem_like;
	private String mem_memorial;
	private String mem_memorialday;
	private Integer mem_mileage;
	private String mem_delete;
	private List<ProdVO> prodList;
	private MultipartFile mem_image;
	
	private byte[] mem_img;
	public String getMem_imageBase64() {
		if(mem_img==null) return null;
		return Base64.getEncoder().encodeToString(mem_img);
	}
	private String mem_role;
	
	public void setMem_image(MultipartFile mem_image) throws IOException {
		this.mem_image = mem_image;
		if(mem_image.getSize()<=0) return;
		mem_img = mem_image.getBytes();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mem_id == null) ? 0 : mem_id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MemberVO other = (MemberVO) obj;
		if (mem_id == null) {
			if (other.mem_id != null)
				return false;
		} else if (!mem_id.equals(other.mem_id))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return "MemberVO [mem_id=" + mem_id + ", mem_pass=" + mem_pass + ", mem_name=" + mem_name + ", mem_add1="
				+ mem_add1 + ", mem_add2=" + mem_add2 + "]";
	}
	
	
}
