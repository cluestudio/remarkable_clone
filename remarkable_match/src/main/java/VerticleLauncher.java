import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class VerticleLauncher {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("need staging host port");
            return;
        }

        try {
            String staging = args[0];
            String host = args[1];
            int port = Integer.parseInt(args[2]);

            System.out.println("staging:" + staging);
            System.out.println("host:" + host);
            System.out.println("port:" + port);

            DeploymentOptions options = new DeploymentOptions()
                    .setConfig(new JsonObject()
                            .put("staging", staging)
                            .put("host", host)
                            .put("port", port));
            Vertx.vertx().deployVerticle("com.clue.controller.MatchVerticle", options);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
