import java.net.URL;

public class ResourceLoader {
    public static URL getFileUrl(final String path) {
        return Thread.currentThread().getContextClassLoader().getResource(path);
    }
}