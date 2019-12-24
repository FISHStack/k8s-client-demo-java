import com.google.common.io.ByteStreams;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.PodLogs;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.informer.cache.ProcessorListener;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1Pod;
import io.kubernetes.client.models.V1PodList;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.Watch;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class Application {
    public static void main(String[] args) throws IOException, ApiException {
//        KubeConfig kubeConfig = new KubeConfig();
        ApiClient client = null;
        client = Config.fromConfig("d://kubelet.conf");
//        client = Config.fromUrl("https://192.168.1.6");
//        ApiClient client = Config.defaultClient();
        listPod(client);
        System.out.println("=================================================");
//        logs(client);
        System.out.println("=================================================");
        watch(client);
    }

    public static void listPod(ApiClient client) throws ApiException {
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();
        V1PodList list =
                api.listPodForAllNamespaces(null, null, null, null, null, null, null, null);
        for (V1Pod item : list.getItems()) {
            System.out.println(item.getMetadata().getName());
        }
    }

    public static void logs(ApiClient client) throws ApiException, IOException {
        Configuration.setDefaultApiClient(client);
        CoreV1Api coreApi = new CoreV1Api(client);

        PodLogs logs = new PodLogs();
        V1Pod pod =
                coreApi
                        .listNamespacedPod("default", "false", null, null, null, null, null, null, null)
                        .getItems()
                        .get(0);

        System.out.println(pod.toString());
        InputStream is = logs.streamNamespacedPodLog(pod);
        ByteStreams.copy(is, System.out);
    }

    public static void watch(ApiClient client) throws IOException, ApiException {

        // infinite timeout
        OkHttpClient httpClient =
                client.getHttpClient();
        httpClient.setReadTimeout(0, TimeUnit.SECONDS);
        client.setHttpClient(httpClient);
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();

        Watch<V1Namespace> watch =
                Watch.createWatch(
                        client,
                        api.listNamespaceCall(null, null, null, null, null, null, 5, null,null, null),
                        new TypeToken<Watch.Response<V1Namespace>>() {}.getType());

        try {
            for (Watch.Response<V1Namespace> item : watch) {
                System.out.printf("%s : %s%n", item.type, item.object.getMetadata().getName());
            }
        } finally {
            watch.close();
        }
    }

    public static void patch(){

    }

    public static void exec(){

    }

    public static void attach(){

    }

    public static void websocket(){

    }
}
