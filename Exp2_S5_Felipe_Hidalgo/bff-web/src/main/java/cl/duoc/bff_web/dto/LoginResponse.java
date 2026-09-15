package cl.duoc.bff_web.dto;

public class LoginResponse {

    private String token;
    private String tipo;
    private String canal;

    public LoginResponse() {
    }

    public LoginResponse(String token, String tipo, String canal) {
        this.token = token;
        this.tipo = tipo;
        this.canal = canal;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }
}