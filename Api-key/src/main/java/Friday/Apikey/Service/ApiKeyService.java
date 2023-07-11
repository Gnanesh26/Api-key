package Friday.Apikey.Service;




import org.springframework.stereotype.Service;

@Service
public class ApiKeyService {



    private static final String VALID_API_KEY = "hu90Y09j88tXfwfsScTpBgCljEJJNoOKA8q0XmiE6aQMTtbz0TbmBRX3Q9Z0e90D";

    public boolean isValidApiKey(String apiKey) {
        return apiKey.equals(VALID_API_KEY);
    }
}
