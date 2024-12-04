package backend.dev.user.oauth;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OAuth2ServiceRegistry {
    private final Map<String, OAuth2Service> serviceMap;

    public OAuth2ServiceRegistry(List<OAuth2Service> services) {
        this.serviceMap = services.stream().collect(Collectors.toMap(OAuth2Service::getProvider, Function.identity()));
    }
    public OAuth2Service getService(String provider) {
        return serviceMap.get(provider);
    }
}
