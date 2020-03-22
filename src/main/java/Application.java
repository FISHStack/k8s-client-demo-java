import com.google.common.io.ByteStreams;
import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.PodLogs;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.Watch;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class Application {
    public static void main(String[] args) throws IOException, ApiException {
//        KubeConfig kubeConfig = new KubeConfig();
        ApiClient client = null;
//        client = Config.fromToken("https://192.168.1.6:6443","Authorization: Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJjb3JlZG5zLXRva2VuLThrdDI4Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImNvcmVkbnMiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiJhOTcyYjNlMi0wM2I3LTExZWEtOThjNS0wMDBjMjk4MWRkMDYiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZS1zeXN0ZW06Y29yZWRucyJ9.qZxPcA2MOG3l0NRKMtmDJLXlbtreLyQRoNJl9ftrBoXNN4WV-2kvbqg4T0z88zRniAPRpzs5ZDE8zMy1b5urJVGtwXlploNxZ84RqpRFX3GfSsVR_vBVfr0r6MHme8-1FjZXuUp3aU0rl6c1J4DYM_ihicHCEiEYrVVYDUtfALzVtX0UKo6QIgF8VEAf7f9DG55RhfgW_FNdM6nPnc5ZOSdptgtro6tZiCq1y0C1Fa0Zaf8qQY4nCHptDRVq-T8A0ulXmc8hEr8aaB5tLkgtBhrNoBVh5QYoLWRG0WoyOWAEvZ_p0CwXABdwg-OeJPwcMJu3Xra4c2ll5KmV2WdwZg");
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
                api.listPodForAllNamespaces(null, null, null, null, null, null, null, null,null);
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
                        .listNamespacedPod("default", "false", null, null, null, null, null, null, null, null)
                        .getItems()
                        .get(0);

        InputStream is = logs.streamNamespacedPodLog(pod);
        ByteStreams.copy(is, System.out);

    }

    public static void watch(ApiClient client) throws IOException, ApiException {

        // infinite timeout
        OkHttpClient httpClient =
                client.getHttpClient().newBuilder().readTimeout(0, TimeUnit.SECONDS).build();

        client.setHttpClient(httpClient);
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();

        Watch<V1Namespace> watch =
                Watch.createWatch(
                        client,
                        api.listNamespaceCall(null, null, null, null, null, null, null, null,null, null),
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
