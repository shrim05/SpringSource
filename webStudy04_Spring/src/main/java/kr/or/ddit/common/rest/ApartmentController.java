package kr.or.ddit.common.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import kr.or.ddit.vo.BupjungdongVO;

@Controller
public class ApartmentController {
	@Inject
	RestTemplate template;
	
	@Inject
	IApartmentDAO dao;
	
	@RequestMapping(value="/apart", produces=MediaType.TEXT_HTML_VALUE)
	public String view() {
		return "apart/apt_view";
	}
	
	@RequestMapping(value="/nameList", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<BupjungdongVO> getList(@RequestParam(required=true) String keyword){
		return dao.selectCodeList(keyword);
	}
	
	@RequestMapping(value="/apart", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Map proxy(
			String lawd_cd,
			String deal_ymd
			) {
		lawd_cd=lawd_cd.substring(0, 5);
		deal_ymd = deal_ymd.replace("-", "").substring(0,6);
		String serviceUrl = "http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev";
		StringBuffer url = new StringBuffer(serviceUrl);
		url.append("?LAWD_CD={LAWD_CD}");
		url.append("&DEAL_YMD={DEAL_YMD}");
		url.append("&serviceKey={serviceKey}");
		Map<String, Object> uriVariables = new HashMap<>();
		uriVariables.put("LAWD_CD", lawd_cd);
		uriVariables.put("DEAL_YMD", deal_ymd);
		uriVariables.put("serviceKey", "sp5Cs+t34tJwWlJgO8jtj6S0KoWK1e5wD3H3m6kXhVHKNaVgycVSX0wzjrU+f3tcmFJgBp4w4e5Mab5evgC9tg==");
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
		HttpEntity entity = new HttpEntity<>(headers);
		ResponseEntity<Map> resp = template.exchange(url.toString(), HttpMethod.GET, entity, Map.class, uriVariables);
		Map<String,Object> result = resp.getBody();
		return result;
//		ResponseEntity<String> resp = template.getForEntity(url.toString(), String.class, uriVariables);
//		return resp.getBody();
	}
	
	
}
