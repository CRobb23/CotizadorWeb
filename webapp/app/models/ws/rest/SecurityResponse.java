package models.ws.rest;

import java.util.List;

public class SecurityResponse {
	
	private Boolean success;

	private Integer codigo;
	private String token;
	private String mensaje;
	private Integer timeOut;

	private Long codigoSSO;

	private List<Parametro> parametros;

	
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Integer getCodigo(){return codigo;}
	public void setCodigo(Integer codigo){this.codigo = codigo;}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String message) {
		this.mensaje = mensaje;
	}
	public Integer getTimeOut(){return timeOut;}
	public void setTimeOut(Integer timeOut){this.timeOut = timeOut;}

	public List<Parametro> getParametros(){ return parametros; }
	public void setParametros(List<Parametro> parametros){ this.parametros = parametros; }

	public Long getCodigoSSO(){return codigoSSO;}
	public void setCodigoSSO(Long codigoSSO){this.codigoSSO = codigoSSO;}
	
}
