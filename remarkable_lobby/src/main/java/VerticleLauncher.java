import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class VerticleLauncher {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("need staing port");
            return;
        }

        try {
            String staging = args[0];
            int port = Integer.parseInt(args[1]);
            DeploymentOptions options = new DeploymentOptions()
                    .setConfig(new JsonObject()
                            .put("staging", staging)
                            .put("port", port));
            Vertx.vertx().deployVerticle("com.clue.controller.LobbyVerticle", options);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
