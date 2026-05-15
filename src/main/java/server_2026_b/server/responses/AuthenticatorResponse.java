package server_2026_b.server.responses;

public class AuthenticatorResponse extends BasicResponse {
    private String uri;

    public AuthenticatorResponse (boolean success, Integer errorCode, String uri) {
        super(success, errorCode, null);
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
