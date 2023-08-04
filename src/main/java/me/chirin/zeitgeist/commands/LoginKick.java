package me.chirin.zeitgeist.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.chirin.zeitgeist.commands.arguements.PlayerNameArgumentType;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import net.minecraft.command.CommandSource;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkState;
import net.minecraft.network.listener.ClientLoginPacketListener;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.network.packet.s2c.login.*;
import net.minecraft.text.Text;

import java.net.InetSocketAddress;
import java.util.Optional;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;
import static meteordevelopment.meteorclient.MeteorClient.mc;

public class LoginKick extends Command {
    public LoginKick() {
        super("lkick", "Kick players on cracked servers", "login-kick");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("player", PlayerNameArgumentType.player()).executes(ctx -> {
            String playerName = PlayerNameArgumentType.getPlayer(ctx, "player");
            MeteorExecutor.execute(() -> {
                InetSocketAddress sa = (InetSocketAddress) mc.getNetworkHandler().getConnection().getAddress();
                ClientConnection conn = ClientConnection.connect(sa, mc.options.shouldUseNativeTransport());
                conn.setPacketListener(new ClientLoginPacketListener() {
                    @Override
                    public void onHello(LoginHelloS2CPacket packet) {
                        info("success");
                        conn.disconnect(Text.translatable("disconnect.disconnected"));
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
                    public boolean isConnectionOpen() {
                        return false;
                    }
                });
                conn.send(new HandshakeC2SPacket(sa.getHostName(), sa.getPort(), NetworkState.LOGIN));
                conn.send(new LoginHelloC2SPacket(playerName, Optional.empty()));
            });

            return SINGLE_SUCCESS;
        }));
    }
}
