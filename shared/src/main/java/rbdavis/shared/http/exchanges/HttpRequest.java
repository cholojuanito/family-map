package rbdavis.shared.http.exchanges;

import java.util.HashMap;
import java.util.Map;

public interface HttpRequest<T>
{
    Map<String, String> parameters = new HashMap<>();
}
